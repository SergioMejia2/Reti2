/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pool;

import Utils.Persistencia;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author julia
 */
public class PoolManager {
    
    private ArrayList<Pool> subRedes;

    public PoolManager(ArrayList<Pool> subRedes) {
        this.subRedes = subRedes;
    }

    public PoolManager() throws Exception
    {
        this.subRedes = new ArrayList<Pool>();
        Persistencia.leerSubredes(this);
    }

    public ArrayList<Pool> getSubRedes() {
        return subRedes;
    }

    public void setSubRedes(ArrayList<Pool> subRedes) {
        this.subRedes = subRedes;
    }

    public void addPool(Pool pool)
    {
        subRedes.add(pool);
    }
    
    public Pool searchPool(byte[] getw){
        Pool found = null;
        
        for (Pool ss : this.subRedes) {
            if(Arrays.equals(ss.getGatewayIP(), getw))
            {
                found = ss;
            }
        }
        return found;
    }
    
    public byte[] asignarIP(byte[] getw) throws Exception{
        Pool gateway = searchPool(getw);
        byte[] newIP = null;
        
        if(gateway == null){
            throw new Exception("La direccion Ip "+Utils.Utils.IPToString(getw)+" no se encuentra en la topologia");
        }else{
            newIP = gateway.findNext();
            if(newIP == null){
                throw new Exception("No hay IPs disponibles");
            }else{
                gateway.addAsigned(newIP);
            }
        }
        return newIP;
    }
    
    public byte[] asignarIP(byte[] getw, byte[] ipB) throws Exception{
        Pool gateway = searchPool(getw);
        byte[] newip = null;
        
        if(gateway == null){
            throw new Exception("Esta subred no se encuentra en la topologia");
        }
        else{
            boolean asignada = gateway.searchIP(ipB);
            
            if(asignada){
                newip = asignarIP(getw);
            }
            else{
                newip = ipB;
                gateway.addAsigned(ipB);
            }
        }
        return newip;
    }

    public Pool findPoolByIP(byte[] address)
    {
        for (Pool subRed : subRedes)
        {            
            boolean isinrange = Utils.Utils.isIpInRange(subRed.getPrimerIP(),subRed.getUltimaIP(),address);
            if(isinrange)
                return subRed;
        }
        return null;
    }
    
}
