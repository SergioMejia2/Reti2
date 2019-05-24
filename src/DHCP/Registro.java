/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DHCP;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author julia
 */
public class Registro {

    private byte[] MAC;
    private byte[] ip;
    private int xid;
    private int messageType;
    private LocalDateTime horaInicio;
    private LocalDateTime horaRevocacion;

    public Registro(byte[] MAC, byte[] ip, LocalDateTime horaInicio, LocalDateTime horaRevocacion, int xid, int messageType) {
        this.MAC = MAC;
        this.ip = ip;
        this.xid = xid;
        this.messageType = messageType;
        this.horaInicio = horaInicio;
        this.horaRevocacion = horaRevocacion;
    }

    public Registro(byte[] MAC, byte[] ip, int xid, int messageType) {
        this.MAC = MAC;
        this.ip = ip;
        this.xid = xid;
        this.messageType = messageType;
        this.horaInicio = LocalDateTime.now();
        this.horaRevocacion = null;
    }

    public byte[] getMAC() {
        return MAC;
    }

    public int getXid() {
        return xid;
    }

    public byte[] getIp() {
        return ip;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public LocalDateTime getHoraRevocacion() {
        return horaRevocacion;
    }

    int getMessageType() {
        return this.messageType;
    }

    public void mostrar(Registro rr, FXMLDocumentController control) {
        byte[] aux = null;
        if (rr.getMAC() != null) {
            aux = Arrays.copyOfRange(rr.getMAC(), 0, 6);
        }
        String msg;
        if (rr.getMessageType() == 1) {
            msg = "[" + rr.getHoraInicio() + "] " + " {" + rr.getXid() + "} " + " - " + "DHCPDISCOVER" + " - MAC: " + Utils.Utils.bytesToString(aux);
            System.out.println(msg);
            control.update(msg);
            try {
                Utils.Persistencia.escibirLog(msg);
            } catch (Exception ex) {
                Logger.getLogger(Registro.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (rr.getMessageType() == 2) {
            msg = "[" + rr.getHoraInicio() + "] " + " {" + rr.getXid() + "} " + " - " + "DHCPOFFER" + " - MAC: " + Utils.Utils.bytesToString(aux) + " - " + Utils.Utils.IPToString(rr.getIp()) + " - Arrendado desde: " + rr.getHoraInicio() + " - hasta: " + rr.getHoraRevocacion();
            System.out.println(msg);
            control.update(msg);
            try {
                Utils.Persistencia.escibirLog(msg);
            } catch (Exception ex) {
                Logger.getLogger(Registro.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (rr.getMessageType() == 3) {
            msg = "[" + rr.getHoraInicio() + "] " + " {" + rr.getXid() + "} " + " - " + "DHCPREQUEST" + " - MAC: " + Utils.Utils.bytesToString(aux) + " - " + Utils.Utils.IPToString(rr.getIp());
            System.out.println(msg);
            control.update(msg);
            try {
                Utils.Persistencia.escibirLog(msg);
            } catch (Exception ex) {
                Logger.getLogger(Registro.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (rr.getMessageType() == 5) {
            msg = "[" + rr.getHoraInicio() + "] " + " {" + rr.getXid() + "} " + " - " + "DHCPACK" + " - MAC: " + Utils.Utils.bytesToString(aux) + " - " + Utils.Utils.IPToString(rr.getIp());
            System.out.println(msg);
            control.update(msg);
            try {
                Utils.Persistencia.escibirLog(msg);
            } catch (Exception ex) {
                Logger.getLogger(Registro.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (rr.getMessageType() == 6) {
            if (aux != null) {
                msg = "[" + rr.getHoraInicio() + "] " + " {" + rr.getXid() + "]} " + " - " + "DHCPNACK" + " - MAC: " + Utils.Utils.bytesToString(aux);
                System.out.println(msg);
                control.update(msg);
                try {
                    Utils.Persistencia.escibirLog(msg);
                } catch (Exception ex) {
                    Logger.getLogger(Registro.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                msg = "[" + rr.getHoraInicio() + "] " + " {" + rr.getXid() + "} " + " - " + "DHCPNACK DE REVOCACION MANUAL";
                System.out.println(msg);
                control.update(msg);
                try {
                    Utils.Persistencia.escibirLog(msg);
                } catch (Exception ex) {
                    Logger.getLogger(Registro.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (rr.getMessageType() == 10) {
            msg = "[" + rr.getHoraInicio() + "] " + " {" + rr.getXid() + "} " + " - " + "RENOVACION DE IP" + " - MAC: " + Utils.Utils.bytesToString(aux) + " - " + Utils.Utils.IPToString(rr.getIp()) + " - Arrendado desde: " + rr.getHoraInicio() + " - hasta: " + rr.getHoraRevocacion();
            System.out.println(msg);
            control.update(msg);
            try {
                Utils.Persistencia.escibirLog(msg);
            } catch (Exception ex) {
                Logger.getLogger(Registro.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (rr.getMessageType() == 7) {
            msg = "[" + rr.getHoraInicio() + "] " + " {" + rr.getXid() + "} " + " - " + "RELEALSE" + " - MAC: " + Utils.Utils.bytesToString(aux) + " - Hora de liberacion: " + rr.getHoraInicio();
            System.out.println(msg);
            control.update(msg);
            try {
                Utils.Persistencia.escibirLog(msg);
            } catch (Exception ex) {
                Logger.getLogger(Registro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
