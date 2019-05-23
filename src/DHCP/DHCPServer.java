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

public class DHCPServer {
        private static final int MAX_BUFFER_SIZE = 1024; // 1024 bytes
        private int listenPort = 67;//1337;
        public static final int LEASE_TIME = 60;
        private String broadcastIP = "255.255.255.255";
        private byte[] myGatewayIP;
        DHCPMessage mensajeDiscover;
        DHCPMessage mensajeOffer;
        DHCPMessage mensajeRequest;
        DHCPMessage mensajeAck;
        DHCPMessage mensajeNack;
	private PoolManager poolManager;
        private ArrayList<Equipo> asignados;

        public DHCPServer(int servePort) {
                listenPort = servePort;
                
            try {
                new DHCPServer();
            } catch (Exception ex) {
                Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public DHCPServer() throws Exception
        {
            try {
                poolManager = new PoolManager();
                asignados = new ArrayList<Equipo>();
                byte[] ipServer = InetAddress.getLocalHost().getAddress();
                Pool serverPool = poolManager.findPoolByIP(ipServer);
                if(serverPool != null)
                {
                    serverPool.addAsigned(InetAddress.getLocalHost().getAddress());
                    myGatewayIP = serverPool.getGatewayIP();         
                }
            } catch (Exception ex) {
                Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
                DatagramSocket socket = null;
                try {
                        
                        socket = new DatagramSocket(listenPort);  // ipaddress? throws socket exception

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
                                System.out.println(mensajeDiscover);
                                
                                
                                if(mensajeDiscover.getOptionIn(DHCPOption.DHCPMESSAGETYPE).getOpData()[0] == DHCPOption.DHCPDISCOVER) //DISCOVER
                                {
                                    try
                                    {
                                        mensajeOffer = new DHCPMessage();
                                        byte[] gateway = mensajeDiscover.getGIAddr();
                                        if(Utils.Utils.isIpZero(gateway))
                                        {
                                            gateway = myGatewayIP; 
                                        }
                                        Pool pool = poolManager.searchPool(gateway);
                                        byte[] nuevaIP = null;
                                        if(mensajeDiscover.getOptionIn(50) == null)
                                        {
                                            nuevaIP = poolManager.asignarIP(gateway);
                                        }
                                        else
                                        {
                                            nuevaIP = poolManager.asignarIP(gateway, mensajeDiscover.getOptionIn(50).getOpData());
                                        }
                                        byte[] envio = mensajeOffer.offerMsg(mensajeDiscover,pool, nuevaIP);
                                        sendMessage(socket,envio,68);
                                    }
                                    catch(Exception ex)
                                    {
                                        Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
            
                                    }
                                }
                                mensajeRequest = new DHCP.DHCPMessage(p.getData(), p.getLength());
                                if(mensajeRequest.getOptionIn(DHCPOption.DHCPMESSAGETYPE).getOpData()[0] == DHCPOption.DHCPREQUEST) //DISCOVER
                                {
                                    mensajeAck = new DHCPMessage();
                                    mensajeNack = new DHCPMessage();
                                    byte[] gateway = mensajeRequest.getGIAddr();
                                        if(Utils.Utils.isIpZero(gateway))
                                        {
                                            gateway = myGatewayIP; 
                                        }
                                        Pool pool = poolManager.searchPool(gateway);
                                        byte[] ipRequest = mensajeRequest.getOptionIn(50).getOpData();
                                        byte[] ipOffer = mensajeOffer.getYIAddr();
                                        if(Arrays.equals(ipRequest, ipOffer)){
                                            byte[] envio = mensajeAck.ackMsg(mensajeRequest,pool, ipOffer);
                                            sendMessage(socket,envio,68);
                                            Equipo nuevo = new Equipo(mensajeAck.getCHAddr(), mensajeAck.getYIAddr());
                                            asignados.add(nuevo);
                                            try {
                                                Utils.Persistencia.generarLog(asignados);
                                            } catch (Exception ex) {
                                                Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                            
                                        }else
                                        {
                                            byte[] envio = mensajeNack.nackMsg(mensajeRequest,pool);
                                            sendMessage(socket,envio,68);
                                        }
                                }
                        }
                } catch (SocketException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
                catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }

        }

        /**
         * @param args
         */
        public static void main(String[] args) {
                DHCPServer server;
                if (args.length >= 1) {
                        server = new DHCPServer(Integer.parseInt(args[0]));
                } else {
                    try {
                        server = new DHCPServer();
                    } catch (Exception ex) {
                        Logger.getLogger(DHCPServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

        }

    public void sendMessage(DatagramSocket socket, byte[] envio, int port) throws IOException
    {
        DatagramPacket packet = new DatagramPacket(envio,envio.length,InetAddress.getByName(broadcastIP), port);
        socket.send(packet);
    }

}