package com.usm.jyd.usemista.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.events.CCBackUserRegi;

/**
 * Created by der_w on 12/5/2015.
 */
public class UserRegiDialog extends DialogFragment {

    CCBackUserRegi ccBackUserRegi;

    LayoutInflater inflater;
    TextView textNomb, textCIoProfCod;
    EditText editTextNomb, editTextCIoProfCod;
    ImageView imgCurrentType;

    String statusUser="0";
    public void setStatus(String statusUser){
        this.statusUser=statusUser;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Layout Inflater
        inflater  = getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_user_regi,null);

        textNomb=(TextView)view.findViewById(R.id.textNomb);
        textCIoProfCod =(TextView)view.findViewById(R.id.textCIoProfCod);
        editTextNomb=(EditText)view.findViewById(R.id.editTextNomb);
        editTextCIoProfCod =(EditText)view.findViewById(R.id.editTextCIoProfCod);
        imgCurrentType=(ImageView)view.findViewById(R.id.imgCurrentType);

        if(statusUser.equals("1")) {
            imgCurrentType.setImageResource(R.drawable.ic_estudiante_launch);
            editTextCIoProfCod.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else if(statusUser.equals("2")) {
            imgCurrentType.setImageResource(R.drawable.ic_profesor_launch);
            textCIoProfCod.setText("Codigo de Profesor");
            editTextCIoProfCod.setText("Codigo");
            editTextCIoProfCod.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        imgCurrentType.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ccBackUserRegi.onUserRegiComplete(statusUser,editTextNomb.getText().toString()
                        ,editTextCIoProfCod.getText().toString());
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });



        return builder.create(); //super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //gracias al metodo on Attach damos valor al clickCallBack evitamos Null value
            ccBackUserRegi=(CCBackUserRegi)context;
            //  mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
}
