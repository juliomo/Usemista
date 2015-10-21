package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.content.DialogInterface;
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
 * Created by der_w on 10/17/2015.
 */
public class AdapterRecyclerMenu extends RecyclerView.Adapter<AdapterRecyclerMenu.RMViewHolder> {

    private ArrayList<String> listTitulo = new ArrayList<>();
    private int[] listImage = new int[4];
    private LayoutInflater inflater;

    private int previousPosition=0;

    public AdapterRecyclerMenu (Context context){
        inflater = LayoutInflater.from(context);
        listTitulo.add("Pensum");  listImage[0]=R.drawable.rc_menu_pensum_01;
        listTitulo.add("Mis Materias");listImage[1]=R.drawable.rc_menu_materia_01;
        listTitulo.add("Horario"); listImage[2]=R.drawable.rc_menu_horario_01;
        listTitulo.add("Noticia"); listImage[3]=R.drawable.rc_menu_news_01;

    }

    @Override
    public RMViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_rc_fr_base_menu, parent, false);
        RMViewHolder holder = new RMViewHolder(root);

        return holder;
    }

    @Override
    public void onBindViewHolder(RMViewHolder holder, int position) {

        holder.textViewItemTitulo.setText(listTitulo.get(position));
        holder.imageViewItemImagen.setImageResource(listImage[position]);

        //Sistema de animacion Gracias a la clase AnimUtilis
        if(position>previousPosition) {
            AnimUtils.animate(holder, true);
        }else{
            AnimUtils.animate(holder, false);
        }previousPosition=position;
    }

    @Override
    public int getItemCount() {
        return listTitulo.size();
    }

    public class RMViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewItemTitulo;
        ImageView imageViewItemImagen;

        public RMViewHolder(View itemView) {
            super(itemView);

            textViewItemTitulo = (TextView) itemView.findViewById(R.id.itemTitulo);
            imageViewItemImagen = (ImageView) itemView.findViewById(R.id.itemImagen);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
