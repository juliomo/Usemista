package com.usm.jyd.usemista.events;

import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.objects.Materia;

import java.util.ArrayList;

/**
 * Created by der_w on 10/11/2015.
 */
public interface ClickCallBack {

    void onRSCItemSelected(int position);
    void onRSCSemestreSelected(int position, ArrayList<Materia> listMateria);

}
