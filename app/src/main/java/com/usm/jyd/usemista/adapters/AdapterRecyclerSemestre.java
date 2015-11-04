package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.anim.AnimUtils;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.objects.Materia;

import java.util.ArrayList;

/**
 * Created by der_w on 10/31/2015.
 */
public class AdapterRecyclerSemestre extends RecyclerView.Adapter<AdapterRecyclerSemestre.RCSViewHolder> {

    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<Materia> listMateria =  new ArrayList<>();
    private ArrayList<Materia> listMateriaSemestre =  new ArrayList<>();

    private LayoutInflater inflater;

    private int previousPosition=0;

    private ClickCallBack clickCallBack;
    private Context context;

    public  AdapterRecyclerSemestre(Context context){
        inflater = LayoutInflater.from(context);
        list.add("1er Semestre");list.add("2do Semestre");
        list.add("3er Semestre");list.add("4to Semestre");
        list.add("5to Semestre");list.add("6to Semestre");
        list.add("7mo Semestre");list.add("8vo Semestre");
        list.add("9no Semestre");list.add("10mo Semestre");

    }

    public void setClickListener(Context context, ClickCallBack clickCallBack){
        this.context=context;
        this.clickCallBack=clickCallBack;
    }
    public void setListMateria(ArrayList<Materia> listMateria){
        this.listMateria=listMateria;
    }

    @Override
    public RCSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_rc_fr_base_semestre, parent, false);
        RCSViewHolder holder = new RCSViewHolder(root);

        return holder;
    }

    @Override
    public void onBindViewHolder(RCSViewHolder holder, int position) {
        holder.textViewSemestreTitulo.setText(list.get(position));


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

    public ArrayList<Materia> SelectorSemestre(int numSemestre){
        ArrayList<Materia> listDeSemestre = new ArrayList<>();
        String auxNumSemestre= Integer.toString(numSemestre);
        for(int i=0; i<listMateria.size();i++){
               Materia currentMa;
               currentMa=listMateria.get(i);
            if(currentMa.getSemestre().equals(auxNumSemestre)){
                listDeSemestre.add(currentMa);
            }
        }
        return listDeSemestre;
    }

    public class RCSViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout relativeLayout;
        TextView textViewSemestreTitulo;

        public RCSViewHolder(View itemView) {
            super(itemView);
            relativeLayout =   (RelativeLayout) itemView.findViewById(R.id.bodyRelative);
            textViewSemestreTitulo = (TextView) itemView.findViewById(R.id.semestreTitulo);

            relativeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v==v.findViewById(R.id.bodyRelative)){
                L.t(context, "Este es el semestre: " +(1+ getAdapterPosition()));
                if (clickCallBack != null ) {
                    listMateriaSemestre=SelectorSemestre(getAdapterPosition()+1);
                    if(listMateriaSemestre.isEmpty()){
                        L.t(context,"Un segundo, Cargando listas");
                    }else {
                        clickCallBack.onRSCSemestreSelected(1000 + getAdapterPosition(), listMateriaSemestre);
                    }

                   // L.t(context,"Mt1: "+listMateriaSemestre.get(0).getTitulo());
                }
            }

        }
    }
}
