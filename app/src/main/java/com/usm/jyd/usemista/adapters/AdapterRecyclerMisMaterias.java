package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.objects.HorarioVirtual;
import com.usm.jyd.usemista.objects.Materia;

import java.util.ArrayList;

/**
 * Created by der_w on 11/23/2015.
 */
public class AdapterRecyclerMisMaterias extends RecyclerView.Adapter<AdapterRecyclerMisMaterias.RMMViewHolder>{

    private ArrayList<Materia> listUserMaterias= new ArrayList<>();

    private LayoutInflater inflater;
    private Context context;
    private ClickCallBack clickCallBack;

    public AdapterRecyclerMisMaterias (Context context){
        inflater = LayoutInflater.from(context);
        this.context=context;

    }

    public void setClickCallBack(ClickCallBack clickCallBack){
        this.clickCallBack=clickCallBack;
    }
    public void setListUserMateria(ArrayList<Materia> listUserMaterias){this.listUserMaterias=listUserMaterias; notifyDataSetChanged();}

    @Override
    public RMMViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_rc_fr_base_mis_materias, parent, false);
        RMMViewHolder holder = new RMMViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(RMMViewHolder holder, int position) {
        holder.textTituloMateria.setText(listUserMaterias.get(position).getTitulo());
        holder.textSubTituloMateria.setText(listUserMaterias.get(position).getSemestre());

    }

    @Override
    public int getItemCount() {
        return listUserMaterias.size();
    }

    public class RMMViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewMateria;
        TextView textTituloMateria;
        TextView textSubTituloMateria;
        RelativeLayout relativeBody;


        public RMMViewHolder(View itemView) {
            super(itemView);
            imageViewMateria=(ImageView)itemView.findViewById(R.id.materiaImagenFRMM);
            textTituloMateria=(TextView)itemView.findViewById(R.id.materiaTituFRMM);
            textSubTituloMateria=(TextView)itemView.findViewById(R.id.materiaSubTituloFRMM);
            relativeBody=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);

        }
    }
}
