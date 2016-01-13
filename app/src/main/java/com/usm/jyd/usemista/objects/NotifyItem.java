package com.usm.jyd.usemista.objects;

/**
 * Created by der_w on 12/25/2015.
 */
public class NotifyItem {

    private int id;
    private String clase;
    private String msj;
    private String mod;
    public NotifyItem(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getMsj() {
        return msj;
    }

    public void setMsj(String msj) {
        this.msj = msj;
    }

    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }
}
