package com.example.diegox101.models;

/**
 * Created by Diegox101 on 03/09/2015.
 */
public class Ciudad {
    private String nombre = "";
    private String temperatura = "";
    private String imagen = "";
    private String humedad = "";
    private String tempMax = "";
    private String tempMin = "";
    private String velocViento = "";
    private String presion = "";

    public Ciudad(){

    }

    public Ciudad(String nombre, String temperatura, String imagen) {
        this.nombre = nombre;
        this.temperatura = temperatura;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getHumedad() {
        return humedad;
    }

    public void setHumedad(String humedad) {
        this.humedad = humedad;
    }

    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempMin() {
        return tempMin;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public String getVelocViento() {
        return velocViento;
    }

    public void setVelocViento(String velocViento) {
        this.velocViento = velocViento;
    }

    public String getPresion() {
        return presion;
    }

    public void setPresion(String presion) {
        this.presion = presion;
    }
}
