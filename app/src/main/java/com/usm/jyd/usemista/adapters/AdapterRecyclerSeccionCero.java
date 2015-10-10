package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.anim.AnimUtils;

import java.util.ArrayList;

/**
 * Created by der_w on 10/9/2015.
 */
public class AdapterRecyclerSeccionCero extends RecyclerView.Adapter<AdapterRecyclerSeccionCero.SCViewHolder> {

    private ArrayList<String> list = new ArrayList<>();
    private LayoutInflater inflater;

    private int previousPosition=0;

    public  AdapterRecyclerSeccionCero(Context context){
        inflater = LayoutInflater.from(context);
        list.add("Ingenieria Sistemas");
        list.add("Ingenieria Telecomunicaciones");
        list.add("Ingenieria Industrial");
        list.add("Ingenieria Civil");
        list.add("Arquitectura");

    }
    @Override
    public AdapterRecyclerSeccionCero.SCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View root = inflater.inflate(R.layout.custom_row_fragment_base_recycler, parent, false);
        SCViewHolder holder = new SCViewHolder(root);

        return holder;
    }

    @Override
    public void onBindViewHolder(AdapterRecyclerSeccionCero.SCViewHolder holder, int position) {
        holder.textViewPensumTitulo.setText(list.get(position));



        //Sistema de animacion Gracias a la clase AnimUtilis
        if(position>previousPosition) {
            AnimUtils.animate(holder, true);
        }else{
            AnimUtils.animate(holder, false);
        }previousPosition=position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SCViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPensumTitulo;
        TextView textViewPensumInfo;
        public SCViewHolder(View itemView) {
            super(itemView);

            textViewPensumTitulo = (TextView) itemView.findViewById(R.id.penusmTitulo);
            textViewPensumInfo = (TextView) itemView.findViewById(R.id.pensumInfo);
        }
    }
}
