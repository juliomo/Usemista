package com.usm.jyd.usemista.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.adapters.AdapterRecyclerProfAlum;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.network.Key;
import com.usm.jyd.usemista.network.VolleySingleton;
import com.usm.jyd.usemista.objects.ProfAlum;
import com.usm.jyd.usemista.objects.UserRegistro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by der_w on 12/14/2015.
 */
public class FragmentBaseProfAlum extends Fragment {

    private ClickCallBack clickCallBack;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private RecyclerView rcListAlumClassPe;
    private AdapterRecyclerProfAlum adapterRecyclerProfAlum;
    private ArrayList<ProfAlum> listAlumClass;


    private UserRegistro userRegistro;
    private Boolean flagEdition=false;


    public static FragmentBaseProfAlum newInstance() {
        FragmentBaseProfAlum fragment = new FragmentBaseProfAlum();
        return fragment;
    }
    public FragmentBaseProfAlum(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        volleySingleton= VolleySingleton.getInstance();
        requestQueue=volleySingleton.getRequestQueue();

        userRegistro= MiAplicativo.getWritableDatabase().getUserRegistro();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.act_base_fr_prof_prof_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(flagEdition) {
            menu.findItem(R.id.action_edit).setVisible(false);
            menu.findItem(R.id.action_save).setVisible(true);
        }else{
            menu.findItem(R.id.action_edit).setVisible(true);
            menu.findItem(R.id.action_save).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.action_edit){
            flagEdition=true;
            adapterRecyclerProfAlum.setEdition(true);
            getActivity().invalidateOptionsMenu();
        }
        else if(id==R.id.action_save){
            flagEdition=false;
            adapterRecyclerProfAlum.setEdition(false);
            getActivity().invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_00, container, false);

        rcListAlumClassPe=(RecyclerView) rootView.findViewById(R.id.recycleView);
        rcListAlumClassPe.setLayoutManager(new LinearLayoutManager(getContext()));
        rcListAlumClassPe.setNestedScrollingEnabled(false);
        adapterRecyclerProfAlum= new AdapterRecyclerProfAlum(getContext());
        adapterRecyclerProfAlum.setClickCallBack(clickCallBack);

        //seteo de lista Json request Please


        rcListAlumClassPe.setSoundEffectsEnabled(true);
        rcListAlumClassPe.setAdapter(adapterRecyclerProfAlum);

        getAlumClassInBackEnd(userRegistro.getCi());

        return rootView;  //super.onCreateView(inflater, container, savedInstanceState);
    }

    public void getAlumClassInBackEnd(String cedula) {

        final Context context=getContext();
        String url = "http://usmpemsun.esy.es/fr_prof_alum";

        Map<String, String> map = new HashMap<>();
        map.put("geAlmCls", "1");
        map.put("alc_cedula", cedula);

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

                    String alc_id="NA";
                    String alc_pro_cod="NA";
                    String alc_acces_cod="NA";
                    String alc_ma_modulo="NA";
                    String alc_ma_cod="NA";
                    String alc_seccion="NA";
                    String alc_nombre="NA";
                    String alc_cedula="NA";
                    String alc_regist="NA";
                    String alc_regiDV ="NA";



                    if(estado.equals("1")){
                        L.t(context, "Esta Validando en el Server");


                        listAlumClass=new ArrayList<>();

                        JSONArray currentProfClassArray = response.getJSONArray("AlumnoClass");
                        for(int i=0; i<currentProfClassArray.length();i++){
                            JSONObject currentProfClss = currentProfClassArray.getJSONObject(i);



                            if(currentProfClss.has("alc_id")&&
                                    !currentProfClss.isNull("alc_id")){
                                alc_id=currentProfClss.getString("alc_id");
                            }
                            if(currentProfClss.has("alc_pro_cod")&&
                                    !currentProfClss.isNull("alc_pro_cod")){
                                alc_pro_cod=currentProfClss.getString("alc_pro_cod");
                            }
                            if(currentProfClss.has("alc_acces_cod")&&
                                    !currentProfClss.isNull("alc_acces_cod")){
                                alc_acces_cod=currentProfClss.getString("alc_acces_cod");
                            }
                            if(currentProfClss.has("alc_ma_modulo")&&
                                    !currentProfClss.isNull("alc_ma_modulo")){
                                alc_ma_modulo=currentProfClss.getString("alc_ma_modulo");
                            }
                            if(currentProfClss.has("alc_ma_cod")&&
                                    !currentProfClss.isNull("alc_ma_cod")){
                                alc_ma_cod=currentProfClss.getString("alc_ma_cod");
                            }
                            if(currentProfClss.has("alc_seccion")&&
                                    !currentProfClss.isNull("alc_seccion")){
                                alc_seccion=currentProfClss.getString("alc_seccion");
                            }
                            if(currentProfClss.has("alc_seccion")&&
                                    !currentProfClss.isNull("alc_seccion")){
                                alc_seccion=currentProfClss.getString("alc_seccion");
                            }
                            if(currentProfClss.has("alc_nombre")&&
                                    !currentProfClss.isNull("alc_nombre")){
                                alc_nombre=currentProfClss.getString("alc_nombre");
                            }
                            if(currentProfClss.has("alc_cedula")&&
                                    !currentProfClss.isNull("alc_cedula")){
                                alc_cedula=currentProfClss.getString("alc_cedula");
                            }
                            if(currentProfClss.has("alc_regist")&&
                                    !currentProfClss.isNull("alc_regist")){
                                alc_regist=currentProfClss.getString("alc_regist");
                            }
                            if(currentProfClss.has("alc_regiDV")&&
                                    !currentProfClss.isNull("alc_regiDV")){
                                alc_regiDV=currentProfClss.getString("alc_regiDV");
                            }

                            ProfAlum profAlum=new ProfAlum();
                            profAlum.setProCodHash(alc_pro_cod);
                            profAlum.setCodAcces(alc_acces_cod);
                            profAlum.setMa_mod(alc_ma_modulo);
                            profAlum.setMa_cod(alc_ma_cod);
                            profAlum.setMa_sec(alc_seccion);
                            profAlum.setNomb(alc_nombre);
                            profAlum.setCed(alc_cedula);
                            profAlum.setRegist(alc_regist);
                            profAlum.setRegiDV(alc_regiDV);

                            //Carga completa del Json
                            listAlumClass.add(profAlum);


                        }
                        L.t(context,"ProCod: "+listAlumClass.get(0).getProCodHash());
                        //  L.t(context,"List Size: "+listProfClass.size());
                        adapterRecyclerProfAlum.setlistAlumClass(listAlumClass);



                    }else if(estado.equals("2")){
                        L.t(context,"No Hay Clases Registradas");
                    }else if(estado.equals("3"))
                        L.t(context,"Codigo de Prof erroneo");

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                L.t(context,"Volley Error");

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
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //gracias al metodo on Attach damos valor al clickCallBack evitamos Null value
            clickCallBack=(ClickCallBack)context;
            //  mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



}
