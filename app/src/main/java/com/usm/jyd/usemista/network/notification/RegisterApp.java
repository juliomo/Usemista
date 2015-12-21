package com.usm.jyd.usemista.network.notification;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.games.request.Requests;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.usm.jyd.usemista.acts.ActBase;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.network.Key;
import com.usm.jyd.usemista.network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by der_w on 10/15/2015.
 */
public class RegisterApp extends AsyncTask<Void, Void, String> {

    private static final String TAG = "GCMRelated";
    Context context;
    GoogleCloudMessaging gcm;
    String SENDER_ID="631318156323";
    String regid= null;
    private  int appVersion;

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;


    public  RegisterApp(Context context, GoogleCloudMessaging gcm, int appVersion){
        this.context=context;
        this.gcm=gcm;
        this.appVersion=appVersion;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        String msg="";
        try{
            if(gcm ==null){
                gcm =GoogleCloudMessaging.getInstance(context);
            }

            regid = gcm.register(SENDER_ID);
            msg="Device Registrado, Reg ID= "+regid;

            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
            MiAplicativo.getWritableDatabase().updateUserRegistroGCM(regid);
            sendRegistrationIdToBackend();

            // For this demo: we don't need to send it because the device
            // will send upstream messages to a server that echo back the
            // message using the 'from' address in the message.

            // Persist the regID - no need to register again.
            storeRegistrationId(context, regid);

        }catch (IOException e){
            msg="Error: "+e.getMessage();
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.
        }

        return msg;
    }

    private void sendRegistrationIdToBackend() {
        volleySingleton=VolleySingleton.getInstance();
        requestQueue=volleySingleton.getRequestQueue();

    //   String url = "http://usmpemsun.esy.es/register?regId="+regid;
        String url = "http://usmpemsun.esy.es/register";


        Map<String, String> map = new HashMap<>();
        map.put("regId", regid);

        JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST,
                url,new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if(response==null || response.length()>0){
                    try{
                        String estado="NA";
                        String mensaje="NA";
                        if(response.has(Key.EndPointMateria.KEY_ESTADO)&&
                                !response.isNull(Key.EndPointMateria.KEY_ESTADO)){
                            estado = response.getString(Key.EndPointMateria.KEY_ESTADO);

                        }
                        if(response.has(Key.EndPointMateria.KEY_MENSAJE)&&
                                !response.isNull(Key.EndPointMateria.KEY_MENSAJE)){
                            mensaje = response.getString(Key.EndPointMateria.KEY_MENSAJE);

                        }
                        if(estado.equals("1")){
                            //L.t(context,"Estado: "+estado+" "+mensaje);
                        }else if(estado.equals("2")){
                          //  L.t(context,"Estado: "+estado+" "+mensaje);
                        }


                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);



    }


    private void storeRegistrationId(Context ctx, String regid) {
        final SharedPreferences prefs = ctx.getSharedPreferences(ActBase.class.getSimpleName(),
                Context.MODE_PRIVATE);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("registration_id", regid);
        editor.putInt("appVersion", appVersion);
        editor.commit();

    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Toast.makeText(context,
                "Registration Completed. Now you can see the notifications",
                Toast.LENGTH_SHORT).show();
        Log.v(TAG, s);
    }
}
