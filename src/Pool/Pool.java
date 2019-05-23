/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pool;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author julia
 */
public class Pool {
    
    private byte[] redIP;
    private byte[] gatewayIP;
    private byte[] primerIP;
    private byte[] ultimaIP;
    private byte[] DNS;
    private byte[] broadcast;
    private byte[] mask;
    private long hostDisp;
    private ArrayList<byte[]> asignadas;
    private int time;

    public Pool(byte[] redIP, byte[] gatewayIP, byte[] primerIP, byte[] ultimaIP, byte[] DNS, byte[] broadcast, byte[] mask, long hostDisp, int t) {
        this.redIP = redIP;
        this.gatewayIP = gatewayIP;
        this.primerIP = primerIP;
        this.ultimaIP = ultimaIP;
        this.DNS = DNS;
        this.broadcast = broadcast;
        this.mask = mask;
        this.hostDisp = hostDisp;
        asignadas = new ArrayList<byte[]>();
        this.time = t;
    }
    
    
    public boolean searchIP(byte[] ipS){
        boolean found = false;
        
        for (byte[] asig : this.asignadas) {
            if(ipS.equals(asig)){
                found = true;
            }   
        }
        
        return found;
    }

    public byte[] getRedIP() {
        return redIP;
    }

    public byte[] getGatewayIP() {
        return gatewayIP;
    }

    public byte[] getPrimerIP() {
        return primerIP;
    }

    public byte[] getUltimaIP() {
        return ultimaIP;
    }

    public byte[] getDNS() {
        return DNS;
    }

    public byte[] getBroadcast() {
        return broadcast;
    }

    public byte[] getMask() {
        return mask;
    }

    public long getHostDisp() {
        return hostDisp;
    }

    public ArrayList<byte[]> getAsignadas() {
        return asignadas;
    }
    
    public byte[] findNext(){
    
        for (long i = Utils.Utils.bytesToLong(this.primerIP); i <= Utils.Utils.bytesToLong(this.ultimaIP); i++)
        {
            boolean estaAsignado = false;
            byte[] ip = Utils.Utils.longToByte(i);
                
            for (byte[] asig : asignadas)
            {
                //System.out.println("asignada:"+Utils.Utils.IPToString(asig));
                //System.out.println("comparada:"+Utils.Utils.IPToString(ip));
                
                if(Arrays.equals(ip, asig))
                {
                    //System.out.println("ENTRE");
                    estaAsignado = true;
                }
            }
            if(!estaAsignado)
            {
                return (ip);
            }
        }
        return null;
}
    
    public void addAsigned(byte[] ipN){
        this.asignadas.add(ipN);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
    
    
}
