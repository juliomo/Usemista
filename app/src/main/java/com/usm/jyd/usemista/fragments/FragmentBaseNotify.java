package com.usm.jyd.usemista.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.adapters.AdapterRecyclerNotify;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.dialogs.GuiaUsuario;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.objects.NotifyItem;

import java.util.ArrayList;

/**
 * Created by der_w on 12/25/2015.
 */
public class FragmentBaseNotify extends Fragment {

    private ClickCallBack clickCallBack;
    private ArrayList<NotifyItem> listUserNoti;
    private RecyclerView rcListNoti;
    private AdapterRecyclerNotify adapterRecyclerNotify;
    private Boolean flagEdition=false;

    //Empty Handle
    private ImageView imgEmptyList;
    private TextView textEmptyList;

    public FragmentBaseNotify() {
        // Required empty public constructor
    }

    public static FragmentBaseNotify newInstance( ) {
        FragmentBaseNotify fragment = new FragmentBaseNotify();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listUserNoti= MiAplicativo.getWritableDatabase().getAllNotiItem();

        String auxGuiaUsuario = "";
        auxGuiaUsuario = MiAplicativo.getWritableDatabase().getUserGuia("noti");
        if (auxGuiaUsuario.equals("0")) {
            GuiaUsuario guiaUsuario = new GuiaUsuario();
            guiaUsuario.setGuiaUsuario("noti");
            guiaUsuario.show(getChildFragmentManager(),"Dialog");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.act_base_fr_notify_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(flagEdition) {
            menu.findItem(R.id.action_edit).setVisible(false);
            menu.findItem(R.id.action_save).setVisible(true);
        }else{
            menu.findItem(R.id.action_edit).setVisible(true);
            menu.findItem(R.id.action_save).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.action_edit){
            flagEdition=true;
            adapterRecyclerNotify.setEdition(true);
            getActivity().invalidateOptionsMenu();
        }
        else if(id==R.id.action_save){
            flagEdition=false;
            adapterRecyclerNotify.setEdition(false);
            getActivity().invalidateOptionsMenu();
        }
        else if(id==R.id.action_delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Borrar Notificaciones");
            builder.setMessage("Seguro que desea eliminar todas las notificaciones?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MiAplicativo.getWritableDatabase().deleteAllNotiItem();
                    listUserNoti=new ArrayList<>();
                    adapterRecyclerNotify.setlistNotifyItem(listUserNoti);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_base_00, container, false);

        ImageView imageViewIcon= (ImageView)rootView.findViewById(R.id.seccionCeroImageView);
        imageViewIcon.setImageResource(R.drawable.ic_notifications_black_36dp);
        imageViewIcon.setColorFilter(0xffffffff);
        TextView textViewTituloFragment = (TextView) rootView.findViewById(R.id.seccionCeroTitulo);
        textViewTituloFragment.setText("Notify");
        rcListNoti=(RecyclerView) rootView.findViewById(R.id.recycleView);

        rcListNoti.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterRecyclerNotify= new AdapterRecyclerNotify(getContext());
        adapterRecyclerNotify.setClickCallBack(clickCallBack);
        adapterRecyclerNotify.setlistNotifyItem(listUserNoti);

        rcListNoti.setSoundEffectsEnabled(true);
        rcListNoti.setAdapter(adapterRecyclerNotify);

        imgEmptyList=(ImageView)rootView.findViewById(R.id.imgEmptyList);
        textEmptyList=(TextView)rootView.findViewById(R.id.textEmptyList);
        if(listUserNoti.isEmpty()){
            imgEmptyList.setVisibility(View.VISIBLE);textEmptyList.setVisibility(View.VISIBLE);
            imgEmptyList.setImageResource(R.drawable.ic_notify_01);
            imgEmptyList.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
            textEmptyList.setText("No hay Notificaciones");
            textEmptyList.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextSecondary));
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
}
