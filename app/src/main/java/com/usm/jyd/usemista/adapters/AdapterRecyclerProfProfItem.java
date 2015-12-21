package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.network.VolleySingleton;
import com.usm.jyd.usemista.objects.ProfAlum;

import java.util.ArrayList;

/**
 * Created by der_w on 12/16/2015.
 */
public class AdapterRecyclerProfProfItem extends RecyclerView.Adapter<AdapterRecyclerProfProfItem.RPPIViewHolder> {

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

        ImageView imageViewAlum;
        ImageView imageViewDelete;
        TextView textAlumName;
        TextView textSubAlumCi;
        RelativeLayout relativeBody;


        public RPPIViewHolder(View itemView) {
            super(itemView);
            imageViewDelete=(ImageView)itemView.findViewById(R.id.deleteImg);
            imageViewAlum =(ImageView)itemView.findViewById(R.id.alumImg);
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
