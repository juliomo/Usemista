package com.usm.jyd.usemista.objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by der_w on 11/18/2015.
 */
public class HorarioVirtual {

    private String id;
    private String cod;
    private String titulo;
    private String calendar;
    private Calendar calIni;
    private Calendar calEnd;
    private int color;

    public HorarioVirtual(){}
    public HorarioVirtual(String id,
                      String cod,
                      String titulo,
                      String calendar,
                          Calendar calIni,
                          Calendar calEnd,
                          int color){
        this.id=id;
        this.cod=cod;
        this.titulo=titulo;
        this.calendar=calendar;
        this.calIni=calIni;
        this.calEnd=calEnd;
        this.color=color;
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public Date getCalIni() {
       // return calIni.getTime();

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        Date date = new Date();
        dateFormat.format(date);
        date=calIni.getTime();
        return date;

    }
    public String getCalIniToText(){
        return (calIni.get(Calendar.DAY_OF_MONTH)<=9 ?
                "0"+calIni.get(Calendar.DAY_OF_MONTH):
                calIni.get(Calendar.DAY_OF_MONTH))+"/"+

                ((calIni.get(Calendar.MONTH)+1)<=9 ?
                        "0"+(calIni.get(Calendar.MONTH)+1):
                        (calIni.get(Calendar.MONTH)+1))+"/"+
                calIni.get(Calendar.YEAR)+" Hasta";
    }

    public void setCalIni(Date calIni) {
        this.calIni= Calendar.getInstance();
        this.calIni.setTime(calIni);
    }public void setCalIni(Calendar calIni) {
        this.calIni= calIni;
    }

    public Date getCalEnd() {
      ///  return calEnd.getTime().toString();
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        Date date = new Date();
        dateFormat.format(date);
        date=calEnd.getTime();
        return date;
    }
    public String getCalEndToText(){
        return (calEnd.get(Calendar.DAY_OF_MONTH)<=9 ?
                "0"+calEnd.get(Calendar.DAY_OF_MONTH):
                calEnd.get(Calendar.DAY_OF_MONTH))+"/"+

                ((calEnd.get(Calendar.MONTH)+1)<=9 ?
                "0"+(calEnd.get(Calendar.MONTH)+1):
                        (calEnd.get(Calendar.MONTH)+1))+"/"+

                calEnd.get(Calendar.YEAR);
    }

    public void setCalEnd(Date calEnd) {
        this.calEnd= Calendar.getInstance();
        this.calEnd.setTime(calEnd);
    }public void setCalEnd(Calendar calEnd) {
        this.calEnd=calEnd;
    }

    public int getColor(){return color;}

    public void setColor(int color){this.color=color;}

}
