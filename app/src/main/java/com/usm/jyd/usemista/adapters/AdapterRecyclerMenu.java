package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.usm.jyd.usemista.acts.ActBase;
import com.usm.jyd.usemista.anim.AnimUtils;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.network.VolleySingleton;
import com.usm.jyd.usemista.objects.HVWeek;
import com.usm.jyd.usemista.objects.HorarioVirtual;
import com.usm.jyd.usemista.objects.MenuStatus;
import com.usm.jyd.usemista.objects.NotifyItem;
import com.usm.jyd.usemista.objects.UserRegistro;
import com.usm.jyd.usemista.objects.UserTask;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by der_w on 10/17/2015.
 */
public class AdapterRecyclerMenu extends RecyclerView.Adapter<AdapterRecyclerMenu.RMViewHolder> {


    private ArrayList<String> listTitulo = new ArrayList<>();
    private ArrayList<MenuStatus> listMenuStatus =  new ArrayList<>();
    private UserRegistro userRegistro;
    private int[] listImage = new int[6];
    private int[] listcolor = new int[6];
    private LayoutInflater inflater;

    private int previousPosition=0;

    private ClickCallBack clickCallBack;
    private Context context;


    public AdapterRecyclerMenu (Context context){
        inflater = LayoutInflater.from(context);
        listTitulo.add(context.getResources().getString(R.string.tittle_menu_1));listImage[0]=R.drawable.ic_pensum_01; listcolor[0]= ContextCompat.getColor(context, R.color.colorTextMenuGreen);
        listTitulo.add(context.getResources().getString(R.string.tittle_menu_2));listImage[1]=R.drawable.ic_materia_01;listcolor[1]=ContextCompat.getColor(context, R.color.colorTextMenuGreen);
        listTitulo.add(context.getResources().getString(R.string.tittle_menu_3));listImage[2]=R.drawable.ic_horario_01;listcolor[2]=ContextCompat.getColor(context, R.color.colorTextMenuYellow);
        listTitulo.add(context.getResources().getString(R.string.tittle_menu_4));listImage[3]=R.drawable.ic_notify_01; listcolor[3]=ContextCompat.getColor(context, R.color.colorTextMenuRed);
        listTitulo.add(context.getResources().getString(R.string.tittle_menu_5));listImage[4]=R.drawable.ic_calendar_01;listcolor[4]=ContextCompat.getColor(context, R.color.colorTextMenuYellow);
        listTitulo.add(context.getResources().getString(R.string.tittle_menu_6));listImage[5]=R.drawable.ic_profesor_02;listcolor[5]=ContextCompat.getColor(context, R.color.colorTextMenuBlue);

        listMenuStatus= MiAplicativo.getWritableDatabase().getAllMenuStatus();userRegistro
                =MiAplicativo.getWritableDatabase().getUserRegistro();
    }

    public int subIcon(String  cod){
        int aux=0;
        if(cod.equals("100")){
            aux=R.drawable.ic_gear_24dp_01;
        }else if(cod.equals("200")){
            aux=R.drawable.ic_telecom_24dp_01;
        }else if(cod.equals("300")){
            aux=R.drawable.ic_industrial_01_24dp;
        }else if(cod.equals("400")){
            aux=R.drawable.ic_civil_01_24dp;
        }else if(cod.equals("500")){
            aux=R.drawable.ic_arq_01_24dp;
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
        if(position==5 && userRegistro.getStatus().equals("2")){
           holder.textViewItemTitulo.setText(context.getResources().getString(R.string.tittle_menu_6_2)+"\n"+
           userRegistro.getNomb());
        }
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

        if(position==1){
            ArrayList<UserTask> lisUT=MiAplicativo.getWritableDatabase().getAllUserTask();
            if(!lisUT.isEmpty()){
                Calendar current= Calendar.getInstance();

                for(int i=0;i<lisUT.size();i++){
                    Calendar dayEvent = Calendar.getInstance();
                    dayEvent.setTime(lisUT.get(i).getDayTime());
                    if(current.get(Calendar.DAY_OF_YEAR)==dayEvent.get(Calendar.DAY_OF_YEAR)){
                        holder.imageViewSub1.setVisibility(View.VISIBLE);
                        holder.imageViewSub1.setImageResource(R.drawable.ic_insert_invitation_black_36dp);
                        holder.imageViewSub1.setColorFilter(ContextCompat.getColor(context,R.color.colorTextMenuGreen));
                    }
                }
            }
        }
        if(position==2){
            ArrayList<HorarioVirtual> listHV=MiAplicativo.getWritableDatabase().getAllHorarioVirtual();
            ArrayList<HVWeek> listHVW=MiAplicativo.getWritableDatabase().getAllHorarioVirtualWeek();
            Calendar current= Calendar.getInstance();
            for(int i =0;i<listHV.size();i++){
                for(int j=0;j<listHVW.size();j++){
                    if(listHV.get(i).getCod().equals(listHVW.get(j).getCod())){
                        boolean flag=false;
                        Calendar event=Calendar.getInstance();
                        event.setTime(listHVW.get(j).getTimeIni());
                        if(listHVW.get(j).getWeekDay().equals("Lun") &&
                                current.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY){
                            if((event.get(Calendar.HOUR_OF_DAY)- current.get(Calendar.HOUR_OF_DAY))<=2 &&
                                    (event.get(Calendar.HOUR_OF_DAY)- current.get(Calendar.HOUR_OF_DAY))>=0){
                                holder.imageViewSub2.setVisibility(View.VISIBLE);
                                holder.imageViewSub2.setImageResource(R.drawable.ic_info_black_36dp);
                                holder.imageViewSub2.setColorFilter(ContextCompat.getColor(context, R.color.colorTextMenuRed));
                                holder.textViewItemTitulo.setTextColor(ContextCompat.getColor(context, R.color.colorTextMenuRed));
                                holder.imageViewItemImagen.setColorFilter(ContextCompat.getColor(context, R.color.colorTextMenuRed));
                                flag=true;
                            }else{
                                holder.imageViewSub2.setVisibility(View.VISIBLE);
                                holder.imageViewSub2.setImageResource(R.drawable.ic_info_black_36dp);
                                holder.imageViewSub2.setColorFilter(ContextCompat.getColor(context,R.color.colorTextMenuYellow));
                            }
                        }else  if(listHVW.get(j).getWeekDay().equals("Mar") &&
                                current.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY){
                            if((event.get(Calendar.HOUR_OF_DAY)- current.get(Calendar.HOUR_OF_DAY))<=2 &&
                                    (event.get(Calendar.HOUR_OF_DAY)- current.get(Calendar.HOUR_OF_DAY))>=0){
                                holder.imageViewSub2.setVisibility(View.VISIBLE);
                                holder.imageViewSub2.setImageResource(R.drawable.ic_info_black_36dp);
                                holder.imageViewSub2.setColorFilter(ContextCompat.getColor(context, R.color.colorTextMenuRed));
                                holder.textViewItemTitulo.setTextColor(ContextCompat.getColor(context, R.color.colorTextMenuRed));
                                holder.imageViewItemImagen.setColorFilter(ContextCompat.getColor(context,R.color.colorTextMenuRed));
                                flag=true;
                            }else{
                                holder.imageViewSub2.setVisibility(View.VISIBLE);
                                holder.imageViewSub2.setImageResource(R.drawable.ic_info_black_36dp);
                                holder.imageViewSub2.setColorFilter(ContextCompat.getColor(context,R.color.colorTextMenuYellow));
                            }

                        }else if(listHVW.get(j).getWeekDay().equals("Mie") &&
                                current.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY){
                            if((event.get(Calendar.HOUR_OF_DAY)- current.get(Calendar.HOUR_OF_DAY))<=2 &&
                                    (event.get(Calendar.HOUR_OF_DAY)- current.get(Calendar.HOUR_OF_DAY))>=0){
                                holder.imageViewSub2.setVisibility(View.VISIBLE);
                                holder.imageViewSub2.setImageResource(R.drawable.ic_info_black_36dp);
                                holder.imageViewSub2.setColorFilter(ContextCompat.getColor(context, R.color.colorTextMenuRed));
                                holder.textViewItemTitulo.setTextColor(ContextCompat.getColor(context, R.color.colorTextMenuRed));
                                holder.imageViewItemImagen.setColorFilter(ContextCompat.getColor(context, R.color.colorTextMenuRed));
                                flag=true;
                            }else{
                                holder.imageViewSub2.setVisibility(View.VISIBLE);
                                holder.imageViewSub2.setImageResource(R.drawable.ic_info_black_36dp);
                                holder.imageViewSub2.setColorFilter(ContextCompat.getColor(context,R.color.colorTextMenuYellow));
                            }

                        }else if(listHVW.get(j).getWeekDay().equals("Jue") &&
                                current.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY){
                            if((event.get(Calendar.HOUR_OF_DAY)- current.get(Calendar.HOUR_OF_DAY))<=2 &&
                                    (event.get(Calendar.HOUR_OF_DAY)- current.get(Calendar.HOUR_OF_DAY))>=0){
                                holder.imageViewSub2.setVisibility(View.VISIBLE);
                                holder.imageViewSub2.setImageResource(R.drawable.ic_info_black_36dp);
                                holder.imageViewSub2.setColorFilter(ContextCompat.getColor(context, R.color.colorTextMenuRed));
                                holder.textViewItemTitulo.setTextColor(ContextCompat.getColor(context, R.color.colorTextMenuRed));
                                holder.imageViewItemImagen.setColorFilter(ContextCompat.getColor(context, R.color.colorTextMenuRed));
                                flag=true;
                            }else{
                                holder.imageViewSub2.setVisibility(View.VISIBLE);
                                holder.imageViewSub2.setImageResource(R.drawable.ic_info_black_36dp);
                                holder.imageViewSub2.setColorFilter(ContextCompat.getColor(context,R.color.colorTextMenuYellow));
                            }

                        }else if(listHVW.get(j).getWeekDay().equals("Vie") &&
                                current.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY){
                            if((event.get(Calendar.HOUR_OF_DAY)- current.get(Calendar.HOUR_OF_DAY))<=2 &&
                                    (event.get(Calendar.HOUR_OF_DAY)- current.get(Calendar.HOUR_OF_DAY))>=0){
                                holder.imageViewSub2.setVisibility(View.VISIBLE);
                                holder.imageViewSub2.setImageResource(R.drawable.ic_info_black_36dp);
                                holder.imageViewSub2.setColorFilter(ContextCompat.getColor(context, R.color.colorTextMenuRed));
                                holder.textViewItemTitulo.setTextColor(ContextCompat.getColor(context, R.color.colorTextMenuRed));
                                holder.imageViewItemImagen.setColorFilter(ContextCompat.getColor(context, R.color.colorTextMenuRed));
                                flag=true;
                            }else{
                                holder.imageViewSub2.setVisibility(View.VISIBLE);
                                holder.imageViewSub2.setImageResource(R.drawable.ic_info_black_36dp);
                                holder.imageViewSub2.setColorFilter(ContextCompat.getColor(context,R.color.colorTextMenuYellow));
                            }

                            if(flag){j=listHVW.size();i=listHV.size();}

                        }
                    }
                }

            }
        }

        if(position==3){
            ArrayList<NotifyItem> listNT=MiAplicativo.getWritableDatabase().getAllNotiItem();
            if(!listNT.isEmpty()){
                holder.imageViewSub2.setVisibility(View.VISIBLE);
                holder.imageViewSub2.setImageResource(R.drawable.ic_visibility_black_36dp);
                holder.imageViewSub2.setColorFilter(ContextCompat.getColor(context,R.color.colorTextMenuRed));

            }
        }


       // holder.textViewItemTitulo.setTextColor(0xffffffff);

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


        RelativeLayout relativeLayout;

        public RMViewHolder(View itemView) {
            super(itemView);


            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            textViewItemTitulo = (TextView) itemView.findViewById(R.id.itemTitulo);
            imageViewItemImagen = (ImageView) itemView.findViewById(R.id.itemImagen);
            imageViewSub1=(ImageView)itemView.findViewById(R.id.imageView3);
            imageViewSub2=(ImageView)itemView.findViewById(R.id.imageView4);


            relativeLayout.setOnClickListener(this);

            imageViewSub1.setOnClickListener(this);
            imageViewSub2.setOnClickListener(this);

        }

        public void dialogoHV(HorarioVirtual hv, HVWeek hvw){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(hv.getTitulo());
            builder.setMessage("\nClase - Modulo: " + hvw.getModulo() +
                    "\nSalon: " + hvw.getAula() +
                    "\nHora: " + hvw.getTimeIniToText() + " - " + hvw.getTimeEndToText())
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        }

        @Override
        public void onClick(View v) {

            if(v==v.findViewById(R.id.bodyRelative)){
               // L.t(context,"Este es el body: "+getAdapterPosition());
                if (clickCallBack != null && (getAdapterPosition()==0 ||
                        getAdapterPosition()==1|| getAdapterPosition()==2 ||
                        getAdapterPosition()==4|| getAdapterPosition()==5)) {
                    clickCallBack.onRSCItemSelected(10+getAdapterPosition());
                }
                if(getAdapterPosition()==3 && clickCallBack != null ){
                    YoYo.with(Techniques.Shake)
                            .duration(1000)
                            .playOn(v.findViewById(R.id.itemImagen));

                    new CountDownTimer(1000, 1000) {

                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            clickCallBack.onRSCItemSelected(10+getAdapterPosition());
                        }

                    }.start();

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
                }else if(getAdapterPosition()==1){

                    ArrayList<UserTask> lisUT=MiAplicativo.getWritableDatabase().getAllUserTask();
                    if(!lisUT.isEmpty()){
                        Calendar current= Calendar.getInstance();
                        for(int i=0;i<lisUT.size();i++){
                            Calendar dayEvent = Calendar.getInstance();
                            dayEvent.setTime(lisUT.get(i).getDayTime());
                            if(current.get(Calendar.DAY_OF_YEAR)==dayEvent.get(Calendar.DAY_OF_YEAR)){

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle(lisUT.get(i).getMtName());
                                builder.setMessage("\n" + lisUT.get(i).getType() +
                                        "\nSalon: " + lisUT.get(i).getSalon() +
                                        "\nHora: " + lisUT.get(i).getHrIniToText());
                                builder.setPositiveButton("Sig", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.show();
                            }
                        }
                    }


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
                if(getAdapterPosition()==2){
                    ArrayList<HorarioVirtual> listHV=MiAplicativo.getWritableDatabase().getAllHorarioVirtual();
                    ArrayList<HVWeek> listHVW=MiAplicativo.getWritableDatabase().getAllHorarioVirtualWeek();
                    Calendar current= Calendar.getInstance();
                    for(int i =0;i<listHV.size();i++) {
                        for (int j = 0; j < listHVW.size(); j++) {
                            if (listHV.get(i).getCod().equals(listHVW.get(j).getCod())) {
                                Calendar event = Calendar.getInstance();
                                event.setTime(listHVW.get(j).getTimeIni());
                                if (listHVW.get(j).getWeekDay().equals("Lun") &&
                                        current.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                                            dialogoHV(listHV.get(i),listHVW.get(j));
                                }else if (listHVW.get(j).getWeekDay().equals("Mar") &&
                                        current.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
                                    dialogoHV(listHV.get(i),listHVW.get(j));
                                }else if (listHVW.get(j).getWeekDay().equals("Mie") &&
                                        current.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
                                    dialogoHV(listHV.get(i),listHVW.get(j));
                                }else if (listHVW.get(j).getWeekDay().equals("Jue") &&
                                        current.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
                                    dialogoHV(listHV.get(i),listHVW.get(j));
                                }else if (listHVW.get(j).getWeekDay().equals("Vie") &&
                                        current.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
                                    dialogoHV(listHV.get(i),listHVW.get(j));
                                }
                            }
                        }
                    }
                }
                if(getAdapterPosition()==3){

                    ArrayList<NotifyItem> listNT=MiAplicativo.getWritableDatabase().getAllNotiItem();
                    if(!listNT.isEmpty()){

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Ultima Notificacion");
                        builder.setMessage(listNT.get(0).getClase()+
                                            "\n\n"+listNT.get(0).getMsj());
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();

                    }
                }
            }


        }
    }


}
