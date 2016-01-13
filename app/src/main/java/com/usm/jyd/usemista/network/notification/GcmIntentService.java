package com.usm.jyd.usemista.network.notification;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.acts.ActBase;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.objects.NotifyItem;

/**
 * Created by der_w on 10/15/2015.
 */
public class GcmIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private static final  String TAG = "GcmIntentService";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras  = intent.getExtras();
        GoogleCloudMessaging gcm =GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.

        String messageType = gcm.getMessageType(intent);

        if(!extras.isEmpty()){ // has effect of unparcelling Bundle
             /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if(GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)){
                sendNotification("Send error: " + extras.toString(),"Error","base");
            }else if(GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)){
                sendNotification("Deleted messages on server: " +
                        extras.toString(),"Error","base");
            }else if(GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)){

                for (int i =0; i<5; i++){
                    Log.i(TAG, "Working... " + (i + 1)
                            + "/5 @ " + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                sendNotification(extras.getString("Notice"),extras.getString("Materia"),extras.getString("Modulo"));
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg,String mt, String mod){
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(
                this,0, new Intent(this, ActBase.class),0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
         //. setSmallIcon(R.drawable.ic)
        .setContentTitle(mt)
        .setAutoCancel(true)
       // .setSmallIcon(R.mipmap.ic_launcher)
        .setStyle(new NotificationCompat.BigTextStyle())
        .setContentText(msg)
        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
        .setLights(Color.BLUE, 3000, 3000);

        if(mod.equals("base"))
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        else if(mod.equals("ingSis"))
            mBuilder.setSmallIcon(R.drawable.ic_gear_white_24dp_01);
        else if(mod.equals("telecom"))
            mBuilder.setSmallIcon(R.drawable.ic_telecom_white_24dp_01);
        else if(mod.equals("ingInd"))
            mBuilder.setSmallIcon(R.drawable.ic_industrial_01_white_24dp);
        else if(mod.equals("ingCiv"))
            mBuilder.setSmallIcon(R.drawable.ic_civil_01_white_24dp);
        else if(mod.equals("arq"))
            mBuilder.setSmallIcon(R.drawable.ic_arq_01_white_24dp);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(
                NOTIFICATION_ID, mBuilder.build()
        );

        NotifyItem notifyItem= new NotifyItem();
        notifyItem.setClase(mt);notifyItem.setMsj(msg);
        notifyItem.setMod(mod);

        MiAplicativo.getWritableDatabase().insertNotiItem(notifyItem);


    }





}
