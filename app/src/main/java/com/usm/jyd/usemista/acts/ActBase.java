package com.usm.jyd.usemista.acts;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.fragments.FragmentBase;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.network.notification.RegisterApp;

import java.util.ArrayList;
import java.util.TooManyListenersException;
import java.util.concurrent.atomic.AtomicInteger;

public class ActBase extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ClickCallBack {

    // Need this to link with the Snackbar
    private CoordinatorLayout mCoordinator;
    //Need this to set the title of the app bar
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;


    //Localizador del Back Press
    private int stateBackPress=0;

    //VARIABLES EMPLEADAS PARA Push notifications
    private static final  int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = "GCMRelated";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    String regid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_base);

        //Grupo de Coordinacion para la actividad Base
        mCoordinator = (CoordinatorLayout) findViewById(R.id.root_coordinator);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);

        iniToolBar();
        iniNavDrawer();
        iniFab();
        //Notice how the title is set on the Collapsing Toolbar Layout instead of the Toolbar
        mCollapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contenedor_base, FragmentBase.newInstance(0))
                .commit();
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
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Notice how the Coordinator Layout object is used here
                Snackbar.make(mCoordinator, "FAB Clicked", Snackbar.LENGTH_SHORT).setAction("DISMISS", null).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else if(stateBackPress==10){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBase.newInstance(0))
                    .commit();
            stateBackPress=0;
        }else if(stateBackPress==100){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.contenedor_base, FragmentBase.newInstance(10))
                    .commit();
            stateBackPress=10;
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
        if(id == R.id.action_person){
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
          }

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


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contenedor_base, FragmentBase.newInstance(poss))
                .commit();


        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRSCItemSelected(int position) {
        stateBackPress=position;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.contenedor_base, FragmentBase.newInstance(position))
                .commit();
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

}