package com.usm.jyd.usemista.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by der_w on 11/19/2015.
 */
public class HVWeek {

    private String id;
    private String cod;
    private String modulo;
    private String aula;
    private String weekDay;
    private Calendar timeIni;
    private Calendar timeEnd;

    public HVWeek(){}
    public HVWeek(String id,
                          String cod,
                          String modulo,
                          String aula,
                            String weekDay,
                          Calendar timeIni,
                          Calendar timeEnd,
                          int color){
        this.id=id;
        this.cod=cod;
        this.modulo=modulo;
        this.aula=aula;
        this.weekDay=weekDay;
        this.timeIni=timeIni;
        this.timeEnd=timeEnd;
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

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public Date getTimeIni() {
        // return calIni.getTime();

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        Date date = new Date();
        dateFormat.format(date);
        date=timeIni.getTime();
        return date;

    }
    public String getTimeIniToText(){
        return (timeIni.get(Calendar.HOUR_OF_DAY)<=12 ?
                timeIni.get(Calendar.HOUR_OF_DAY):
                (timeIni.get(Calendar.HOUR_OF_DAY)-12))+" : "+

                (timeIni.get(Calendar.MINUTE)<=9 ?
                        "0"+timeIni.get(Calendar.MINUTE):
                        timeIni.get(Calendar.MINUTE));
    }

    public void setTimeIni(Date calIni) {
        this.timeIni= Calendar.getInstance();
        this.timeIni.setTime(calIni);
    }public void setTimeIni(Calendar calIni) {
        this.timeIni= calIni;
    }

    public Date getTimeEnd() {
        ///  return calEnd.getTime().toString();
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        Date date = new Date();
        dateFormat.format(date);
        date=timeEnd.getTime();
        return date;
    }
    public String getTimeEndToText(){
        return (timeEnd.get(Calendar.HOUR_OF_DAY)<=12 ?
                timeEnd.get(Calendar.HOUR_OF_DAY):
                (timeEnd.get(Calendar.HOUR_OF_DAY)-12))+" : "+

                (timeEnd.get(Calendar.MINUTE)<=9 ?
                        "0"+timeEnd.get(Calendar.MINUTE):
                        timeEnd.get(Calendar.MINUTE));
    }

    public void setTimeEnd(Date calEnd) {
        this.timeEnd= Calendar.getInstance();
        this.timeEnd.setTime(calEnd);
    }public void setTimeEnd(Calendar calEnd) {
        this.timeEnd=calEnd;
    }
}
