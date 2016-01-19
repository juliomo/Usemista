package com.usm.jyd.usemista.fragments;

import android.app.ProgressDialog;
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
import com.usm.jyd.usemista.adapters.AdapterRecyclerProfProfItem;
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
 * Created by der_w on 12/16/2015.
 */
public class FragmentBasePPyPAItem extends Fragment {


    private static final String ARG_PROFCOD = "profCod";
    private static final String ARG_ACCESCOD = "accesCod";
    private String profOrUser, profCod, accesCod;

    private UserRegistro userRegistro;

    private ClickCallBack clickCallBack;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private RecyclerView rcListAlumInClass;
    private AdapterRecyclerProfProfItem adapterRecyclerProfProfItem;
    private ArrayList<ProfAlum> listAlumInClass;
    private ArrayList<ProfAlum> listAlumInClassFilter;
    private Boolean flagEdition=false;

    //Empty Handle
    private ImageView imgEmptyList;
    private TextView textEmptyList;

    private ProgressDialog progressDialog ;
    private int persistenTry=0;

    public static FragmentBasePPyPAItem newInstance( String profCod, String accesCod) {
        FragmentBasePPyPAItem fragment = new FragmentBasePPyPAItem();
        Bundle args = new Bundle();
        args.putString(ARG_PROFCOD, profCod);
        args.putString(ARG_ACCESCOD, accesCod);
        fragment.setArguments(args);
        return fragment;
    }
    public FragmentBasePPyPAItem(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(getActivity());

        if (getArguments() != null) {
            profCod = getArguments().getString(ARG_PROFCOD);
            accesCod = getArguments().getString(ARG_ACCESCOD);
        }

        listAlumInClass=new ArrayList<>();
        listAlumInClassFilter=new ArrayList<>();
        volleySingleton= VolleySingleton.getInstance();
        requestQueue=volleySingleton.getRequestQueue();

        userRegistro= MiAplicativo.getWritableDatabase().getUserRegistro();
        setHasOptionsMenu(true);

        String auxGuiaUsuario = "";
        auxGuiaUsuario = MiAplicativo.getWritableDatabase().getUserGuia("prof2");
        if (auxGuiaUsuario.equals("0")) {
            GuiaUsuario guiaUsuario = new GuiaUsuario();
            guiaUsuario.setGuiaUsuario("prof2");
            guiaUsuario.show(getChildFragmentManager(),"Dialog");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.act_base_fr_prof_prof_item_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_filter_aprobados).setChecked(true);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.action_update_list){
            getAlumInClassInBackEnd(true);
            getActivity().invalidateOptionsMenu();
        }
        else if(id==R.id.action_filter_aprobados){
            item.setChecked(!item.isChecked());

            getAlumInClassInBackEnd(true);

        }else if(id==R.id.action_filter_esperando) {
            item.setChecked(!item.isChecked());
            getAlumInClassInBackEnd(false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_00, container, false);

        if(userRegistro.getStatus().equals("1")){

        }
        if(userRegistro.getStatus().equals("2")) {

            ImageView imgIcon=(ImageView)rootView.findViewById(R.id.seccionCeroImageView);
            imgIcon.setImageResource(R.drawable.ic_horario_gris_24dp);
            imgIcon.setColorFilter(0xffffffff);
            TextView textViewTituloFragment = (TextView) rootView.findViewById(R.id.seccionCeroTitulo);
            textViewTituloFragment.setText("Estudiantes");

            rcListAlumInClass =(RecyclerView) rootView.findViewById(R.id.recycleView);
            rcListAlumInClass.setLayoutManager(new LinearLayoutManager(getContext()));
            rcListAlumInClass.setNestedScrollingEnabled(false);
            adapterRecyclerProfProfItem = new AdapterRecyclerProfProfItem(getContext());
            adapterRecyclerProfProfItem.setClickCallBack(clickCallBack);

            //seteo de lista Json request Please


            rcListAlumInClass.setSoundEffectsEnabled(true);
            rcListAlumInClass.setAdapter(adapterRecyclerProfProfItem);

            imgEmptyList=(ImageView)rootView.findViewById(R.id.imgEmptyList);
            textEmptyList=(TextView)rootView.findViewById(R.id.textEmptyList);
            if(listAlumInClass.isEmpty()){
                imgEmptyList.setVisibility(View.VISIBLE);textEmptyList.setVisibility(View.VISIBLE);
                imgEmptyList.setImageResource(R.drawable.ic_materia_01);
                imgEmptyList.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                textEmptyList.setText("No hay Registro de Alumnos");
                textEmptyList.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
            }

            getAlumInClassInBackEnd(true);
        }




        return rootView;  //super.onCreateView(inflater, container, savedInstanceState);
    }

    public void getAlumInClassInBackEnd(final boolean flagEstado) {

        progressDialog.setMessage("Cargando ...try: "+persistenTry);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final Context context=getContext();
        String url = "http://usmpemsun.esy.es/fr_prof_alum";

        Map<String, String> map = new HashMap<>();
        map.put("geProfAlmCls", "1");
        map.put("prc_pro_cod", profCod);
        map.put("prc_acces_cod", accesCod);

        JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST,
                url,new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    progressDialog.dismiss();persistenTry=0;
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


                        listAlumInClass =new ArrayList<>();

                        JSONArray currentProfClassArray = response.getJSONArray("profesorClassAlum");
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
                            profAlum.setId(alc_id);
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
                            listAlumInClass.add(profAlum);


                        }


                        listAlumInClassFilter=new ArrayList<>();
                        if(flagEstado){
                            for(int i=0;i<listAlumInClass.size();i++){
                                if(listAlumInClass.get(i).getRegist().equals("1")){
                                    listAlumInClassFilter.add(listAlumInClass.get(i));
                                }
                            }
                        }else{
                            for(int i=0;i<listAlumInClass.size();i++){
                                if(listAlumInClass.get(i).getRegist().equals("0")){
                                    listAlumInClassFilter.add(listAlumInClass.get(i));
                                }
                            }
                        }

                        adapterRecyclerProfProfItem.setListAlumInClass(listAlumInClassFilter);
                        if(listAlumInClassFilter.isEmpty()){
                            imgEmptyList.setVisibility(View.VISIBLE);textEmptyList.setVisibility(View.VISIBLE);
                            imgEmptyList.setImageResource(R.drawable.ic_profesor_01);
                            imgEmptyList.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                            if(flagEstado)
                            textEmptyList.setText("No hay Alumnos Comprobados");
                            else
                                textEmptyList.setText("No hay Alumnos en Espera");
                            textEmptyList.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                        }
                        else{
                            imgEmptyList.setVisibility(View.GONE);textEmptyList.setVisibility(View.GONE);
                        }

                    }else if(estado.equals("2")){
                        L.t(context,"No Hay  Registro");
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

                imgEmptyList.setVisibility(View.VISIBLE);textEmptyList.setVisibility(View.VISIBLE);
                imgEmptyList.setImageResource(R.drawable.ic_profesor_01);
                imgEmptyList.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                textEmptyList.setText(auxMsj);
                textEmptyList.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));

                if(persistenTry>=5) {
                    persistenTry = 0;


                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        auxMsj = "Fuera de Conexion \nFuera de Tiempo";
                    } else if (error instanceof AuthFailureError) {
                        auxMsj = "Fallo de Ruta";
                    } else if (error instanceof ServerError) {
                        auxMsj = "Fallo en el Servidor";
                    } else if (error instanceof NetworkError) {
                        auxMsj = "Problemas de Conexion";
                    } else if (error instanceof ParseError) {
                        auxMsj = "Problemas Internos";
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Error en la Nube")
                            .setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getAlumInClassInBackEnd(flagEstado);
                                }
                            })
                            .setNegativeButton("Canlear", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setMessage("\n\nCode Error: " + auxMsj)
                            .show();
                }else
                    getAlumInClassInBackEnd(flagEstado);

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
