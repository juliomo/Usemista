package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.anim.AnimUtils;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.events.ClickCallBackMateriaDialog;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.network.VolleySingleton;
import com.usm.jyd.usemista.objects.Materia;

import java.util.ArrayList;

/**
 * Created by der_w on 10/31/2015.
 */
public class AdapterRecyclerMateria extends RecyclerView.Adapter<AdapterRecyclerMateria.RCMViewHolder>{

    private LayoutInflater layoutInflater;
    private ArrayList<Materia> listMateria = new ArrayList<>();
    private ArrayList<Materia> listUserMateria=new ArrayList<>();
    private VolleySingleton volleySingleton;

    private int previousPosition=0;
    private ClickCallBackMateriaDialog clickCallBackMateriaDialog;
    private Context context;


    public AdapterRecyclerMateria(Context context){
        layoutInflater=LayoutInflater.from(context);
        volleySingleton=VolleySingleton.getInstance();
    }
    public void setClickListener(Context context, ClickCallBackMateriaDialog clickCallBackMateriaDialog){
        this.context=context;
        this.clickCallBackMateriaDialog=clickCallBackMateriaDialog;
    }
    public void setMateriaList(ArrayList<Materia> listMateria){
        this.listMateria=listMateria;
        notifyItemChanged(0, listMateria.size());
    }

    @Override
    public RCMViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_rc_fr_base_pensum_materia,parent,false);
        RCMViewHolder rcmViewHolder = new RCMViewHolder(view);
        return rcmViewHolder;
    }

    @Override
    public void onBindViewHolder(RCMViewHolder holder, int position) {
        Materia currentMateria= listMateria.get(position);
        holder.textViewTitulo.setText(currentMateria.getTitulo());
        holder.textViewSemestre.setText(currentMateria.getSemestre());

        if(currentMateria.getU_materia().equals("0")){
            holder.checkBoxMateria.setChecked(false);}
        else if(currentMateria.getU_materia().equals("1")){
            holder.checkBoxMateria.setChecked(true);
        }


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


    public class RCMViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RelativeLayout relativeLayout;
        private TextView textViewTitulo;
        private TextView textViewSemestre;
        private CheckBox checkBoxMateria;


        public RCMViewHolder(View itemView) {
            super(itemView);
            relativeLayout=(RelativeLayout) itemView.findViewById(R.id.bodyRelative);
            textViewTitulo=(TextView) itemView.findViewById(R.id.nombMateria);
            textViewSemestre=(TextView) itemView.findViewById(R.id.semestreMateria);
            checkBoxMateria=(CheckBox) itemView.findViewById(R.id.checkBoxMateria);

            relativeLayout.setOnClickListener(this);

            checkBoxMateria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        MiAplicativo.getWritableDatabase()
                                .updateMateriaUserPic("1",
                                        listMateria.get(getAdapterPosition()).getCod());
                        // The toggle is enabled
                    } else {
                        MiAplicativo.getWritableDatabase()
                                .updateMateriaUserPic("0",
                                        listMateria.get(getAdapterPosition()).getCod());
                        // The toggle is disabled
                    }
                }
            });

        }

        @Override
        public void onClick(View v) {
            L.t(context, "Materia: " + getAdapterPosition());
            if (clickCallBackMateriaDialog != null ) {
                if(getAdapterPosition()==0) {
                    clickCallBackMateriaDialog.onRSCMateriaSelected(
                            getAdapterPosition(), listMateria.get(getAdapterPosition()));
                }else{clickCallBackMateriaDialog.onRSCMateriaSelected(
                        getAdapterPosition(), listMateria.get(getAdapterPosition()));}
            }
        }
    }
}
