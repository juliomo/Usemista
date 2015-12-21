package com.usm.jyd.usemista.fragments;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.adapters.AdapterRecyclerMMTask;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.events.HVTimeToSet;
import com.usm.jyd.usemista.objects.Materia;
import com.usm.jyd.usemista.objects.UserTask;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by der_w on 11/25/2015.
 */
public class FragmentBaseMMTask extends Fragment implements HVTimeToSet {

    private ClickCallBack clickCallBack;
    public FragmentBaseMMTask fragmentBaseMMTask;


    boolean sis=false,telc=false;
    private int pensumValidator=0;
    private ArrayList<Materia> listUserMateria;
    private ArrayList<Materia> ListUserMateriaByPensum=new ArrayList<>();

    private ArrayList<UserTask> listUserTask;
    private ArrayList<UserTask> listUserTaskCarrer =new ArrayList<>();
    private ArrayList<UserTask> listUserTaskByFilter=new ArrayList<>();

    private RecyclerView rcListTask;
    private AdapterRecyclerMMTask adapterRecyclerMMTask;
    private LinearLayoutManager manager;

    private FloatingActionButton FabMMT;

   public FragmentBaseMMTask (){}

    public static FragmentBaseMMTask newInstance() {
        FragmentBaseMMTask fragment = new FragmentBaseMMTask();
        fragment.knowFrMMTInstance(fragment);
        return fragment;
    }
    public void knowFrMMTInstance(FragmentBaseMMTask fragmentBaseMMTask){
        this.fragmentBaseMMTask=fragmentBaseMMTask;
    }
    public void updateMainListOnLoad(){
            listUserTask=MiAplicativo.getWritableDatabase().getAllUserTask();
            getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listUserMateria= MiAplicativo.getWritableDatabase().getAllUserMateria();
        listUserTask=MiAplicativo.getWritableDatabase().getAllUserTask();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.act_base_fr_mm_task_menu, menu);
        if(!listUserMateria.isEmpty() ) {
            listUserTaskCarrer=new ArrayList<>();
            ListUserMateriaByPensum=new ArrayList<>();

            for(int i =0;i<listUserMateria.size();i++){
                if(listUserMateria.get(i).getModulo().equals("ingSis")){sis=true;}
                else if(listUserMateria.get(i).getModulo().equals("telecom")){telc=true;}
            }

            if(!sis){menu.findItem(R.id.action_sistema_fr_mmt).setVisible(false);}
            if(!telc){menu.findItem(R.id.action_telecom_fr_mmt).setVisible(false);}


            int seValidator=0;
            if (listUserMateria.get(0).getModulo().equals("ingSis")) {
                menu.findItem(R.id.action_sistema_fr_mmt).setChecked(true); seValidator=1;
            } else if (listUserMateria.get(0).getModulo().equals("telecom")) {
                menu.findItem(R.id.action_telecom_fr_mmt).setChecked(true); seValidator=2;
            }

            menu.findItem(R.id.action_seAll).setChecked(true);



            if(seValidator==1){
                for(int i=0;i<listUserMateria.size();i++) {
                    for(int j=0;j<listUserTask.size();j++) {

                        if (listUserMateria.get(i).getModulo().equals("ingSis") &&
                                listUserMateria.get(i).getCod().equals(listUserTask.get(j).getCod())) {
                            listUserTaskCarrer.add(listUserTask.get(j));
                        }
                    }
                    if (listUserMateria.get(i).getModulo().equals("ingSis")){
                        ListUserMateriaByPensum.add(listUserMateria.get(i));
                    }

                }
            }else if(seValidator==2){
                for(int i=0;i<listUserMateria.size();i++) {
                    for(int j=0;j<listUserTask.size();j++) {

                        if (listUserMateria.get(i).getModulo().equals("telecom") &&
                                listUserMateria.get(i).getCod().equals(listUserTask.get(j).getCod())) {
                            listUserTaskCarrer.add(listUserTask.get(j));
                        }
                    }
                    if (listUserMateria.get(i).getModulo().equals("telecom")){
                        ListUserMateriaByPensum.add(listUserMateria.get(i));
                    }
                }
            }


        }  if(listUserMateria.isEmpty()){
            menu.findItem(R.id.action_usm_fr_mmt).setVisible(false);
            menu.findItem(R.id.action_fill_fr_mmt).setVisible(false);
        }
        adapterRecyclerMMTask.setListUser(listUserTaskCarrer, ListUserMateriaByPensum);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(pensumValidator!=0) {
            listUserTaskCarrer = new ArrayList<>();
            ListUserMateriaByPensum= new ArrayList<>();
        }


        if(pensumValidator==1){
            for(int i=0;i<listUserMateria.size();i++){
                for(int j=0;j<listUserTask.size();j++) {

                    if (listUserMateria.get(i).getModulo().equals("ingSis") &&
                            listUserMateria.get(i).getCod().equals(listUserTask.get(j).getCod())) {
                        listUserTaskCarrer.add(listUserTask.get(j));
                    }
                }
                if (listUserMateria.get(i).getModulo().equals("ingSis")){
                    ListUserMateriaByPensum.add(listUserMateria.get(i));
                }
            }

            adapterRecyclerMMTask.setListUser(listUserTaskCarrer, ListUserMateriaByPensum);
            menu.findItem(R.id.action_sistema_fr_mmt).setChecked(true);

        }
        else if(pensumValidator==2){
            for(int i=0;i<listUserMateria.size();i++){
                for(int j=0;j<listUserTask.size();j++) {

                    if (listUserMateria.get(i).getModulo().equals("telecom") &&
                            listUserMateria.get(i).getCod().equals(listUserTask.get(j).getCod())) {
                        listUserTaskCarrer.add(listUserTask.get(j));
                    }
                }
                if (listUserMateria.get(i).getModulo().equals("telecom")){
                    ListUserMateriaByPensum.add(listUserMateria.get(i));
                }
            }


            adapterRecyclerMMTask.setListUser(listUserTaskCarrer,ListUserMateriaByPensum);
            menu.findItem(R.id.action_telecom_fr_mmt).setChecked(true);

        } menu.findItem(R.id.action_seAll).setChecked(true);


        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();


            boolean flag = true;
            for (int i = 0; i < adapterRecyclerMMTask.listState.size(); i++) {
                if ((adapterRecyclerMMTask.listState.get(i) == 0 ||
                        adapterRecyclerMMTask.listState.get(i) == 1) && flag) {
                    flag = false;
                }
            }
            if (!flag) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Notify Info");
                alertDialog.setMessage("Actualmente estas editando un Task.\n" +
                        "Culminar antes de cambiar filtros");
                alertDialog.setCancelable(false);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // L.t(getContext(), "Guarde o borre ");

                    }
                });
                alertDialog.show();

                if (id == R.id.action_usm_fr_mmt) {
                    item.getSubMenu().setGroupVisible(R.id.groupByPensum, false);
                } else if (id == R.id.action_fill_fr_mmt) {
                    item.getSubMenu().setGroupVisible(R.id.groupByFill, false);
                }

            }

            if (flag) {
                if (id == R.id.action_usm_fr_mmt) {
                    if(sis){item.getSubMenu().findItem(R.id.action_sistema_fr_mmt).setVisible(true);}
                    if(telc){item.getSubMenu().findItem(R.id.action_telecom_fr_mmt).setVisible(true);}
                  //  item.getSubMenu().setGroupVisible(R.id.groupByPensum, true);
                } else if (id == R.id.action_fill_fr_mmt) {
                    item.getSubMenu().setGroupVisible(R.id.groupByPensum, true);
                }

                //En cada Boton Seleccionar
                switch (id) {
                    case R.id.action_sistema_fr_mmt: {
                        item.setChecked(!item.isChecked());
                        pensumValidator = 1;
                        getActivity().invalidateOptionsMenu();
                    }
                    break;
                    case R.id.action_telecom_fr_mmt: {
                        item.setChecked(!item.isChecked());
                        pensumValidator = 2;
                        getActivity().invalidateOptionsMenu();
                    }
                    break;
                    case R.id.action_seAll: {
                        item.setChecked(!item.isChecked());
                        adapterRecyclerMMTask.setListUser(listUserTaskCarrer);
                    }
                    break;
                    case R.id.action_seNoCmplt: {
                        item.setChecked(!item.isChecked());
                        listUserTaskByFilter = new ArrayList<>();
                        for (int i = 0; i < listUserTaskCarrer.size(); i++) {
                            if (listUserTaskCarrer.get(i).getCmplt().equals("0")) {
                                listUserTaskByFilter.add(listUserTaskCarrer.get(i));
                            }
                        }
                        adapterRecyclerMMTask.setListUser(listUserTaskByFilter);
                    }
                    break;
                    case R.id.action_seCmplt: {
                        item.setChecked(!item.isChecked());
                        listUserTaskByFilter = new ArrayList<>();
                        for (int i = 0; i < listUserTaskCarrer.size(); i++) {
                            if (listUserTaskCarrer.get(i).getCmplt().equals("1")) {
                                listUserTaskByFilter.add(listUserTaskCarrer.get(i));
                            }
                        }
                        adapterRecyclerMMTask.setListUser(listUserTaskByFilter);
                    }
                    break;
                }
            }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_00, container, false);

        LinearLayout linearLayout= (LinearLayout)rootView.findViewById(R.id.seccionCeroHead);
        linearLayout.setVisibility(View.GONE);

        FabMMT=(FloatingActionButton)rootView.findViewById(R.id.fabMM);
        FabMMT.setVisibility(View.VISIBLE);
        FabMMT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                String msj = "";
                for (int i = 0; i < adapterRecyclerMMTask.listState.size(); i++) {
                    if ((adapterRecyclerMMTask.listState.get(i) == 0 ||
                            adapterRecyclerMMTask.listState.get(i) == 1) && flag) {
                        flag = false;
                        msj = "Actualmente estas editando un Task.\n" +
                                "Culminar antes de agregar otro";
                    }
                }
                if (listUserMateria.isEmpty()) {
                    flag = false;
                    msj = "No tienes agregada ninguna materia para designar un Task";
                }

                if (flag) {

                    adapterRecyclerMMTask.addUserTask();
                    manager.scrollToPosition(manager.getItemCount() - 1);

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Notify Info");
                    alertDialog.setMessage(msj);
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // L.t(getContext(), "Guarde o borre ");

                        }
                    });
                    alertDialog.show();
                }


            }
        });

        rcListTask=(RecyclerView)rootView.findViewById(R.id.recycleView);
        adapterRecyclerMMTask=new AdapterRecyclerMMTask(getContext());
        adapterRecyclerMMTask.setClickCallBack(clickCallBack);
        adapterRecyclerMMTask.setFRMMT(fragmentBaseMMTask);

        rcListTask.setNestedScrollingEnabled(false);
         manager=new LinearLayoutManager(getContext());

        rcListTask.setLayoutManager(manager);
        rcListTask.setSoundEffectsEnabled(true);
        rcListTask.setAdapter(adapterRecyclerMMTask);

        return rootView; //super.onCreateView(inflater, container, savedInstanceState);
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
    public void seteoDeTiempo(int hora, int minuto, String weekDay, int itemPoss) {
      final  Calendar ob =Calendar.getInstance();
        ob.set(Calendar.HOUR_OF_DAY,hora);
        ob.set(Calendar.MINUTE,minuto);

      final   Calendar ob2=Calendar.getInstance();
        ob2.set(Calendar.HOUR_OF_DAY,hora+1);
        ob2.set(Calendar.MINUTE,minuto);

        final int poss=itemPoss;
        TimePickerDialog timepick = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                ob2.set(Calendar.HOUR_OF_DAY,hourOfDay);
                ob2.set(Calendar.MINUTE,minute);

                adapterRecyclerMMTask.setHRItem(ob,ob2,poss);
            }
        }, hora,  minuto, false
        );
        timepick.setTitle("Hora de Culminacion");
        timepick.setCancelable(false);
        timepick.show();

    }

    @Override
    public void seteoDeFecha(int year, int monthOfYear, int dayOfMonth, int itemPoss) {

        Calendar ob=Calendar.getInstance();
        ob.set(Calendar.YEAR,year);
        ob.set(Calendar.MONTH,monthOfYear);
        ob.set(Calendar.DAY_OF_MONTH,dayOfMonth);



        adapterRecyclerMMTask.setDayTime(ob,itemPoss);
    }

    @Override
    public void seteoDeColor(int color) {

    }
}
