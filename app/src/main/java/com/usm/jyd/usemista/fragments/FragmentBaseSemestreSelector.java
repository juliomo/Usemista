package com.usm.jyd.usemista.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.adapters.AdapterRecyclerSemestre;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.events.ClickCallBack;
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

    //Vars Parte en Linea
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private RecyclerView recyclerViewListSemestre;
    private AdapterRecyclerSemestre adapterRecyclerSemestre;

    private ArrayList<Materia> listMateria = new ArrayList<>();
    private TextView textViewVolleyError;

    private ProgressDialog progressDialog ;
    private int persistenTry=0;

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
        progressDialog = new ProgressDialog(getActivity());
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
        String auxTypePensum="", auxTittle="";
        int auxImgCarrera=0;

        if(getArguments().getInt(ARG_NUMERO_SECCION)==100) {
            auxTypePensum=UrlEndPoint.URL_SIS;
            auxTittle="Sistema";
            auxImgCarrera=R.drawable.ic_gear_white_24dp_01;
        }
        else if(getArguments().getInt(ARG_NUMERO_SECCION)==200) {
            auxTypePensum=UrlEndPoint.URL_TELECOM;
            auxTittle="Telecom";
            auxImgCarrera=R.drawable.ic_telecom_white_24dp_01;
        }
        else if(getArguments().getInt(ARG_NUMERO_SECCION)==300) {
            auxTypePensum=UrlEndPoint.URL_INDUSTRIAL;
            auxTittle="Industrial";
            auxImgCarrera=R.drawable.ic_industrial_01_white_24dp;
        }
        else if(getArguments().getInt(ARG_NUMERO_SECCION)==400) {
            auxTypePensum=UrlEndPoint.URL_CIVIL;
            auxTittle="Civil";
            auxImgCarrera=R.drawable.ic_civil_01_white_24dp;
        }
        else if(getArguments().getInt(ARG_NUMERO_SECCION)==500) {
            auxTypePensum=UrlEndPoint.URL_ARQ;
            auxTittle="Arquitectura";
            auxImgCarrera=R.drawable.ic_arq_01_white_24dp;
        }


            rootView = inflater.inflate(R.layout.fragment_base_00, container, false);

            LinearLayout linearLayout=(LinearLayout)rootView.findViewById(R.id.seccionCeroHead);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                }
            });

            ImageView imageViewIcon= (ImageView)rootView.findViewById(R.id.seccionCeroImageView);
            imageViewIcon.setImageResource(auxImgCarrera);
            TextView textViewTituloFragment = (TextView) rootView.findViewById(R.id.seccionCeroTitulo);
            textViewTituloFragment.setText(auxTittle);

            recyclerViewListSemestre=(RecyclerView)rootView.findViewById(R.id.recycleView);
            recyclerViewListSemestre.setLayoutManager(new LinearLayoutManager(getContext()));
            adapterRecyclerSemestre=new AdapterRecyclerSemestre(getContext());
            adapterRecyclerSemestre.setClickListener(getContext(), clickCallBack);

            OffsetDecorationRC offsetDecorationRC=
                    new OffsetDecorationRC(75,35,getContext().getResources().getDisplayMetrics().density);
            recyclerViewListSemestre.addItemDecoration(offsetDecorationRC);
            recyclerViewListSemestre.setAdapter(adapterRecyclerSemestre);






            if(savedInstanceState!=null){

                listMateria=savedInstanceState.getParcelableArrayList(STATE_MATERIA);
                adapterRecyclerSemestre.setListMateria(listMateria);

            }else{

                listMateria= MiAplicativo.getWritableDatabase().getAllMateriaPensumIndividual(auxTypePensum);
                if(listMateria.isEmpty()){
                    enviarPeticionJson(auxTypePensum);}
            }
                adapterRecyclerSemestre.setListMateria(listMateria);





        return rootView;
    }



    //CLass DECORADOR PARA LINEAR RC ESPACIO AL FINAL
    static class OffsetDecorationRC extends RecyclerView.ItemDecoration {
        private int mBottomOffset;
        private int mTopOffset;

        public OffsetDecorationRC(int bottomOffset,int topOffset, float density) {
            mBottomOffset =(int)(bottomOffset * density);
            mTopOffset = (int)(topOffset * density);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int dataSize = state.getItemCount();
            int position =  parent.getChildAdapterPosition(view);
            if (dataSize > 0 && position == dataSize - 1) {
                outRect.set(0, 0, 0, mBottomOffset);
            }else {
                outRect.set(0, 0, 0, 0);
            }

            if(parent.getChildAdapterPosition(view)==0){
                outRect.set(0, mTopOffset, 0, 0);
            }

        }
    }



    ///Llamados Pensum////
    public void enviarPeticionJson(final String ma_modulo){

        progressDialog.setMessage("Cargando ...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                UrlEndPoint.URL_PENSUM+UrlEndPoint.URL_QUESTION+
                        UrlEndPoint.URL_MODULO+"="+ma_modulo
                , (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        listMateria=parseJsonResponse(response);
                        MiAplicativo.getWritableDatabase().insertMateriaPensumIndividual(listMateria, ma_modulo, true);//IMPORTANTE
                        adapterRecyclerSemestre.setListMateria(listMateria);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                progressDialog.dismiss();
                persistenTry++;
                String auxError="";

                if(persistenTry>=5) {
                    persistenTry = 0;

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                        auxError = getString(R.string.error_timeOut);
                    } else if (error instanceof AuthFailureError) {

                        auxError = getString(R.string.error_AuthFail);
                    } else if (error instanceof ServerError) {

                        auxError = getString(R.string.error_Server);
                    } else if (error instanceof NetworkError) {

                        auxError = getString(R.string.error_NetWork);
                    } else if (error instanceof ParseError) {

                        auxError = getString(R.string.error_NetWork);
                    }

                    //  textViewVolleyError.setVisibility(View.VISIBLE);
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("Error en la Nube");
                    alertDialog.setMessage("Error: " + auxError + "\n\n"
                            + "Reintentar Conexion?");
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            enviarPeticionJson(ma_modulo);
                        }
                    });
                    alertDialog.show();
                }else
                    enviarPeticionJson(ma_modulo);


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
                progressDialog.dismiss();
                persistenTry=0;
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
