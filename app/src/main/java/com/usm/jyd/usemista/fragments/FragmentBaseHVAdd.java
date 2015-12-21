package com.usm.jyd.usemista.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.events.HVTimeToSet;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.objects.HVWeek;
import com.usm.jyd.usemista.objects.HorarioVirtual;
import com.usm.jyd.usemista.objects.Materia;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by der_w on 11/12/2015.
 */
public class FragmentBaseHVAdd extends Fragment implements View.OnClickListener, HVTimeToSet {

    private static final String ARG_NUMERO_SECCION = "numero_seccion";

    private ClickCallBack clickCallBack;
    private ArrayList<Materia> listUserMateria = new ArrayList<>();
    private ArrayList<Materia> listUMLoad = new ArrayList<>();
    private Spinner spinnerMateria;
    private ArrayList<String> categories ;
    private ArrayAdapter<String> adapterSpinner;





    private Materia materiaInEd;
    private HorarioVirtual hvInEd;
    private ArrayList<HVWeek> ltHVWInEd;

    /**
     * Varibles de On dialog Time Seter
     */

    private int colorHV=0xff1976D2;
    private int positionListSpinnerAux=0;
    private EditText editTextTitulo;
    private Calendar calIni= Calendar.getInstance();
    private Calendar calEnd = Calendar.getInstance();

    private Calendar timeLunIni=Calendar.getInstance();private Calendar timeLunEnd=Calendar.getInstance();
    private Calendar timeMarIni=Calendar.getInstance();private Calendar timeMarEnd=Calendar.getInstance();
    private Calendar timeMieIni=Calendar.getInstance();private Calendar timeMieEnd=Calendar.getInstance();
    private Calendar timeJueIni=Calendar.getInstance();private Calendar timeJueEnd=Calendar.getInstance();
    private Calendar timeVieIni=Calendar.getInstance();private Calendar timeVieEnd=Calendar.getInstance();

    private Switch switchLun;private Switch switchMar;private Switch switchMie;
    private Switch switchJue;private Switch switchVie; private Switch switchCalendar;

    private TextView textViewlunIni; private TextView textViewlunEnd;
    private TextView textViewMarIni; private TextView textViewMarEnd;
    private TextView textViewMieIni; private TextView textViewMieEnd;
    private TextView textViewJueIni; private TextView textViewJueEnd;
    private TextView textViewVieIni; private TextView textViewVieEnd;

    private TextView textViewCalendarIni; private TextView textViewCalendarEnd;
    private ImageView imageViewIconColor;
    private ImageView imageViewIconMateria;
    private ImageView imageViewIconClock, imageViewIconCal;


    private EditText editTextLunMod, editTextMarMod,editTextMieMod,editTextJueMod,editTextVieMod ;
    private EditText editTextLunSa, editTextMarSa,editTextMieSa,editTextJueSa,editTextVieSa ;



    public static FragmentBaseHVAdd newInstance(int num_seccion, Materia materia, HorarioVirtual horarioVirtual, ArrayList<HVWeek> listHVWeek) {
        FragmentBaseHVAdd fragment = new FragmentBaseHVAdd();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMERO_SECCION, num_seccion);
        fragment.setArguments(args);
        fragment.setUserDataInEd(materia,horarioVirtual,listHVWeek);
        return fragment;
    }
    public void setUserDataInEd(Materia mt, HorarioVirtual hV, ArrayList<HVWeek> ltHVW ) {
       materiaInEd=mt;  hvInEd=hV; ltHVWInEd=ltHVW;
        calIni.setTime(hvInEd.getCalIni());calEnd.setTime(hvInEd.getCalEnd());
        for(int i=0;i<ltHVWInEd.size();i++){
            if(ltHVWInEd.get(i).getWeekDay().equals("Lun")){
                timeLunIni.setTime(ltHVWInEd.get(i).getTimeIni());timeLunEnd.setTime(ltHVWInEd.get(i).getTimeEnd());
            }else if(ltHVWInEd.get(i).getWeekDay().equals("Mar")) {
                timeMarIni.setTime(ltHVWInEd.get(i).getTimeIni());timeMarEnd.setTime(ltHVWInEd.get(i).getTimeEnd());
            }else if(ltHVWInEd.get(i).getWeekDay().equals("Mie")){
                timeMieIni.setTime(ltHVWInEd.get(i).getTimeIni());timeMieEnd.setTime(ltHVWInEd.get(i).getTimeEnd());
            }else if(ltHVWInEd.get(i).getWeekDay().equals("Jue")){
                timeJueIni.setTime(ltHVWInEd.get(i).getTimeIni());timeJueEnd.setTime(ltHVWInEd.get(i).getTimeEnd());
            }else if(ltHVWInEd.get(i).getWeekDay().equals("Vie")){
                timeVieIni.setTime(ltHVWInEd.get(i).getTimeIni());timeVieEnd.setTime(ltHVWInEd.get(i).getTimeEnd());
            }
        }colorHV=hvInEd.getColor();
    }
        public static FragmentBaseHVAdd newInstance(int num_seccion,ArrayList<Materia> listUserMateria) {
        FragmentBaseHVAdd fragment = new FragmentBaseHVAdd();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMERO_SECCION, num_seccion);
        fragment.setArguments(args);
        fragment.setListUserMateria(listUserMateria);
        return fragment;
    }
    public void setListUserMateria(ArrayList<Materia> listUserMateria ){
        ArrayList<Materia> listUAux=new ArrayList<>();
        for(int i=0;i<listUserMateria.size();i++){
            if(listUserMateria.get(i).getU_materia().equals("0")){
                listUAux.add(listUserMateria.get(i));
            }
        }
        this.listUserMateria=listUAux;
    }

    public FragmentBaseHVAdd(){}

    @Override
    public void seteoDeTiempo(int hora, int minuto, String weekDay, int itemPoss) {

        if(weekDay.equals("Lun")&& itemPoss==1){textViewlunIni.setText(hora + ":" + minuto);
        timeLunIni.set(Calendar.HOUR_OF_DAY,hora);timeLunIni.set(Calendar.MINUTE,minuto);}
        else if(weekDay.equals("Lun")&& itemPoss==2){textViewlunEnd.setText(hora+":"+minuto);
            timeLunEnd.set(Calendar.HOUR_OF_DAY,hora);timeLunEnd.set(Calendar.MINUTE, minuto);}
        else if(weekDay.equals("Mar")&& itemPoss==1){textViewMarIni.setText(hora+":"+minuto);
            timeMarIni.set(Calendar.HOUR_OF_DAY,hora);timeMarIni.set(Calendar.MINUTE, minuto);}
        else if(weekDay.equals("Mar")&& itemPoss==2){textViewMarEnd.setText(hora+":"+minuto);
            timeMarEnd.set(Calendar.HOUR_OF_DAY,hora);timeMarEnd.set(Calendar.MINUTE, minuto);}
        else if(weekDay.equals("Mie")&& itemPoss==1){textViewMieIni.setText(hora+":"+minuto);
            timeMieIni.set(Calendar.HOUR_OF_DAY,hora);timeMieIni.set(Calendar.MINUTE, minuto);}
        else if(weekDay.equals("Mie")&& itemPoss==2){textViewMieEnd.setText(hora+":"+minuto);
            timeMieEnd.set(Calendar.HOUR_OF_DAY,hora);timeMieEnd.set(Calendar.MINUTE, minuto);}
        else if(weekDay.equals("Jue")&& itemPoss==1){textViewJueIni.setText(hora+":"+minuto);
            timeJueIni.set(Calendar.HOUR_OF_DAY,hora);timeJueIni.set(Calendar.MINUTE, minuto);}
        else if(weekDay.equals("Jue")&& itemPoss==2){textViewJueEnd.setText(hora+":"+minuto);
            timeJueEnd.set(Calendar.HOUR_OF_DAY,hora);timeJueEnd.set(Calendar.MINUTE, minuto);}
        else if(weekDay.equals("Vie")&& itemPoss==1){textViewVieIni.setText(hora+":"+minuto);
            timeVieIni.set(Calendar.HOUR_OF_DAY,hora);timeVieIni.set(Calendar.MINUTE, minuto);}
        else if(weekDay.equals("Vie")&& itemPoss==2){textViewVieEnd.setText(hora+":"+minuto);
            timeVieEnd.set(Calendar.HOUR_OF_DAY,hora);timeVieEnd.set(Calendar.MINUTE, minuto);}

    }

    @Override
    public void seteoDeFecha(int year, int monthOfYear, int dayOfMonth, int itemPoss) {
        if(itemPoss==1){textViewCalendarIni.setText(dayOfMonth+" / "+monthOfYear+" / "+year+" Hasta");
        calIni.set(year,monthOfYear,dayOfMonth);}
        else if(itemPoss==2){textViewCalendarEnd.setText(dayOfMonth+" / "+monthOfYear+" / "+year);
        calEnd.set(year,monthOfYear,dayOfMonth);}
    }

    @Override
    public void seteoDeColor(int color) {
        colorHV=color;
        imageViewIconColor.setColorFilter(color);
        imageViewIconMateria.setColorFilter(color);
        imageViewIconClock.setColorFilter(color);
        imageViewIconCal.setColorFilter(color);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.act_base_fr_hvadd_menu, menu);
        if(getArguments().getInt(ARG_NUMERO_SECCION)==121){
            menu.findItem(R.id.action_delete).setVisible(false);

            if(!listUserMateria.isEmpty()) {
                boolean sis=false,telc=false;
                for(int i =0;i<listUserMateria.size();i++){
                    if(listUserMateria.get(i).getModulo().equals("ingSis")){sis=true;}
                    else if(listUserMateria.get(i).getModulo().equals("telecom")){telc=true;}
                }

                if(!sis){menu.findItem(R.id.action_sistema).setVisible(false);}
                if(!telc){menu.findItem(R.id.action_telecom).setVisible(false);}

                if (listUserMateria.get(0).getModulo().equals("ingSis")) {
                    menu.findItem(R.id.action_sistema).setChecked(true);
                } else if (listUserMateria.get(0).getModulo().equals("telecom")) {
                    menu.findItem(R.id.action_telecom).setChecked(true);
                }
            }
        }else if(getArguments().getInt(ARG_NUMERO_SECCION)==122){
            menu.findItem(R.id.action_usm).setVisible(false);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            String auxCodMateria="";
            if(getArguments().getInt(ARG_NUMERO_SECCION)==121){
                MiAplicativo.getWritableDatabase().updateUserMateriaHvPic("1",listUMLoad.get(positionListSpinnerAux).getCod());
                auxCodMateria=listUMLoad.get(positionListSpinnerAux).getCod();
            }else if(getArguments().getInt(ARG_NUMERO_SECCION)==122){
                MiAplicativo.getWritableDatabase().deleteHorarioVirtual(materiaInEd.getCod());
                MiAplicativo.getWritableDatabase().deleteHorarioVirtualWeek(materiaInEd.getCod());
                auxCodMateria=materiaInEd.getCod();
            }


            HorarioVirtual horarioVirtual =new HorarioVirtual();
            horarioVirtual.setCod(auxCodMateria);
            horarioVirtual.setTitulo(editTextTitulo.getText().toString());
            if(switchCalendar.isChecked()){
                horarioVirtual.setCalendar("1");
            }else{
                horarioVirtual.setCalendar("0");
            }
            horarioVirtual.setCalIni(calIni);
            horarioVirtual.setCalEnd(calEnd);
            horarioVirtual.setColor(colorHV);

            if(switchLun.isChecked()){
                HVWeek hvWeek = new HVWeek();
                hvWeek.setCod(auxCodMateria);
                hvWeek.setModulo(editTextLunMod.getText().toString());
                hvWeek.setAula(editTextLunSa.getText().toString());
                hvWeek.setWeekDay("Lun");
                hvWeek.setTimeIni(timeLunIni);
                hvWeek.setTimeEnd(timeLunEnd);
                MiAplicativo.getWritableDatabase().insertHorarioVirtualWeek(hvWeek);
            }if(switchMar.isChecked()){
                HVWeek hvWeek = new HVWeek();
                hvWeek.setCod(auxCodMateria);
                hvWeek.setModulo(editTextMarMod.getText().toString());
                hvWeek.setAula(editTextMarSa.getText().toString());
                hvWeek.setWeekDay("Mar");
                hvWeek.setTimeIni(timeMarIni);
                hvWeek.setTimeEnd(timeMarEnd);
                MiAplicativo.getWritableDatabase().insertHorarioVirtualWeek(hvWeek);
            }if(switchMie.isChecked()){
                HVWeek hvWeek = new HVWeek();
                hvWeek.setCod(auxCodMateria);
                hvWeek.setModulo(editTextMieMod.getText().toString());
                hvWeek.setAula(editTextMieSa.getText().toString());
                hvWeek.setWeekDay("Mie");
                hvWeek.setTimeIni(timeMieIni);
                hvWeek.setTimeEnd(timeMieEnd);
                MiAplicativo.getWritableDatabase().insertHorarioVirtualWeek(hvWeek);
            }if(switchJue.isChecked()){
                HVWeek hvWeek = new HVWeek();
                hvWeek.setCod(auxCodMateria);
                hvWeek.setModulo(editTextJueMod.getText().toString());
                hvWeek.setAula(editTextJueSa.getText().toString());
                hvWeek.setWeekDay("Jue");
                hvWeek.setTimeIni(timeJueIni);
                hvWeek.setTimeEnd(timeJueEnd);
                MiAplicativo.getWritableDatabase().insertHorarioVirtualWeek(hvWeek);
            }if(switchVie.isChecked()){
                HVWeek hvWeek = new HVWeek();
                hvWeek.setCod(auxCodMateria);
                hvWeek.setModulo(editTextVieMod.getText().toString());
                hvWeek.setAula(editTextVieSa.getText().toString());
                hvWeek.setWeekDay("Vie");
                hvWeek.setTimeIni(timeVieIni);
                hvWeek.setTimeEnd(timeVieEnd);
                MiAplicativo.getWritableDatabase().insertHorarioVirtualWeek(hvWeek);
            }

            MiAplicativo.getWritableDatabase().insertHorarioVirtual(horarioVirtual);
            L.t(getContext(),"Clase Guardada");
            clickCallBack.onRSCItemSelected(12);
            return true;
        }
        if (id == R.id.action_delete) {

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Alerta");
            alertDialog.setMessage("Desea Borrar esta Clase?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MiAplicativo.getWritableDatabase().updateUserMateriaHvPic("0", materiaInEd.getCod());
                    MiAplicativo.getWritableDatabase().deleteHorarioVirtual(materiaInEd.getCod());
                    MiAplicativo.getWritableDatabase().deleteHorarioVirtualWeek(materiaInEd.getCod());
                    L.t(getContext(), "Clase Borrada");
                    clickCallBack.onRSCItemSelected(12);
                }
            });alertDialog.show();


            return true;
        }
        if(id==R.id.action_sistema){
            item.setChecked(!item.isChecked());

            listUMLoad = new ArrayList<>();
            categories= new ArrayList<>();
            for (int i = 0; i < listUserMateria.size(); i++) {
                if(listUserMateria.get(i).getModulo().equals("ingSis")){
                    listUMLoad.add(listUserMateria.get(i));
                    categories.add(listUserMateria.get(i).getTitulo());
                }

            }
            adapterSpinner = new
                    ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, categories);

            adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinnerMateria.setAdapter(adapterSpinner);
            spinnerMateria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    positionListSpinnerAux = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            return true;
        }
        if(id==R.id.action_telecom){
            item.setChecked(!item.isChecked());

            listUMLoad = new ArrayList<>();
            categories= new ArrayList<>();
            for (int i = 0; i < listUserMateria.size(); i++) {
                if(listUserMateria.get(i).getModulo().equals("telecom")){
                    listUMLoad.add(listUserMateria.get(i));
                    categories.add(listUserMateria.get(i).getTitulo());
                }

            }
            adapterSpinner = new
                    ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, categories);

            adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinnerMateria.setAdapter(adapterSpinner);
            spinnerMateria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    positionListSpinnerAux = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        View rootView = inflater.inflate(R.layout.fragment_base_hv, container, false);

        NestedScrollView nestedScrollView=(NestedScrollView)rootView.findViewById(R.id.scrollViewHVADD);
        nestedScrollView.setNestedScrollingEnabled(false);
        imageViewIconMateria=(ImageView)rootView.findViewById(R.id.imageViewIconMateria);
        imageViewIconClock=(ImageView)rootView.findViewById(R.id.imageViewClock);
        imageViewIconCal=(ImageView)rootView.findViewById(R.id.imageViewCalendar);
        editTextTitulo=(EditText)rootView.findViewById(R.id.editTextTitulo);

        final TextView textViewNombMateria=(TextView)rootView.findViewById(R.id.textNombMateria);

         spinnerMateria=(Spinner)rootView.findViewById(R.id.spinnerMaterias);
         categories = new ArrayList<>();

        if(!listUserMateria.isEmpty()) {

            for (int i = 0; i < listUserMateria.size(); i++) {
                if(listUserMateria.get(i).getModulo().equals(listUserMateria.get(0).getModulo())) {
                    listUMLoad.add(listUserMateria.get(i));
                    categories.add(listUserMateria.get(i).getTitulo());
                }
            }
            adapterSpinner = new
                    ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, categories);

            adapterSpinner.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinnerMateria.setAdapter(adapterSpinner);
            spinnerMateria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    positionListSpinnerAux = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }else if(getArguments().getInt(ARG_NUMERO_SECCION)==121){
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Notify Info");
            alertDialog.setMessage("Actualmente no hay mas materias que registrar en Horario Virtual");
            alertDialog.setCancelable(false);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    L.t(getContext(), "De Vuela a Horario Virtual");
                    clickCallBack.onRSCItemSelected(12);
                }
            });  alertDialog.show();
        }
        switchCalendar =(Switch)rootView.findViewById(R.id.switchCalendar);
        textViewCalendarIni=(TextView)rootView.findViewById(R.id.textViewCalendarIni);
        textViewCalendarEnd=(TextView)rootView.findViewById(R.id.textViewCalendarEnd);

         switchLun = (Switch)rootView.findViewById(R.id.switchLun);
         textViewlunIni=(TextView)rootView.findViewById(R.id.textViewLunIni);
         textViewlunEnd=(TextView)rootView.findViewById(R.id.textViewLunEnd);
        final TextView textViewLunMod=(TextView)rootView.findViewById(R.id.textViewLunModulo);
        final TextView textViewLunSa=(TextView)rootView.findViewById(R.id.textViewLunSalon);
         editTextLunMod=(EditText)rootView.findViewById(R.id.editTextLunModulo);
         editTextLunSa=(EditText)rootView.findViewById(R.id.editTextLunSalon);

         switchMar = (Switch)rootView.findViewById(R.id.switchMar);
        textViewMarIni=(TextView)rootView.findViewById(R.id.textViewMarIni);
         textViewMarEnd=(TextView)rootView.findViewById(R.id.textViewMarEnd);
        final TextView textViewMarMod=(TextView)rootView.findViewById(R.id.textViewMarModulo);
        final TextView textViewMarSa=(TextView)rootView.findViewById(R.id.textViewMarSalon);
         editTextMarMod=(EditText)rootView.findViewById(R.id.editTextMarModulo);
         editTextMarSa=(EditText)rootView.findViewById(R.id.editTextMarSalon);

         switchMie = (Switch)rootView.findViewById(R.id.switchMie);
         textViewMieIni=(TextView)rootView.findViewById(R.id.textViewMieIni);
         textViewMieEnd=(TextView)rootView.findViewById(R.id.textViewMieEnd);
        final TextView textViewMieMod=(TextView)rootView.findViewById(R.id.textViewMieModulo);
        final TextView textViewMieSa=(TextView)rootView.findViewById(R.id.textViewMieSalon);
         editTextMieMod=(EditText)rootView.findViewById(R.id.editTextMieModulo);
        editTextMieSa=(EditText)rootView.findViewById(R.id.editTextMieSalon);

         switchJue = (Switch)rootView.findViewById(R.id.switchJue);
         textViewJueIni=(TextView)rootView.findViewById(R.id.textViewJueIni);
         textViewJueEnd=(TextView)rootView.findViewById(R.id.textViewJueEnd);
        final TextView textViewJueMod=(TextView)rootView.findViewById(R.id.textViewJueModulo);
        final TextView textViewJueSa=(TextView)rootView.findViewById(R.id.textViewJueSalon);
         editTextJueMod=(EditText)rootView.findViewById(R.id.editTextJueModulo);
         editTextJueSa=(EditText)rootView.findViewById(R.id.editTextJueSalon);

         switchVie = (Switch)rootView.findViewById(R.id.switchVie);
        textViewVieIni=(TextView)rootView.findViewById(R.id.textViewVieIni);
        textViewVieEnd=(TextView)rootView.findViewById(R.id.textViewVieEnd);
        final TextView textViewVieMod=(TextView)rootView.findViewById(R.id.textViewVieModulo);
        final TextView textViewVieSa=(TextView)rootView.findViewById(R.id.textViewVieSalon);
         editTextVieMod=(EditText)rootView.findViewById(R.id.editTextVieModulo);
        editTextVieSa=(EditText)rootView.findViewById(R.id.editTextVieSalon);


        switchLun.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    textViewlunIni.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
                    textViewlunEnd.setTextColor(ContextCompat.getColor(getContext(),R.color.colorAccent));

                   textViewLunMod.setVisibility(View.VISIBLE);
                    editTextLunMod.setVisibility(View.VISIBLE);
                    textViewLunSa.setVisibility(View.VISIBLE);
                    editTextLunSa.setVisibility(View.VISIBLE);

                }else {
                    textViewlunIni.setTextColor(ContextCompat.getColor(getContext(),R.color.colorTextSecondary));
                    textViewlunEnd.setTextColor(ContextCompat.getColor(getContext(),R.color.colorTextSecondary));
                    textViewLunMod.setVisibility(View.GONE);
                    editTextLunMod.setVisibility(View.GONE);
                    textViewLunSa.setVisibility(View.GONE);
                    editTextLunSa.setVisibility(View.GONE);
                }
            }
        });
        switchMar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewMarIni.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    textViewMarEnd.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    textViewMarMod.setVisibility(View.VISIBLE);
                    editTextMarMod.setVisibility(View.VISIBLE);
                    textViewMarSa.setVisibility(View.VISIBLE);
                    editTextMarSa.setVisibility(View.VISIBLE);
                } else {

                    textViewMarIni.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                    textViewMarEnd.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                    textViewMarMod.setVisibility(View.GONE);
                    editTextMarMod.setVisibility(View.GONE);
                    textViewMarSa.setVisibility(View.GONE);
                    editTextMarSa.setVisibility(View.GONE);
                }
            }
        });
        switchMie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewMieIni.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    textViewMieEnd.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    textViewMieMod.setVisibility(View.VISIBLE);
                    editTextMieMod.setVisibility(View.VISIBLE);
                    textViewMieSa.setVisibility(View.VISIBLE);
                    editTextMieSa.setVisibility(View.VISIBLE);
                } else {
                    textViewMieIni.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                    textViewMieEnd.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                    textViewMieMod.setVisibility(View.GONE);
                    editTextMieMod.setVisibility(View.GONE);
                    textViewMieSa.setVisibility(View.GONE);
                    editTextMieSa.setVisibility(View.GONE);
                }
            }
        });
        switchJue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewJueIni.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    textViewJueEnd.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    textViewJueMod.setVisibility(View.VISIBLE);
                    editTextJueMod.setVisibility(View.VISIBLE);
                    textViewJueSa.setVisibility(View.VISIBLE);
                    editTextJueSa.setVisibility(View.VISIBLE);
                } else {
                    textViewJueIni.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                    textViewJueEnd.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                    textViewJueMod.setVisibility(View.GONE);
                    editTextJueMod.setVisibility(View.GONE);
                    textViewJueSa.setVisibility(View.GONE);
                    editTextJueSa.setVisibility(View.GONE);
                }
            }
        });
        switchVie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewVieIni.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    textViewVieEnd.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    textViewVieMod.setVisibility(View.VISIBLE);
                    editTextVieMod.setVisibility(View.VISIBLE);
                    textViewVieSa.setVisibility(View.VISIBLE);
                    editTextVieSa.setVisibility(View.VISIBLE);
                } else {
                    textViewVieIni.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                    textViewVieEnd.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                    textViewVieMod.setVisibility(View.GONE);
                    editTextVieMod.setVisibility(View.GONE);
                    textViewVieSa.setVisibility(View.GONE);
                    editTextVieSa.setVisibility(View.GONE);
                }
            }
        });

        textViewlunIni.setOnClickListener(this);textViewlunEnd.setOnClickListener(this);
        textViewMarIni.setOnClickListener(this);textViewMarEnd.setOnClickListener(this);
        textViewMieIni.setOnClickListener(this);textViewMieEnd.setOnClickListener(this);
        textViewJueIni.setOnClickListener(this);textViewJueEnd.setOnClickListener(this);
        textViewVieIni.setOnClickListener(this);textViewVieEnd.setOnClickListener(this);


        switchCalendar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewCalendarIni.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    textViewCalendarEnd.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                    textViewCalendarIni.setVisibility(View.VISIBLE);
                    textViewCalendarEnd.setVisibility(View.VISIBLE);
                    imageViewIconCal.setImageResource(R.drawable.ic_event_available_black_24dp);
                } else {
                    textViewCalendarIni.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                    textViewCalendarEnd.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
                    textViewCalendarIni.setVisibility(View.GONE);
                    textViewCalendarEnd.setVisibility(View.GONE);
                    imageViewIconCal.setImageResource(R.drawable.ic_event_busy_black_24dp);
                }
            }
        });

        textViewCalendarIni.setOnClickListener(this);
        textViewCalendarEnd.setOnClickListener(this);


        Button buttonColor=(Button)rootView.findViewById(R.id.buttonColor);
        buttonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.onHVColorSelected(colorHV);
            }
        });
         imageViewIconColor=(ImageView)rootView.findViewById(R.id.imageViewColorPalette);


        if(getArguments().getInt(ARG_NUMERO_SECCION)==121){

            textViewNombMateria.setVisibility(View.GONE);
            imageViewIconColor.setColorFilter(colorHV);
            imageViewIconMateria.setColorFilter(colorHV);
            imageViewIconClock.setColorFilter(colorHV);
            imageViewIconCal.setColorFilter(colorHV);

        }else if(getArguments().getInt(ARG_NUMERO_SECCION)==122){

            spinnerMateria.setVisibility(View.GONE);
            textViewNombMateria.setText(materiaInEd.getTitulo());
            editTextTitulo.setText(hvInEd.getTitulo());

            imageViewIconColor.setColorFilter(colorHV);
            imageViewIconMateria.setColorFilter(colorHV);
            imageViewIconClock.setColorFilter(colorHV);
            imageViewIconCal.setColorFilter(colorHV);

            if(hvInEd.getCalendar().equals("1")){
                switchCalendar.setChecked(true);
                textViewCalendarIni.setText(hvInEd.getCalIniToText());
                textViewCalendarEnd.setText(hvInEd.getCalEndToText());
            }
            for(int i = 0;i<ltHVWInEd.size();i++){
                if(ltHVWInEd.get(i).getWeekDay().equals("Lun")){
                    switchLun.setChecked(true);
                    textViewlunIni.setText(ltHVWInEd.get(i).getTimeIniToText());
                    textViewlunEnd.setText(ltHVWInEd.get(i).getTimeEndToText());
                    editTextLunMod.setText(ltHVWInEd.get(i).getModulo());
                    editTextLunSa.setText(ltHVWInEd.get(i).getAula());

                }else  if(ltHVWInEd.get(i).getWeekDay().equals("Mar")){
                    switchMar.setChecked(true);
                    textViewMarIni.setText(ltHVWInEd.get(i).getTimeIniToText());
                    textViewMarEnd.setText(ltHVWInEd.get(i).getTimeEndToText());
                    editTextMarMod.setText(ltHVWInEd.get(i).getModulo());
                    editTextMarSa.setText(ltHVWInEd.get(i).getAula());

                }else  if(ltHVWInEd.get(i).getWeekDay().equals("Mie")){
                    switchMie.setChecked(true);
                    textViewMieIni.setText(ltHVWInEd.get(i).getTimeIniToText());
                    textViewMieEnd.setText(ltHVWInEd.get(i).getTimeEndToText());
                    editTextMieMod.setText(ltHVWInEd.get(i).getModulo());
                    editTextMieSa.setText(ltHVWInEd.get(i).getAula());

                }else  if(ltHVWInEd.get(i).getWeekDay().equals("Jue")){
                    switchJue.setChecked(true);
                    textViewJueIni.setText(ltHVWInEd.get(i).getTimeIniToText());
                    textViewJueEnd.setText(ltHVWInEd.get(i).getTimeEndToText());
                    editTextJueMod.setText(ltHVWInEd.get(i).getModulo());
                    editTextJueSa.setText(ltHVWInEd.get(i).getAula());

                }else  if(ltHVWInEd.get(i).getWeekDay().equals("Vie")){
                    switchVie.setChecked(true);
                    textViewVieIni.setText(ltHVWInEd.get(i).getTimeIniToText());
                    textViewVieEnd.setText(ltHVWInEd.get(i).getTimeEndToText());
                    editTextVieMod.setText(ltHVWInEd.get(i).getModulo());
                    editTextVieSa.setText(ltHVWInEd.get(i).getAula());
                }
            }
        }

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //gracias al metodo on Attach damos valor al clickCallBack evitamos Null value
            clickCallBack=(ClickCallBack)context;
            //  mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onClick(View v) {
        if (v == v.findViewById(R.id.textViewLunIni)) {
            clickCallBack.onHVTimeSelected(1, "Lun");
        } else if (v == v.findViewById(R.id.textViewLunEnd)) {
            clickCallBack.onHVTimeSelected(2, "Lun");
        } else if (v == v.findViewById(R.id.textViewMarIni)) {
            clickCallBack.onHVTimeSelected(1, "Mar");
        } else if (v == v.findViewById(R.id.textViewMarEnd)) {
            clickCallBack.onHVTimeSelected(2, "Mar");
        } else if (v == v.findViewById(R.id.textViewMieIni)) {
            clickCallBack.onHVTimeSelected(1, "Mie");
        } else if (v == v.findViewById(R.id.textViewMieEnd)) {
            clickCallBack.onHVTimeSelected(2, "Mie");
        } else if (v == v.findViewById(R.id.textViewJueIni)) {
            clickCallBack.onHVTimeSelected(1, "Jue");
        } else if (v == v.findViewById(R.id.textViewJueEnd)) {
            clickCallBack.onHVTimeSelected(2, "Jue");
        } else if (v == v.findViewById(R.id.textViewVieIni)) {
            clickCallBack.onHVTimeSelected(1, "Vie");
        } else if (v == v.findViewById(R.id.textViewVieEnd)) {
            clickCallBack.onHVTimeSelected(2, "Vie");
        }


        else if (v == v.findViewById(R.id.textViewCalendarIni)) {
            clickCallBack.onHVCalendarSelected(1);
        } else if (v == v.findViewById(R.id.textViewCalendarEnd)) {
            clickCallBack.onHVCalendarSelected(2);
        }
    }

}
