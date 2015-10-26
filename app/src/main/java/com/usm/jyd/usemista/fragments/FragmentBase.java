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
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.usm.jyd.usemista.R;

import com.usm.jyd.usemista.adapters.AdapterRecyclerMenu;
import com.usm.jyd.usemista.adapters.AdapterRecyclerSeccionCero;
import com.usm.jyd.usemista.adapters.AdapterRecyclerSeccionCeroMateria;
import com.usm.jyd.usemista.adapters.AdapterReyclerSemestreMateria;
import com.usm.jyd.usemista.adapters.AdapterViewPagerSeccionUno;
import com.usm.jyd.usemista.adapters.SimpleSectionedRecyclerViewAdapter;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.events.ClickCallBackMateriaDialog;
import com.usm.jyd.usemista.events.ClickListener;
import com.usm.jyd.usemista.events.RecyclerTouchListener;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.network.Key;
import com.usm.jyd.usemista.network.UrlEndPoint;
import com.usm.jyd.usemista.network.VolleySingleton;
import com.usm.jyd.usemista.objects.Materia;
import com.usm.jyd.usemista.objects.Semestre;

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
    //Grupo de variables-interfas q habilitan un llamado en la actividad Base
    private ClickCallBack clickCallBack;
    private ClickCallBackMateriaDialog clickCallBackMateriaDialog;

    private String mParam1;
    private OnFragmentInteractionListener mListener;

    private ViewPager viewPager;
    private AdapterViewPagerSeccionUno adapterViewPagerSeccionUno;
    private TabLayout tabLayout;

    private RecyclerView rcListPensums;
    private AdapterRecyclerSeccionCero adapterRecyclerSeccionCero;

    private RecyclerView rcListMenu;
    private AdapterRecyclerMenu adapterRecyclerMenu;


    //Vars Parte en Linea
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private ArrayList<Materia> listMateria = new ArrayList<>();
    private ArrayList<Materia> listUserMateria= new ArrayList<>();
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


      /*  if(getArguments().getInt(ARG_NUMERO_SECCION)==0){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Usemista");

        }
        if(getArguments().getInt(ARG_NUMERO_SECCION)==10){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Pensum & programa");

        }
        if(getArguments().getInt(ARG_NUMERO_SECCION)==100){
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Ingenieria Sistemas");

        }*/
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
            ImageView imageViewIcon= (ImageView)rootView.findViewById(R.id.seccionCeroImageView);
            imageViewIcon.setImageResource(R.drawable.ic_home_white_24dp);
            TextView textViewTituloFragment = (TextView) rootView.findViewById(R.id.seccionCeroTitulo);
            textViewTituloFragment.setText("Home");
            rcListMenu=(RecyclerView) rootView.findViewById(R.id.recycleView);
            NavMenuCallPrincipal();
                ///metodo ON ITEM TOUCH LISTENER
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


        //////////Cambio del fragmento mediante MENU PRINCIPAL ///////////////////////////////
        ///El argumento == 10 indica Pensum///////////////
        if(getArguments().getInt(ARG_NUMERO_SECCION)==10) {
            rootView = inflater.inflate(R.layout.fragment_base_00, container, false);

            ImageView imageViewIcon= (ImageView)rootView.findViewById(R.id.seccionCeroImageView);
            imageViewIcon.setImageResource(R.drawable.ic_pensum_white_24dp_01);
            TextView textViewTituloFragment = (TextView) rootView.findViewById(R.id.seccionCeroTitulo);
            textViewTituloFragment.setText("P&P");

            rcListPensums = (RecyclerView) rootView.findViewById(R.id.recycleView);
            PensumCallCero(); //simple funcion Void para aligerar a la vista

        }
        /////////////////////////////////FIN DEL TRAMO MENU PRICIPAL/////////////////////////


        ////////////// Cambio del Fragmento Mediante SELECCION DE PENSUM///////////////////////
        ///El argumento == 100 indica Pensum de sistema/////////
        if(getArguments().getInt(ARG_NUMERO_SECCION)==100) {

            rootView = inflater.inflate(R.layout.fragment_base_00, container, false);

            ImageView imageViewIcon= (ImageView)rootView.findViewById(R.id.seccionCeroImageView);
            imageViewIcon.setImageResource(R.drawable.ic_gear_white_24dp_01);
            TextView textViewTituloFragment = (TextView) rootView.findViewById(R.id.seccionCeroTitulo);
            textViewTituloFragment.setText("Sistemas");
            textViewVolleyError=(TextView)rootView.findViewById(R.id.textVolleyError);
            recyclerViewListMateria=(RecyclerView)rootView.findViewById(R.id.recycleView);
            recyclerViewListMateria.setLayoutManager(new LinearLayoutManager(getContext()));
            adapterRecyclerSeccionCeroMateria = new AdapterRecyclerSeccionCeroMateria(getContext());
            adapterRecyclerSeccionCeroMateria.setClickListener(getContext(),clickCallBackMateriaDialog);

        /*    //Aca proveemos la lista seccionada por Ejm : Modulo ing, modulo farmacia
            List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                    new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

            //Secciones de pensum
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, "1er Semestre"));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(5, "2do Semestre"));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(10, "3er Semestre"));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(15, "4to Semestre"));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(20, "5to Semestre"));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(25, "6to Semestre"));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(30, "7mo Semestre"));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(34, "8vo Semestre"));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(38, "9no Semestre"));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(44, "10mo Semestre"));
            // sections.add(new SimpleSectionedRecyclerViewAdapter.Section(5, "Farmacia"));

            //Combinamos nuestro adaptador con el Adap seccionador :DDDD  listPensums.setAdapter(adapterRecyclerSeccionCero);
            SimpleSectionedRecyclerViewAdapter.Section[] dummy =
                    new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
            SimpleSectionedRecyclerViewAdapter mSectionedAdapter =
                    new SimpleSectionedRecyclerViewAdapter(getContext(),R.layout.row_rc_section_adapter,
                            R.id.section_text,adapterRecyclerSeccionCeroMateria);
            mSectionedAdapter.setSections(sections.toArray(dummy));

            //finalmente podemos adaptar al Recycler
            recyclerViewListMateria.setAdapter(mSectionedAdapter);*/
           recyclerViewListMateria.setAdapter(adapterRecyclerSeccionCeroMateria);

           /* AdapterReyclerSemestreMateria adapterReyclerSemestreMateria;
            adapterReyclerSemestreMateria =
                    new AdapterReyclerSemestreMateria(getContext(),getListSemestre());

            recyclerViewListMateria.setAdapter(adapterReyclerSemestreMateria);*/

            if(savedInstanceState!=null){

                listMateria=savedInstanceState.getParcelableArrayList(STATE_MATERIA);
                adapterRecyclerSeccionCeroMateria.setMateriaList(listMateria);

            }else{

                listMateria=MiAplicativo.getWritableDatabase().getAllMateriaPensum();
                //L.t(getContext(),"data reg 1 DB: "+listMateria.get(0).getModulo());
                if(listMateria.isEmpty()){
                enviarPeticionJson();}
            }

            adapterRecyclerSeccionCeroMateria.setMateriaList(listMateria);

        }
        /////////////////////////////////FIN DEL TRAMO SELECCION DE PENSUM/////////////////////////
        return  rootView;
    }

    public List<ParentListItem> getListSemestre(){

        enviarPeticionJson();
        L.t(getContext(), "aca: " + listMateria.get(1).getTitulo());
        List<ParentListItem> parenList=new ArrayList<>();
        Semestre s1= new Semestre();Semestre s2= new Semestre();
     /*   Semestre s3= new Semestre();Semestre s4= new Semestre();
        Semestre s5= new Semestre();Semestre s6= new Semestre();
        Semestre s7= new Semestre();Semestre s8= new Semestre();
        Semestre s9= new Semestre();Semestre s10= new Semestre();*/


        List<Materia> filtroListMateria1= new ArrayList<>();
        List<Materia> filtroListMateria2= new ArrayList<>();
      /*  List<Materia> filtroListMateria3= new ArrayList<>();
        List<Materia> filtroListMateria4= new ArrayList<>();
        List<Materia> filtroListMateria5= new ArrayList<>();
        List<Materia> filtroListMateria6= new ArrayList<>();
        List<Materia> filtroListMateria7= new ArrayList<>();
        List<Materia> filtroListMateria8= new ArrayList<>();
        List<Materia> filtroListMateria9= new ArrayList<>();
        List<Materia> filtroListMateria10= new ArrayList<>();*/

        for(int i=0; i<listMateria.size();i++) {
            if(listMateria.get(i).getSemestre().equals("1")){
                filtroListMateria1.add(listMateria.get(i));
                L.t(getContext(),"aca: "+listMateria.get(i).getTitulo());
            }
        }
        for(int i=0; i<listMateria.size();i++) {
            if(listMateria.get(i).getSemestre().equals("2")){
                filtroListMateria2.add(listMateria.get(i));
            }
        }
      /*  for(int i=0; i<listMateria.size();i++) {
            if(listMateria.get(i).getSemestre().equals("3")){
                filtroListMateria3.add(listMateria.get(i));
            }
        }
        for(int i=0; i<listMateria.size();i++) {
            if(listMateria.get(i).getSemestre().equals("4")){
                filtroListMateria4.add(listMateria.get(i));
            }
        }
        for(int i=0; i<listMateria.size();i++) {
            if(listMateria.get(i).getSemestre().equals("5")){
                filtroListMateria5.add(listMateria.get(i));
            }
        }
        for(int i=0; i<listMateria.size();i++) {
            if(listMateria.get(i).getSemestre().equals("6")){
                filtroListMateria6.add(listMateria.get(i));
            }
        }
        for(int i=0; i<listMateria.size();i++) {
            if(listMateria.get(i).getSemestre().equals("7")){
                filtroListMateria7.add(listMateria.get(i));
            }
        }
        for(int i=0; i<listMateria.size();i++) {
            if(listMateria.get(i).getSemestre().equals("8")){
                filtroListMateria8.add(listMateria.get(i));
            }
        }
        for(int i=0; i<listMateria.size();i++) {
            if(listMateria.get(i).getSemestre().equals("9")){
                filtroListMateria9.add(listMateria.get(i));
            }
        }
        for(int i=0; i<listMateria.size();i++) {
            if(listMateria.get(i).getSemestre().equals("10")){
                filtroListMateria10.add(listMateria.get(i));
            }
        }*/

        s1.setMateriaItemList(filtroListMateria1);
        s2.setMateriaItemList(filtroListMateria2);
     /*   s3.setMateriaItemList(filtroListMateria3);
        s4.setMateriaItemList(filtroListMateria4);
        s5.setMateriaItemList(filtroListMateria5);
        s6.setMateriaItemList(filtroListMateria6);
        s7.setMateriaItemList(filtroListMateria7);
        s8.setMateriaItemList(filtroListMateria8);
        s9.setMateriaItemList(filtroListMateria9);
        s10.setMateriaItemList(filtroListMateria10);*/

        parenList.add(s1);parenList.add(s2);/*parenList.add(s3);
        parenList.add(s4);parenList.add(s5);parenList.add(s6);
        parenList.add(s7);parenList.add(s8);parenList.add(s9);
        parenList.add(s10);*/
        return parenList;
    }

    //Llamados NAVIGATION Menuu
    public void NavMenuCallPrincipal(){
        GridLayoutManager manager = new GridLayoutManager(getActivity(),
                2,GridLayoutManager.VERTICAL,false);
        rcListMenu.setLayoutManager(manager);
        adapterRecyclerMenu= new AdapterRecyclerMenu(getContext());
        adapterRecyclerMenu.setClickListener(getContext(),clickCallBack);
        //Agregamos GEstos Touch a nuestro recycler
        rcListMenu.setSoundEffectsEnabled(true);
        rcListMenu.setAdapter(adapterRecyclerMenu);

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

    //Llamados Menu Principal
    public void PensumCallCero(){
        rcListPensums.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Nuestro Adaptador de Data
        adapterRecyclerSeccionCero = new AdapterRecyclerSeccionCero(getActivity());
        adapterRecyclerSeccionCero.setClickListener(getContext(),clickCallBack);

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
        rcListPensums.setAdapter(mSectionedAdapter);


        //Agregamos GEstos Touch a nuestro recycler
        rcListPensums.setSoundEffectsEnabled(true);
     /*   listPensums.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
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
        }));*/
    }


    ///Llamados Pensum////
    public void enviarPeticionJson(){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                UrlEndPoint.URL_PENSUM+UrlEndPoint.URL_QUESTION+
                        UrlEndPoint.URL_MODULO+"="+UrlEndPoint.URL_SIS
                , (String) null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        textViewVolleyError.setVisibility(View.GONE);
                        listMateria=parseJsonResponse(response);
                        MiAplicativo.getWritableDatabase().insertMateriaPensum(listMateria, true);//IMPORTANTE
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
            clickCallBackMateriaDialog=(ClickCallBackMateriaDialog)context;
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
