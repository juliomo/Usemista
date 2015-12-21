package com.usm.jyd.usemista.services;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.acts.ActBase;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.objects.HVWeek;
import com.usm.jyd.usemista.objects.HorarioVirtual;
import com.usm.jyd.usemista.objects.Materia;
import com.usm.jyd.usemista.objects.UserTask;

import java.util.ArrayList;
import java.util.Calendar;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Created by der_w on 12/17/2015.
 */
public class ClassService extends JobService {

    public static final int NOTIFICATION_ID = 2;
    private NotificationManager mNotificationManager;

    public void sendNotification(String clase, String msg){
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(
                this,0, new Intent(this, ActBase.class),0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        //. setSmallIcon(R.drawable.ic)
                        .setContentTitle(clase)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setContentText(msg)
                        .setAutoCancel(true)
                .setColor(0xff5C6BC0)
                ;

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(
                NOTIFICATION_ID,mBuilder.build()
        );
    }
    public  void setNotyIfHour(Calendar hrIni, Calendar currentCal, Materia materia, HVWeek hvWeek){


        if(hrIni.get(Calendar.HOUR_OF_DAY)==(currentCal.get(Calendar.HOUR_OF_DAY)+1)&&
                (hrIni.get(Calendar.MINUTE)+currentCal.get(Calendar.MINUTE))<60){
            sendNotification(materia.getTitulo(),
                    (hvWeek.getAula()+" "+
                            hvWeek.getTimeIniToText()+" - "+
                            hvWeek.getTimeEndToText()));

        }else if(hrIni.get(Calendar.HOUR_OF_DAY)==currentCal.get(Calendar.HOUR_OF_DAY) &&
                 hrIni.get(Calendar.MINUTE)<= currentCal.get(Calendar.MINUTE)  ){
            sendNotification(materia.getTitulo(),
                    (hvWeek.getAula()+" "+
                            hvWeek.getTimeIniToText()+" - "+
                            hvWeek.getTimeEndToText()));
        }
    }

    @Override
    public boolean onStartJob(JobParameters params) {

       // L.t(this, "Funciona");

      //  new ClyEvTask(this).execute(params);

        ArrayList<HVWeek> listHorario = MiAplicativo.getWritableDatabase().getAllHorarioVirtualWeek();
        ArrayList<UserTask> listEventos = MiAplicativo.getWritableDatabase().getAllUserTask();

        Calendar currentCal= Calendar.getInstance();

        if(!listEventos.isEmpty()){
            for (int i=0; i<listEventos.size();i++){
                Calendar eventDay= Calendar.getInstance();
                eventDay.setTime(listEventos.get(i).getDayTime());
                if(eventDay.get(Calendar.DAY_OF_YEAR)==currentCal.get(Calendar.DAY_OF_YEAR)){

                    Calendar hrIni= Calendar.getInstance();
                    hrIni.setTime(listEventos.get(i).getHrIni());
                    UserTask userEvent= listEventos.get(i);

                    if(hrIni.get(Calendar.HOUR_OF_DAY)==(currentCal.get(Calendar.HOUR_OF_DAY)+1)&&
                            (hrIni.get(Calendar.MINUTE)+currentCal.get(Calendar.MINUTE))<60){

                        sendNotification(userEvent.getType()+" "+userEvent.getMtName(),
                                userEvent.getSalon()+" "+
                                userEvent.getHrIniToText()+" - "+
                                userEvent.getHrEndToText());

                    }else if(hrIni.get(Calendar.HOUR_OF_DAY)==currentCal.get(Calendar.HOUR_OF_DAY) &&
                            hrIni.get(Calendar.MINUTE)<= currentCal.get(Calendar.MINUTE)  ){
                        sendNotification(userEvent.getType()+" "+userEvent.getMtName(),
                                userEvent.getSalon()+" "+
                                        userEvent.getHrIniToText()+" - "+
                                        userEvent.getHrEndToText());
                    }
                }
            }
        }

        if(!listHorario.isEmpty()){
            for(int i=0;i<listHorario.size();i++){
                if(listHorario.get(i).getWeekDay().equals("Lun")&&
                        currentCal.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY){

                    Calendar hrIni=Calendar.getInstance();
                    hrIni.setTime( listHorario.get(i).getTimeIni());
                    Materia materia=MiAplicativo.getWritableDatabase().getOneUserMateria(listHorario.get(i).getCod());
                    HVWeek  hvWeek =listHorario.get(i);
                    setNotyIfHour(hrIni,currentCal,materia,hvWeek);



                }else  if(listHorario.get(i).getWeekDay().equals("Mar")&&
                        currentCal.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY){

                    Calendar hrIni=Calendar.getInstance();
                    hrIni.setTime( listHorario.get(i).getTimeIni());
                    Materia materia=MiAplicativo.getWritableDatabase().getOneUserMateria(listHorario.get(i).getCod());
                    HVWeek  hvWeek =listHorario.get(i);
                    setNotyIfHour(hrIni,currentCal,materia,hvWeek);

                }else if(listHorario.get(i).getWeekDay().equals("Mie")&&
                        currentCal.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY){

                    Calendar hrIni=Calendar.getInstance();
                    hrIni.setTime( listHorario.get(i).getTimeIni());
                    Materia materia=MiAplicativo.getWritableDatabase().getOneUserMateria(listHorario.get(i).getCod());
                    HVWeek  hvWeek =listHorario.get(i);
                    setNotyIfHour(hrIni,currentCal,materia,hvWeek);

                }else if(listHorario.get(i).getWeekDay().equals("Jue")&&
                        currentCal.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY){

                    Calendar hrIni=Calendar.getInstance();
                    hrIni.setTime( listHorario.get(i).getTimeIni());
                    Materia materia=MiAplicativo.getWritableDatabase().getOneUserMateria(listHorario.get(i).getCod());
                    HVWeek  hvWeek =listHorario.get(i);
                    setNotyIfHour(hrIni,currentCal,materia,hvWeek);

                }else if(listHorario.get(i).getWeekDay().equals("Vie")&&
                        currentCal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY){



                    Calendar hrIni=Calendar.getInstance();
                    hrIni.setTime( listHorario.get(i).getTimeIni());
                    Materia materia=MiAplicativo.getWritableDatabase().getOneUserMateria(listHorario.get(i).getCod());
                    HVWeek  hvWeek =listHorario.get(i);
                    setNotyIfHour(hrIni,currentCal,materia,hvWeek);



                }
            }
        }


        jobFinished(params, false);
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {

        return false;
    }




}
