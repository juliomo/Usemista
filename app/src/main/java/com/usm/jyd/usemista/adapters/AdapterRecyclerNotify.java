package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import com.usm.jyd.usemista.objects.NotifyItem;
import com.usm.jyd.usemista.objects.ProfAlum;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

/**
 * Created by der_w on 12/25/2015.
 */
public class AdapterRecyclerNotify extends RecyclerView.Adapter<AdapterRecyclerNotify.RCSNViewHolder>{

    private ClickCallBack clickCallBack;
    private ArrayList<NotifyItem> listNotifyItem;
    private boolean flagEdition;
    private LayoutInflater inflater;
    private Context context;

    public  AdapterRecyclerNotify(Context context){
        inflater = LayoutInflater.from(context);
        this.context=context;
    }
    public void setClickCallBack(ClickCallBack clickCallBack){
        this.clickCallBack=clickCallBack;
    }
    public void setlistNotifyItem(ArrayList<NotifyItem> listNotifyItem){
        this.listNotifyItem=listNotifyItem;
        notifyDataSetChanged();

    }

    public void setEdition(boolean flag){
        flagEdition=flag;
        notifyDataSetChanged();
    }

    @Override
    public RCSNViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View root = inflater.inflate(R.layout.row_rc_fr_base_notify, parent, false);
        RCSNViewHolder holder = new RCSNViewHolder(root);

        return holder;
    }

    @Override
    public void onBindViewHolder(RCSNViewHolder holder, int position) {

        if(position==0){
            int density= (int)((context.getResources().getDisplayMetrics().density)*35);
            holder.bodyRelative.setPadding(0,density,0,0);
            //posiblemente un Background particular
        }

        if(flagEdition)
            holder.imgDel.setVisibility(View.VISIBLE);
        else
            holder.imgDel.setVisibility(View.GONE);

        if(listNotifyItem.get(position).getMod().equals("ingSis")){
            holder.imgNotify.setImageResource(R.drawable.ic_gear_01);
        }else if(listNotifyItem.get(position).getMod().equals("telecom")){
            holder.imgNotify.setImageResource(R.drawable.ic_telecom_01);
        }else if(listNotifyItem.get(position).getMod().equals("ingInd")){
            holder.imgNotify.setImageResource(R.drawable.ic_industrial_01);
        }else if(listNotifyItem.get(position).getMod().equals("ingCiv")){
            holder.imgNotify.setImageResource(R.drawable.ic_civil_01);
        }else if(listNotifyItem.get(position).getMod().equals("arq")){
            holder.imgNotify.setImageResource(R.drawable.ic_arq_01);
        }

        holder.textTitulo.setText(listNotifyItem.get(position).getClase());
        holder.textMSJ.setText(listNotifyItem.get(position).getMsj());
        holder.imgDel.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    @Override
    public int getItemCount() {
        return listNotifyItem.size();
    }

    public class RCSNViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout bodyRelative;
        TextView textTitulo, textMSJ;
        ImageView imgDel, imgNotify;

        public RCSNViewHolder(View itemView) {
            super(itemView);

            bodyRelative=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);
            textTitulo=(TextView)itemView.findViewById(R.id.textTituloMt);
            textMSJ=(TextView)itemView.findViewById(R.id.textMsjMt);
            imgDel=(ImageView)itemView.findViewById(R.id.deleteImg);
            imgNotify=(ImageView)itemView.findViewById(R.id.imgNotify);


            imgDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MiAplicativo.getWritableDatabase().deleteNotiItem(listNotifyItem.get(getAdapterPosition()).getId());
                    listNotifyItem.remove(getAdapterPosition());notifyItemRemoved(getAdapterPosition());
                }
            });
        }
    }
}
