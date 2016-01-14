package com.usm.jyd.usemista.acts;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.dialogs.HorarioVDialog;
import com.usm.jyd.usemista.dialogs.MateriaDialog;
import com.usm.jyd.usemista.dialogs.NewProfAlumClassDialog;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.events.HVTimeToSet;
import com.usm.jyd.usemista.fragments.FragmentBase;
import com.usm.jyd.usemista.fragments.FragmentBaseCalendar;
import com.usm.jyd.usemista.fragments.FragmentBaseHVAdd;
import com.usm.jyd.usemista.fragments.FragmentBaseMMTask;
import com.usm.jyd.usemista.fragments.FragmentBaseMateriaSelector;
import com.usm.jyd.usemista.fragments.FragmentBaseNotify;
import com.usm.jyd.usemista.fragments.FragmentBasePPyPAItem;
import com.usm.jyd.usemista.fragments.FragmentBaseProfAlum;
import com.usm.jyd.usemista.fragments.FragmentBaseProfPorf;
import com.usm.jyd.usemista.fragments.FragmentBaseSemestreSelector;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.network.Key;
import com.usm.jyd.usemista.network.VolleySingleton;
import com.usm.jyd.usemista.objects.HVWeek;
import com.usm.jyd.usemista.objects.HorarioVirtual;
import com.usm.jyd.usemista.objects.Materia;
import com.usm.jyd.usemista.objects.ProfAlum;
import com.usm.jyd.usemista.objects.UserRegistro;
import com.usm.jyd.usemista.objects.UserTask;
import com.usm.jyd.usemista.services.ClassService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;

public class ActBase extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ClickCallBack,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener  {

    private static final int JOB_ID_CLASES_AND_EVENTS = 102;
    //VARIABLES PARA FRAGMENTO PROF PROF
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    UserRegistro userRegistro;
    public String codProfesor="";
    public FragmentBaseProfPorf frBsPrPr;
    public FragmentBaseProfAlum frBsPrAl;


    // Necesario para coordinar vistas dentro del Layout "SnackBar"
    private CoordinatorLayout mCoordinator;
    //Vars para setear el titulo del App Bar
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout mAppBarLayout;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    //VARIABLES PARA HORARIO VIRTUAL
    private int HVposition=0;
    private String HVweekDay="";
    private HVTimeToSet hvTimeToSet;
    public int HVColorToSet;

    //Localizador del Back Press
    private int stateBackPress=0;


    //VARIABLES EMPLEADAS PARA Push notifications MSJ
    private static final  int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = "GCMRelated";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    String regid;


    //VARIABLES SERVICE CLASES en HORARIO Y EVENTOS
    private JobScheduler mJobScheduler;
    int varViewChanger=0;
    private ImageView imgViewChanger;


    private ProgressDialog progressDialog ;


    private void jobClasesEventos(){
        ArrayList<HorarioVirtual> listHorario=MiAplicativo.getWritableDatabase().getAllHorarioVirtual();
        ArrayList<UserTask> listEventos=MiAplicativo.getWritableDatabase().getAllUserTask();

        if(!listHorario.isEmpty() || !listEventos.isEmpty()){

            mJobScheduler=JobScheduler.getInstance(this);

            JobInfo.Builder builder=new
                    JobInfo.Builder(JOB_ID_CLASES_AND_EVENTS,new ComponentName(this, ClassService.class));
            builder.setPersisted(true)
                    .setPeriodic(60000*1000);

            mJobScheduler.schedule(builder.build());
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_base);
        progressDialog = new ProgressDialog(this);
        jobClasesEventos();

        userRegistro=MiAplicativo.getWritableDatabase().getUserRegistro();
        volleySingleton=VolleySingleton.getInstance();
        requestQueue=volleySingleton.getRequestQueue();

        //Grupo de Coordinacion para la actividad Base
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mAppBarLayout=(AppBarLayout)findViewById(R.id.app_bar_layout);
       // spinnerCarrera=(Spinner)findViewById(R.id.spinnerCarrera);

        iniToolBar();
        iniNavDrawer();
        iniFab();

        imgViewChanger=(ImageView)findViewById(R.id.imgViewChanger);
      //  viewChanger();
        //Notice how the title is set on the Collapsing Toolbar Layout instead of the Toolbar
        mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));

        //Transaction del fragmento
        setFragmentBase(0);
    }
    public void viewChanger(){

        Timer t = new Timer();

        t.scheduleAtFixedRate(new TimerTask() {
                                  @Override
                                  public void run() {
                                      if(varViewChanger==0){
                                          varViewChanger=1;
                                          imgViewChanger.setImageResource(R.drawable.main_view_lg1);
                                      }else{
                                          varViewChanger=0;
                                          imgViewChanger.setImageResource(R.drawable.main_view_lg4);
                                      }

                                      //Called each time when 1000 milliseconds (1 second) (the period parameter)
                                  }
                              },
//Set how long before to start calling the TimerTask (in milliseconds)
                0,
//Set the amount of time between each execution (in milliseconds)
                3000);
    }
    public void iniToolBar(){
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
    }
    public void iniNavDrawer(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setBackgroundColor(ContextCompat.getColor(this,R.color.colorWhite));
        View view = navigationView.getHeaderView(0);
        ImageView imgNav=(ImageView)view.findViewById(R.id.imgNavHeader);
        TextView text1=(TextView)view.findViewById(R.id.textNavHeader1);
        TextView text2=(TextView)view.findViewById(R.id.textNavHeader2);

        if(userRegistro.getStatus().equals("1")){
            text1.setText(userRegistro.getNomb());
            text2.setText(userRegistro.getCi());
        }else if(userRegistro.getStatus().equals("2")){
            text1.setText(userRegistro.getNomb());
            text2.setText("Funcion de Tutor Activa");
        }

        LinearLayout boxUser=(LinearLayout)view.findViewById(R.id.boxUser);
        boxUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEdition(userRegistro.getStatus());
            }
        });


    }
    private void userEdition(final String estado){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater;
        TextView textNomb, textCIoProfCod;
        final EditText editTextNomb, editTextCIoProfCod;
        ImageView imgCurrentType;

        inflater  = this.getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_user_regi,null);

        textNomb=(TextView)view.findViewById(R.id.textNomb);
        textCIoProfCod =(TextView)view.findViewById(R.id.textCIoProfCod);
        editTextNomb=(EditText)view.findViewById(R.id.editTextNomb);
        editTextCIoProfCod =(EditText)view.findViewById(R.id.editTextCIoProfCod);
        imgCurrentType=(ImageView)view.findViewById(R.id.imgCurrentType);


        if(estado.equals("1")){
            imgCurrentType.setImageResource(R.drawable.ic_estudiante_launch);
            textCIoProfCod.setText(getResources().getString(R.string.user_edit_ced));
            editTextNomb.setText(userRegistro.getNomb());
            editTextCIoProfCod.setText(userRegistro.getCi());
            editTextCIoProfCod.setInputType(InputType.TYPE_CLASS_NUMBER);

        }else if(estado.equals("2")){
            imgCurrentType.setImageResource(R.drawable.ic_profesor_launch);
            textCIoProfCod.setText(getResources().getString(R.string.user_edit_codProf));
            editTextNomb.setText(userRegistro.getNomb());
            editTextCIoProfCod.setText("");
        }


        imgCurrentType.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (estado.equals("1")) {
                            MiAplicativo.getWritableDatabase()
                                    .updateUserRegistroAlumno("1", editTextNomb.getText().toString(),
                                            editTextCIoProfCod.getText().toString());

                            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
                            View view = navigationView.getHeaderView(0);
                            TextView text1=(TextView)view.findViewById(R.id.textNavHeader1);
                            TextView text2=(TextView)view.findViewById(R.id.textNavHeader2);
                            text1.setText(editTextNomb.getText());
                            text2.setText(editTextCIoProfCod.getText());

                        } else if (estado.equals("2")) {
                            String auxPRCod="", auxNomb="";
                            auxPRCod=editTextCIoProfCod.getText().toString();
                            auxNomb=editTextNomb.getText().toString();

                            sendRegistrationProfCodToBackend(auxPRCod,"2",auxNomb);
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }
    private void sendRegistrationProfCodToBackend(String profCod,final String status,final String nomb) {

        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final Context context=this;
        String url = "http://usmpemsun.esy.es/register";

        Map<String, String> map = new HashMap<>();
        map.put("profCod", profCod);

        JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST,
                url,new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    progressDialog.dismiss();
                    String estado="NA";
                    if(response.has(Key.EndPointMateria.KEY_ESTADO)&&
                            !response.isNull(Key.EndPointMateria.KEY_ESTADO)){
                        estado = response.getString(Key.EndPointMateria.KEY_ESTADO);
                    }

                    if(estado.equals("1")){
                        MiAplicativo.getWritableDatabase().updateUserRegistroProfesor(status,nomb);
                        iniNavDrawer();
                        L.t(context, getResources().getString(R.string.user_edit_check1));

                        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
                        View view = navigationView.getHeaderView(0);
                        TextView text1=(TextView)view.findViewById(R.id.textNavHeader1);
                        text1.setText(nomb);

                    }else if(estado.equals("2")){
                        L.t(context,getResources().getString(R.string.user_edit_check2));

                    }



                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                String auxMsj="";

                error.printStackTrace();
                if (error instanceof TimeoutError || error instanceof NoConnectionError){
                    auxMsj="Fuera de Conexion \nFuera de Tiempo";
                }else if(error instanceof AuthFailureError){
                    auxMsj="Fallo de Ruta" ;
                }else if (error instanceof ServerError){
                    auxMsj="Fallo en el Servidor";
                }else if (error instanceof NetworkError){
                    auxMsj="Problemas de Conexion";
                }else if (error instanceof ParseError){
                    auxMsj="Problemas Internos";
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Error en la Nube")
                        .setMessage("\n\nCode Error: "+auxMsj)
                        .show();
            }
        });
        requestQueue.add(request);
    }
    public void iniFab(){
        mFab = (FloatingActionButton) findViewById(R.id.fab);

    }
    public void setColorThemeByMenu(int color){
        mCollapsingToolbarLayout.setBackgroundColor(color);
        mCollapsingToolbarLayout.setContentScrimColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }


    public void setFragmentBase(int pos){
        if(pos==0||pos==1){

            mAppBarLayout.setExpanded(true,true);
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_1));
            mFab.setVisibility(View.VISIBLE);

            mFab.setImageResource(R.drawable.ic_home_white_24dp);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_1));
                    //Notice how the Coordinator Layout object is used here
                    Snackbar.make(mCoordinator, getResources().getString(R.string.fab_text_1),
                            Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.contenedor_base, FragmentBase.newInstance(0))
                            .commit();
                }
            });

            if(pos==1){
                mAppBarLayout.setExpanded(false, true);
                mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_2));
             //   mFab.setVisibility(View.GONE);
            }




            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBase.newInstance(pos))
                    .commit();
        }else if(pos==10){
           // setColorThemeByMenu(ContextCompat.getColor(ActBase.this,R.color.colorTextMenuGreen));
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_10));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBase.newInstance(pos))
                    .commit();

        }else if(pos==11){
            mAppBarLayout.setExpanded(false,true);
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_11));
            mFab.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBase.newInstance(pos))
                    .commit();
        }else if(pos==12){
            setColorThemeByMenu(ContextCompat.getColor(this,R.color.colorPrimary));
           // setColorThemeByMenu(ContextCompat.getColor(ActBase.this,R.color.colorTextMenuYellow));
            mAppBarLayout.setExpanded(false,true);
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_12));
            mFab.setVisibility(View.VISIBLE);
            mFab.setImageResource(R.drawable.ic_add_white_48dp);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Notice how the Coordinator Layout object is used here
                    Snackbar.make(mCoordinator, getResources().getString(R.string.fab_text_2),
                            Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();

                    stateBackPress = 121;
                    mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_12_1));
                    mFab.setVisibility(View.GONE);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.contenedor_base, FragmentBaseHVAdd.newInstance(121,
                                    MiAplicativo.getWritableDatabase().getAllUserMateria()))
                            .commit();

                }
            });

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBase.newInstance(pos))
                    .commit();

        }else if(pos==13){
            mAppBarLayout.setExpanded(false,true);
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_13));
            mFab.setVisibility(View.GONE);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBaseNotify.newInstance())
                    .commit();

        }else if(pos==14){
            mAppBarLayout.setExpanded(false,true);
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_14));
            mFab.setVisibility(View.GONE);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBaseCalendar.newInstance())
                    .commit();

        }else if(pos==15){

            if(userRegistro.getStatus().equals("1")){
                mAppBarLayout.setExpanded(false, true);
                mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_15_1));
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.contenedor_base, FragmentBaseProfAlum.newInstance())
                        .commit();

                //Boton Flotante SEteo
                mFab.setVisibility(View.VISIBLE);
                mFab.setImageResource(R.drawable.ic_add_white_48dp);
                mFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Notice how the Coordinator Layout object is used here
                        Snackbar.make(mCoordinator, getResources().getString(R.string.fab_text_2),
                                Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();

                        ArrayList<Materia> listToCheck=MiAplicativo.getWritableDatabase().getAllUserMateria();
                        if(!listToCheck.isEmpty()) {
                            NewProfAlumClassDialog newProfAlumClassDialog = new NewProfAlumClassDialog();
                            newProfAlumClassDialog.show(getSupportFragmentManager(), "Class Maker");
                            newProfAlumClassDialog.setFrProfAlum(frBsPrAl);
                        }else
                            L.t(getApplicationContext(),getResources().getString(R.string.msj_sin_materias));
                    }
                });

            }else if(userRegistro.getStatus().equals("2")){
                ProfChecker();
            }else
                L.t(this,getResources().getString(R.string.msj_sin_materias));
        }

        else if(pos==100){
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_100));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBaseSemestreSelector.newInstance(pos))
                    .commit();

        }else if(pos==200){
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_200));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBaseSemestreSelector.newInstance(pos))
                    .commit();
        }else if(pos==300){
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_300));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBaseSemestreSelector.newInstance(pos))
                    .commit();
        }else if(pos==400){
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_400));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBaseSemestreSelector.newInstance(pos))
                    .commit();
        }else if(pos==500){
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_500));
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBaseSemestreSelector.newInstance(pos))
                    .commit();
        }




    }

    private void ProfChecker(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater;
        TextView textNomb, textCIoProfCod;
        final EditText editTextNomb, editTextCIoProfCod;
        ImageView imgCurrentType;

        inflater  = this.getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_user_regi,null);

        textNomb=(TextView)view.findViewById(R.id.textNomb);
        textCIoProfCod =(TextView)view.findViewById(R.id.textCIoProfCod);
        editTextNomb=(EditText)view.findViewById(R.id.editTextNomb);
        editTextCIoProfCod =(EditText)view.findViewById(R.id.editTextCIoProfCod);
        imgCurrentType=(ImageView)view.findViewById(R.id.imgCurrentType);

        textNomb.setVisibility(View.GONE);
        editTextNomb.setVisibility(View.GONE);


        imgCurrentType.setImageResource(R.drawable.ic_profesor_launch);
        textCIoProfCod.setText(getResources().getString(R.string.user_edit_codProf));
        editTextCIoProfCod.setText("");


        imgCurrentType.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       codProfesor=editTextCIoProfCod.getText().toString();
                        sendProfCodCheckToBackend(codProfesor);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    public void sendProfCodCheckToBackend(final String profCod) {

        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final Context context=this;
        String url = "http://usmpemsun.esy.es/register";

        Map<String, String> map = new HashMap<>();
        map.put("profCod", profCod);

        JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST,
                url,new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    progressDialog.dismiss();
                    String estado="NA";
                    if(response.has(Key.EndPointMateria.KEY_ESTADO)&&
                            !response.isNull(Key.EndPointMateria.KEY_ESTADO)){
                        estado = response.getString(Key.EndPointMateria.KEY_ESTADO);
                    }

                    if(estado.equals("1")){
                       // L.t(context,"profcod: "+profCod+ "\n\nValido");
                        //tu FRagmento si es validado debe ir aqui
                        mAppBarLayout.setExpanded(false, true);
                        mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_15_2));

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.contenedor_base, FragmentBaseProfPorf.newInstance(profCod))
                                .commit();

                        //Boton Flotante SEteo
                        mFab.setVisibility(View.VISIBLE);
                        mFab.setImageResource(R.drawable.ic_add_white_48dp);
                        mFab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Notice how the Coordinator Layout object is used here
                                Snackbar.make(mCoordinator, getResources().getString(R.string.fab_text_2),
                                        Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();

                                ArrayList<Materia> listToCheck=MiAplicativo.getWritableDatabase().getAllUserMateria();
                                if(!listToCheck.isEmpty()) {


                                NewProfAlumClassDialog newProfAlumClassDialog = new NewProfAlumClassDialog();
                                newProfAlumClassDialog.show(getSupportFragmentManager(), "Class Maker");
                                newProfAlumClassDialog.setProfCod(codProfesor);
                                newProfAlumClassDialog.setFrProfProf(frBsPrPr);
                                 }else
                                    L.t(getApplicationContext(),getResources().getString(R.string.msj_sin_materias));
                            }
                        });

                    }else if(estado.equals("2")){
                        L.t(context,"profcod: "+profCod+ "\n\n"+getResources().getString(R.string.user_edit_check2));
                        stateBackPress=0;
                    }


                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                String auxMsj="";

                error.printStackTrace();
                if (error instanceof TimeoutError || error instanceof NoConnectionError){
                    auxMsj=getResources().getString(R.string.volley_error_time1)+"\n"
                        +getResources().getString(R.string.volley_error_time2);
                }else if(error instanceof AuthFailureError){
                    auxMsj=getResources().getString(R.string.volley_error_aut) ;
                }else if (error instanceof ServerError){
                    auxMsj=getResources().getString(R.string.volley_error_serv);
                }else if (error instanceof NetworkError){
                    auxMsj=getResources().getString(R.string.volley_error_net);
                }else if (error instanceof ParseError){
                    auxMsj=getResources().getString(R.string.volley_error_par);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(getResources().getString(R.string.error_de_conexion))
                        .setMessage( "\n\n"+getResources().getString(R.string.cod_error)+auxMsj)
                        .show();
            }
        });
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else if(stateBackPress==10 ||stateBackPress==11||stateBackPress==12||
                stateBackPress==13||stateBackPress==14 || stateBackPress==15){
            setFragmentBase(0);
            stateBackPress=0;
        }else if(stateBackPress==100 || stateBackPress==200 || stateBackPress==300
                || stateBackPress==400 || stateBackPress==500){
            setFragmentBase(10);
            stateBackPress=10;
        }else if(stateBackPress==121){
            //Horario Virtual Agregar o modificar
            setFragmentBase(12);
            stateBackPress=12;
        }else if(stateBackPress==151){
            //Item RC de FR Prof Aulm
            setFragmentBase(15);
            stateBackPress=15;
        }else if(stateBackPress==152){
            // la forma de retorno a ProfProf FRag es distinta por eso no
            // se utiliza setFragmentBase
            mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.tittle_toolbar_15_2));
            mFab.setVisibility(View.VISIBLE);
            mFab.setImageResource(R.drawable.ic_add_white_48dp);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Notice how the Coordinator Layout object is used here
                    Snackbar.make(mCoordinator, getResources().getString(R.string.fab_text_2),
                            Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();

                    ArrayList<Materia> listToCheck=MiAplicativo.getWritableDatabase().getAllUserMateria();
                    if(!listToCheck.isEmpty()) {

                        NewProfAlumClassDialog newProfAlumClassDialog = new NewProfAlumClassDialog();
                        newProfAlumClassDialog.show(getSupportFragmentManager(), "Class Maker");
                        newProfAlumClassDialog.setProfCod(codProfesor);
                        newProfAlumClassDialog.setFrProfProf(frBsPrPr);
                    }else
                        L.t(getApplicationContext(),getResources().getString(R.string.msj_sin_materias));

                }
            });
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base,
                            FragmentBaseProfPorf.newInstance(codProfesor))
                    .commit();
            stateBackPress=15;
        }else if(stateBackPress==1000){
            setFragmentBase(100);
            stateBackPress=100;
        }else if(stateBackPress==2000){
            setFragmentBase(200);
            stateBackPress=200;
        }else if(stateBackPress==3000){
            setFragmentBase(300);
            stateBackPress=300;
        }else if(stateBackPress==4000){
            setFragmentBase(400);
            stateBackPress=400;
        }else if(stateBackPress==5000){
            setFragmentBase(500);
            stateBackPress=500;
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.act_base_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
       /* if(id == R.id.action_person){
            if(checkPlayServices()){
                gcm=GoogleCloudMessaging.getInstance(getApplicationContext());
                regid= getRegistrationId(getApplicationContext());

                if(regid.isEmpty()){
                    item.setEnabled(false);
                    new RegisterApp(
                            getApplicationContext(),
                            gcm,
                            getAppVersion(getApplicationContext()))
                            .execute();
                }else{
                    L.t(getApplicationContext(),"Device already Regi");
                }
            }else{
                Log.i(TAG,"No vailid GP Services APK found");
            }
          }  */

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        int poss = 0;

        if (id == R.id.navigation_item_1) {
            poss = 0;
        } else if (id == R.id.navigation_item_2) {

            poss = 1;
        }
        setFragmentBase(poss);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRSCItemSelected(int position) {
        setFragmentBase(position);
        stateBackPress=position;
    }

    @Override
    public void onRSCSemestreSelected(int position, ArrayList<Materia> listMateria) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contenedor_base, FragmentBaseMateriaSelector.newInstance(position,listMateria))
                .commit();

        if(listMateria.get(0).getModulo().equals("ingSis")){
            stateBackPress=1000;
        }else if(listMateria.get(0).getModulo().equals("telecom")){
            stateBackPress=2000;
        }else if(listMateria.get(0).getModulo().equals("ingInd")){
            stateBackPress=3000;
        }else if(listMateria.get(0).getModulo().equals("ingCiv")){
            stateBackPress=4000;
        }else if(listMateria.get(0).getModulo().equals("arq")){
            stateBackPress=5000;
        }
    }

    @Override
    public void onRSCMateriaSelected(int position, Materia materia) {

        MateriaDialog newDialog= new MateriaDialog();
        newDialog.setMateriaObject(materia);
        newDialog.show(getSupportFragmentManager(), "Materia");
    }

    @Override
    public void onRSCHorarioVSelected(int position, Materia materia, HorarioVirtual horarioVirtual, ArrayList<HVWeek> listHVWeek) {
        stateBackPress = 121;

       setColorThemeByMenu(horarioVirtual.getColor());
        mFab.setVisibility(View.GONE);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contenedor_base, FragmentBaseHVAdd.newInstance(position, materia, horarioVirtual, listHVWeek))
                .commit();
    }


    @Override
    public void onHVTimeSelected(int HVposition, String HVweekDay) {

        this.HVposition=HVposition;this.HVweekDay=HVweekDay;

        Calendar now = Calendar.getInstance();
        TimePickerDialog dpd = TimePickerDialog.newInstance(
                ActBase.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");


    }

    @Override
    public void onHVCalendarSelected(int HVposition) {

        this.HVposition=HVposition;
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                ActBase.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");

    }

    @Override
    public void onHVColorSelected(int prevColor) {

        HorarioVDialog newHvColorPicker = new HorarioVDialog();
        newHvColorPicker.setHVCallBack(hvTimeToSet, prevColor);
                newHvColorPicker.show(getSupportFragmentManager(), "Color Picker");
       /* mCollapsingToolbarLayout.setBackgroundColor(HVColorToSet);
        mCollapsingToolbarLayout.setContentScrimColor(HVColorToSet);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(HVColorToSet);
        }*/
    }

    @Override
    public void setFrMMTAux(FragmentBaseMMTask frMMT) {
        hvTimeToSet=(HVTimeToSet)frMMT;
    }

    @Override
    public void onRSCItemProfClassSelected(final String profCod, final String accesCod, final ProfAlum profAlum, String auxName) {
        if(userRegistro.getStatus().equals("1")){
           // stateBackPress=151;



        }else if(userRegistro.getStatus().equals("2")){

            mCollapsingToolbarLayout.setTitle(auxName);

            mFab.setVisibility(View.VISIBLE);
            mFab.setImageResource(R.drawable.ic_email_white_24dp);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Notice how the Coordinator Layout object is used here
                    Snackbar.make(mCoordinator, getResources().getString(R.string.fab_text_2),
                            Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();

                    dialogMsjSendProfToAlum(profCod,accesCod,profAlum);

                }
            });

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base,
                            FragmentBasePPyPAItem.newInstance(profCod, accesCod))
                    .commit();


            stateBackPress=152;
        }

    }

    public void dialogMsjSendProfToAlum(final String profCod, final String accesCod, final ProfAlum profAlum){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater;
        TextView textNomb, textCIoProfCod;
        final EditText editTextNomb, editTextCIoProfCod;
        ImageView imgCurrentType;

        inflater  = this.getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_user_regi,null);

        textNomb=(TextView)view.findViewById(R.id.textNomb);
        textCIoProfCod =(TextView)view.findViewById(R.id.textCIoProfCod);
        editTextNomb=(EditText)view.findViewById(R.id.editTextNomb);
        editTextCIoProfCod =(EditText)view.findViewById(R.id.editTextCIoProfCod);
        imgCurrentType=(ImageView)view.findViewById(R.id.imgCurrentType);

        textNomb.setVisibility(View.GONE);
        editTextNomb.setVisibility(View.GONE);


        imgCurrentType.setImageResource(R.drawable.ic_profesor_launch);
        textCIoProfCod.setText(getResources().getString(R.string.msj_para_clase));
        editTextCIoProfCod.setText("");


        imgCurrentType.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary));

        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String MSJ=
                        editTextCIoProfCod.getText().toString();
                        sendMSJToBackend(profCod, accesCod, profAlum,MSJ);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    public void sendMSJToBackend(final String profCod, final String accesCod,ProfAlum profAlum, final String MSJ) {

        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final Context context=this;
        String url = "http://usmpemsun.esy.es/fr_prof_alum";

       final   Materia currentMT=MiAplicativo.getWritableDatabase().getOneUserMateria(profAlum.getMa_cod());


        Map<String, String> map = new HashMap<>();

        map.put("msjProfAlmCls", "1");
        map.put("prc_pro_cod", profCod);
        map.put("prc_acces_cod", accesCod);
        map.put("msj",MSJ );
        map.put("mt", currentMT.getTitulo());
        map.put("mod", currentMT.getModulo());

        JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST,
                url,new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    progressDialog.dismiss();
                    String estado="NA";
                    if(response.has(Key.EndPointMateria.KEY_ESTADO)&&
                            !response.isNull(Key.EndPointMateria.KEY_ESTADO)){
                        estado = response.getString(Key.EndPointMateria.KEY_ESTADO);
                    }

                    if(estado.equals("1")){

                        L.t(context,getResources().getString(R.string.msj_para_clase_enviado));

                    }else if(estado.equals("2")){

                       L.t(context,getResources().getString(R.string.msj_para_clase_fallo));
                    }


                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                String auxMsj="";

                error.printStackTrace();
                if (error instanceof TimeoutError || error instanceof NoConnectionError){
                    auxMsj=getResources().getString(R.string.volley_error_time1)+" \n"
                        +getResources().getString(R.string.volley_error_time2);
                }else if(error instanceof AuthFailureError){
                    auxMsj=getResources().getString(R.string.volley_error_aut) ;
                }else if (error instanceof ServerError){
                    auxMsj=getResources().getString(R.string.volley_error_serv);
                }else if (error instanceof NetworkError){
                    auxMsj=getResources().getString(R.string.volley_error_net);
                }else if (error instanceof ParseError){
                    auxMsj=getResources().getString(R.string.volley_error_par);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(getResources().getString(R.string.error_de_conexion))
                        .setMessage("ProfCod: "+profCod+
                                "\nAccesCod: "+accesCod+
                                "\nMsj: "+MSJ+
                                "\nMt:" + currentMT.getTitulo()+
                                "\nMod:"+ currentMT.getModulo() + "\n\n"
                                +getResources().getString(R.string.cod_error)+auxMsj)
                        .show();
            }
        });
        requestQueue.add(request);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        hvTimeToSet.seteoDeTiempo(hourOfDay, minute, HVweekDay, HVposition);
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        hvTimeToSet.seteoDeFecha(year, monthOfYear, dayOfMonth, HVposition);
    }




    ///FUNCIONES y APARTADO DE PUSH NOTIFICATION

/*private boolean checkPlayServices(){
    int resultCode = GooglePlayServicesUtil.
            isGooglePlayServicesAvailable(this);
    if(resultCode != ConnectionResult.SUCCESS){
        if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
            GooglePlayServicesUtil.getErrorDialog(resultCode,this,
                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
        }else{
            Log.i(TAG,"Device Not Supported" );
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
*/
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if(fragment instanceof FragmentBaseHVAdd){
            hvTimeToSet=(HVTimeToSet)fragment;
        }
        if(fragment instanceof FragmentBaseProfPorf){
            frBsPrPr=(FragmentBaseProfPorf)fragment;
        }
        if(fragment instanceof FragmentBaseProfAlum){
            frBsPrAl=(FragmentBaseProfAlum)fragment;
        }
    }



}