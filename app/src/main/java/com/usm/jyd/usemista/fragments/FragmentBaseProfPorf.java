package com.usm.jyd.usemista.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.usm.jyd.usemista.adapters.AdapterRecyclerProfProf;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.network.Key;
import com.usm.jyd.usemista.network.VolleySingleton;
import com.usm.jyd.usemista.objects.ProfAlum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by der_w on 12/12/2015.
 */
public class FragmentBaseProfPorf extends Fragment {

    private static final String ARG_PROF_COD = "profCod";

    private ClickCallBack clickCallBack;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private RecyclerView rcListProfClass;
    private AdapterRecyclerProfProf adapterRecyclerProfProf;
    private ArrayList<ProfAlum> listProfClass;
    private Boolean flagEdition=false;

    public static FragmentBaseProfPorf newInstance(String profCod) {
        FragmentBaseProfPorf fragment = new FragmentBaseProfPorf();
        Bundle args = new Bundle();
        args.putString(ARG_PROF_COD, profCod);
        fragment.setArguments(args);
        return fragment;
    }
    public FragmentBaseProfPorf(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        volleySingleton= VolleySingleton.getInstance();
        requestQueue=volleySingleton.getRequestQueue();

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
            adapterRecyclerProfProf.setEdition(true);
            getActivity().invalidateOptionsMenu();
        }
       else if(id==R.id.action_save){
            flagEdition=false;
            adapterRecyclerProfProf.setEdition(false);
            getActivity().invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_00, container, false);

        rcListProfClass=(RecyclerView) rootView.findViewById(R.id.recycleView);
        rcListProfClass.setLayoutManager(new LinearLayoutManager(getContext()));
        rcListProfClass.setNestedScrollingEnabled(false);
        adapterRecyclerProfProf= new AdapterRecyclerProfProf(getContext(),getArguments().getString(ARG_PROF_COD));
        adapterRecyclerProfProf.setClickCallBack(clickCallBack);

        //seteo de lista Json request Please


        rcListProfClass.setSoundEffectsEnabled(true);
        rcListProfClass.setAdapter(adapterRecyclerProfProf);

        getProfClassInBackEnd(getArguments().getString(ARG_PROF_COD));

        return rootView;  //super.onCreateView(inflater, container, savedInstanceState);
    }

    public void getProfClassInBackEnd(String prc_pro_cod) {

        final Context context=getContext();
        String url = "http://usmpemsun.esy.es/fr_prof_alum";

        Map<String, String> map = new HashMap<>();
        map.put("geProfCls","1");
        map.put("prc_pro_cod", prc_pro_cod);

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

                    String prc_id="NA";
                    String prc_pro_cod="NA";
                    String prc_acces_cod="NA";
                    String prc_ma_modulo="NA";
                    String prc_ma_cod="NA";
                    String prc_seccion="NA";



                    if(estado.equals("1")){
                        L.t(context, "Esta Validando en el Server");


                        listProfClass=new ArrayList<>();

                        JSONArray currentProfClassArray = response.getJSONArray("profesorClass");
                        for(int i=0; i<currentProfClassArray.length();i++){
                            JSONObject currentProfClss = currentProfClassArray.getJSONObject(i);



                            if(currentProfClss.has("prc_id")&&
                                    !currentProfClss.isNull("prc_id")){
                                prc_id=currentProfClss.getString("prc_id");
                            }
                            if(currentProfClss.has("prc_pro_cod")&&
                                    !currentProfClss.isNull("prc_pro_cod")){
                                prc_pro_cod=currentProfClss.getString("prc_pro_cod");
                            }
                            if(currentProfClss.has("prc_acces_cod")&&
                                    !currentProfClss.isNull("prc_acces_cod")){
                                prc_acces_cod=currentProfClss.getString("prc_acces_cod");
                            }
                            if(currentProfClss.has("prc_ma_modulo")&&
                                    !currentProfClss.isNull("prc_ma_modulo")){
                                prc_ma_modulo=currentProfClss.getString("prc_ma_modulo");
                            }
                            if(currentProfClss.has("prc_ma_cod")&&
                                    !currentProfClss.isNull("prc_ma_cod")){
                                prc_ma_cod=currentProfClss.getString("prc_ma_cod");
                            }
                            if(currentProfClss.has("prc_seccion")&&
                                    !currentProfClss.isNull("prc_seccion")){
                                prc_seccion=currentProfClss.getString("prc_seccion");
                            }

                            ProfAlum profAlum=new ProfAlum();
                            profAlum.setProCodHash(prc_pro_cod);
                            profAlum.setCodAcces(prc_acces_cod);
                            profAlum.setMa_mod(prc_ma_modulo);
                            profAlum.setMa_cod(prc_ma_cod);
                            profAlum.setMa_sec(prc_seccion);

                            //Carga completa del Json
                            listProfClass.add(profAlum);


                        }
                        L.t(context,"ProCod: "+listProfClass.get(0).getProCodHash());
                      //  L.t(context,"List Size: "+listProfClass.size());
                        adapterRecyclerProfProf.setlistProfClass(listProfClass);



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
