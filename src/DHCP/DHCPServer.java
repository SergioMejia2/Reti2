package DHCP;

import Pool.Pool;
import Pool.PoolManager;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;

public class DHCPServer extends Thread {

    private static final int MAX_BUFFER_SIZE = 1024; // 1024 bytes
    private int listenPort = 67;//1337;
    //public static final int LEASE_TIME = 60;
    private String broadcastIP = "255.255.255.255";
    private byte[] myGatewayIP;
    DHCPMessage mensajeDiscover;
    DHCPMessage mensajeOffer;
    DHCPMessage mensajeRequest;
    DHCPMessage mensajeAck;
    DHCPMessage mensajeNack;
    DHCPMessage mensajeRelease;
    private PoolManager poolManager;
    private ArrayList<Registro> asignados;
    private DatagramSocket socket;
    private FXMLDocumentController controlador;


    public DHCPServer(FXMLDocumentController control) throws Exception {
        try {
            controlador = control;
            poolManager = new PoolManager();
            asignados = new ArrayList<Registro>();
            byte[] ipServer = InetAddress.getLocalHost().getAddress();
            Pool serverPool = poolManager.findPoolByIP(ipServer);
            if (serverPool != null) {
                serverPool.addAsigned(InetAddress.getLocalHost().getAddress());
                myGatewayIP = serverPool.getGatewayIP();
            }
        } catch (Exception ex) {
            Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {

            socket = new DatagramSocket(listenPort);  // ipaddress? throws socket exception

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            byte[] payload = new byte[MAX_BUFFER_SIZE];
            int length = 600;
            DatagramPacket p = new DatagramPacket(payload, length);
            //System.out.println("Success! Now listening on port " + listenPort + "...");
            System.out.println("Listening on port " + listenPort + "...");

            //server is always listening
            boolean listening = true;
            while (listening) {
                socket.receive(p); //throws i/o exception

//                                System.out.println("Connection established from " + p.getAddress());
//                        
//                                System.out.println("Data Received: " + Arrays.toString(p.getData()));
                mensajeDiscover = new DHCP.DHCPMessage(p.getData(), p.getLength());
//                                System.out.println("Data Parsed:   "+ Arrays.toString(mensajeDiscover.externalize()));
                //System.out.println(mensajeDiscover);

                if (mensajeDiscover.getOptionIn(DHCPOption.DHCPMESSAGETYPE).getOpData()[0] == DHCPOption.DHCPDISCOVER) //DISCOVER
                {
                    Registro nuevo = new Registro(mensajeDiscover.getCHAddr(), mensajeDiscover.getYIAddr(), LocalDateTime.now(), null, mensajeDiscover.getXid(), DHCPOption.DHCPDISCOVER);
                    asignados.add(nuevo);
                    nuevo.mostrar(nuevo, controlador);

                    boolean isZero = false;
                    try {
                        mensajeOffer = new DHCPMessage();
                        byte[] gateway = mensajeDiscover.getGIAddr();
                        if (Utils.Utils.isIpZero(gateway)) {
                            isZero = true;
                            gateway = myGatewayIP;
                        }
                        Pool pool = poolManager.searchPool(gateway);
                        byte[] nuevaIP = null;
                        if (mensajeDiscover.getOptionIn(50) == null) {
                            nuevaIP = poolManager.asignarIP(gateway);
                        } else {
                            nuevaIP = poolManager.asignarIP(gateway, mensajeDiscover.getOptionIn(50).getOpData());
                        }
                        byte[] envio = mensajeOffer.offerMsg(mensajeDiscover, pool, nuevaIP);

                        nuevo = new Registro(mensajeOffer.getCHAddr(), mensajeOffer.getYIAddr(), LocalDateTime.now(), LocalDateTime.now().plusSeconds(pool.getTime()), mensajeOffer.getXid(), DHCPOption.DHCPOFFER);
                        asignados.add(nuevo);
                        nuevo.mostrar(nuevo, controlador);

                        if (isZero) {
                            byte[] brIp = new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255};
                            sendMessage(socket, envio, 68, brIp);
                        } else {
                            sendMessage(socket, envio, 67, gateway);
                        }

                    } catch (Exception ex) {
                        Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);

                    }
                }
                mensajeRequest = new DHCP.DHCPMessage(p.getData(), p.getLength());

                if (mensajeRequest.getOptionIn(DHCPOption.DHCPMESSAGETYPE).getOpData()[0] == DHCPOption.DHCPREQUEST) //DISCOVER
                {
                    boolean isZero = false;
                    boolean isUpdateRequest = false;
                    mensajeAck = new DHCPMessage();
                    mensajeNack = new DHCPMessage();

                    byte[] gateway = mensajeRequest.getGIAddr();

                    if (Utils.Utils.isIpZero(gateway)) {
                        isZero = true;
                        if (Utils.Utils.isIpZero(mensajeRequest.getCIAddr())) {
                            gateway = myGatewayIP;
                        } else {
                            isUpdateRequest = true;
                            Pool poool = poolManager.findPoolByIP(mensajeRequest.getCIAddr());
                            gateway = poool.getGatewayIP();
                        }
                    }

                    //System.out.println("ga: "+Utils.Utils.IPToString(gateway)+"  ip: "+Utils.Utils.IPToString(mensajeRequest.getCIAddr()));
                    //System.out.println("isZero: "+isZero+"  isUR: "+isUpdateRequest);
                    Pool pool = poolManager.searchPool(gateway);

                    byte[] ipRequest;
                    DHCPOption data = mensajeRequest.getOptionIn(50);
                    byte[] ipOffer;
                    if (data != null) {
                        ipRequest = data.getOpData();
                        ipOffer = mensajeOffer.getYIAddr();
                    } else {
                        ipRequest = mensajeRequest.getCIAddr();
                        ipOffer = buscarIPenRegistro(mensajeRequest.getCHAddr(), mensajeRequest.getXid(), DHCPOption.DHCPOFFER);
                    }
                    //System.out.println("sdjcbdskcbdskjbsj123456");
                    //System.out.println("ipR:" + Utils.Utils.IPToString(ipRequest) + "  ipO:" + Utils.Utils.IPToString(ipOffer));
                    if (ipOffer == null) {
                        Registro request = new Registro(mensajeRequest.getCHAddr(), mensajeRequest.getCIAddr(), LocalDateTime.now(), null, mensajeRequest.getXid(), DHCPOption.DHCPREQUEST);
                        asignados.add(request);
                        request.mostrar(request, controlador);

                        byte[] envio = mensajeAck.ackMsg(mensajeRequest, pool, mensajeRequest.getCIAddr());
                        if (isZero) {
                            byte[] brIp = new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255};
                            sendMessage(socket, envio, 68, brIp);
                        } else {
                            sendMessage(socket, envio, 67, gateway);
                        }
                        Registro ack = new Registro(mensajeAck.getCHAddr(), mensajeAck.getYIAddr(), LocalDateTime.now(), null, mensajeAck.getXid(), DHCPOption.DHCPACK);
                        asignados.add(ack);
                        ack.mostrar(ack, controlador);
                        if (!isUpdateRequest) {
                            pool.addAsigned(mensajeRequest.getCIAddr());
                        } else {
                            Registro renovacion = new Registro(mensajeAck.getCHAddr(), mensajeAck.getYIAddr(), LocalDateTime.now(), LocalDateTime.now().plusSeconds(pool.getTime()), mensajeAck.getXid(), DHCPOption.RENOVACIONIP);
                            asignados.add(renovacion);
                            renovacion.mostrar(renovacion, controlador);
                        }
                        pool.activate(mensajeRequest.getCIAddr());

                    } else {
                        if (Arrays.equals(ipRequest, ipOffer)) {
                            byte[] ipRequest2;
                            DHCPOption data2 = mensajeRequest.getOptionIn(50);
                            if (data != null) {
                                ipRequest2 = data2.getOpData();
                            } else {
                                ipRequest2 = mensajeRequest.getCIAddr();
                            }
                            Registro request = new Registro(mensajeRequest.getCHAddr(), ipRequest2, LocalDateTime.now(), null, mensajeRequest.getXid(), DHCPOption.DHCPREQUEST);
                            asignados.add(request);
                            request.mostrar(request, controlador);
                            byte[] envio = mensajeAck.ackMsg(mensajeRequest, pool, ipOffer);
                            if (isZero) {
                                byte[] brIp = new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255};
                                sendMessage(socket, envio, 68, brIp);
                            } else {
                                sendMessage(socket, envio, 67, gateway);
                            }
                            Registro ack = new Registro(mensajeAck.getCHAddr(), mensajeAck.getYIAddr(), LocalDateTime.now(), null, mensajeAck.getXid(), DHCPOption.DHCPACK);
                            pool.activate(ipOffer);
                            asignados.add(ack);
                            ack.mostrar(ack, controlador);

                        } else {
                            byte[] envio = mensajeNack.nackMsg(mensajeRequest, pool);
                            if (isZero) {
                                byte[] brIp = new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255};
                                sendMessage(socket, envio, 68, brIp);
                            } else {
                                sendMessage(socket, envio, 67, gateway);
                            }
                            Registro nack = new Registro(mensajeNack.getCHAddr(), mensajeNack.getYIAddr(), LocalDateTime.now(), null, mensajeNack.getXid(), DHCPOption.DHCPNACK);
                            asignados.add(nack);
                            nack.mostrar(nack, controlador);
                        }
                    }
                }
                mensajeRelease = new DHCP.DHCPMessage(p.getData(), p.getLength());

                if (mensajeRelease.getOptionIn(DHCPOption.DHCPMESSAGETYPE).getOpData()[0] == DHCPOption.DHCPRELEASE) //DISCOVER
                {
                    boolean isZero = false;
                    boolean isUpdateRequest = false;
                    mensajeAck = new DHCPMessage();
                    mensajeNack = new DHCPMessage();

                    byte[] gateway = mensajeRelease.getGIAddr();

                    if (Utils.Utils.isIpZero(gateway)) {
                        isZero = true;
                        if (Utils.Utils.isIpZero(mensajeRelease.getCIAddr())) {
                            gateway = myGatewayIP;
                        } else {
                            isUpdateRequest = true;
                            Pool poool = poolManager.findPoolByIP(mensajeRelease.getCIAddr());
                            gateway = poool.getGatewayIP();
                        }
                    }

                    //System.out.println("ga: "+Utils.Utils.IPToString(gateway)+"  ip: "+Utils.Utils.IPToString(mensajeRequest.getCIAddr()));
                    //System.out.println("isZero: "+isZero+"  isUR: "+isUpdateRequest);
                    Pool pool = poolManager.searchPool(gateway);
                    pool.releaseIP(mensajeRelease.getCIAddr());
                    Registro liberacion = new Registro(mensajeRelease.getCHAddr(), mensajeRelease.getYIAddr(), LocalDateTime.now(), null, mensajeRelease.getXid(), DHCPOption.DHCPRELEASE);
                    asignados.add(liberacion);
                    liberacion.mostrar(liberacion, controlador);
                }

                for (Pool poool : poolManager.getSubRedes()) {
                    System.out.println("P: " + poool);

                }

                //revocarIP("192.168.10.67");
            }
        } catch (Exception ex) {

            Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void sendMessage(DatagramSocket socket, byte[] envio, int port, byte[] ip) throws IOException {
        DatagramPacket packet = new DatagramPacket(envio, envio.length, InetAddress.getByAddress(ip), port);
        socket.send(packet);
    }

    public byte[] buscarIPenRegistro(byte[] chAddr, int xid, int messageType) {
        for (Registro registro : this.asignados) {
            //System.out.println("SEAARCHIVNG");
            //System.out.println("type: " + registro.getMessageType() + ",  chAddr" + chAddr + " == " + registro.getMAC() + "  xid: " + xid + " == " + registro.getXid());
            if (registro.getMessageType() == messageType && Arrays.equals(chAddr, registro.getMAC()) && xid == registro.getXid()) {
                return registro.getIp();
            }
        }
        return null;
    }

    public void revocarIP(String ip) throws Exception {
        try {
            byte[] ipRevoc = InetAddress.getByName(ip).getAddress();
            DHCPMessage mNack = new DHCPMessage();
            Pool pool = poolManager.findPoolByIP(ipRevoc);

            if (pool != null) {
                pool.releaseIP(ipRevoc);
                int ram = (int)(Math.random()*1e9);
                byte[] envio = mNack.nackMsg(pool, ipRevoc, ram);

                if (Arrays.equals(myGatewayIP, pool.getGatewayIP())) {
                    byte[] brIp = new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255};
                    sendMessage(socket, envio, 68, brIp);
                } else {
                    sendMessage(socket, envio, 67, myGatewayIP);
                }
                Registro nack = new Registro(null, ipRevoc, LocalDateTime.now(), null, ram, DHCPOption.DHCPNACK);
                asignados.add(nack);
                nack.mostrar(nack, controlador);
            }

        } catch (UnknownHostException ex) {
            Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
