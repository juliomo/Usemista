package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        listHV = MiAplicativo.getWritableDatabase().getAllHorarioVirtual();
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
    @Override
    public RHVViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root=inflater.inflate(R.layout.row_rc_fr_base_hv, parent, false);
        RHVViewHolder holder = new RHVViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(RHVViewHolder holder, int position) {
        holder.textViewTitulo.setText(listHV.get(position).getTitulo());
        holder.textViewCalIni.setText(listHV.get(position).getCalIniToText());
        holder.textViewCalEnd.setText(listHV.get(position).getCalEndToText());

        for(int i=0;i<listHVWeek.size();i++) {
            if (listHV.get(position).getCod().equals(
                    listHVWeek.get(i).getCod()
            )) {
                if (listHVWeek.get(i).getWeekDay().equals("Lun")) {
                    holder.textViewLun.setText("Lun "+listHVWeek.get(i).getTimeIniToText()+" a "+listHVWeek.get(i).getTimeEndToText());
                } else if (listHVWeek.get(i).getWeekDay().equals("Mar")) {
                    holder.textViewMar.setText("Mar "+listHVWeek.get(i).getTimeIniToText()+" a "+listHVWeek.get(i).getTimeEndToText());
                } else if (listHVWeek.get(i).getWeekDay().equals("Mie")) {
                    holder.textViewMie.setText("Mie "+listHVWeek.get(i).getTimeIniToText()+" a "+listHVWeek.get(i).getTimeEndToText());
                } else if (listHVWeek.get(i).getWeekDay().equals("Jue")) {
                    holder.textViewJue.setText("Jue "+listHVWeek.get(i).getTimeIniToText()+" a "+listHVWeek.get(i).getTimeEndToText());
                } else if (listHVWeek.get(i).getWeekDay().equals("Vie")) {
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
        ImageView imageViewAdd;
        ImageView imageViewTest;

        TextView textViewLun;TextView textViewMar;TextView textViewMie;
        TextView textViewJue;TextView textViewVie;

        public RHVViewHolder(View itemView) {
            super(itemView);
            relativeBody=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            textViewTitulo=(TextView)itemView.findViewById(R.id.textTitulo);
            textViewCalIni=(TextView)itemView.findViewById(R.id.textCalIni);
            textViewCalEnd=(TextView)itemView.findViewById(R.id.textCalEnd);
            imageViewAdd=(ImageView)itemView.findViewById(R.id.imageviewAdd);
            imageViewTest=(ImageView)itemView.findViewById(R.id.imgTest);

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
