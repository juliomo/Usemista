package com.usm.jyd.usemista.acts;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.anim.AnimUtils;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.dialogs.UserRegiDialog;
import com.usm.jyd.usemista.events.CCBackUserRegi;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.network.Key;
import com.usm.jyd.usemista.network.VolleySingleton;
import com.usm.jyd.usemista.network.notification.RegisterApp;
import com.usm.jyd.usemista.objects.UserRegistro;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ActApertura extends AppCompatActivity implements CCBackUserRegi {

    private int SPLASH_TIME = 30000;


    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private int validadorProfCod,validadorEstudiante;


    //VARIABLES EMPLEADAS PARA Push notifications
    private static final  int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = "GCMRelated";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    String regid;

    //VARIABLES DE REGISTRO
    public static final String ESTUDIANTE= "1", PROFESOR="2", GUESSoPAUSA="3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_apertura);

        volleySingleton=VolleySingleton.getInstance();
        requestQueue=volleySingleton.getRequestQueue();
        validadorProfCod=0;
        validadorEstudiante=0;


        ImageView imgEST=(ImageView)findViewById(R.id.imageViewEST);
        ImageView imgPROF=(ImageView)findViewById(R.id.imageViewPROF);
        TableLayout regiUser=(TableLayout)findViewById(R.id.regiUser);

        imgEST.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary));
        imgPROF.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary));

        if(getRegistrationId(getApplicationContext()).equals("") ){
            if(checkPlayServices()) {
                gcm=GoogleCloudMessaging.getInstance(getApplicationContext());
                regid=getRegistrationId(getApplicationContext());

                if(regid.isEmpty()){
                new RegisterApp(
                        getApplicationContext(),
                        gcm,
                        getAppVersion(getApplicationContext()))
                        .execute();
                }

            }
        }
        UserRegistro userRegistro=MiAplicativo.getWritableDatabase().getUserRegistro();
        if(!getRegistrationId(getApplicationContext()).equals("") &&
                userRegistro.getNotiGCM().equals("noAsig")     ){

            MiAplicativo.getWritableDatabase().updateUserRegistroGCM(getRegistrationId(getApplicationContext()));
        }

        if(userRegistro.getStatus().equals("3")){
            Calendar blockDate =Calendar.getInstance();Calendar now = Calendar.getInstance();
            blockDate.setTime(userRegistro.getDayTime());
            if(now.get(Calendar.DAY_OF_YEAR)> blockDate.get(Calendar.DAY_OF_YEAR)){
                MiAplicativo.getWritableDatabase().updateUserRegistroFail("0",now.getTime());
                userRegistro=MiAplicativo.getWritableDatabase().getUserRegistro();
            }else
                L.t(this, "Ingreso Bloqueado \nIntente mas Tarde");
        }

        if(userRegistro.getStatus().equals("0")){
            regiUser.setVisibility(View.VISIBLE);
            final UserRegiDialog userRegiDialog = new UserRegiDialog();

            imgEST.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userRegiDialog.setStatus(ESTUDIANTE);
                    userRegiDialog.show(getSupportFragmentManager(),"Estudiante");
                }
            });
            imgPROF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userRegiDialog.setStatus(PROFESOR);
                    userRegiDialog.show(getSupportFragmentManager(), "Profesor");
                }
            });
        }
        else if(userRegistro.getStatus().equals("1")||userRegistro.getStatus().equals("2")){
            //animacion de presentacion Librerias By Daimajia 3 packs
            SPLASH_TIME=2000;
            YoYo.with(Techniques.Shake)
                    .duration(1000)
                    .playOn(findViewById(R.id.imageView));
            new AperturaDeAplicacion().execute();

        }


    }

/// INTERFAS DE REGISTRO /////////////////
    @Override
    public void onUserRegiComplete(String status, String nomb, String cIorProfCod) {

        Calendar now = Calendar.getInstance();
        Calendar blockDate =Calendar.getInstance();
        UserRegistro userRegistro =MiAplicativo.getWritableDatabase().getUserRegistro();


        if(!userRegistro.getStatus().equals("3")) {

            if (status.equals("1")) {
                boolean pase = true;

                if (nomb.equals("") || nomb.length() <= 3) {
                    pase = false;
                }
                if (cIorProfCod.equals("") || cIorProfCod.equals("Cedula") || cIorProfCod.length() <= 5) {
                    pase = false;
                }

                if (pase) {
                    MiAplicativo.getWritableDatabase().updateUserRegistroAlumno(status, nomb, cIorProfCod);
                    goToActBase(700);
                } else {
                    L.t(this, "Datos invalidos");
                    validadorEstudiante++;
                    L.t(this, 6-validadorEstudiante+" Intentos restantes");
                    if(validadorEstudiante>=6){
                        MiAplicativo.getWritableDatabase().updateUserRegistroFail("3",now.getTime());
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }
            }


            if(status.equals("2")){
                boolean pase=true;

                if(nomb.equals("")||nomb.length()<=3){  pase=false;  }

                if(pase && validadorProfCod<3) {
                    sendRegistrationProfCodToBackend(cIorProfCod, status, nomb);
                }else{
                    L.t(this,"Verifique sus Datos");
                    if(validadorProfCod>=3){
                        MiAplicativo.getWritableDatabase().updateUserRegistroFail("3",now.getTime());
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }
            }

        }else{
            L.t(this, "Ingreso Bloqueado");
        }



    }

    private void goToActBase(int timer){
        SPLASH_TIME=timer;
        YoYo.with(Techniques.Shake)
                .duration(timer-200)
                .playOn(findViewById(R.id.imageView));


        new AperturaDeAplicacion().execute();
    }


    public void muestraMSJ2(String string) {
        L.t(this,string);
    }


    //FUNCION SERVER CHECK PROFESOR COD

    private void sendRegistrationProfCodToBackend(String profCod,final String status,final String nomb) {

        final Context context = getApplicationContext();

        String url = "http://usmpemsun.esy.es/register";

        Map<String, String> map = new HashMap<>();
        map.put("profCod", profCod);

        JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST,
                url,new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    try{
                        String estado="NA";
                        if(response.has(Key.EndPointMateria.KEY_ESTADO)&&
                                !response.isNull(Key.EndPointMateria.KEY_ESTADO)){
                            estado = response.getString(Key.EndPointMateria.KEY_ESTADO);
                        }

                        if(estado.equals("1")){
                            MiAplicativo.getWritableDatabase().updateUserRegistroProfesor(status,nomb);
                            goToActBase(700);
                        }else if(estado.equals("2")){
                            muestraMSJ2(getResources().getString( R.string.prof_invalid));
                            validadorProfCod++;
                            L.t(context, 3-validadorProfCod+" Intentos restantes");
                        }


                    }catch (JSONException e){
                        e.printStackTrace();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                error.printStackTrace();
                if (error instanceof TimeoutError || error instanceof NoConnectionError){
                    muestraMSJ2(getResources().getString( R.string.volley_error_time));

                }else if(error instanceof AuthFailureError){
                    muestraMSJ2(getResources().getString( R.string.volley_error_aut));

                }else if (error instanceof ServerError){
                    muestraMSJ2(getResources().getString( R.string.volley_error_serv));

                }else if (error instanceof NetworkError){
                    muestraMSJ2(getResources().getString( R.string.volley_error_net));

                }else if (error instanceof ParseError){
                    muestraMSJ2(getResources().getString( R.string.volley_error_par));
                }
            }
        });
        requestQueue.add(request);
    }





    ///FUNCIONES y APARTADO DE PUSH NOTIFICATION

    private boolean checkPlayServices(){
        int resultCode = GooglePlayServicesUtil.
                isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }else{
                Log.i(TAG, "Device Not Supported");
                finish();
            }return false;
        }return true;
    }

    private String getRegistrationId(Context context){
        final SharedPreferences prefs= getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID,"");
        if(registrationId.isEmpty()){
            Log.i(TAG, "Registration Not Found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
                Integer.MIN_VALUE);
        int currentVersion = getAppVersion(getApplicationContext());
        if(registeredVersion != currentVersion){
            Log.i(TAG,"App version changed");
            return "";
        }return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context){
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return  getSharedPreferences(ActBase.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


//LANZADOR DE APLICACION

    public class AperturaDeAplicacion extends AsyncTask {

        private Intent myIntent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             myIntent = new Intent(ActApertura.this, ActBase.class);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Thread.sleep(SPLASH_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            startActivity(myIntent);
            finish();
        }

    }
}
