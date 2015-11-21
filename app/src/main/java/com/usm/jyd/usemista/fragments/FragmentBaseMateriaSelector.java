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

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.adapters.AdapterRecyclerMateria;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.objects.Materia;

import java.util.ArrayList;


/**
 * Created by der_w on 10/31/2015.
 */
public class FragmentBaseMateriaSelector extends Fragment {

    private static final String ARG_NUMERO_SECCION = "numero_seccion";
  //  private static final String ARG_NUMERO_SECCION_ANT = "numero_seccion_ant";
    private static final String STATE_MATERIA = "state_materia";
    private String mParam1;


    private ClickCallBack clickCallBack;


    private ArrayList<Materia> listMateria = new ArrayList<>();
    private RecyclerView recyclerViewListMateria;
    private AdapterRecyclerMateria adapterRecyclerMateria;


    public static FragmentBaseMateriaSelector newInstance(int num_seccion, ArrayList<Materia> listMateria) {
        FragmentBaseMateriaSelector fragment = new FragmentBaseMateriaSelector();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMERO_SECCION, num_seccion);
        fragment.setArguments(args);
        fragment.setListMateria(listMateria);
        return fragment;
    }
    public void setListMateria(ArrayList<Materia> listMateria ){
        this.listMateria=listMateria;
    }
    public FragmentBaseMateriaSelector() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_base_00, container, false);

        rootView = inflater.inflate(R.layout.fragment_base_00, container, false);


            ImageView imageViewIcon = (ImageView) rootView.findViewById(R.id.seccionCeroImageView);
            imageViewIcon.setImageResource(R.drawable.ic_gear_white_24dp_01);
            TextView textViewTituloFragment = (TextView) rootView.findViewById(R.id.seccionCeroTitulo);
            textViewTituloFragment.setText((getArguments().getInt(ARG_NUMERO_SECCION) - 999) + " Semestre");

            recyclerViewListMateria = (RecyclerView) rootView.findViewById(R.id.recycleView);
            recyclerViewListMateria.setLayoutManager(new LinearLayoutManager(getContext()));
            adapterRecyclerMateria = new AdapterRecyclerMateria(getContext());
            adapterRecyclerMateria.setMateriaList(listMateria);
            adapterRecyclerMateria.setClickListener(getContext(), clickCallBack);

            recyclerViewListMateria.setAdapter(adapterRecyclerMateria);



        return rootView;
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
