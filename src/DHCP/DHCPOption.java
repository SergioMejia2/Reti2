package DHCP;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author julia
 */
public class DHCPOption
{
    //DHCP Message Types
    public static final int DHCPDISCOVER = 1;
    public static final int DHCPOFFER = 2;
    public static final int DHCPREQUEST = 3;
    public static final int DHCPDECLINE = 4;
    public static final int DHCPACK = 5;
    public static final int DHCPNACK = 6;
    public static final int DHCPRELEASE = 7;
    public static final int RENOVACIONIP = 10;
	
    //DHCP Option Identifiers
    public static final int DHCPMESSAGETYPE = 53;
    
    private byte opLength;
    private byte[] opData;

    public DHCPOption(byte opLength, byte[] opData)
    {
        this.opLength = opLength;
        this.opData = opData;
    }

    public byte getOpLength() {
        return opLength;
    }

    public void setOpLength(byte opLength) {
        this.opLength = opLength;
    }

    public byte[] getOpData() {
        return opData;
    }

    public void setOpData(byte[] opData) {
        this.opData = opData;
    }

    @Override
    public String toString() {
        return "DHCPOption: " + "opLength=" + opLength + ", opData=" + opData;
    }
    
    
}
