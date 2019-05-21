/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import DHCP.Equipo;
import Pool.Pool;
import Pool.PoolManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 *
 * @author julia
 */
public class Persistencia {
    	public static void leerSubredes(PoolManager poolmanager) throws Exception
	{
		BufferedReader br = new BufferedReader (new FileReader ("./info.txt"));
		String linea = br.readLine();
                
		while(linea != null) {
			long hostDisponibles = Integer.parseInt(linea.trim());
			linea = br.readLine();
                        byte[] red = InetAddress.getByName(linea).getAddress();
                        linea = br.readLine();
                        byte[] mask = InetAddress.getByName(linea).getAddress();
                        linea = br.readLine();
                        byte[] gateway = InetAddress.getByName(linea).getAddress();
                        linea = br.readLine();
                        byte[] primerIP = InetAddress.getByName(linea).getAddress();
                        linea = br.readLine();
                        byte[] ultimaIP = InetAddress.getByName(linea).getAddress();
                        linea = br.readLine();
                        byte[] broadcast = InetAddress.getByName(linea).getAddress();
                        linea = br.readLine();
                        byte[] dnsIP = InetAddress.getByName(linea).getAddress();
                        linea = br.readLine();
                        Pool pool = new Pool(red, gateway, primerIP, ultimaIP, dnsIP, broadcast, mask, hostDisponibles);
                        poolmanager.addPool(pool);
                }
		br.close();
	}
        
        public static void generarLog(ArrayList<Equipo> asignados) throws Exception
	{
		File archivo = new File("./log.txt");
		PrintWriter pw = new PrintWriter(archivo);
		pw.println("LOG DE REPORTE");
		pw.println();
                
                for (Equipo asignado : asignados) {
                pw.println("MAC: "+Utils.bytesToString(asignado.getMAC()));
                pw.println("Ip: "+Utils.IPToString(asignado.getIp()));
                pw.println("Hora Inicio: "+asignado.getHoraInicio());
            }
                
		pw.close();
	}
}
