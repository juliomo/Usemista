package com.usm.jyd.usemista.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
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

import com.usm.jyd.usemista.adapters.AdapterRecyclerMenu;
import com.usm.jyd.usemista.adapters.AdapterRecyclerSeccionCero;
import com.usm.jyd.usemista.adapters.AdapterRecyclerSeccionCeroMateria;
import com.usm.jyd.usemista.adapters.AdapterViewPagerSeccionUno;
import com.usm.jyd.usemista.adapters.SimpleSectionedRecyclerViewAdapter;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.events.ClickListener;
import com.usm.jyd.usemista.events.RecyclerTouchListener;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.network.Key;
import com.usm.jyd.usemista.network.VolleySingleton;
import com.usm.jyd.usemista.objects.Materia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentBase.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentBase#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBase extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NUMERO_SECCION = "numero_seccion";
    private static final String STATE_MATERIA = "state_materia";


    // TODO: Rename and change types of parameters
    private ClickCallBack clickCallBack;

    private String mParam1;

    private OnFragmentInteractionListener mListener;

    private ViewPager viewPager;
    private AdapterViewPagerSeccionUno adapterViewPagerSeccionUno;
    private TabLayout tabLayout;

    private RecyclerView listPensums;
    private AdapterRecyclerSeccionCero adapterRecyclerSeccionCero;

    private RecyclerView rcListMenu;
    private AdapterRecyclerMenu adapterRecyclerMenu;


    //Vars Parte en Linea
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private ArrayList<Materia> listMateria = new ArrayList<>();
    private TextView textViewVolleyError;
    private RecyclerView recyclerViewListMateria;
    private AdapterRecyclerSeccionCeroMateria adapterRecyclerSeccionCeroMateria;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param num_seccion Parameter 1.
     * @return A new instance of fragment FragmentBase.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBase newInstance(int num_seccion) {
        FragmentBase fragment = new FragmentBase();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMERO_SECCION, num_seccion);
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentBase() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MATERIA,listMateria);
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

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.app_bar);

        if(getArguments().getInt(ARG_NUMERO_SECCION)==0){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Usemista");

        }
        if(getArguments().getInt(ARG_NUMERO_SECCION)==10){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Pensum & programa");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_base_01, container, false);

        //////////Cambio del fragmento mediante NAVIGATION VIEW ///////////////////////////////
        ///El argumento == 0 indica HOME/////////
        if(getArguments().getInt(ARG_NUMERO_SECCION)==0) {
            rootView = inflater.inflate(R.layout.fragment_base_00, container, false);
            TextView textViewTituloFragment = (TextView) rootView.findViewById(R.id.seccionCeroTitulo);
            textViewTituloFragment.setText("Inicio");

            rcListMenu=(RecyclerView) rootView.findViewById(R.id.recycleView);
            GridLayoutManager manager = new GridLayoutManager(getActivity(),
                    2,GridLayoutManager.VERTICAL,false);
            rcListMenu.setLayoutManager(manager);
            adapterRecyclerMenu= new AdapterRecyclerMenu(getContext());
            adapterRecyclerMenu.setClickListener(getContext(),clickCallBack);
            //Agregamos GEstos Touch a nuestro recycler
            rcListMenu.setSoundEffectsEnabled(true);
            rcListMenu.setAdapter(adapterRecyclerMenu);



         /*   rcListMenu.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                    rcListMenu, new ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    int aux=10;aux=aux+position;
                    L.t(getContext(), "On Click " + aux);
                    rcListMenu.playSoundEffect(SoundEffectConstants.CLICK);

                    if (clickCallBack != null && position==0) {
                        clickCallBack.onRSCItemSelected(aux);
                    }
                }

                @Override
                public void onLongClick(View view, int position) {
                    L.t(getContext(), "Long Click on This");
                }
            }));*/

        }
        ///El argumento == 1 indica TEST/////////
        if(getArguments().getInt(ARG_NUMERO_SECCION)==1) {
            rootView = inflater.inflate(R.layout.fragment_base_01, container, false);
            viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
            tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
            NavMenuCallTest();//simple funcion Void para aligerar a la vista
        }
        /////////////////////////////////FIN DEL TRAMO NAVIGATION VIEW/////////////////////////


        ///El argumento == 10 indica Pensum///////////////
        if(getArguments().getInt(ARG_NUMERO_SECCION)==10) {
            rootView = inflater.inflate(R.layout.fragment_base_00, container, false);
            TextView textViewTituloFragment = (TextView) rootView.findViewById(R.id.seccionCeroTitulo);
            textViewTituloFragment.setText("Pensum y Programa");

            listPensums = (RecyclerView) rootView.findViewById(R.id.recycleView);
            NavMenuCallCero();//simple funcion Void para aligerar a la vista

        }




        ////////////// Cambio del Fragmento Mediante Seleccion de Pensum///////////////////////
        ///El argumento == 100 indica Pensum de sistema/////////
        if(getArguments().getInt(ARG_NUMERO_SECCION)==100) {

            rootView = inflater.inflate(R.layout.fragment_base_00, container, false);
            TextView textViewTituloFragment = (TextView) rootView.findViewById(R.id.seccionCeroTitulo);
            textViewTituloFragment.setText("Ingenieria Sistemas");
            textViewVolleyError=(TextView)rootView.findViewById(R.id.textVolleyError);
            recyclerViewListMateria=(RecyclerView)rootView.findViewById(R.id.recycleView);
            recyclerViewListMateria.setLayoutManager(new LinearLayoutManager(getContext()));
            adapterRecyclerSeccionCeroMateria = new AdapterRecyclerSeccionCeroMateria(getContext());

            recyclerViewListMateria.setAdapter(adapterRecyclerSeccionCeroMateria);

            if(savedInstanceState!=null){

                listMateria=savedInstanceState.getParcelableArrayList(STATE_MATERIA);
                adapterRecyclerSeccionCeroMateria.setMateriaList(listMateria);
            }else{

                enviarPeticionJson();
            }

        }
        /////////////////////////////////FIN DEL TRAMO Seleccion de pensum/////////////////////////
        return  rootView;
    }


    //Llamados NAVIGATION Menuu
    public void NavMenuCallCero(){
        listPensums.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Nuestro Adaptador de Data
        adapterRecyclerSeccionCero = new AdapterRecyclerSeccionCero(getActivity());


        //Aca proveemos la lista seccionada por Ejm : Modulo ing, modulo farmacia
        List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

        //Secciones de pensum
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, "Ingenieria y Arquitectura"));
      // sections.add(new SimpleSectionedRecyclerViewAdapter.Section(5, "Farmacia"));

        //Combinamos nuestro adaptador con el Adap seccionador :DDDD  listPensums.setAdapter(adapterRecyclerSeccionCero);
        SimpleSectionedRecyclerViewAdapter.Section[] dummy =
                new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter =
                new SimpleSectionedRecyclerViewAdapter(getContext(),R.layout.row_rc_section_adapter,
                        R.id.section_text,adapterRecyclerSeccionCero);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //finalmente podemos adaptar al Recycler
        listPensums.setAdapter(mSectionedAdapter);


        //Agregamos GEstos Touch a nuestro recycler
        listPensums.setSoundEffectsEnabled(true);
        listPensums.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                listPensums, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                L.t(getContext(), "On Click " + (100 * position));
                listPensums.playSoundEffect(SoundEffectConstants.CLICK);

                if (position != 0 && clickCallBack != null && position == 1) {
                    clickCallBack.onRSCItemSelected(100 * position);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                L.t(getContext(), "Long Click on This");
            }
        }));
    }
    public void NavMenuCallTest(){

        adapterViewPagerSeccionUno = new AdapterViewPagerSeccionUno(getFragmentManager());
        viewPager.setAdapter(adapterViewPagerSeccionUno);
        //link between  tabs an pager adapter
        tabLayout.setTabsFromPagerAdapter(adapterViewPagerSeccionUno);
        //link tab & viewpager object
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }


    ///Llamados Pensum////
    public void enviarPeticionJson(){



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                "http://usmpemsun.esy.es/materias?ma_modulo=ingSis"
                , (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        textViewVolleyError.setVisibility(View.GONE);
                        listMateria=parseJsonResponse(response);
                        adapterRecyclerSeccionCeroMateria.setMateriaList(listMateria);

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
                    materia.setTitulo(ma_titulo);
                    materia.setSemestre(ma_semestre);
                    materia.setObjetivo(ma_objetivo);
                    materia.setContenido(ma_contenido);
                    materia.setModulo(ma_modulo);

                    //Carga completa del Json
                    listMateria.add(materia);

                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }


        return listMateria;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
