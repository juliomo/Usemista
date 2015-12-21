package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.content.DialogInterface;
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
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.network.Key;
import com.usm.jyd.usemista.network.VolleySingleton;
import com.usm.jyd.usemista.objects.ProfAlum;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by der_w on 12/12/2015.
 */
public class AdapterRecyclerProfProf extends RecyclerView.Adapter<AdapterRecyclerProfProf.RPPViewHolder> {

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    private ArrayList<ProfAlum> listProfClass = new ArrayList<>();
    private boolean flagEdition;
    private String profCod;

    private LayoutInflater inflater;
    private Context context;
    private ClickCallBack clickCallBack;

    public AdapterRecyclerProfProf (Context context, String profCod){
        inflater = LayoutInflater.from(context);
        this.context=context;
        this.profCod=profCod;
        flagEdition=false;

    }
    public void setEdition(boolean flag){
        flagEdition=flag;
        notifyDataSetChanged();
    }

    public void setClickCallBack(ClickCallBack clickCallBack){
        this.clickCallBack=clickCallBack;
    }
    public void setlistProfClass(ArrayList<ProfAlum> listProfClass){
        this.listProfClass=listProfClass; notifyDataSetChanged();
        L.t(context,"list size adapter: \n"+listProfClass.size());
    }



    @Override
    public RPPViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_rc_fr_base_prof_prof, parent, false);
        RPPViewHolder holder = new RPPViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(RPPViewHolder holder, int position) {
        if(flagEdition)
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        else
            holder.imageViewDelete.setVisibility(View.GONE);


        holder.textTituloProfClass.setText(listProfClass.get(position).getMa_cod());
        holder.textSubTituloProfClass.setText(listProfClass.get(position).getCodAcces());

    }

    @Override
    public int getItemCount() {
        return listProfClass.size();
    }

    public class RPPViewHolder extends RecyclerView.ViewHolder{

        ImageView imageViewProfClass;
        ImageView imageViewDelete;
        TextView textTituloProfClass;
        TextView textSubTituloProfClass;
        RelativeLayout relativeBody;


        public RPPViewHolder(View itemView) {
            super(itemView);
            imageViewDelete=(ImageView)itemView.findViewById(R.id.deleteImg);
            imageViewProfClass =(ImageView)itemView.findViewById(R.id.classImagenProf);
            textTituloProfClass =(TextView)itemView.findViewById(R.id.classTituProf);
            textSubTituloProfClass =(TextView)itemView.findViewById(R.id.classSubTituloProf);
            relativeBody=(RelativeLayout)itemView.findViewById(R.id.bodyRelative);

            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteProfClassToBackend(listProfClass.get(getAdapterPosition()).getCodAcces());

                }
            });

            relativeBody.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickCallBack.onRSCItemProfClassSelected(profCod,listProfClass.get(getAdapterPosition()).getCodAcces());
                }
            });

        }

        public void deleteProfClassToBackend( String codAcces) {

            volleySingleton= VolleySingleton.getInstance();
            requestQueue=volleySingleton.getRequestQueue();


            String url = "http://usmpemsun.esy.es/fr_prof_alum";



            Map<String, String> map = new HashMap<>();
            map.put("deProfCls", "1");
            map.put("prc_pro_cod", profCod);
            map.put("prc_acces_cod", codAcces);




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
                            listProfClass.remove(getAdapterPosition());
                            notifyDataSetChanged();

                        }else if(estado.equals("2")){
                            L.t(context,"Codigo de Profesor erroneo");


                        }else if(estado.equals("3"))
                            L.t(context,"Codigo de Profesor erroneo");


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
