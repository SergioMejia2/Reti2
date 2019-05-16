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
