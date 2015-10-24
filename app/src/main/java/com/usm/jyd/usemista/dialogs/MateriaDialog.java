package com.usm.jyd.usemista.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.objects.Materia;

/**
 * Created by der_w on 10/24/2015.
 */
public class MateriaDialog extends DialogFragment {

    private Materia materia;
  public void setMateriaObject(Materia materia){this.materia=materia;}


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Layout Inflater
        LayoutInflater inflater  = getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.materia_dialog,null);

        ImageView imageView =  (ImageView)view.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.ic_search_white_24dp);

        TextView textView =  (TextView) view.findViewById(R.id.textMateria);
        textView.setText(materia.getTitulo());

        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //cod
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return  builder.create(); //super.onCreateDialog(savedInstanceState);

    }
}
