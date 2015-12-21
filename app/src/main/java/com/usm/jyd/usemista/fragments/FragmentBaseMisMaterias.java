package com.usm.jyd.usemista.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.adapters.AdapterRecyclerMisMaterias;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.objects.Materia;

import java.util.ArrayList;

/**
 * Created by der_w on 11/23/2015.
 */
public class FragmentBaseMisMaterias extends Fragment {

    private static final String ARG_NUMERO_SECCION = "numero_seccion";
    private ClickCallBack clickCallBack;

    private AdapterRecyclerMisMaterias adapterRecyclerMisMaterias;
    private RecyclerView rcListUserMateria;
    private ArrayList<Materia> listUserMateria;
    private ArrayList<Materia> listUMLoad = new ArrayList<>();
    private ArrayList<Materia> listUMbySe=new ArrayList<>();
    private int pensumValidator=0;

    public FragmentBaseMisMaterias(){}

    public static FragmentBaseMisMaterias newInstance(int num_seccion) {
        FragmentBaseMisMaterias fragment = new FragmentBaseMisMaterias();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMERO_SECCION, num_seccion);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listUserMateria= MiAplicativo.getWritableDatabase().getAllUserMateria();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Inflar Menu y condicionar el menu
        inflater.inflate(R.menu.act_base_fr_mm_menu, menu);
        if(!listUserMateria.isEmpty()) {
            listUMLoad=new ArrayList<>();
            boolean sis=false,telc=false;
            for(int i =0;i<listUserMateria.size();i++){
                if(listUserMateria.get(i).getModulo().equals("ingSis")){sis=true;}
                else if(listUserMateria.get(i).getModulo().equals("telecom")){telc=true;}
            }

            if(!sis){menu.findItem(R.id.action_sistema_fr_mm).setVisible(false);}
            if(!telc){menu.findItem(R.id.action_telecom_fr_mm).setVisible(false);}


            int seValidator=0;
            if (listUserMateria.get(0).getModulo().equals("ingSis")) {
                menu.findItem(R.id.action_sistema_fr_mm).setChecked(true); seValidator=1;
            } else if (listUserMateria.get(0).getModulo().equals("telecom")) {
                menu.findItem(R.id.action_telecom_fr_mm).setChecked(true); seValidator=2;
            }menu.findItem(R.id.action_seAll).setChecked(true);



            if(seValidator==1){
                for(int i=0;i<listUserMateria.size();i++) {
                    if (listUserMateria.get(i).getModulo().equals("ingSis")) {
                        listUMLoad.add(listUserMateria.get(i));
                    }
                }
            }else if(seValidator==2){
                for(int i=0;i<listUserMateria.size();i++) {
                    if (listUserMateria.get(i).getModulo().equals("telecom")) {
                        listUMLoad.add(listUserMateria.get(i));
                    }
                }
            }
            boolean se1=false,se2=false,se3=false,se4=false,se5=false,
                    se6=false,se7=false,se8=false,se9=false,se10=false;
            for(int i=0;i<listUMLoad.size();i++){
                if(listUMLoad.get(i).getSemestre().equals("1")){se1=true;}
                else if(listUMLoad.get(i).getSemestre().equals("2")){se2=true;}
                else if(listUMLoad.get(i).getSemestre().equals("3")){se3=true;}
                else if(listUMLoad.get(i).getSemestre().equals("4")){se4=true;}
                else if(listUMLoad.get(i).getSemestre().equals("5")){se5=true;}
                else if(listUMLoad.get(i).getSemestre().equals("6")){se6=true;}
                else if(listUMLoad.get(i).getSemestre().equals("7")){se7=true;}
                else if(listUMLoad.get(i).getSemestre().equals("8")){se8=true;}
                else if(listUMLoad.get(i).getSemestre().equals("9")){se9=true;}
                else if(listUMLoad.get(i).getSemestre().equals("10")){se10=true;}
            }
            if(!se1){menu.findItem(R.id.action_se1).setVisible(false);}
            if(!se2){menu.findItem(R.id.action_se2).setVisible(false);}
            if(!se3){menu.findItem(R.id.action_se3).setVisible(false);}
            if(!se4){menu.findItem(R.id.action_se4).setVisible(false);}
            if(!se5){menu.findItem(R.id.action_se5).setVisible(false);}
            if(!se6){menu.findItem(R.id.action_se6).setVisible(false);}
            if(!se7){menu.findItem(R.id.action_se7).setVisible(false);}
            if(!se8){menu.findItem(R.id.action_se8).setVisible(false);}
            if(!se9){menu.findItem(R.id.action_se9).setVisible(false);}
            if(!se10){menu.findItem(R.id.action_se10).setVisible(false);}

        }else{
            menu.findItem(R.id.action_usm_fr_mm).setVisible(false);
            menu.findItem(R.id.action_sem_fr_mm).setVisible(false);
        }
        adapterRecyclerMisMaterias.setListUserMateria(listUMLoad);


        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        if(pensumValidator!=0)
        listUMLoad=new ArrayList<>();


        boolean se1=false,se2=false,se3=false,se4=false,se5=false,
                se6=false,se7=false,se8=false,se9=false,se10=false;

        if(pensumValidator==1){
            for(int i=0;i<listUserMateria.size();i++){
                if(listUserMateria.get(i).getModulo().equals("ingSis")){
                    listUMLoad.add(listUserMateria.get(i));
                }
            }
            for(int i=0;i<listUMLoad.size();i++){
                if(listUMLoad.get(i).getSemestre().equals("1")){se1=true;}
                else if(listUMLoad.get(i).getSemestre().equals("2")){se2=true;}
                else if(listUMLoad.get(i).getSemestre().equals("3")){se3=true;}
                else if(listUMLoad.get(i).getSemestre().equals("4")){se4=true;}
                else if(listUMLoad.get(i).getSemestre().equals("5")){se5=true;}
                else if(listUMLoad.get(i).getSemestre().equals("6")){se6=true;}
                else if(listUMLoad.get(i).getSemestre().equals("7")){se7=true;}
                else if(listUMLoad.get(i).getSemestre().equals("8")){se8=true;}
                else if(listUMLoad.get(i).getSemestre().equals("9")){se9=true;}
                else if(listUMLoad.get(i).getSemestre().equals("10")){se10=true;}
            }
            if(!se1){menu.findItem(R.id.action_se1).setVisible(false);}
            if(!se2){menu.findItem(R.id.action_se2).setVisible(false);}
            if(!se3){menu.findItem(R.id.action_se3).setVisible(false);}
            if(!se4){menu.findItem(R.id.action_se4).setVisible(false);}
            if(!se5){menu.findItem(R.id.action_se5).setVisible(false);}
            if(!se6){menu.findItem(R.id.action_se6).setVisible(false);}
            if(!se7){menu.findItem(R.id.action_se7).setVisible(false);}
            if(!se8){menu.findItem(R.id.action_se8).setVisible(false);}
            if(!se9){menu.findItem(R.id.action_se9).setVisible(false);}
            if(!se10){menu.findItem(R.id.action_se10).setVisible(false);}

            adapterRecyclerMisMaterias.setListUserMateria(listUMLoad);
            menu.findItem(R.id.action_sistema_fr_mm).setChecked(true);

        }
        else if(pensumValidator==2){
            for(int i=0;i<listUserMateria.size();i++){
                if(listUserMateria.get(i).getModulo().equals("telecom")){
                    listUMLoad.add(listUserMateria.get(i));
                }
            }

            for(int i=0;i<listUMLoad.size();i++){
                if(listUMLoad.get(i).getSemestre().equals("1")){se1=true;}
                else if(listUMLoad.get(i).getSemestre().equals("2")){se2=true;}
                else if(listUMLoad.get(i).getSemestre().equals("3")){se3=true;}
                else if(listUMLoad.get(i).getSemestre().equals("4")){se4=true;}
                else if(listUMLoad.get(i).getSemestre().equals("5")){se5=true;}
                else if(listUMLoad.get(i).getSemestre().equals("6")){se6=true;}
                else if(listUMLoad.get(i).getSemestre().equals("7")){se7=true;}
                else if(listUMLoad.get(i).getSemestre().equals("8")){se8=true;}
                else if(listUMLoad.get(i).getSemestre().equals("9")){se9=true;}
                else if(listUMLoad.get(i).getSemestre().equals("10")){se10=true;}
            }
            if(!se1){menu.findItem(R.id.action_se1).setVisible(false);}
            if(!se2){menu.findItem(R.id.action_se2).setVisible(false);}
            if(!se3){menu.findItem(R.id.action_se3).setVisible(false);}
            if(!se4){menu.findItem(R.id.action_se4).setVisible(false);}
            if(!se5){menu.findItem(R.id.action_se5).setVisible(false);}
            if(!se6){menu.findItem(R.id.action_se6).setVisible(false);}
            if(!se7){menu.findItem(R.id.action_se7).setVisible(false);}
            if(!se8){menu.findItem(R.id.action_se8).setVisible(false);}
            if(!se9){menu.findItem(R.id.action_se9).setVisible(false);}
            if(!se10){menu.findItem(R.id.action_se10).setVisible(false);}

            adapterRecyclerMisMaterias.setListUserMateria(listUMLoad);
            menu.findItem(R.id.action_telecom_fr_mm).setChecked(true);

        } menu.findItem(R.id.action_seAll).setChecked(true);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //En cada Boton Seleccionar
        switch (id){
            case R.id.action_sistema_fr_mm:{
                item.setChecked(!item.isChecked());
                pensumValidator=1;
                getActivity().invalidateOptionsMenu();
            }break;
            case R.id.action_telecom_fr_mm:{
                item.setChecked(!item.isChecked());
                pensumValidator=2;
                getActivity().invalidateOptionsMenu();
            }break;
            case R.id.action_seAll:{
                item.setChecked(!item.isChecked());
                adapterRecyclerMisMaterias.setListUserMateria(listUMLoad);
            }break;
            case R.id.action_se1:{
                item.setChecked(!item.isChecked());
                listUMbySe=new ArrayList<>();
                for(int i=0;i<listUMLoad.size();i++){
                    if(listUMLoad.get(i).getSemestre().equals("1")){listUMbySe.add(listUMLoad.get(i));}
                }adapterRecyclerMisMaterias.setListUserMateria(listUMbySe);
            }break;
            case R.id.action_se2:{
                item.setChecked(!item.isChecked());
                listUMbySe=new ArrayList<>();
                for(int i=0;i<listUMLoad.size();i++){
                    if(listUMLoad.get(i).getSemestre().equals("2")){listUMbySe.add(listUMLoad.get(i));}
                }adapterRecyclerMisMaterias.setListUserMateria(listUMbySe);
            }break;
            case R.id.action_se3:{
                item.setChecked(!item.isChecked());
                listUMbySe=new ArrayList<>();
                for(int i=0;i<listUMLoad.size();i++){
                    if(listUMLoad.get(i).getSemestre().equals("3")){listUMbySe.add(listUMLoad.get(i));}
                }adapterRecyclerMisMaterias.setListUserMateria(listUMbySe);
            }break;
            case R.id.action_se4:{
                item.setChecked(!item.isChecked());
                listUMbySe=new ArrayList<>();
                for(int i=0;i<listUMLoad.size();i++){
                    if(listUMLoad.get(i).getSemestre().equals("4")){listUMbySe.add(listUMLoad.get(i));}
                }adapterRecyclerMisMaterias.setListUserMateria(listUMbySe);
            }break;
            case R.id.action_se5:{
                item.setChecked(!item.isChecked());
                listUMbySe=new ArrayList<>();
                for(int i=0;i<listUMLoad.size();i++){
                    if(listUMLoad.get(i).getSemestre().equals("5")){listUMbySe.add(listUMLoad.get(i));}
                }adapterRecyclerMisMaterias.setListUserMateria(listUMbySe);
            }break;
            case R.id.action_se6:{
                item.setChecked(!item.isChecked());
                listUMbySe=new ArrayList<>();
                for(int i=0;i<listUMLoad.size();i++){
                    if(listUMLoad.get(i).getSemestre().equals("6")){listUMbySe.add(listUMLoad.get(i));}
                }adapterRecyclerMisMaterias.setListUserMateria(listUMbySe);
            }break;
            case R.id.action_se7:{
                item.setChecked(!item.isChecked());
                listUMbySe=new ArrayList<>();
                for(int i=0;i<listUMLoad.size();i++){
                    if(listUMLoad.get(i).getSemestre().equals("7")){listUMbySe.add(listUMLoad.get(i));}
                }adapterRecyclerMisMaterias.setListUserMateria(listUMbySe);
            }break;
            case R.id.action_se8:{
                item.setChecked(!item.isChecked());
                listUMbySe=new ArrayList<>();
                for(int i=0;i<listUMLoad.size();i++){
                    if(listUMLoad.get(i).getSemestre().equals("8")){listUMbySe.add(listUMLoad.get(i));}
                }adapterRecyclerMisMaterias.setListUserMateria(listUMbySe);
            }break;
            case R.id.action_se9:{
                item.setChecked(!item.isChecked());
                listUMbySe=new ArrayList<>();
                for(int i=0;i<listUMLoad.size();i++){
                    if(listUMLoad.get(i).getSemestre().equals("9")){listUMbySe.add(listUMLoad.get(i));}
                }adapterRecyclerMisMaterias.setListUserMateria(listUMbySe);
            }break;
            case R.id.action_se10:{
                item.setChecked(!item.isChecked());
                listUMbySe=new ArrayList<>();
                for(int i=0;i<listUMLoad.size();i++){
                    if(listUMLoad.get(i).getSemestre().equals("10")){listUMbySe.add(listUMLoad.get(i));}
                }adapterRecyclerMisMaterias.setListUserMateria(listUMbySe);
            }break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_base_00, container, false);

        LinearLayout linearLayout= (LinearLayout)rootView.findViewById(R.id.seccionCeroHead);
        linearLayout.setVisibility(View.GONE);

        rcListUserMateria=(RecyclerView)rootView.findViewById(R.id.recycleView);
        rcListUserMateria.setNestedScrollingEnabled(false);
        rcListUserMateria.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterRecyclerMisMaterias= new AdapterRecyclerMisMaterias(getContext());
        adapterRecyclerMisMaterias.setClickCallBack(clickCallBack);
        adapterRecyclerMisMaterias.setListUserMateria(listUMLoad);



        rcListUserMateria.setSoundEffectsEnabled(true);
        rcListUserMateria.setAdapter(adapterRecyclerMisMaterias);

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
}
