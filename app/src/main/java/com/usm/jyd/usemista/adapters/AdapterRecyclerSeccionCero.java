package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.anim.AnimUtils;

import java.util.ArrayList;

/**
 * Created by der_w on 10/9/2015.
 */
public class AdapterRecyclerSeccionCero extends RecyclerView.Adapter<AdapterRecyclerSeccionCero.SCViewHolder> {

    private ArrayList<String> list = new ArrayList<>();
    private int[] listImage = new int[5];
    private LayoutInflater inflater;

    private int previousPosition=0;

    public  AdapterRecyclerSeccionCero(Context context){
        inflater = LayoutInflater.from(context);
        list.add("Ingenieria Sistemas");listImage[0]=R.drawable.ic_gear_01;
        list.add("Ingenieria Telecom");listImage[1]=R.drawable.ic_telecom_01;
        list.add("Ingenieria Industrial");listImage[2]=R.drawable.ic_industrial_01;
        list.add("Ingenieria Civil");listImage[3]=R.drawable.ic_civil_01;
        list.add("Arquitectura");listImage[4]=R.drawable.ic_arq_01;

    }
    @Override
    public AdapterRecyclerSeccionCero.SCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View root = inflater.inflate(R.layout.row_rc_fr_base_pensum, parent, false);
        SCViewHolder holder = new SCViewHolder(root);

        return holder;
    }

    @Override
    public void onBindViewHolder(AdapterRecyclerSeccionCero.SCViewHolder holder, int position) {
        holder.textViewPensumTitulo.setText(list.get(position));
        holder.imageViewPensumImagen.setImageResource(listImage[position]);

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
        ImageView imageViewPensumImagen;
        public SCViewHolder(View itemView) {
            super(itemView);

            textViewPensumTitulo = (TextView) itemView.findViewById(R.id.penusmTitulo);
            textViewPensumInfo = (TextView) itemView.findViewById(R.id.pensumInfo);
            imageViewPensumImagen= (ImageView)itemView.findViewById(R.id.pensumImagen);
        }
    }
}
