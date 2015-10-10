package com.usm.jyd.usemista.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.usm.jyd.usemista.R;

import com.usm.jyd.usemista.adapters.AdapterRecyclerSeccionCero;
import com.usm.jyd.usemista.adapters.AdapterViewPagerSeccionUno;
import com.usm.jyd.usemista.adapters.SimpleSectionedRecyclerViewAdapter;

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


    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;

    private ViewPager viewPager;
    private AdapterViewPagerSeccionUno adapterViewPagerSeccionUno;
    private TabLayout tabLayout;

    private RecyclerView listPensums;
    private AdapterRecyclerSeccionCero adapterRecyclerSeccionCero;


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_NUMERO_SECCION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_base_01, container, false);

        if(getArguments().getInt(ARG_NUMERO_SECCION)==0) {

            rootView = inflater.inflate(R.layout.fragment_base_00, container, false);
            listPensums = (RecyclerView) rootView.findViewById(R.id.recycleView);
            listPensums.setLayoutManager(new LinearLayoutManager(getActivity()));

            //Nuestro Adaptador de Data
            adapterRecyclerSeccionCero = new AdapterRecyclerSeccionCero(getActivity());


            //Aca proveemos la lista seccionada por Ejm : Modulo ing, modulo farmacia
            List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                    new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

            //Secciones de pensum
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0, "Ingenieria y Arquitectura"));
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(5, "Farmacia"));

            //Combinamos nuestro adaptador con el Adap seccionador :DDDD  listPensums.setAdapter(adapterRecyclerSeccionCero);
            SimpleSectionedRecyclerViewAdapter.Section[] dummy =
                    new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
            SimpleSectionedRecyclerViewAdapter mSectionedAdapter =
                    new SimpleSectionedRecyclerViewAdapter(getContext(),R.layout.section_recycler_adapter,
                            R.id.section_text,adapterRecyclerSeccionCero);
            mSectionedAdapter.setSections(sections.toArray(dummy));

            //finalmente podemos adaptar al Recycler
            listPensums.setAdapter(mSectionedAdapter);

        }

        if(getArguments().getInt(ARG_NUMERO_SECCION)==1) {

            rootView = inflater.inflate(R.layout.fragment_base_01, container, false);

            viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
            tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);

            adapterViewPagerSeccionUno = new AdapterViewPagerSeccionUno(getFragmentManager());
            viewPager.setAdapter(adapterViewPagerSeccionUno);
            //link between  tabs an pager adapter
           tabLayout.setTabsFromPagerAdapter(adapterViewPagerSeccionUno);
         //link tab & viewpager object
          tabLayout.setupWithViewPager(viewPager);
          viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        }

        return  rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/



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
