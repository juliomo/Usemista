package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.anim.AnimUtils;
import com.usm.jyd.usemista.network.VolleySingleton;
import com.usm.jyd.usemista.objects.Materia;

import java.util.ArrayList;

/**
 * Created by der_w on 10/12/2015.
 */
public class AdapterRecyclerSeccionCeroMateria extends
        RecyclerView.Adapter<AdapterRecyclerSeccionCeroMateria.ViewHolderMateria> {

    private LayoutInflater layoutInflater;
    private ArrayList<Materia> listMateria = new ArrayList<>();
    private VolleySingleton volleySingleton;

    private int previousPosition=0;

    public AdapterRecyclerSeccionCeroMateria(Context context){
        layoutInflater=LayoutInflater.from(context);
        volleySingleton=VolleySingleton.getInstance();
    }

    public void setMateriaList(ArrayList<Materia> listMateria){
        this.listMateria=listMateria;
        notifyItemChanged(0, listMateria.size());
    }

    @Override
    public ViewHolderMateria onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_row_materia,parent,false);
        ViewHolderMateria viewHolderMateria = new ViewHolderMateria(view);
        return viewHolderMateria;
    }

    @Override
    public void onBindViewHolder(ViewHolderMateria holder, int position) {
        Materia currentMateria= listMateria.get(position);
        holder.textViewTitulo.setText(currentMateria.getTitulo());
      //  holder.textViewSemestre.setText(currentMateria.getSemestre());

        if(position>previousPosition)
        {
            AnimUtils.animate(holder, true);
        }
        else{
            AnimUtils.animate(holder, false);
        }
        previousPosition=position;

    }

    @Override
    public int getItemCount() {
        return listMateria.size();
    }

    public class ViewHolderMateria extends RecyclerView.ViewHolder{
        private TextView textViewTitulo;
        private TextView textViewSemestre;

        public ViewHolderMateria(View itemView) {
            super(itemView);
            textViewTitulo=(TextView) itemView.findViewById(R.id.nombMateria);
            textViewSemestre=(TextView) itemView.findViewById(R.id.semestreMateria);
        }
    }


}
