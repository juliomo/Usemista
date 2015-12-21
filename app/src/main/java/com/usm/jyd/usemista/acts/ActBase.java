package com.usm.jyd.usemista.acts;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
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
    private String codProfesor="";
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
        //Notice how the title is set on the Collapsing Toolbar Layout instead of the Toolbar
        mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));

        //Transaction del fragmento
        setFragmentBase(0);
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
            mCollapsingToolbarLayout.setTitle("Usemista");
            mFab.setVisibility(View.VISIBLE);
            mFab.setImageResource(R.drawable.ic_home_white_24dp);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Notice how the Coordinator Layout object is used here
                    Snackbar.make(mCoordinator, "FAB in HOME",
                            Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.contenedor_base, FragmentBase.newInstance(0))
                            .commit();
                }
            });

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBase.newInstance(pos))
                    .commit();
        }else if(pos==10){
           // setColorThemeByMenu(ContextCompat.getColor(ActBase.this,R.color.colorTextMenuGreen));
            mCollapsingToolbarLayout.setTitle("Pensum");
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBase.newInstance(pos))
                    .commit();

        }else if(pos==11){
            mAppBarLayout.setExpanded(false,true);
            mCollapsingToolbarLayout.setTitle("Mis Materias");
            mFab.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBase.newInstance(pos))
                    .commit();
        }else if(pos==12){
           // setColorThemeByMenu(ContextCompat.getColor(ActBase.this,R.color.colorTextMenuYellow));
            mAppBarLayout.setExpanded(false,true);
            mCollapsingToolbarLayout.setTitle("Horario");
            mFab.setVisibility(View.VISIBLE);
            mFab.setImageResource(R.drawable.ic_add_white_48dp);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Notice how the Coordinator Layout object is used here
                    Snackbar.make(mCoordinator, "FAB To Add",
                            Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();

                    stateBackPress = 121;
                    mCollapsingToolbarLayout.setTitle("Add");
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

        }else if(pos==14){
            mAppBarLayout.setExpanded(false,true);
            mCollapsingToolbarLayout.setTitle("Calendar");
            mFab.setVisibility(View.GONE);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBaseCalendar.newInstance())
                    .commit();

        }else if(pos==15){

            if(userRegistro.getStatus().equals("1")){
                mAppBarLayout.setExpanded(false, true);
                mCollapsingToolbarLayout.setTitle("Tutores");
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
                        Snackbar.make(mCoordinator, "FAB To Add",
                                Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();
                        NewProfAlumClassDialog newProfAlumClassDialog = new NewProfAlumClassDialog();
                        newProfAlumClassDialog.show(getSupportFragmentManager(), "Class Maker");
                        newProfAlumClassDialog.setFrProfAlum(frBsPrAl);
                    }
                });

            }else if(userRegistro.getStatus().equals("2")){
                ProfChecker();
            }else
                L.t(this,"Funcion Inactiva");
        }

        else if(pos==100){
            mCollapsingToolbarLayout.setTitle("Sistema");
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBaseSemestreSelector.newInstance(pos))
                    .commit();

        }else if(pos==200){
            mCollapsingToolbarLayout.setTitle("Telecom");
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
        textCIoProfCod.setText("Codigo de Profesor");
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

        final Context context=this;
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
                        L.t(context,"profcod: "+profCod+ "\n\nValido");
                        //tu FRagmento si es validado debe ir aqui
                        mAppBarLayout.setExpanded(false, true);
                        mCollapsingToolbarLayout.setTitle("Clases");

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
                                Snackbar.make(mCoordinator, "FAB To Add",
                                        Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();
                                NewProfAlumClassDialog newProfAlumClassDialog = new NewProfAlumClassDialog();
                                newProfAlumClassDialog.show(getSupportFragmentManager(), "Class Maker");
                                newProfAlumClassDialog.setProfCod(codProfesor);
                                newProfAlumClassDialog.setFrProfProf(frBsPrPr);

                            }
                        });

                    }else if(estado.equals("2")){
                        L.t(context,"profcod: "+profCod+ "\n\nNo Valido");
                        stateBackPress=0;
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


                }else if(error instanceof AuthFailureError){


                }else if (error instanceof ServerError){


                }else if (error instanceof NetworkError){


                }else if (error instanceof ParseError){


                }
            }
        });
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else if(stateBackPress==10 ||stateBackPress==11||
                stateBackPress==12||stateBackPress==14 || stateBackPress==15){
            setFragmentBase(0);
            stateBackPress=0;
        }else if(stateBackPress==100 || stateBackPress==200){
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
            mFab.setVisibility(View.VISIBLE);
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
    public void onRSCItemProfClassSelected(String profCod, String accesCod) {
        if(userRegistro.getStatus().equals("1")){
           // stateBackPress=151;



        }else if(userRegistro.getStatus().equals("2")){

            mFab.setVisibility(View.GONE);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base,
                            FragmentBasePPyPAItem.newInstance(profCod, accesCod))
                    .commit();


            stateBackPress=152;
        }

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