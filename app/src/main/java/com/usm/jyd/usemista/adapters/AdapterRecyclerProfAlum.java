package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.dialogs.UserRegiDialog;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.network.Key;
import com.usm.jyd.usemista.network.VolleySingleton;
import com.usm.jyd.usemista.objects.Materia;
import com.usm.jyd.usemista.objects.ProfAlum;
import com.usm.jyd.usemista.objects.UserRegistro;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by der_w on 12/14/2015.
 */
public class AdapterRecyclerProfAlum  extends RecyclerView.Adapter<AdapterRecyclerProfAlum.RPAViewHolder>  {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private ArrayList<ProfAlum> listAlumClass = new ArrayList<>();
    private boolean flagEdition;

    private LayoutInflater inflater;
    private Context context;
    private ClickCallBack clickCallBack;

    public AdapterRecyclerProfAlum (Context context){
        inflater = LayoutInflater.from(context);
        this.context=context;
        flagEdition=false;

    }

    public void setEdition(boolean flag){
        flagEdition=flag;
        notifyDataSetChanged();
    }

    public void setClickCallBack(ClickCallBack clickCallBack){
        this.clickCallBack=clickCallBack;
    }
    public void setlistAlumClass(ArrayList<ProfAlum> listAlumClass){
        this.listAlumClass=listAlumClass; notifyDataSetChanged();
    }

    @Override
    public RPAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_rc_fr_base_prof_alum, parent, false);
        RPAViewHolder holder = new RPAViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(RPAViewHolder holder, int position) {

        if(position==0){
            int density= (int)((context.getResources().getDisplayMetrics().density)*35);
            holder.relativeBody.setPadding(0,density,0,0);
            //posiblemente un Background particular
        }

        if(flagEdition)
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        else
            holder.imageViewDelete.setVisibility(View.GONE);

        if(listAlumClass.get(position).getMa_mod().equals("ingSis")){
            holder.imageViewAlumClass.setImageResource(R.drawable.ic_gear_01);
        }else if(listAlumClass.get(position).getMa_mod().equals("telecom")){
            holder.imageViewAlumClass.setImageResource(R.drawable.ic_telecom_01);
        }else if(listAlumClass.get(position).getMa_mod().equals("ingInd")){
            holder.imageViewAlumClass.setImageResource(R.drawable.ic_industrial_01);
        }else if(listAlumClass.get(position).getMa_mod().equals("ingCiv")){
            holder.imageViewAlumClass.setImageResource(R.drawable.ic_civil_01);
        }else if(listAlumClass.get(position).getMa_mod().equals("arq")){
            holder.imageViewAlumClass.setImageResource(R.drawable.ic_arq_01);
        }



        if(listAlumClass.get(position).getRegist().equals("1")){
            holder.imgServerCheck.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.imgServerCheck.setImageResource(R.drawable.ic_check_circle_black_36dp);
        }else {
            holder.imgServerCheck.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary));
            holder.imgServerCheck.setImageResource(R.drawable.ic_panorama_fish_eye_black_36dp);
        }


        Materia currentMT= MiAplicativo.getWritableDatabase().getOneUserMateria(listAlumClass.get(position).getMa_cod());
        if(currentMT.getTitulo()!=null)
            holder.textTituloAlumClass.setText(currentMT.getTitulo());
        else
            holder.textTituloAlumClass.setText(listAlumClass.get(position).getMa_cod());

        holder.textSubTituloAlumClass.setText("Cod: "+listAlumClass.get(position).getCodAcces());
        holder.textClassSeccionAlum.setText("Sec: "+listAlumClass.get(position).getMa_sec());

        holder.imageViewDelete.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    @Override
    public int getItemCount() {
        return listAlumClass.size();
    }


    public class RPAViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewAlumClass;
        ImageView imageViewDelete,imgServerCheck;
        TextView textTituloAlumClass;
        TextView textSubTituloAlumClass, textClassSeccionAlum;
        RelativeLayout relativeBody;


        public RPAViewHolder(View itemView) {
            super(itemView);
            imageViewAlumClass=(ImageView)itemView.findViewById(R.id.classImagenAlum);
            imageViewDelete=(ImageView)itemView.findViewById(R.id.deleteImg);
            imgServerCheck=(ImageView)itemView.findViewById(R.id.imgCheckServer);

            textTituloAlumClass =(TextView)itemView.findViewById(R.id.classTituAlum);
            textSubTituloAlumClass =(TextView)itemView.findViewById(R.id.classSubTituloAlum);
            textClassSeccionAlum=(TextView)itemView.findViewById(R.id.classSeccionAlum);
            relativeBody=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);

            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteAlumClassToBackend(
                            listAlumClass.get(getAdapterPosition()).getProCodHash(),
                            listAlumClass.get(getAdapterPosition()).getCodAcces());

                }
            });

        }

        public void deleteAlumClassToBackend(String profCod, String codAcces) {

            volleySingleton= VolleySingleton.getInstance();
            requestQueue=volleySingleton.getRequestQueue();


            String url = "http://usmpemsun.esy.es/fr_prof_alum";

            UserRegistro userRegistro= MiAplicativo.getWritableDatabase().getUserRegistro();

            Map<String, String> map = new HashMap<>();
            map.put("deAlm", "1");
            map.put("alc_cedula", userRegistro.getCi());
            map.put("alc_pro_cod", profCod);
            map.put("alc_acces_cod", codAcces);




            JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST,
                    url,new JSONObject(map), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        String estado="NA";
                        if(response.has(Key.EndPointMateria.KEY_ESTADO)&&
                                !response.isNull(Key.EndPointMateria.KEY_ESTADO)){
                            estado = response.getString(Key.EndPointMateria.KEY_ESTADO);
                        }

                        if(estado.equals("1")){
                            L.t(context, "Borro la Clase");
                            //notifyItemRemoved(getAdapterPosition());
                            listAlumClass.remove(getAdapterPosition());
                            notifyDataSetChanged();

                        }else if(estado.equals("2")) {
                            L.t(context, "No Encontrado");


                        }


                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    error.printStackTrace();
                    if (error instanceof TimeoutError || error instanceof NoConnectionError){
                        L.t(context, "Error: time Out or No Conect");

                    }else if(error instanceof AuthFailureError){
                        L.t(context, "Auth Fail");

                    }else if (error instanceof ServerError) {
                        L.t(context, "Server Error");

                    }else if (error instanceof NetworkError){
                        L.t(context, "Network Fail");

                    }else if (error instanceof ParseError){
                        L.t(context, "Problemas de Parseo: " + error);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Problemas de Parseo");
                        builder.setMessage(error + "");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.show();

                    }
                }
            });
            requestQueue.add(request);
        }



    }
}
