package com.usm.jyd.usemista.network.notification;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by der_w on 10/15/2015.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver{
    @Override
    //indicamos que GcmIntentService manejara el intent
    public void onReceive(Context context, Intent intent) {
        ComponentName comp= new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());
    //Iniciar Servicio, manteniendo el dispositivo encendido mientras procede
        startWakefulService(context,(intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
