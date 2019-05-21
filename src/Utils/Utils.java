/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.net.InetAddress;
import java.nio.ByteBuffer;

/**
 *
 * @author Sergio A. Mejía
 */
public class Utils {

    public static byte[] shortToByte(short val) {

        byte[] arr = new byte[]{
            (byte) (val >>> 8), (byte) (val & 0xFF)
        };
        return arr;
    }

    public static void printBytes(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : arr) {
            sb.append(String.format("%02X ", b));
        }
        System.out.println(sb.toString());
    }

    public static String bytesToString(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (byte b : arr) {
            sb.append(String.format("%02X:", b));
        }
        
        return (sb.length() > 0 )?(sb.deleteCharAt(sb.length() - 1).toString()):(sb.toString());
    }
    
    public static String IPToString(byte[] arr)
    {
        StringBuilder sb = new StringBuilder();
        for (byte b : arr) {
            int num = b & 0xff;
            sb.append(num);
            sb.append(".");
        }
        
        return (sb.length() > 0 )?(sb.deleteCharAt(sb.length() - 1).toString()):(sb.toString());
    }
    
    public static String BytesToText(byte[] arr)
    {
        String s = new String(arr);
        return s;
    }

    public static byte[] intToBytes(int i)
    {
        byte[] dword = new byte[4];
        dword[0] = (byte) ((i >> 24) & 0x000000FF);
        dword[1] = (byte) ((i >> 16) & 0x000000FF);
        dword[2] = (byte) ((i >> 8) & 0x000000FF);
        dword[3] = (byte) (i & 0x00FF);
        return dword;
    }
    
    public static boolean isIpZero(byte[] ip)
    {
        if(ip[0] == 0 && ip[1] == 0 && ip[2] == 0 && ip[3] == 0)
            return true;
        return false;
    }
    
    public static long  bytesToLong(byte[] ip)
    {
        long result = 0;
        for(byte byt : ip)
        {
            result <<= 8;
            result |= byt & 0xff;
        }
        return result;
    }
    
    public static byte[] longToByte(long x)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        
        byte[] longinb = buffer.array();             
        byte[] retorno = new byte[4];
        for(int i = 0; i < 4; i++)
        {
            retorno[i] = longinb[i+4];
        }
        return retorno;
    }

    public static boolean isIpInRange(byte[] primerIP, byte[] ultimaIP, byte[] address)
    {
        long primer = bytesToLong(primerIP);
        long ultima = bytesToLong(ultimaIP);
        long dir = bytesToLong(address);
        
        if(primer <= dir && dir <= ultima)
            return true;
        else return false;
    }

}
