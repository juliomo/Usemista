package com.usm.jyd.usemista.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.adapters.AdapterRecyclerProfProf;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.dialogs.GuiaUsuario;
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

    //Empty Handle
    private ImageView imgEmptyList;
    private TextView textEmptyList;

    private ProgressDialog progressDialog ;

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

        progressDialog = new ProgressDialog(getActivity());

        listProfClass=new ArrayList<>();
        volleySingleton= VolleySingleton.getInstance();
        requestQueue=volleySingleton.getRequestQueue();

        setHasOptionsMenu(true);

        String auxGuiaUsuario = "";
        auxGuiaUsuario = MiAplicativo.getWritableDatabase().getUserGuia("prof1");
        if (auxGuiaUsuario.equals("0")) {
            GuiaUsuario guiaUsuario = new GuiaUsuario();
            guiaUsuario.setGuiaUsuario("prof1");
            guiaUsuario.show(getChildFragmentManager(),"Dialog");
        }
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
        else if(id==R.id.action_update_list){
            getProfClassInBackEnd(getArguments().getString(ARG_PROF_COD));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_00, container, false);

        ImageView imgIcon=(ImageView)rootView.findViewById(R.id.seccionCeroImageView);
        imgIcon.setImageResource(R.drawable.ic_horario_gris_24dp);
        imgIcon.setColorFilter(0xffffffff);
        TextView textViewTituloFragment = (TextView) rootView.findViewById(R.id.seccionCeroTitulo);
        textViewTituloFragment.setText("Clases");

        rcListProfClass=(RecyclerView) rootView.findViewById(R.id.recycleView);
        rcListProfClass.setLayoutManager(new LinearLayoutManager(getContext()));
        rcListProfClass.setNestedScrollingEnabled(false);
        adapterRecyclerProfProf= new AdapterRecyclerProfProf(getContext(),getArguments().getString(ARG_PROF_COD));
        adapterRecyclerProfProf.setClickCallBack(clickCallBack);

        //seteo de lista Json request Please


        rcListProfClass.setSoundEffectsEnabled(true);
        rcListProfClass.setAdapter(adapterRecyclerProfProf);

        getProfClassInBackEnd(getArguments().getString(ARG_PROF_COD));

        imgEmptyList=(ImageView)rootView.findViewById(R.id.imgEmptyList);
        textEmptyList=(TextView)rootView.findViewById(R.id.textEmptyList);
        if(listProfClass.isEmpty()){
            imgEmptyList.setVisibility(View.VISIBLE);textEmptyList.setVisibility(View.VISIBLE);
            imgEmptyList.setImageResource(R.drawable.ic_profesor_01);
            imgEmptyList.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
            textEmptyList.setText("No Hay Clases Registradas");
            textEmptyList.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
        }

        return rootView;  //super.onCreateView(inflater, container, savedInstanceState);
    }

    public void getProfClassInBackEnd(final String prc_pro_cod) {

        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

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
                    progressDialog.dismiss();
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

                      //  L.t(context,"List Size: "+listProfClass.size());
                        adapterRecyclerProfProf.setlistProfClass(listProfClass);
                        if(listProfClass.isEmpty()){
                            imgEmptyList.setVisibility(View.VISIBLE);textEmptyList.setVisibility(View.VISIBLE);
                            imgEmptyList.setImageResource(R.drawable.ic_profesor_01);
                            imgEmptyList.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                            textEmptyList.setText("No Hay Clases Registradas");
                            textEmptyList.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                        }
                        else{
                            imgEmptyList.setVisibility(View.GONE);textEmptyList.setVisibility(View.GONE);
                        }


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
                        .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getProfClassInBackEnd(prc_pro_cod);
                            }
                        })
                        .setNegativeButton("Canlear", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setMessage("\n\nCode Error: " + auxMsj)
                        .show();

                imgEmptyList.setVisibility(View.VISIBLE);textEmptyList.setVisibility(View.VISIBLE);
                imgEmptyList.setImageResource(R.drawable.ic_profesor_01);
                imgEmptyList.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                textEmptyList.setText(auxMsj);
                textEmptyList.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
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
