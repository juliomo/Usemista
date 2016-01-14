package com.usm.jyd.usemista.adapters;

import android.app.ProgressDialog;
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
import com.usm.jyd.usemista.acts.ActBase;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
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
 * Created by der_w on 12/16/2015.
 */
public class AdapterRecyclerProfProfItem extends RecyclerView.Adapter<AdapterRecyclerProfProfItem.RPPIViewHolder> {

    private ProgressDialog progressDialog ;

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private ArrayList<ProfAlum> listAlumInClass = new ArrayList<>();
    private boolean flagEdition;

    private LayoutInflater inflater;
    private Context context;
    private ClickCallBack clickCallBack;

    public AdapterRecyclerProfProfItem (Context context){
        inflater = LayoutInflater.from(context);
        this.context=context;
        flagEdition=false;
        progressDialog = new ProgressDialog(context);

    }

    public void setEdition(boolean flag){
        flagEdition=flag;
        notifyDataSetChanged();
    }

    public void setClickCallBack(ClickCallBack clickCallBack){
        this.clickCallBack=clickCallBack;
    }
    public void setListAlumInClass(ArrayList<ProfAlum> listAlumInClass){
        this.listAlumInClass=listAlumInClass; notifyDataSetChanged();
    }


    @Override
    public RPPIViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_rc_fr_base_prof_prof_item, parent, false);
        RPPIViewHolder holder = new RPPIViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(RPPIViewHolder holder, int position) {

        if(position==0){
            int density= (int)((context.getResources().getDisplayMetrics().density)*35);
            holder.relativeBody.setPadding(0,density,0,0);
            //posiblemente un Background particular
        }

        if(listAlumInClass.get(position).getRegist().equals("1")){
            holder.imgServerCheck.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary));
            holder.imgServerCheck.setImageResource(R.drawable.ic_check_circle_black_36dp);
        }else {
            holder.imgServerCheck.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary));
            holder.imgServerCheck.setImageResource(R.drawable.ic_panorama_fish_eye_black_36dp);
        }

        if(flagEdition)
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        else
            holder.imageViewDelete.setVisibility(View.GONE);

        holder.textAlumName.setText(listAlumInClass.get(position).getNomb());
        holder.textSubAlumCi.setText(listAlumInClass.get(position).getCed());
    }

    @Override
    public int getItemCount() {
        return listAlumInClass.size();
    }


    public class RPPIViewHolder extends RecyclerView.ViewHolder{


        ImageView imageViewDelete , imgServerCheck;
        TextView textAlumName;
        TextView textSubAlumCi;
        RelativeLayout relativeBody;


        public RPPIViewHolder(View itemView) {
            super(itemView);
            imageViewDelete=(ImageView)itemView.findViewById(R.id.deleteImg);

            imgServerCheck=(ImageView)itemView.findViewById(R.id.imgCheckServer);
            textAlumName =(TextView)itemView.findViewById(R.id.alumName);
            textSubAlumCi =(TextView)itemView.findViewById(R.id.alumCi);
            relativeBody=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);

            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  deleteAlumClassToBackend(
                            listAlumClass.get(getAdapterPosition()).getProCodHash(),
                            listAlumClass.get(getAdapterPosition()).getCodAcces());*/

                }
            });

            imgServerCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listAlumInClass.get(getAdapterPosition()).getRegist().equals("1")){
                        updateAlumInClassStatusToBackend("0",
                                listAlumInClass.get(getAdapterPosition()).getId());
                    }else {
                        updateAlumInClassStatusToBackend("1",
                                listAlumInClass.get(getAdapterPosition()).getId());
                    }
                }
            });

        }
        public void updateAlumInClassStatusToBackend( final String regi, String id) {

            progressDialog.setMessage("Cargando ...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            volleySingleton= VolleySingleton.getInstance();
            requestQueue=volleySingleton.getRequestQueue();


            String url = "http://usmpemsun.esy.es/fr_prof_alum";



            Map<String, String> map = new HashMap<>();
            map.put("upProfAlmCls", "1");
            map.put("prc_pro_cod", ((ActBase)context).codProfesor);
            map.put("alc_regist", regi);
            map.put("alc_id", id);





            JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST,
                    url,new JSONObject(map), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        progressDialog.dismiss();
                        String estado="NA";
                        if(response.has(Key.EndPointMateria.KEY_ESTADO)&&
                                !response.isNull(Key.EndPointMateria.KEY_ESTADO)){
                            estado = response.getString(Key.EndPointMateria.KEY_ESTADO);
                        }

                        if(estado.equals("1")){
                            L.t(context, "Registro Atualizado");
                            listAlumInClass.get(getAdapterPosition()).setRegist(regi);
                            notifyItemChanged(getAdapterPosition());

                            notifyDataSetChanged();

                        }else if(estado.equals("2")){
                            L.t(context,"No Hay tal registro");


                        }else if(estado.equals("3"))
                            L.t(context,"Codigo de Profesor erroneo");


                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    progressDialog.dismiss();

                    error.printStackTrace();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Problemas ");
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


                    }
                }
            });
            requestQueue.add(request);
        }



       /* public void deleteAlumClassToBackend(String profCod, String codAcces) {

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
        }*/



    }

}
