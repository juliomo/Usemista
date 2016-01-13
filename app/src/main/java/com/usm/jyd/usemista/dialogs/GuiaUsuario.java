package com.usm.jyd.usemista.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.acts.ActBase;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;

/**
 * Created by der_w on 12/26/2015.
 */
public class GuiaUsuario extends DialogFragment {

    ImageView imgGuiaUsuario;
    String guiaUsuario="";

    public void setGuiaUsuario(String guiaUsuario){
        this.guiaUsuario=guiaUsuario;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Layout Inflater
        LayoutInflater inflater  = getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_guia_usuario,null);
        imgGuiaUsuario=(ImageView)view.findViewById(R.id.imgGuiaUsuario);

        if(guiaUsuario.equals("menu")){
            imgGuiaUsuario.setImageResource((R.drawable.guia01_user_menu));
        }else if(guiaUsuario.equals("materia")){
            imgGuiaUsuario.setImageResource((R.drawable.guia02_user_materia_select));
        }else if(guiaUsuario.equals("mm")){
            imgGuiaUsuario.setImageResource((R.drawable.guia03_user_mis_materias));
        }else if(guiaUsuario.equals("mmt")){
            imgGuiaUsuario.setImageResource((R.drawable.guia04_user_mm_task));
        }else if(guiaUsuario.equals("hv")){
            imgGuiaUsuario.setImageResource((R.drawable.guia05_user_hv));
        }else if(guiaUsuario.equals("hve")){
            imgGuiaUsuario.setImageResource((R.drawable.guia06_user_hv_edit));
        }else if(guiaUsuario.equals("noti")){
            imgGuiaUsuario.setImageResource((R.drawable.guia07_user_notify));
        }else if(guiaUsuario.equals("cal")){
            imgGuiaUsuario.setImageResource((R.drawable.guia08_user_calendar));
        }else if(guiaUsuario.equals("prof1")){
            imgGuiaUsuario.setImageResource((R.drawable.guia09_user_prof_01));
        }else if(guiaUsuario.equals("prof2")){
            imgGuiaUsuario.setImageResource((R.drawable.guia10_user_prof_02));
        }else if(guiaUsuario.equals("alum")){
            imgGuiaUsuario.setImageResource((R.drawable.guia11_user_alum));
        }


        builder.setView(view)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MiAplicativo.getWritableDatabase().updateUserGuia("1",guiaUsuario);
                    }
                });

        return builder.create();
    }
}
