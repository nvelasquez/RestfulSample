package com.restfultest;

/**
 * Created by Novalogiq on 2/9/2015.
 */
public class Feriado {
    private int id;
    private String fecha_original;
    private String fecha_movido;
    private String motivo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha_original() {
        return fecha_original;
    }

    public void setFecha_original(String fecha_original) {
        this.fecha_original = fecha_original;
    }

    public String getFecha_movido() {
        return fecha_movido;
    }

    public void setFecha_movido(String fecha_movido) {
        this.fecha_movido = fecha_movido;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
