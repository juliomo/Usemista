package com.usm.jyd.usemista.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.adapters.AdapterRecyclerMateria;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.dialogs.GuiaUsuario;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.logs.L;
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


    ImageView imageViewIcon;
    private ClickCallBack clickCallBack;


    private ArrayList<Materia> listMateria ;
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
        String auxGuiaUsuario = "";
        auxGuiaUsuario = MiAplicativo.getWritableDatabase().getUserGuia("materia");
        if (auxGuiaUsuario.equals("0")) {
            GuiaUsuario guiaUsuario = new GuiaUsuario();
            guiaUsuario.setGuiaUsuario("materia");
            guiaUsuario.show(getChildFragmentManager(),"Dialog");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView ;

        rootView = inflater.inflate(R.layout.fragment_base_00, container, false);

             imageViewIcon = (ImageView) rootView.findViewById(R.id.seccionCeroImageView);








            TextView textViewTituloFragment = (TextView) rootView.findViewById(R.id.seccionCeroTitulo);
            textViewTituloFragment.setText((getArguments().getInt(ARG_NUMERO_SECCION) - 999) + " Semestre");

            recyclerViewListMateria = (RecyclerView) rootView.findViewById(R.id.recycleView);
            recyclerViewListMateria.setLayoutManager(new LinearLayoutManager(getContext()));
            adapterRecyclerMateria = new AdapterRecyclerMateria(getContext());
            adapterRecyclerMateria.setMateriaList(listMateria);
            adapterRecyclerMateria.setClickListener(getContext(), clickCallBack);


        OffsetDecorationRC offsetDecorationRC=
                new OffsetDecorationRC(75,35,getContext().getResources().getDisplayMetrics().density);
        recyclerViewListMateria.addItemDecoration(offsetDecorationRC);
            recyclerViewListMateria.setAdapter(adapterRecyclerMateria);



        if(listMateria.get(0).getModulo().equals("ingSis")){
            imageViewIcon.setImageResource(R.drawable.ic_gear_white_24dp_01);
        }else if(listMateria.get(0).getModulo().equals("telecom")){
            imageViewIcon.setImageResource(R.drawable.ic_telecom_white_24dp_01);
        }else if(listMateria.get(0).getModulo().equals("ingInd")){
            imageViewIcon.setImageResource(R.drawable.ic_industrial_01_white_24dp);
        }else if(listMateria.get(0).getModulo().equals("ingCiv")){
            imageViewIcon.setImageResource(R.drawable.ic_civil_01_white_24dp);
        }else if(listMateria.get(0).getModulo().equals("arq")){
            imageViewIcon.setImageResource(R.drawable.ic_arq_01_white_24dp);
        }


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


}
