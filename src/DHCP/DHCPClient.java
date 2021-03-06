package DHCP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DHCPClient {
	private static final int MAX_BUFFER_SIZE = 1024; // 1024 bytes
	private int listenPort =  68;//1338;
	private String serverIP = "255.255.255.255";
	private int serverPort =  67;//1337;

	/*
	 * public DHCPClient(int servePort) { listenPort = servePort; new
	 * DHCPServer(); }
	 */

	public DHCPClient() throws Exception {
		System.out.println("Connecting to DHCPServer at " + serverIP + " on port " + serverPort + "...");

                
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(listenPort);  // ipaddress? throws socket exception

			byte[] request = {(byte) 0x01, (byte) 0x01, (byte) 0x06, (byte) 0x00, (byte) 0x62, (byte) 0x55, (byte) 0xd4, (byte) 0xf0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x14, (byte) 0x2d, (byte) 0x27, (byte) 0x21, (byte) 0xc5, (byte) 0x05, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x63, (byte) 0x82, (byte) 0x53, (byte) 0x63, (byte) 0x35, (byte) 0x01, (byte) 0x03, (byte) 0x3d, (byte) 0x07, (byte) 0x01, (byte) 0x14, (byte) 0x2d, (byte) 0x27, (byte) 0x21, (byte) 0xc5, (byte) 0x05, (byte) 0x32, (byte) 0x04, (byte) 0xc0, (byte) 0xa8, (byte) 0x00, (byte) 0x0d, (byte) 0x0c, (byte) 0x08, (byte) 0x53, (byte) 0x65, (byte) 0x62, (byte) 0x61, (byte) 0x73, (byte) 0x2d, (byte) 0x50, (byte) 0x43, (byte) 0x51, (byte) 0x0b, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x53, (byte) 0x65, (byte) 0x62, (byte) 0x61, (byte) 0x73, (byte) 0x2d, (byte) 0x50, (byte) 0x43, (byte) 0x3c, (byte) 0x08, (byte) 0x4d, (byte) 0x53, (byte) 0x46, (byte) 0x54, (byte) 0x20, (byte) 0x35, (byte) 0x2e, (byte) 0x30, (byte) 0x37, (byte) 0x0e, (byte) 0x01, (byte) 0x03, (byte) 0x06, (byte) 0x0f, (byte) 0x1f, (byte) 0x21, (byte) 0x2b, (byte) 0x2c, (byte) 0x2e, (byte) 0x2f, (byte) 0x77, (byte) 0x79, (byte) 0xf9, (byte) 0xfc, (byte) 0xff};
			byte[] discover = {(byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xac, (byte)0xcf, (byte)0x85, (byte)0x15, (byte)0x6a, (byte)0xd1, (byte)0x08, (byte)0x00, (byte)0x45, (byte)0x00, (byte)0x01, (byte)0x48, (byte)0x11, (byte)0x1b, (byte)0x00, (byte)0x00, (byte)0x40, (byte)0x11, (byte)0x68, (byte)0x8b, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff, (byte)0x00, (byte)0x44, (byte)0x00, (byte)0x43, (byte)0x01, (byte)0x34, (byte)0x98, (byte)0x86, (byte)0x01, (byte)0x01, (byte)0x06, (byte)0x00, (byte)0xa8, (byte)0x91, (byte)0x4c, (byte)0xa0, (byte)0x00, (byte)0x04, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xac, (byte)0xcf, (byte)0x85, (byte)0x15, (byte)0x6a, (byte)0xd1, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x63, (byte)0x82, (byte)0x53, (byte)0x63, (byte)0x35, (byte)0x01, (byte)0x01, (byte)0x39, (byte)0x02, (byte)0x05, (byte)0x78, (byte)0x3c, (byte)0x0c, (byte)0x64, (byte)0x68, (byte)0x63, (byte)0x70, (byte)0x63, (byte)0x64, (byte)0x2d, (byte)0x35, (byte)0x2e, (byte)0x35, (byte)0x2e, (byte)0x36, (byte)0x0c, (byte)0x18, (byte)0x61, (byte)0x6e, (byte)0x64, (byte)0x72, (byte)0x6f, (byte)0x69, (byte)0x64, (byte)0x2d, (byte)0x34, (byte)0x62, (byte)0x63, (byte)0x30, (byte)0x39, (byte)0x38, (byte)0x61, (byte)0x34, (byte)0x31, (byte)0x33, (byte)0x34, (byte)0x64, (byte)0x61, (byte)0x36, (byte)0x37, (byte)0x63, (byte)0x37, (byte)0x0a, (byte)0x01, (byte)0x21, (byte)0x03, (byte)0x06, (byte)0x0f, (byte)0x1a, (byte)0x1c, (byte)0x33, (byte)0x3a, (byte)0x3b, (byte)0xff};

                        int length_r = 308;
                        int length_d = 342;
			
			DatagramPacket p = new DatagramPacket(request, length_r, InetAddress.getByName(serverIP), serverPort);
			socket.send(p); //throws i/o exception
			socket.send(p);
			System.out.println("Connection Established Successfully!");
			System.out.println("Sending data: " + Arrays.toString(p.getData()));
			

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
		DHCPClient client;
            try {
                /*
                * if (args.length >= 1) { server = new
                * DHCPClient(Integer.parseInt(args[0])); } else {
                */
                client = new DHCPClient();
            } catch (Exception ex) {
                Logger.getLogger(DHCPClient.class.getName()).log(Level.SEVERE, null, ex);
            }
		//DHCPMessage msgTest = new DHCPMessage();
		printMacAddress();
		// }

	}
	
	public static byte[] getMacAddress() {
		byte[] mac = null;
		try {
			InetAddress address = InetAddress.getLocalHost();

			/*
			 * Get NetworkInterface for the current host and then read the
			 * hardware address.
			 */
			NetworkInterface ni = NetworkInterface.getByInetAddress(address);
			mac = ni.getHardwareAddress();

			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		assert(mac != null);
		return mac;
	}
	
	public static void printMacAddress() {
		try {
			InetAddress address = InetAddress.getLocalHost();

			/*
			 * Get NetworkInterface for the current host and then read the
			 * hardware address.
			 */
			NetworkInterface ni = NetworkInterface.getByInetAddress(address);
			byte[] mac = ni.getHardwareAddress();

			/*
			 * Extract each array of mac address and convert it to hexa with the
			 * . * following format 08-00-27-DC-4A-9E.
			 */
			for (int i = 0; i < mac.length; i++) {
				System.out.format("%02X%s", mac[i], (i < mac.length - 1) ? "-"
						: "");
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

}