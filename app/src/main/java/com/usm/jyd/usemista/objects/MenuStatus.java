package com.usm.jyd.usemista.objects;

import android.view.Menu;

/**
 * Created by der_w on 11/5/2015.
 */
public class MenuStatus {


    private String id;
    private String cod;
    private String item;
    private String activo;

    public MenuStatus(){}
    public MenuStatus(String id,
                      String cod,
                      String item,
                      String activo){
        this.id=id;
        this.cod=cod;
        this.item=item;
        this.activo=activo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

}
