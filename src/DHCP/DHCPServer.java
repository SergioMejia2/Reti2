package DHCP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DHCPServer {
        private static final int MAX_BUFFER_SIZE = 1024; // 1024 bytes
        private int listenPort = 67;//1337;
        public static final int LEASE_TIME = 60;
        private String broadcastIP = "255.255.255.255";
	

        public DHCPServer(int servePort) {
                listenPort = servePort;
                new DHCPServer();
        }

        public DHCPServer() {
                
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
                                
                                System.out.println("Connection established from " + p.getAddress());
                        
                                System.out.println("Data Received: " + Arrays.toString(p.getData()));
                                DHCPMessage mensaje = new DHCP.DHCPMessage(p.getData(), p.getLength());
                                System.out.println("Data Parsed:   "+ Arrays.toString(mensaje.externalize()));
                                System.out.println(mensaje);
                                
                                
                                if(mensaje.getOptionIn(DHCPOption.DHCPMESSAGETYPE).getOpData()[0] == DHCPOption.DHCPDISCOVER) //DISCOVER
                                {
                                    DHCPMessage mensajeOffer = new DHCPMessage();
                                    byte[] envio = mensajeOffer.offerMsg(mensaje);
                                    sendMessage(socket,envio,68);
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
                        server = new DHCPServer();
                }

        }

    public void sendMessage(DatagramSocket socket, byte[] envio, int port) throws IOException
    {
        DatagramPacket packet = new DatagramPacket(envio,envio.length,InetAddress.getByName(broadcastIP), port);
        socket.send(packet);
    }

}