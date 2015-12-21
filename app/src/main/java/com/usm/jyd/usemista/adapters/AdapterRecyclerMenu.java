package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.anim.AnimUtils;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.network.VolleySingleton;
import com.usm.jyd.usemista.objects.MenuStatus;
import com.usm.jyd.usemista.objects.UserRegistro;

import java.util.ArrayList;

/**
 * Created by der_w on 10/17/2015.
 */
public class AdapterRecyclerMenu extends RecyclerView.Adapter<AdapterRecyclerMenu.RMViewHolder> {


    private ArrayList<String> listTitulo = new ArrayList<>();
    private ArrayList<MenuStatus> listMenuStatus =  new ArrayList<>();
    private int[] listImage = new int[6];
    private int[] listcolor = new int[6];
    private LayoutInflater inflater;

    private int previousPosition=0;

    private ClickCallBack clickCallBack;
    private Context context;


    public AdapterRecyclerMenu (Context context){
        inflater = LayoutInflater.from(context);
        listTitulo.add("Pensum & Programa");  listImage[0]=R.drawable.ic_pensum_01; listcolor[0]= ContextCompat.getColor(context, R.color.colorTextMenuGreen);
        listTitulo.add("Mis Materias");listImage[1]=R.drawable.ic_materia_01;       listcolor[1]=ContextCompat.getColor(context, R.color.colorTextMenuGreen);
        listTitulo.add("Horario Virtual"); listImage[2]=R.drawable.ic_horario_01;   listcolor[2]=ContextCompat.getColor(context, R.color.colorTextMenuYellow);
        listTitulo.add("Notify"); listImage[3]=R.drawable.ic_notify_01;             listcolor[3]=ContextCompat.getColor(context, R.color.colorTextMenuRed);
        listTitulo.add("Tu Calendario"); listImage[4]=R.drawable.ic_calendar_01;    listcolor[4]=ContextCompat.getColor(context, R.color.colorTextMenuYellow);
        listTitulo.add("Mis Tutores"); listImage[5]=R.drawable.ic_profesor_01;      listcolor[5]=ContextCompat.getColor(context, R.color.colorTextMenuBlue);

        listMenuStatus= MiAplicativo.getWritableDatabase().getAllMenuStatus();
    }

    public int subIcon(String  cod){
        int aux=0;
        if(cod.equals("100")){
            aux=R.drawable.ic_gear_24dp_01;
        }else if(cod.equals("200")){
            aux=R.drawable.ic_telecom_24dp_01;
        }
        return aux;
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
        holder.textViewItemTitulo.setTextColor(listcolor[position]);
        holder.textViewItemTitulo.setHighlightColor(listcolor[position]);
        holder.imageViewItemImagen.setImageResource(listImage[position]);

        if(!listMenuStatus.isEmpty()){
            for(int i=0;i<listMenuStatus.size();i++ ){
                if(listMenuStatus.get(i).getCod().equals("pensumSub1")&&
                   listMenuStatus.get(i).getActivo().equals("1") && position==0){
                    holder.imageViewSub1.setImageResource(subIcon(
                            listMenuStatus.get(i).getItem()));
                    holder.imageViewSub1.setVisibility(View.VISIBLE);
                }
                if(listMenuStatus.get(i).getCod().equals("pensumSub2")&&
                   listMenuStatus.get(i).getActivo().equals("1") && position==0){
                    holder.imageViewSub2.setImageResource(subIcon(
                            listMenuStatus.get(i).getItem()));
                    holder.imageViewSub2.setVisibility(View.VISIBLE);
                }
            }
        }

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
        ImageView imageViewSub1;
        ImageView imageViewSub2;
        Button buttonAction;

        RelativeLayout relativeLayout;

        public RMViewHolder(View itemView) {
            super(itemView);


            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            textViewItemTitulo = (TextView) itemView.findViewById(R.id.itemTitulo);
            imageViewItemImagen = (ImageView) itemView.findViewById(R.id.itemImagen);
            imageViewSub1=(ImageView)itemView.findViewById(R.id.imageView3);
            imageViewSub2=(ImageView)itemView.findViewById(R.id.imageView4);
            buttonAction = (Button)itemView.findViewById(R.id.buttonAction);

            buttonAction.setOnClickListener(this);
            relativeLayout.setOnClickListener(this);

            imageViewSub1.setOnClickListener(this);
            imageViewSub2.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(v==v.findViewById(R.id.bodyRelative)){
                L.t(context,"Este es el body: "+getAdapterPosition());
                if (clickCallBack != null && (getAdapterPosition()==0 ||
                        getAdapterPosition()==1|| getAdapterPosition()==2 ||
                        getAdapterPosition()==4|| getAdapterPosition()==5)) {
                    clickCallBack.onRSCItemSelected(10+getAdapterPosition());
                }
                if(getAdapterPosition()==3){
                    YoYo.with(Techniques.Shake)
                            .duration(1000)
                            .playOn(v.findViewById(R.id.itemImagen));       //(findViewById(R.id.imageView));
                }


            }else if(v==v.findViewById(R.id.imageView3)){
                if(getAdapterPosition()==0){
                    String selector="";
                    if(!listMenuStatus.isEmpty()) {
                        for (int i = 0; i < listMenuStatus.size(); i++) {
                            if (listMenuStatus.get(i).getCod().equals("pensumSub1")) {
                                selector=listMenuStatus.get(i).getItem();
                            }
                        }
                    }  clickCallBack.onRSCItemSelected(Integer.valueOf(selector));
                }
            }else if(v==v.findViewById(R.id.imageView4)){
                if(getAdapterPosition()==0){
                    String selector="";
                    if(!listMenuStatus.isEmpty()) {
                        for (int i = 0; i < listMenuStatus.size(); i++) {
                            if (listMenuStatus.get(i).getCod().equals("pensumSub2")) {
                                selector=listMenuStatus.get(i).getItem();
                            }
                        }
                    }  clickCallBack.onRSCItemSelected(Integer.valueOf(selector));
                }
            }
            else if(v==v.findViewById(R.id.buttonAction)){
                L.t(context,"Boton de Accion: "+getAdapterPosition());
            }

        }
    }


}
