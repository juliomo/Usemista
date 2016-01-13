package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.objects.HVWeek;
import com.usm.jyd.usemista.objects.HorarioVirtual;
import com.usm.jyd.usemista.objects.Materia;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by der_w on 11/6/2015.
 */
public class AdapterRecyclerHorarioV extends RecyclerView.Adapter<AdapterRecyclerHorarioV.RHVViewHolder> {

    private ArrayList<String> listTitulo = new ArrayList<>();
    private ArrayList<String> listBoolean = new ArrayList<>();

    private ArrayList<HorarioVirtual> listHV=new ArrayList<>();
    private ArrayList<HVWeek> listHVWeek=new ArrayList<>();

    private LayoutInflater inflater;
    private Context context;
    private ClickCallBack clickCallBack;

    public AdapterRecyclerHorarioV(Context context){
        inflater = LayoutInflater.from(context);
        //listHV = MiAplicativo.getWritableDatabase().getAllHorarioVirtual();
        listHVWeek = MiAplicativo.getWritableDatabase().getAllHorarioVirtualWeek();
        this.context=context;
        listTitulo.add("Clase X ");
        listBoolean.add("0");

        listTitulo.add("Class X 2");
        listBoolean.add("0");

        listTitulo.add("Class N ");
        listBoolean.add("0");
    }
    public void setClickCallBack(ClickCallBack clickCallBack){
        this.clickCallBack=clickCallBack;
    }
    public void setListHorarioV(ArrayList<HorarioVirtual> listHV){this.listHV=listHV; notifyDataSetChanged();}
    @Override
    public RHVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root=inflater.inflate(R.layout.row_rc_fr_base_hv, parent, false);
        RHVViewHolder holder = new RHVViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(RHVViewHolder holder, int position) {

        if(position==0){
            int density= (int)((context.getResources().getDisplayMetrics().density)*35);
            holder.relativeBody.setPadding(0,density,0,0);
            holder.relativeBody.setBackgroundResource(R.drawable.hv_row0_bg);
        }


        holder.textViewTitulo.setText(listHV.get(position).getTitulo());

        Calendar calIni=Calendar.getInstance();
        calIni.setTime(listHV.get(position).getCalIni());
        Calendar calEnd=Calendar.getInstance();
        calEnd.setTime(listHV.get(position).getCalEnd());

        holder.imgCal.setColorFilter(listHV.get(position).getColor());
        holder.imgClock.setColorFilter(listHV.get(position).getColor());
        holder.linearLayout.setBackgroundColor(listHV.get(position).getColor());

        holder.textViewCalIni.setVisibility(View.GONE);
        holder.textViewCalEnd.setVisibility(View.GONE);

        if(calIni.get(Calendar.DAY_OF_YEAR)!=calEnd.get(Calendar.DAY_OF_YEAR)){
            holder.imgCal.setImageResource(R.drawable.ic_event_available_black_24dp);
            holder.textViewCalIni.setVisibility(View.VISIBLE);
            holder.textViewCalEnd.setVisibility(View.VISIBLE);

            holder.textViewCalIni.setText(listHV.get(position).getCalIniToText());
            holder.textViewCalEnd.setText(listHV.get(position).getCalEndToText());
        }else {
            holder.imgCal.setImageResource(R.drawable.ic_event_busy_black_24dp);
        }

        holder.textViewLun.setVisibility(View.GONE);holder.textViewMie.setVisibility(View.GONE);
        holder.textViewMar.setVisibility(View.GONE);holder.textViewJue.setVisibility(View.GONE);
        holder.textViewVie.setVisibility(View.GONE);

        for(int i=0;i<listHVWeek.size();i++) {
            if (listHV.get(position).getCod().equals(
                    listHVWeek.get(i).getCod()
            )) {
                if (listHVWeek.get(i).getWeekDay().equals("Lun")) {
                    holder.textViewLun.setVisibility(View.VISIBLE);
                    holder.textViewLun.setText("Lun "+listHVWeek.get(i).getTimeIniToText()+" a "+listHVWeek.get(i).getTimeEndToText());
                } else if (listHVWeek.get(i).getWeekDay().equals("Mar")) {
                    holder.textViewMar.setVisibility(View.VISIBLE);
                    holder.textViewMar.setText("Mar "+listHVWeek.get(i).getTimeIniToText()+" a "+listHVWeek.get(i).getTimeEndToText());
                } else if (listHVWeek.get(i).getWeekDay().equals("Mie")) {
                    holder.textViewMie.setVisibility(View.VISIBLE);
                    holder.textViewMie.setText("Mie "+listHVWeek.get(i).getTimeIniToText()+" a "+listHVWeek.get(i).getTimeEndToText());
                } else if (listHVWeek.get(i).getWeekDay().equals("Jue")) {
                    holder.textViewJue.setVisibility(View.VISIBLE);
                    holder.textViewJue.setText("Jue "+listHVWeek.get(i).getTimeIniToText()+" a "+listHVWeek.get(i).getTimeEndToText());
                } else if (listHVWeek.get(i).getWeekDay().equals("Vie")) {
                    holder.textViewVie.setVisibility(View.VISIBLE);
                    holder.textViewVie.setText("Vie "+listHVWeek.get(i).getTimeIniToText()+" a "+listHVWeek.get(i).getTimeEndToText());
                }
            }
        }


       /* if(listBoolean.get(position).equals("1")){
            holder.imageViewTest.setVisibility(View.VISIBLE);
        }else if(listBoolean.get(position).equals("0")){
            holder.imageViewTest.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return listHV.size();
    }

    public void addClass (){
        listTitulo.add("Item agregado");
        notifyItemInserted(listTitulo.size());

    }
    public void visibleGoneItem(int position){
        if(listBoolean.get(position).equals("0")){
            listBoolean.set(position,"1");
        }else if(listBoolean.get(position).equals("1")){
            listBoolean.set(position,"0");
        }
        notifyItemChanged(position);
    }


    public class RHVViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeBody;
        TextView textViewTitulo;
        TextView textViewCalIni;
        TextView textViewCalEnd;
        ImageView imgClock;
        ImageView imgCal;

        LinearLayout linearLayout;

        TextView textViewLun;TextView textViewMar;TextView textViewMie;
        TextView textViewJue;TextView textViewVie;

        public RHVViewHolder(View itemView) {
            super(itemView);
            relativeBody=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            textViewTitulo=(TextView)itemView.findViewById(R.id.textTitulo);
            textViewCalIni=(TextView)itemView.findViewById(R.id.textCalIni);
            textViewCalEnd=(TextView)itemView.findViewById(R.id.textCalEnd);
            imgClock=(ImageView)itemView.findViewById(R.id.imgClock);
            imgCal=(ImageView)itemView.findViewById(R.id.imgCal);

            linearLayout=(LinearLayout)itemView.findViewById(R.id.linearColor);

            textViewLun=(TextView)itemView.findViewById(R.id.textViewLun);
            textViewMar=(TextView)itemView.findViewById(R.id.textViewMar);
            textViewMie=(TextView)itemView.findViewById(R.id.textViewMie);
            textViewJue=(TextView)itemView.findViewById(R.id.textViewJue);
            textViewVie=(TextView)itemView.findViewById(R.id.textViewVie);

            relativeBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Materia materia=MiAplicativo
                            .getWritableDatabase()
                            .getOneUserMateria(listHV.get(getAdapterPosition()).getCod());

                    ArrayList<HVWeek> ltHVWeek=new ArrayList<>();
                    for(int i=0;i<listHVWeek.size();i++){
                        if (materia.getCod().equals(
                                listHVWeek.get(i).getCod()
                        )){
                            ltHVWeek.add(listHVWeek.get(i));
                        }
                    }
                    clickCallBack.onRSCHorarioVSelected(122,materia,listHV.get(getAdapterPosition()),ltHVWeek);
                }
            });

         /*   imageViewAdd.setOnClickListener(
                    new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            if(v==v.findViewById(R.id.imageviewAdd)){
                                //addClass();
                              visibleGoneItem(getAdapterPosition());
                            }
                        }
                    }
            );*/
        }
    }
}
