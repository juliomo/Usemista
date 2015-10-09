package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.usm.jyd.usemista.R;

import java.util.ArrayList;

/**
 * Created by der_w on 10/9/2015.
 */
class AdapterRecyclerSeccionCero extends RecyclerView.Adapter<AdapterRecyclerSeccionCero.SCViewHolder> {
    private ArrayList<String> list = new ArrayList<>();
    private LayoutInflater inflater;

    public AdapterRecyclerSeccionCero(Context context) {
        inflater = LayoutInflater.from(context);
        list.add("Ingenieria Sistemas");
        list.add("Ingenieria Telecom");
        list.add("Arquitectura");


    }

    @Override
    public SCViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = inflater.inflate(R.layout.custom_row_fragment_base_recycler, viewGroup, false);
        SCViewHolder holder = new SCViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(SCViewHolder adapterRecyclerSeccionUnoViewHolder, int i) {
        adapterRecyclerSeccionUnoViewHolder.textViewPensumTitulo.setText(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class SCViewHolder extends RecyclerView.ViewHolder {

        TextView textViewPensumTitulo;
        TextView textViewPensumInfo;

        public SCViewHolder(View itemView) {
            super(itemView);
            textViewPensumTitulo = (TextView) itemView.findViewById(R.id.penusmTitulo);
            textViewPensumInfo = (TextView) itemView.findViewById(R.id.pensumInfo);
        }
    }
}
