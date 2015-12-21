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
import com.usm.jyd.usemista.anim.AnimUtils;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.objects.MenuStatus;

import java.util.ArrayList;

/**
 * Created by der_w on 10/9/2015.
 */
public class AdapterRecyclerPensum extends RecyclerView.Adapter<AdapterRecyclerPensum.SCViewHolder> {

    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> listPInfo = new ArrayList<>();
    private ArrayList<MenuStatus> listMenuStatus =  new ArrayList<>();
    private int[] listImage = new int[5];
    private LayoutInflater inflater;

    private int previousPosition=0;

    private ClickCallBack clickCallBack;
    private Context context;

    public AdapterRecyclerPensum(Context context){
        inflater = LayoutInflater.from(context);
        list.add("Ingenieria Sistemas");listImage[0]=R.drawable.ic_gear_01;
        list.add("Ingenieria Telecom");listImage[1]=R.drawable.ic_telecom_01;
        list.add("Ingenieria Industrial");listImage[2]=R.drawable.ic_industrial_01;
        list.add("Ingenieria Civil");listImage[3]=R.drawable.ic_civil_01;
        list.add("Arquitectura");listImage[4]=R.drawable.ic_arq_01;

        listPInfo.add("Formacion profesional para prestar servicios como ingeniero de sistemas de información, sistemas de control y ciencias gerenciales, haciendo énfasis en el uso de tecnologías y técnicas de los sistemas.");
        listPInfo.add("Especialista preparado para prestar servicios con suficiencia y capacidad en electrónica analógica y digital, líneas de trasmisión y señalización, antenas, redes, trasmisión por cable e inalámbrica, tecnología celular, protocolos de comunicación, controles de transmisión, redes móviles e inalámbricas y, en general, en sistemas de trasmisión de voz, video y datos.");
        listPInfo.add("Su formacion toma el estudio de sistemas estructurales y de servicio a fin de utilizar los diversos recursos (mano de obra, capital, equipos, materia prima e información) en forma efectiva para obtener como resultado productos y servicios de óptima calidad.");
        listPInfo.add("Su formación se cumplirá principalmente en las áreas de Diseño Estructural, Planificación de Proyectos, Saneamiento Ambiental y Evaluación de Proyectos.");
        listPInfo.add("Profesional capaz de prestar sus servicios tanto en el sector institucional como en el de servicios. Poseerá una formación básica que le permita adaptarse a los cambios que ocurran en el plano profesional y en la participación en proyectos interdisciplinarios.");

        listMenuStatus= MiAplicativo.getWritableDatabase().getAllMenuStatus();
        boolean flag=false;
        if(!listMenuStatus.isEmpty()){flag=true;}
        L.t(context,"estado de lista: "+flag);

    }
    public void setClickListener(Context context, ClickCallBack clickCallBack){
        this.context=context;
        this.clickCallBack=clickCallBack;
    }

    @Override
    public AdapterRecyclerPensum.SCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View root = inflater.inflate(R.layout.row_rc_fr_base_pensum, parent, false);
        SCViewHolder holder = new SCViewHolder(root);

        return holder;
    }

    @Override
    public void onBindViewHolder(AdapterRecyclerPensum.SCViewHolder holder, int position) {
        holder.textViewPensumTitulo.setText(list.get(position));
        holder.imageViewPensumImagen.setImageResource(listImage[position]);
        holder.textViewPensumInfo.setText(listPInfo.get(position));

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

    public class SCViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        RelativeLayout relativeLayout;
        TextView textViewPensumTitulo;
        TextView textViewPensumInfo;
        ImageView imageViewPensumImagen;
        public SCViewHolder(View itemView) {
            super(itemView);

            relativeLayout =   (RelativeLayout) itemView.findViewById(R.id.bodyRelative);
            textViewPensumTitulo = (TextView) itemView.findViewById(R.id.penusmTitulo);
            textViewPensumInfo = (TextView) itemView.findViewById(R.id.pensumInfo);
            imageViewPensumImagen= (ImageView)itemView.findViewById(R.id.pensumImagen);

            relativeLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v==v.findViewById(R.id.bodyRelative)){
                L.t(context, "Este es el pensum: " + 100 * getAdapterPosition());
                if (clickCallBack != null && (getAdapterPosition()==1|| getAdapterPosition()==2)) {

                    ///SETEO DEL MENU ESTATUS ///////
                    if(!listMenuStatus.isEmpty()){


                        for(int i=0;i<listMenuStatus.size();i++){
                            if(listMenuStatus.get(i).getCod().equals("pensumSub1")) {
                                MenuStatus menuStatusUpdate = new MenuStatus();
                                menuStatusUpdate.setCod("pensumSub1");
                                menuStatusUpdate.setItem(Integer.toString(100 * getAdapterPosition()));
                                menuStatusUpdate.setActivo("1");
                                MiAplicativo.getWritableDatabase().updateMenuStatus(menuStatusUpdate);

                                if (!listMenuStatus.get(i).getItem().equals(Integer.toString(0)) &&
                                        !listMenuStatus.get(i).getItem().equals(Integer.toString(100 * getAdapterPosition()))) {

                                MenuStatus menuStatusPreview = new MenuStatus();
                                menuStatusPreview = listMenuStatus.get(i);
                                menuStatusPreview.setCod("pensumSub2");
                                menuStatusPreview.setActivo("1");
                                MiAplicativo.getWritableDatabase().updateMenuStatus(menuStatusPreview);
                                }

                            }
                        }

                    }
                    ///Fin SETEO DEL MENU STATUS/////////


                    clickCallBack.onRSCItemSelected(100 * getAdapterPosition());
                }
            }
        }
    }
}
