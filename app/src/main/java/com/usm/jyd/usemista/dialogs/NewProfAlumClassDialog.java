package com.usm.jyd.usemista.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.usm.jyd.usemista.fragments.FragmentBaseProfAlum;
import com.usm.jyd.usemista.fragments.FragmentBaseProfPorf;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.network.Key;
import com.usm.jyd.usemista.network.UrlEndPoint;
import com.usm.jyd.usemista.network.VolleySingleton;
import com.usm.jyd.usemista.objects.Materia;
import com.usm.jyd.usemista.objects.UserRegistro;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by der_w on 12/12/2015.
 */
public class NewProfAlumClassDialog extends DialogFragment{

    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    LayoutInflater inflater;

    TextView textViewCodAcce;
    EditText editTextCodAcce;

    Spinner spinnerModulo, spinnerMaCod, spinnerSeccion;
    ArrayList<String> catModulo, catMaCod,catSeccion;
    ArrayAdapter<String> spinAdapModulo, spinAdapMaCod, spinAdapSeccion;
    int posSpinModulo=0,posSpinMaCod=0,posSpinSeccion=0;

    ImageView imgCurrentType;

    ArrayList<Materia> listUserMateria, listUserMaCurrent;

    UserRegistro userRegistro;
    String profCod="";
    FragmentBaseProfPorf frBsPrpr;
    FragmentBaseProfAlum frBsPrAl;
    public void setFrProfAlum(FragmentBaseProfAlum frBsPrAl){
        this.frBsPrAl=frBsPrAl;
    }
    public void setFrProfProf(FragmentBaseProfPorf frBsPrpr){
        this.frBsPrpr=frBsPrpr;
    }
    public void setProfCod(String profCod){
        this.profCod=profCod;
    }

    public void setCurrentListMateria(String modulo){
        listUserMaCurrent = new ArrayList<>();
        catMaCod = new ArrayList<>();

        for (int i = 0; i < listUserMateria.size(); i++) {
            if (listUserMateria.get(i).getModulo().equals(modulo)) {
                listUserMaCurrent.add(listUserMateria.get(i));
                catMaCod.add(listUserMateria.get(i).getTitulo());
            }
        }
        spinAdapMaCod = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, catMaCod);
        spinAdapMaCod.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerMaCod.setAdapter(spinAdapMaCod);
        spinnerMaCod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posSpinMaCod = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        userRegistro=MiAplicativo.getWritableDatabase().getUserRegistro();
        listUserMateria= MiAplicativo.getWritableDatabase().getAllUserMateria();
        listUserMaCurrent=new ArrayList<>();
        catMaCod =new ArrayList<>();
        catModulo=new ArrayList<>();
        catSeccion= new ArrayList<>();

        for(int i=0; i<listUserMateria.size();i++){
            if(listUserMateria.get(i).getModulo().equals(listUserMateria.get(0).getModulo())){
                listUserMaCurrent.add(listUserMateria.get(i));
                catMaCod.add(listUserMateria.get(i).getTitulo());
            }
        }
       // L.t(getContext(),listUserMateria.get(0).getModulo());


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        inflater  = getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_pro_alum_new_class,null);

        textViewCodAcce=(TextView)view.findViewById(R.id.textCodAcce);
        editTextCodAcce=(EditText)view.findViewById(R.id.editTextCodAcce);
        spinnerModulo=(Spinner)view.findViewById(R.id.spinnerModulo);
        spinnerMaCod=(Spinner)view.findViewById(R.id.spinnerMaCod);
        spinnerSeccion=(Spinner)view.findViewById(R.id.spinnerMaSec);

        int auxConteo=0; boolean flgSis=false, flgTelc=false, flgInd=false,flgCiv=false, flgArq=false;
        for(int i=0;i<listUserMateria.size();i++){
            if(listUserMateria.get(i).getModulo().equals("ingSis") && !flgSis){
                catModulo.add("Sistema"); flgSis=true; auxConteo++;
            }
            else if(listUserMateria.get(i).getModulo().equals("telecom") && !flgTelc){
                catModulo.add("Telecom"); flgTelc=true; auxConteo++;
            }
            else if(listUserMateria.get(i).getModulo().equals("ingInd") && !flgInd){
                catModulo.add("Industrial"); flgInd=true; auxConteo++;
            }
            else if(listUserMateria.get(i).getModulo().equals("ingCiv") && !flgCiv){
                catModulo.add("Civil"); flgCiv=true; auxConteo++;
            }
            else if(listUserMateria.get(i).getModulo().equals("arq") && !flgArq){
                catModulo.add("Arquitectura"); flgArq=true; auxConteo++;
            }
            else if(auxConteo==5 && flgSis && flgTelc && flgInd && flgCiv && flgArq){
                i=listUserMateria.size();
            }
        }


        spinAdapModulo=new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, catModulo);
        spinAdapModulo.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerModulo.setAdapter(spinAdapModulo);
        spinnerModulo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posSpinModulo = position;


                if (catModulo.get(position).equals("Sistema")) {
                    setCurrentListMateria("ingSis");
                } else if (catModulo.get(position).equals("Telecom")) {
                    setCurrentListMateria("telecom");
                }else if (catModulo.get(position).equals("Industrial")) {
                    setCurrentListMateria("ingInd");
                }else if (catModulo.get(position).equals("Civil")) {
                    setCurrentListMateria("ingCiv");
                }else if (catModulo.get(position).equals("Arquitectura")) {
                    setCurrentListMateria("arq");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(listUserMateria.get(0).getModulo().equals("ingSis")){
            for(int i = 0; i<catModulo.size();i++){
                if(catModulo.get(i).equals("Sistema")){
                    spinnerModulo.setSelection(i);posSpinModulo=i;
                }
            }
        }
        else if(listUserMateria.get(0).getModulo().equals("telecom")){
            for(int i = 0; i<catModulo.size();i++){
                if(catModulo.get(i).equals("Telecom")){
                    spinnerModulo.setSelection(i);posSpinModulo=i;
                }
            }
        }
        else if(listUserMateria.get(0).getModulo().equals("ingInd")){
            for(int i = 0; i<catModulo.size();i++){
                if(catModulo.get(i).equals("Industrial")){
                    spinnerModulo.setSelection(i);posSpinModulo=i;
                }
            }
        }
        else if(listUserMateria.get(0).getModulo().equals("ingCiv")){
            for(int i = 0; i<catModulo.size();i++){
                if(catModulo.get(i).equals("Civil")){
                    spinnerModulo.setSelection(i);posSpinModulo=i;
                }
            }
        }
        else if(listUserMateria.get(0).getModulo().equals("arq")){
            for(int i = 0; i<catModulo.size();i++){
                if(catModulo.get(i).equals("Arquitectura")){
                    spinnerModulo.setSelection(i);posSpinModulo=i;
                }
            }
        }

        spinAdapMaCod=new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, catMaCod);
        spinAdapMaCod.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerMaCod.setAdapter(spinAdapMaCod);
        spinnerMaCod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posSpinMaCod = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        catSeccion.add("A");catSeccion.add("B");catSeccion.add("C");catSeccion.add("D");
        catSeccion.add("E");catSeccion.add("F");


        spinAdapSeccion=new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, catSeccion);
        spinAdapSeccion.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSeccion.setAdapter(spinAdapSeccion);
        spinnerSeccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                posSpinSeccion = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        imgCurrentType=(ImageView)view.findViewById(R.id.imgCurrentType);
        imgCurrentType.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));


        String statusUser =userRegistro.getStatus();
        if(statusUser.equals("1")) {
            imgCurrentType.setImageResource(R.drawable.ic_estudiante_launch);

        } else if(statusUser.equals("2")) {
            imgCurrentType.setImageResource(R.drawable.ic_profesor_launch);
        }

        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (userRegistro.getStatus().equals("1")) {
                            insertAlumClassToBackend();
                        } else if (userRegistro.getStatus().equals("2")) {
                            insertProfClassToBackend();
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });



        return builder.create();
    }

    public void insertProfClassToBackend() {

        volleySingleton=VolleySingleton.getInstance();
        requestQueue=volleySingleton.getRequestQueue();

        final Context context=getContext();
        String url = "http://usmpemsun.esy.es/fr_prof_alum";

        String codAcces=editTextCodAcce.getText().toString();
        String maModulo="";
        if(catModulo.get(posSpinModulo).equals("Sistema")){ maModulo="ingSis";}
        else if(catModulo.get(posSpinModulo).equals("Telecom")){maModulo="telecom";}
        else if(catModulo.get(posSpinModulo).equals("Industrial")){maModulo="ingInd";}
        else if(catModulo.get(posSpinModulo).equals("Civil")){maModulo="ingCiv";}
        else if(catModulo.get(posSpinModulo).equals("Arquitectura")){maModulo="arq";}

        String maCod=listUserMaCurrent.get(posSpinMaCod).getCod();
        String maSec=catSeccion.get(posSpinSeccion);

        Map<String, String> map = new HashMap<>();
        map.put("inProfCls", "1");
        map.put("prc_pro_cod", profCod);
        map.put("prc_acces_cod", codAcces);
        map.put("prc_ma_modulo", maModulo);
        map.put("prc_ma_cod", maCod);
        map.put("prc_seccion", maSec);


        JSONObject obj = new JSONObject();
        try {
            obj.put("insertProfCls", "1");
            obj.put("prc_pro_cod",profCod);
            obj.put("prc_acces_cod",codAcces);
            obj.put("prc_ma_modulo",maModulo);
            obj.put("prc_ma_cod",maCod);
            obj.put("prc_seccion",maSec);
        } catch (JSONException e) {
            e.printStackTrace();
        }



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
                        L.t(context, "Inserto la Clase");
                        frBsPrpr.getProfClassInBackEnd(profCod);


                    }else if(estado.equals("2")){
                        L.t(context,"Valida en el server pero el codigo es erroneo");


                    }else if(estado.equals("3"))
                        L.t(context,"Codigo de Profesor erroneo");
                    else if(estado.equals("4"))
                        L.t(context,"Clase Duplicada");

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

    public void insertAlumClassToBackend() {

        volleySingleton=VolleySingleton.getInstance();
        requestQueue=volleySingleton.getRequestQueue();

        final Context context=getContext();
        String url = "http://usmpemsun.esy.es/fr_prof_alum";

        String codAcces=editTextCodAcce.getText().toString();
        String maModulo="";
        if(catModulo.get(posSpinModulo).equals("Sistema")){ maModulo="ingSis";}
        else if(catModulo.get(posSpinModulo).equals("Telecom")){maModulo="telecom";}
        else if(catModulo.get(posSpinModulo).equals("Industrial")){maModulo="ingInd";}
        else if(catModulo.get(posSpinModulo).equals("Civil")){maModulo="ingCiv";}
        else if(catModulo.get(posSpinModulo).equals("Arquitectura")){maModulo="arq";}
        String maCod=listUserMaCurrent.get(posSpinMaCod).getCod();
        String maSec=catSeccion.get(posSpinSeccion);
        String alc_regist="0";

        Map<String, String> map = new HashMap<>();
        map.put("inAlmCls", "1");
        map.put("alc_pro_cod", profCod);
        map.put("alc_acces_cod", codAcces);
        map.put("alc_ma_modulo", maModulo);
        map.put("alc_ma_cod", maCod);
        map.put("alc_seccion", maSec);
        map.put("alc_nombre",  userRegistro.getNomb());
        map.put("alc_cedula",  userRegistro.getCi() );
        map.put("alc_regist", alc_regist);
        map.put("alc_regiDV",  userRegistro.getNotiGCM());




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
                        L.t(context, "Inserto la Clase");
                        frBsPrAl.getAlumClassInBackEnd(userRegistro.getCi());


                    }else if(estado.equals("2")){
                        L.t(context,"Valida en el server pero el codigo es erroneo");


                    }else if(estado.equals("3"))
                        L.t(context,"No hay class de tutor o \n Peticion duplicada ");


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
