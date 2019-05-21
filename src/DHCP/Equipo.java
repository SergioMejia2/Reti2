/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DHCP;

import java.time.LocalDateTime;

/**
 *
 * @author julia
 */
public class Equipo {
    
    private byte[] MAC;
    private byte[] ip;
    private LocalDateTime horaInicio;
    private LocalDateTime horaRevocacion;

    public Equipo(byte[] MAC, byte[] ip, LocalDateTime horaInicio, LocalDateTime horaRevocacion) {
        this.MAC = MAC;
        this.ip = ip;
        this.horaInicio = horaInicio;
        this.horaRevocacion = horaRevocacion;
    }
    
    public Equipo(byte[] MAC, byte[] ip) {
        this.MAC = MAC;
        this.ip = ip;
        this.horaInicio = LocalDateTime.now();
        this.horaRevocacion = null;
    }

    public byte[] getMAC() {
        return MAC;
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
}
