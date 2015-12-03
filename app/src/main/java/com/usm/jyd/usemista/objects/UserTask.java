package com.usm.jyd.usemista.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by der_w on 11/25/2015.
 */
public class UserTask {

    private int id;
    private String cod;
    private String type;
    private Calendar dayTime;
    private Calendar hrIni;
    private Calendar hrEnd;
    private String cmplt;
    private int nota;
    private String mtName;
    private String salon;

    public UserTask(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDayTime() {
        // return calIni.getTime();

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        Date date = new Date();
        dateFormat.format(date);
        date=dayTime.getTime();
        return date;

    }
    public String getDayTimeToText(){
        return (dayTime.get(Calendar.DAY_OF_MONTH)<=9 ?
                "0"+dayTime.get(Calendar.DAY_OF_MONTH):
                dayTime.get(Calendar.DAY_OF_MONTH))+"/"+

                ((dayTime.get(Calendar.MONTH)+1)<=9 ?
                        "0"+(dayTime.get(Calendar.MONTH)+1):
                        (dayTime.get(Calendar.MONTH)+1))+"/"+
                dayTime.get(Calendar.YEAR);
    }

    public void setDayTime(Date dayTime) {
        this.dayTime= Calendar.getInstance();
        this.dayTime.setTime(dayTime);
    }
    public void setDayTime(Calendar dayTime) {
        this.dayTime= dayTime;
    }


    public Date getHrIni() {
        // return calIni.getTime();

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        Date date = new Date();
        dateFormat.format(date);
        date=hrIni.getTime();
        return date;

    }
    public String getHrIniToText(){
        return (hrIni.get(Calendar.HOUR_OF_DAY)<=12 ?
                hrIni.get(Calendar.HOUR_OF_DAY):
                (hrIni.get(Calendar.HOUR_OF_DAY)-12))+" : "+

                (hrIni.get(Calendar.MINUTE)<=9 ?
                        "0"+hrIni.get(Calendar.MINUTE):
                        hrIni.get(Calendar.MINUTE));
    }

    public void setHrIni(Date hrIni) {
        this.hrIni= Calendar.getInstance();
        this.hrIni.setTime(hrIni);
    }public void setHrIni(Calendar hrIni) {
        this.hrIni= hrIni;
    }

    public Date getHrEnd() {
        ///  return calEnd.getTime().toString();
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
        Date date = new Date();
        dateFormat.format(date);
        date=hrEnd.getTime();
        return date;
    }
    public String getHrEndToText(){
        return (hrEnd.get(Calendar.HOUR_OF_DAY)<=12 ?
                hrEnd.get(Calendar.HOUR_OF_DAY):
                (hrEnd.get(Calendar.HOUR_OF_DAY)-12))+" : "+

                (hrEnd.get(Calendar.MINUTE)<=9 ?
                        "0"+hrEnd.get(Calendar.MINUTE):
                        hrEnd.get(Calendar.MINUTE));
    }

    public void setHrEnd(Date hrEnd) {
        this.hrEnd= Calendar.getInstance();
        this.hrEnd.setTime(hrEnd);
    }public void setHrEnd(Calendar hrEnd) {
        this.hrEnd=hrEnd;
    }


    public String getCmplt() {
        return cmplt;
    }

    public void setCmplt(String cmplt) {
        this.cmplt = cmplt;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getMtName() {
        return mtName;
    }

    public void setMtName(String mtName) {
        this.mtName = mtName;
    }

    public String getSalon() {
        return salon;
    }

    public void setSalon(String salon) {
        this.salon = salon;
    }
}
