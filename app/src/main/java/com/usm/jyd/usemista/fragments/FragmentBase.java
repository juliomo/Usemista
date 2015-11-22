package com.usm.jyd.usemista.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
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

import com.android.volley.RequestQueue;
import com.usm.jyd.usemista.R;

import com.usm.jyd.usemista.adapters.AdapterRecyclerHorarioV;
import com.usm.jyd.usemista.adapters.AdapterRecyclerMenu;
import com.usm.jyd.usemista.adapters.AdapterRecyclerPensum;
import com.usm.jyd.usemista.adapters.AdapterViewPagerSeccionUno;
import com.usm.jyd.usemista.adapters.SimpleSectionedRecyclerViewAdapter;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.network.VolleySingleton;
import com.usm.jyd.usemista.objects.HorarioVirtual;
import com.usm.jyd.usemista.objects.Materia;

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

    //GRUPO de variables HORARIO VIRTUAL
    private ArrayList<Materia> listUserMateria = new ArrayList<>();
    private ArrayList<Materia> listUMLoad = new ArrayList<>();
    private ArrayList<HorarioVirtual> listHV=new ArrayList<>();
    private ArrayList<HorarioVirtual> listHVLoad=new ArrayList<>();

    private String mParam1;
    private OnFragmentInteractionListener mListener;

    //Grupo de Fragment Test
    private ViewPager viewPager;
    private AdapterViewPagerSeccionUno adapterViewPagerSeccionUno;
    private TabLayout tabLayout;
    //fin Grupo fragment Test

    private RecyclerView rcListPensums;
    private AdapterRecyclerPensum adapterRecyclerPensum;

    private RecyclerView rcListMenu;
    private AdapterRecyclerMenu adapterRecyclerMenu;

    private RecyclerView rcListHorarioVirtual;
    private AdapterRecyclerHorarioV adapterRecyclerHorarioV;

    //Vars Parte en Linea
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private ArrayList<Materia> listMateria = new ArrayList<>();



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
    //Propuesta para Separar semestre-materia
    public static  FragmentBase newInstance2(int num_seccion, List<Materia> list){
        FragmentBase fragmentBase = new FragmentBase();

        return fragmentBase;
    }

    public FragmentBase() {
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

        if(getArguments().getInt(ARG_NUMERO_SECCION)==12) {
          ArrayList<Materia>  listUserMateriaAUX = MiAplicativo.getWritableDatabase().getAllUserMateria();
            for(int i=0;i<listUserMateriaAUX.size();i++){
                if(listUserMateriaAUX.get(i).getU_materia().equals("1")){
                    listUserMateria.add(listUserMateriaAUX.get(i));
                }
            }
            listHV=MiAplicativo.getWritableDatabase().getAllHorarioVirtual();
        }
        setHasOptionsMenu(true);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.act_base_fr_sec_menu, menu);
        if(getArguments().getInt(ARG_NUMERO_SECCION)==12) {
            menu.findItem(R.id.action_pensum).setVisible(true);

            if(!listUserMateria.isEmpty()) {
                boolean sis = false, telc = false;
                for (int i = 0; i < listUserMateria.size(); i++) {
                    if (listUserMateria.get(i).getModulo().equals("ingSis")) {
                        sis = true;
                    } else if (listUserMateria.get(i).getModulo().equals("telecom")) {
                        telc = true;
                    }
                }

                if(!sis){menu.findItem(R.id.action_sis).setVisible(false);}
                if(!telc){menu.findItem(R.id.action_telc).setVisible(false);}

                if (listUserMateria.get(0).getModulo().equals("ingSis")) {
                    menu.findItem(R.id.action_sis).setChecked(true);
                } else if (listUserMateria.get(0).getModulo().equals("telecom")) {
                    menu.findItem(R.id.action_telc).setChecked(true);
                }

            }



        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.action_sis){
            item.setChecked(!item.isChecked());
            listUMLoad = new ArrayList<>();
            listHVLoad= new ArrayList<>();

            for (int i = 0; i < listUserMateria.size(); i++) {
                if(listUserMateria.get(i).getModulo().equals("ingSis")){
                    listUMLoad.add(listUserMateria.get(i));
                }
            }

            for(int i=0; i<listUMLoad.size();i++){
                for(int j=0;j<listHV.size();j++){
                    if(listUMLoad.get(i).getCod().equals(listHV.get(j).getCod())){
                        listHVLoad.add(listHV.get(j));
                    }
                }
            }

            adapterRecyclerHorarioV.setListHorarioV(listHVLoad);

        }
        if (id == R.id.action_telc){
            item.setChecked(!item.isChecked());
            listUMLoad = new ArrayList<>();
            listHVLoad = new ArrayList<>();

            for (int i = 0; i < listUserMateria.size(); i++) {
                if(listUserMateria.get(i).getModulo().equals("telecom")){
                    listUMLoad.add(listUserMateria.get(i));
                }
            }

            for(int i=0; i<listUMLoad.size();i++){
                for(int j=0;j<listHV.size();j++){
                    if(listUMLoad.get(i).getCod().equals(listHV.get(j).getCod())){
                        listHVLoad.add(listHV.get(j));
                    }
                }
            }

            adapterRecyclerHorarioV.setListHorarioV(listHVLoad);

        }


        return super.onOptionsItemSelected(item);
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
        ///El argumento == 12 indica Horario Virtual/////////////////
        if(getArguments().getInt(ARG_NUMERO_SECCION)==12){

            if(!listUserMateria.isEmpty()) {
                for (int i = 0; i < listUserMateria.size(); i++) {
                    if (listUserMateria.get(i).getModulo().equals(listUserMateria.get(0).getModulo())) {
                        listUMLoad.add(listUserMateria.get(i));
                    }
                }
            }
            if(!listHV.isEmpty()){
                for(int i=0; i<listUMLoad.size();i++){
                    for(int j=0;j<listHV.size();j++){
                        if(listUMLoad.get(i).getCod().equals(listHV.get(j).getCod())){
                            listHVLoad.add(listHV.get(j));
                        }
                    }
                }
            }


            rootView = inflater.inflate(R.layout.fragment_base_00, container,false);

            TextView textViewTituloFragment = (TextView) rootView.findViewById(R.id.seccionCeroTitulo);
            textViewTituloFragment.setText("HV");

            rcListHorarioVirtual=(RecyclerView)rootView.findViewById(R.id.recycleView);
            rcListHorarioVirtual.setLayoutManager(new LinearLayoutManager(getActivity()));

            adapterRecyclerHorarioV=new AdapterRecyclerHorarioV(getContext());
            adapterRecyclerHorarioV.setClickCallBack(clickCallBack);
            adapterRecyclerHorarioV.setListHorarioV(listHVLoad);
            rcListHorarioVirtual.setSoundEffectsEnabled(true);
            rcListHorarioVirtual.setAdapter(adapterRecyclerHorarioV);



        }
        /////////////////////////////////FIN DEL TRAMO MENU PRICIPAL/////////////////////////


        return  rootView;
    }



    //Llamados NAVIGATION Menuu
    public void NavMenuCallPrincipal(){
        GridLayoutManager manager = new GridLayoutManager(getActivity(),
                2,GridLayoutManager.VERTICAL,false);
        rcListMenu.setLayoutManager(manager);
        adapterRecyclerMenu= new AdapterRecyclerMenu(getContext());
        adapterRecyclerMenu.setClickListener(getContext(), clickCallBack);
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
        adapterRecyclerPensum = new AdapterRecyclerPensum(getActivity());
        adapterRecyclerPensum.setClickListener(getContext(),clickCallBack);

        //Aca proveemos la lista seccionada por Ejm : Modulo ing, modulo farmacia
        List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

        //Secciones de pensum
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, "Ingenieria y Arquitectura"));
        // sections.add(new SimpleSectionedRecyclerViewAdapter.Section(5, "Farmacia"));

        //Combinamos nuestro adaptador con el Adap seccionador :DDDD  listPensums.setAdapter(lerSeccionCero);
        SimpleSectionedRecyclerViewAdapter.Section[] dummy =
                new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter =
                new SimpleSectionedRecyclerViewAdapter(getContext(),R.layout.row_rc_section_adapter,
                        R.id.section_text, adapterRecyclerPensum);
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
