package com.usm.jyd.usemista.events;

/**
 * Created by der_w on 11/14/2015.
 */
public interface HVTimeToSet {
    void seteoDeTiempo(int hora, int minuto,String weekDay, int itemPoss);
    void seteoDeFecha(int year, int monthOfYear, int dayOfMonth, int itemPoss);
    void seteoDeColor(int color);
}
