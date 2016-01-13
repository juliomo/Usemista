package com.usm.jyd.usemista.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.usm.jyd.usemista.adapters.AdapterRecyclerProfAlum;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.dialogs.GuiaUsuario;
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

    //Empty Handle
    private ImageView imgEmptyList;
    private TextView textEmptyList;


    public static FragmentBaseProfAlum newInstance() {
        FragmentBaseProfAlum fragment = new FragmentBaseProfAlum();
        return fragment;
    }
    public FragmentBaseProfAlum(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAlumClass=new ArrayList<>();
        volleySingleton= VolleySingleton.getInstance();
        requestQueue=volleySingleton.getRequestQueue();

        userRegistro= MiAplicativo.getWritableDatabase().getUserRegistro();
        setHasOptionsMenu(true);

        String auxGuiaUsuario = "";
        auxGuiaUsuario = MiAplicativo.getWritableDatabase().getUserGuia("alum");
        if (auxGuiaUsuario.equals("0")) {
            GuiaUsuario guiaUsuario = new GuiaUsuario();
            guiaUsuario.setGuiaUsuario("alum");
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
            adapterRecyclerProfAlum.setEdition(true);
            getActivity().invalidateOptionsMenu();
        }
        else if(id==R.id.action_save){
            flagEdition=false;
            adapterRecyclerProfAlum.setEdition(false);
            getActivity().invalidateOptionsMenu();
        }else if(id==R.id.action_update_list){
            getAlumClassInBackEnd(userRegistro.getCi());
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

        rcListAlumClassPe=(RecyclerView) rootView.findViewById(R.id.recycleView);
        rcListAlumClassPe.setLayoutManager(new LinearLayoutManager(getContext()));
        rcListAlumClassPe.setNestedScrollingEnabled(false);
        adapterRecyclerProfAlum= new AdapterRecyclerProfAlum(getContext());
        adapterRecyclerProfAlum.setClickCallBack(clickCallBack);

        //seteo de lista Json request Please


        rcListAlumClassPe.setSoundEffectsEnabled(true);
        rcListAlumClassPe.setAdapter(adapterRecyclerProfAlum);


        imgEmptyList=(ImageView)rootView.findViewById(R.id.imgEmptyList);
        textEmptyList=(TextView)rootView.findViewById(R.id.textEmptyList);
        if(listAlumClass.isEmpty()){
            imgEmptyList.setVisibility(View.VISIBLE);textEmptyList.setVisibility(View.VISIBLE);
            imgEmptyList.setImageResource(R.drawable.ic_profesor_01);
            imgEmptyList.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
            textEmptyList.setText("No Hay Clases Registradas");
            textEmptyList.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
        }

        getAlumClassInBackEnd(userRegistro.getCi());

        return rootView;  //super.onCreateView(inflater, container, savedInstanceState);
    }

    public void getAlumClassInBackEnd(final String cedula) {

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

                        //  L.t(context,"List Size: "+listProfClass.size());
                        adapterRecyclerProfAlum.setlistAlumClass(listAlumClass);
                        if(listAlumClass.isEmpty()){
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
                                getAlumClassInBackEnd(cedula);
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
