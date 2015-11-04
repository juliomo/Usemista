package com.usm.jyd.usemista.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.usm.jyd.usemista.adapters.AdapterRecyclerSemestre;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.events.ClickCallBackMateriaDialog;
import com.usm.jyd.usemista.network.Key;
import com.usm.jyd.usemista.network.UrlEndPoint;
import com.usm.jyd.usemista.network.VolleySingleton;
import com.usm.jyd.usemista.objects.Materia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by der_w on 10/31/2015.
 */
public class FragmentBaseSemestreSelector extends Fragment {

    private static final String ARG_NUMERO_SECCION = "numero_seccion";
    private static final String STATE_MATERIA = "state_materia";
    private String mParam1;

    private ClickCallBack clickCallBack;
    private ClickCallBackMateriaDialog clickCallBackMateriaDialog;

    //Vars Parte en Linea
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private RecyclerView recyclerViewListSemestre;
    private AdapterRecyclerSemestre adapterRecyclerSemestre;

    private ArrayList<Materia> listMateria = new ArrayList<>();
    private TextView textViewVolleyError;

    public static FragmentBaseSemestreSelector newInstance(int num_seccion) {
        FragmentBaseSemestreSelector fragment = new FragmentBaseSemestreSelector();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMERO_SECCION, num_seccion);
        fragment.setArguments(args);
        return fragment;
    }
    public FragmentBaseSemestreSelector() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MATERIA, listMateria);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_NUMERO_SECCION);
        }
        //parte en linea unico manipulador web
        volleySingleton=VolleySingleton.getInstance();
        requestQueue=volleySingleton.getRequestQueue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_base_00, container, false);

        ////////////// Cambio del Fragmento Mediante SELECCION DE PENSUM///////////////////////
        ///El argumento == 100 indica Pensum de sistema/////////
        if(getArguments().getInt(ARG_NUMERO_SECCION)==100) {

            rootView = inflater.inflate(R.layout.fragment_base_00, container, false);

            ImageView imageViewIcon= (ImageView)rootView.findViewById(R.id.seccionCeroImageView);
            imageViewIcon.setImageResource(R.drawable.ic_gear_white_24dp_01);
            TextView textViewTituloFragment = (TextView) rootView.findViewById(R.id.seccionCeroTitulo);
            textViewTituloFragment.setText("Sistemas");

            recyclerViewListSemestre=(RecyclerView)rootView.findViewById(R.id.recycleView);
            recyclerViewListSemestre.setLayoutManager(new LinearLayoutManager(getContext()));
            adapterRecyclerSemestre=new AdapterRecyclerSemestre(getContext());
            adapterRecyclerSemestre.setClickListener(getContext(),clickCallBack);

            recyclerViewListSemestre.setAdapter(adapterRecyclerSemestre);


            textViewVolleyError=(TextView)rootView.findViewById(R.id.textVolleyError);



            if(savedInstanceState!=null){

                listMateria=savedInstanceState.getParcelableArrayList(STATE_MATERIA);
                adapterRecyclerSemestre.setListMateria(listMateria);

            }else{

                listMateria= MiAplicativo.getWritableDatabase().getAllMateriaPensumIndividual(UrlEndPoint.URL_SIS);
                if(listMateria.isEmpty()){
                    enviarPeticionJson(UrlEndPoint.URL_SIS);}
            }
                adapterRecyclerSemestre.setListMateria(listMateria);
        }
        ///El argumento == 200 indica Pensum de Telecom/////////
        if(getArguments().getInt(ARG_NUMERO_SECCION)==200) {

            rootView = inflater.inflate(R.layout.fragment_base_00, container, false);

            ImageView imageViewIcon= (ImageView)rootView.findViewById(R.id.seccionCeroImageView);
            imageViewIcon.setImageResource(R.drawable.ic_gear_white_24dp_01);
            TextView textViewTituloFragment = (TextView) rootView.findViewById(R.id.seccionCeroTitulo);
            textViewTituloFragment.setText("Telecom");

            recyclerViewListSemestre=(RecyclerView)rootView.findViewById(R.id.recycleView);
            recyclerViewListSemestre.setLayoutManager(new LinearLayoutManager(getContext()));
            adapterRecyclerSemestre=new AdapterRecyclerSemestre(getContext());
            adapterRecyclerSemestre.setClickListener(getContext(),clickCallBack);

            recyclerViewListSemestre.setAdapter(adapterRecyclerSemestre);


            textViewVolleyError=(TextView)rootView.findViewById(R.id.textVolleyError);



            if(savedInstanceState!=null){

                listMateria=savedInstanceState.getParcelableArrayList(STATE_MATERIA);
                adapterRecyclerSemestre.setListMateria(listMateria);

            }else{

                listMateria= MiAplicativo.getWritableDatabase().getAllMateriaPensumIndividual(UrlEndPoint.URL_TELECOM);
                if(listMateria.isEmpty()){
                    enviarPeticionJson(UrlEndPoint.URL_TELECOM);}
            }
            adapterRecyclerSemestre.setListMateria(listMateria);
        }

        return rootView;
    }



    ///Llamados Pensum////
    public void enviarPeticionJson(final String ma_modulo){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                UrlEndPoint.URL_PENSUM+UrlEndPoint.URL_QUESTION+
                        UrlEndPoint.URL_MODULO+"="+ma_modulo
                , (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        textViewVolleyError.setVisibility(View.GONE);
                        listMateria=parseJsonResponse(response);
                        MiAplicativo.getWritableDatabase().insertMateriaPensumIndividual(listMateria, ma_modulo, true);//IMPORTANTE
                        adapterRecyclerSemestre.setListMateria(listMateria);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                textViewVolleyError.setVisibility(View.VISIBLE);

                if (error instanceof TimeoutError || error instanceof NoConnectionError){
                    textViewVolleyError.setText(R.string.error_timeOut);

                }else if(error instanceof AuthFailureError){
                    textViewVolleyError.setText(R.string.error_AuthFail);

                }else if (error instanceof ServerError){
                    textViewVolleyError.setText(R.string.error_Server);

                }else if (error instanceof NetworkError){
                    textViewVolleyError.setText(R.string.error_NetWork);

                }else if (error instanceof ParseError){
                    textViewVolleyError.setText(R.string.error_Parse);

                }
            }
        });

        //sin esta linea no se puede hacer la peticion al server
        requestQueue.add(request);
    }
    public ArrayList<Materia> parseJsonResponse(JSONObject response){
        ArrayList<Materia> listMateria = new ArrayList<>();

        //  if(response.has(Key.EndPointMateria.KEY_ESTADO)&&
        //        !response.isNull(Key.EndPointMateria.KEY_ESTADO))



        if(response==null || response.length()>0){
            try{
                String estado="NA";
                if(response.has(Key.EndPointMateria.KEY_ESTADO)&&
                        !response.isNull(Key.EndPointMateria.KEY_ESTADO)){
                    estado = response.getString(Key.EndPointMateria.KEY_ESTADO);}

                String ma_id ="NA";
                String ma_cod ="NA";
                String ma_titulo = "NA";
                String ma_semestre ="NA";
                String ma_objetivo ="NA";
                String ma_contenido ="NA";
                String ma_modulo= "NA";

                JSONArray currentPensum = response.getJSONArray("materia");
                for(int i=0; i<currentPensum.length();i++){
                    JSONObject currentMateria = currentPensum.getJSONObject(i);

                    if(currentMateria.has(Key.EndPointMateria.KEY_ID)&&
                            !currentMateria.isNull(Key.EndPointMateria.KEY_ID)){
                        ma_id=currentMateria.getString(Key.EndPointMateria.KEY_ID);
                    }
                    if(currentMateria.has(Key.EndPointMateria.KEY_COD)&&
                            !currentMateria.isNull(Key.EndPointMateria.KEY_COD)){
                        ma_cod=currentMateria.getString(Key.EndPointMateria.KEY_COD);
                    }
                    if(currentMateria.has(Key.EndPointMateria.KEY_TITULO)&&
                            !currentMateria.isNull(Key.EndPointMateria.KEY_TITULO)){
                        ma_titulo=currentMateria.getString(Key.EndPointMateria.KEY_TITULO);
                    }
                    if(currentMateria.has(Key.EndPointMateria.KEY_SEMESTRE)&&
                            !currentMateria.isNull(Key.EndPointMateria.KEY_SEMESTRE)){
                        ma_semestre=currentMateria.getString(Key.EndPointMateria.KEY_SEMESTRE);
                    }
                    if(currentMateria.has(Key.EndPointMateria.KEY_OBJETIVO)&&
                            !currentMateria.isNull(Key.EndPointMateria.KEY_OBJETIVO)){
                        ma_objetivo=currentMateria.getString(Key.EndPointMateria.KEY_OBJETIVO);
                    }
                    if(currentMateria.has(Key.EndPointMateria.KEY_CONTENIDO)&&
                            !currentMateria.isNull(Key.EndPointMateria.KEY_CONTENIDO)){
                        ma_contenido=currentMateria.getString(Key.EndPointMateria.KEY_CONTENIDO);
                    }
                    if(currentMateria.has(Key.EndPointMateria.KEY_MODULO)&&
                            !currentMateria.isNull(Key.EndPointMateria.KEY_MODULO)){
                        ma_modulo=currentMateria.getString(Key.EndPointMateria.KEY_MODULO);
                    }

                    Materia materia =new Materia();
                    materia.setId(ma_id);
                    materia.setCod(ma_cod);
                    materia.setTitulo(ma_titulo);
                    materia.setSemestre(ma_semestre);
                    materia.setObjetivo(ma_objetivo);
                    materia.setContenido(ma_contenido);
                    materia.setModulo(ma_modulo);
                    materia.setU_materia("0");

                    //Carga completa del Json
                    listMateria.add(materia);

                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }


        return listMateria;
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
