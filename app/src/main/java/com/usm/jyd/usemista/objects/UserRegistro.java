package com.usm.jyd.usemista.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by der_w on 12/4/2015.
 */
public class UserRegistro {

    private String notiGCM;
    private String status;
    private String nomb;
    private String ci;
    private Calendar dayTime;

    public UserRegistro(){}

    public String  getNotiGCM() {
        return notiGCM;
    }

    public void setNotiGCM(String notiGCM) {
        this.notiGCM = notiGCM;
    }

    public String  getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String  getNomb() {
        return nomb;
    }

    public void setNomb(String nomb) {
        this.nomb = nomb;
    }

    public String  getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
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
    public void setDayTime(Date dayTime) {
        this.dayTime= Calendar.getInstance();
        this.dayTime.setTime(dayTime);
    }
    public void setDayTime(Calendar dayTime) {
        this.dayTime= dayTime;
    }



}
