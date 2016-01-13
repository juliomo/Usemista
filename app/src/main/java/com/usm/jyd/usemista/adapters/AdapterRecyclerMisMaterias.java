package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import com.usm.jyd.usemista.objects.HVWeek;
import com.usm.jyd.usemista.objects.HorarioVirtual;
import com.usm.jyd.usemista.objects.Materia;
import com.usm.jyd.usemista.objects.UserTask;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by der_w on 11/23/2015.
 */
public class AdapterRecyclerMisMaterias extends RecyclerView.Adapter<AdapterRecyclerMisMaterias.RMMViewHolder>{

    private ArrayList<Materia> listUserMaterias= new ArrayList<>();
    private ArrayList<HorarioVirtual> listHV=new ArrayList();
    private ArrayList<HVWeek> listHVW=new ArrayList();
    private ArrayList<UserTask> listUT=new ArrayList();

    private LayoutInflater inflater;
    private Context context;
    private ClickCallBack clickCallBack;

    public AdapterRecyclerMisMaterias (Context context){
        inflater = LayoutInflater.from(context);
        this.context=context;

        listHV=MiAplicativo.getWritableDatabase().getAllHorarioVirtual();
        listHVW=MiAplicativo.getWritableDatabase().getAllHorarioVirtualWeek();
        listUT=MiAplicativo.getWritableDatabase().getAllUserTask();

    }

    public void setClickCallBack(ClickCallBack clickCallBack){
        this.clickCallBack=clickCallBack;
    }
    public void setListUserMateria(ArrayList<Materia> listUserMaterias){
        this.listUserMaterias=listUserMaterias;
         notifyDataSetChanged();
    }

    @Override
    public RMMViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_rc_fr_base_mis_materias, parent, false);
        RMMViewHolder holder = new RMMViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(RMMViewHolder holder, int position) {
        holder.textTituloMateria.setText(listUserMaterias.get(position).getTitulo());
        holder.textSubTituloMateria.setText("Semestre: " + listUserMaterias.get(position).getSemestre());

        Calendar currentDay=Calendar.getInstance();
        for (int i=0;i<listUT.size();i++){
            if(listUT.get(i).getCod().equals(listUserMaterias.get(position).getCod())){
                Calendar uTCal=Calendar.getInstance();
                uTCal.setTime(listUT.get(i).getDayTime());
                if(currentDay.get(Calendar.DAY_OF_YEAR)==uTCal.get(Calendar.DAY_OF_YEAR)){
                    holder.imgTaskCal.setVisibility(View.VISIBLE);
                    holder.imgTaskCal.setColorFilter(ContextCompat.getColor(context,R.color.ut_examen_color));
                }
                else holder.imgTaskCal.setVisibility(View.GONE);
            }
        }

        for(int i=0;i<listHV.size();i++){
            if(listHV.get(i).getCod().equals(listUserMaterias.get(position).getCod())){
                holder.imgClock.setVisibility(View.VISIBLE);
                holder.imgClock.setColorFilter(ContextCompat.getColor(context,R.color.colorTextMenuYellow));
                for (int j=0;j<listHVW.size();j++){
                    if (listHV.get(i).getCod().equals(listHVW.get(j).getCod())){

                        if(currentDay.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY
                                && listHVW.get(j).getWeekDay().equals("Lun")){
                            holder.imgClock.setColorFilter(ContextCompat.getColor(context,R.color.colorTextMenuRed));
                        }else if(currentDay.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY
                                && listHVW.get(j).getWeekDay().equals("Mar")){
                            holder.imgClock.setColorFilter(ContextCompat.getColor(context,R.color.colorTextMenuRed));
                        }else if(currentDay.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY
                                && listHVW.get(j).getWeekDay().equals("Mie")){
                            holder.imgClock.setColorFilter(ContextCompat.getColor(context,R.color.colorTextMenuRed));
                        }else if(currentDay.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY
                                && listHVW.get(j).getWeekDay().equals("Jue")){
                            holder.imgClock.setColorFilter(ContextCompat.getColor(context,R.color.colorTextMenuRed));
                        }else if(currentDay.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY
                                && listHVW.get(j).getWeekDay().equals("Vie")){
                            holder.imgClock.setColorFilter(ContextCompat.getColor(context,R.color.colorTextMenuRed));
                        }
                    }
                }
                i=listHV.size();
            }else holder.imgClock.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return listUserMaterias.size();
    }

    public class RMMViewHolder extends RecyclerView.ViewHolder{

        TextView textTituloMateria;
        TextView textSubTituloMateria;
        RelativeLayout relativeBody;
        ImageView imgClock,imgTaskCal;


        public RMMViewHolder(View itemView) {
            super(itemView);

            textTituloMateria=(TextView)itemView.findViewById(R.id.materiaTituFRMM);
            textSubTituloMateria=(TextView)itemView.findViewById(R.id.materiaSubTituloFRMM);
            relativeBody=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            imgClock=(ImageView)itemView.findViewById(R.id.imgClock);
            imgTaskCal=(ImageView)itemView.findViewById(R.id.imgEvent);

            relativeBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCallBack.onRSCMateriaSelected(getAdapterPosition(),listUserMaterias.get(getAdapterPosition()));
                }
            });

        }
    }
}
