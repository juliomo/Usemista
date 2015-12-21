package com.usm.jyd.usemista.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.acts.ActBase;
import com.usm.jyd.usemista.events.HVTimeToSet;

/**
 * Created by der_w on 11/7/2015.
 */
public class HorarioVDialog extends DialogFragment {

    private HVTimeToSet hvTimeToSet;
    private int prevColor;
    public void setHVCallBack(HVTimeToSet hvTimeToSet, int prevColor){
        this.hvTimeToSet=hvTimeToSet; this.prevColor=prevColor;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Layout Inflater
        LayoutInflater inflater  = getActivity().getLayoutInflater();
        View view= inflater.inflate(R.layout.dialog_hv_color_picker,null);

        final ColorPicker picker = (ColorPicker) view.findViewById(R.id.picker);
        SVBar svBar = (SVBar) view.findViewById(R.id.svbar);
        OpacityBar opacityBar = (OpacityBar) view.findViewById(R.id.opacitybar);

        picker.setOldCenterColor(prevColor);
        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);

        builder.setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((ActBase)getActivity()).HVColorToSet=picker.getColor();
                        hvTimeToSet.seteoDeColor(picker.getColor());
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return  builder.create(); // super.onCreateDialog(savedInstanceState);
    }
}
