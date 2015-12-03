package com.usm.jyd.usemista.events;

import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.fragments.FragmentBaseMMTask;
import com.usm.jyd.usemista.objects.HVWeek;
import com.usm.jyd.usemista.objects.HorarioVirtual;
import com.usm.jyd.usemista.objects.Materia;

import java.util.ArrayList;

/**
 * Created by der_w on 10/11/2015.
 */
public interface ClickCallBack {

    void onRSCItemSelected(int position);
    void onRSCSemestreSelected(int position, ArrayList<Materia> listMateria);
    void onRSCMateriaSelected(int position, Materia materia);
    void onRSCHorarioVSelected(int position, Materia materia, HorarioVirtual horarioVirtual,ArrayList<HVWeek> listHVWeek);
    void onHVTimeSelected(int HVposition, String weekDay);
    void onHVCalendarSelected(int HVposition);
    void onHVColorSelected(int prevColor);
    void setFrMMTAux(FragmentBaseMMTask frMMT);

}
