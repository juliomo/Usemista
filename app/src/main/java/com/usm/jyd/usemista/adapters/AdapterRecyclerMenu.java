package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.anim.AnimUtils;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.logs.L;

import java.util.ArrayList;

/**
 * Created by der_w on 10/17/2015.
 */
public class AdapterRecyclerMenu extends RecyclerView.Adapter<AdapterRecyclerMenu.RMViewHolder> {

    private ArrayList<String> listTitulo = new ArrayList<>();
    private int[] listImage = new int[6];
    private LayoutInflater inflater;

    private int previousPosition=0;

    private ClickCallBack clickCallBack;
    private Context context;


    public AdapterRecyclerMenu (Context context){
        inflater = LayoutInflater.from(context);
        listTitulo.add("Pensum & Programa");  listImage[0]=R.drawable.ic_pensum_01;
        listTitulo.add("Mis Materias");listImage[1]=R.drawable.ic_materia_01;
        listTitulo.add("Horario Virtual"); listImage[2]=R.drawable.ic_horario_01;
        listTitulo.add("Notify"); listImage[3]=R.drawable.ic_notify_01;
        listTitulo.add("Tu Calendario"); listImage[4]=R.drawable.ic_calendar_01;
        listTitulo.add("Mis Tutores"); listImage[5]=R.drawable.ic_profesor_01;


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

    public void setClickListener(Context context, ClickCallBack clickCallBack){
        this.context=context;
        this.clickCallBack=clickCallBack;
    }

    public class RMViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewItemTitulo;
        ImageView imageViewItemImagen;
        Button buttonAction;

        RelativeLayout relativeLayout;

        public RMViewHolder(View itemView) {
            super(itemView);


            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            textViewItemTitulo = (TextView) itemView.findViewById(R.id.itemTitulo);
            imageViewItemImagen = (ImageView) itemView.findViewById(R.id.itemImagen);
            buttonAction = (Button)itemView.findViewById(R.id.buttonAction);

            buttonAction.setOnClickListener(this);
            relativeLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(v==v.findViewById(R.id.bodyRelative)){
                L.t(context,"Este es el body: "+getAdapterPosition());
                if (clickCallBack != null && getAdapterPosition()==0) {
                    clickCallBack.onRSCItemSelected(10+getAdapterPosition());
                }
                if(getAdapterPosition()==3){
                    YoYo.with(Techniques.Shake)
                            .duration(1000)
                            .playOn(v.findViewById(R.id.itemImagen));       //(findViewById(R.id.imageView));
                }
            }
            else if(v==v.findViewById(R.id.buttonAction)){
                L.t(context,"Boton de Accion: "+getAdapterPosition());
            }

        }
    }


}
