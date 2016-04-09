package com.usm.jyd.usemista.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.acts.ActBase;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.fragments.FragmentBaseMMTask;
import com.usm.jyd.usemista.objects.Materia;
import com.usm.jyd.usemista.objects.UserTask;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by der_w on 11/25/2015.
 */
public class AdapterRecyclerMMTask extends RecyclerView.Adapter<AdapterRecyclerMMTask.RMMTViewHolder> {

    public Boolean validatorVHListMateriaUpdate=false, ItemChangeOnFirstAdd=false;

    public ArrayList<Integer> listState=new ArrayList<>();
    private ArrayList<Materia> listUserMateria= new ArrayList<>();
    private ArrayList<UserTask> listUserTask= new ArrayList<>();

    private int positionSpinnerMateria=0;
    private int positionSpinnerTipoAct=0;

    private LayoutInflater inflater;
    private Context context;
    private ClickCallBack clickCallBack;
    private FragmentBaseMMTask fragmentBaseMMTask;

    private boolean onBindCheckCmplt;




    public AdapterRecyclerMMTask (Context context){
        inflater = LayoutInflater.from(context);
        this.context=context;

    }
    public void setFRMMT(FragmentBaseMMTask frmmt){
        fragmentBaseMMTask=frmmt;
        clickCallBack.setFrMMTAux(frmmt);
    }

    public void setClickCallBack(ClickCallBack clickCallBack){
        this.clickCallBack=clickCallBack;
    }
    public void setListUser(ArrayList<UserTask> listUserTask){
        this.listUserTask=listUserTask;
        setListState(listUserTask);
        notifyDataSetChanged();
    }
    public void setListUser(ArrayList<UserTask> listUserTask,ArrayList<Materia> listUserMateria){
        this.listUserTask=listUserTask;
        setListState(listUserTask);
        this.listUserMateria=listUserMateria;  validatorVHListMateriaUpdate=true;
        notifyDataSetChanged();
    }
    private void  setListState(ArrayList<UserTask> listUserTask){
        listState=new ArrayList<>();
        for(int i=0;i<listUserTask.size();i++){
            listState.add(2);
        }
    }
    public void addUserTask(){

        UserTask newUserTask = new UserTask();
        newUserTask.setDayTime(Calendar.getInstance());
        newUserTask.setHrIni(Calendar.getInstance());
        newUserTask.setHrEnd(Calendar.getInstance());
        newUserTask.setNota(0);
        newUserTask.setCmplt("0");
        newUserTask.setCod("sin cod");
        newUserTask.setMtName("Nueva task");
        newUserTask.setType("Examen #1");
        newUserTask.setSalon("822");
        newUserTask.setId(-1);


        listUserTask.add(newUserTask);

       listState.add(0);
        notifyItemInserted(listUserTask.size());
        notifyDataSetChanged();

    }
    public void itemEditionHasSet(int position, int state){
        listState.set(position, state);
        notifyItemChanged(position);
    }
    public void itemSaveHasSet(int position,int state){
        listState.set(position,state);
        notifyItemChanged(position);
    }
    public void itemDeleteHasSet(int position){
        notifyItemRemoved(position);
        listState.remove(position);listUserTask.remove(position);
        notifyDataSetChanged();
    }

    public void setHRItem(Calendar ob, Calendar ob2, int position){
        UserTask currentUT= listUserTask.get(position);
        currentUT.setHrIni(ob);
        currentUT.setHrEnd(ob2);

        listUserTask.set(position, currentUT); ItemChangeOnFirstAdd=true;
        notifyItemChanged(position);

    }
    public void setDayTime(Calendar ob,int position){
        UserTask currentUt = listUserTask.get(position);
        currentUt.setDayTime(ob);

        listUserTask.set(position, currentUt); ItemChangeOnFirstAdd=true;
        notifyItemChanged(position);
    }

    public void setCmplt(int position, String estado, int nota){
        MiAplicativo.getWritableDatabase()
                .updateCmpltUserTask(estado, nota,
                        listUserTask.get(position).getId());

        listUserTask.get(position).setCmplt(estado);
        listUserTask.get(position).setNota(nota);
        notifyItemChanged(position);
       // notifyDataSetChanged();
    }

    @Override
    public RMMTViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = inflater.inflate(R.layout.row_rc_fr_base_mm_task, parent, false);
        RMMTViewHolder holder = new RMMTViewHolder(root);
        return holder;
    }


    @Override
    public void onBindViewHolder(RMMTViewHolder holder, int position) {
        if(validatorVHListMateriaUpdate){
            validatorVHListMateriaUpdate=false;
            holder.setListUserMateriaUpdate(listUserMateria);
        }
        holder.icMtNor.setColorFilter(ContextCompat.getColor(context,R.color.ut_examen_color));
        holder.icMtEdit.setColorFilter(ContextCompat.getColor(context,R.color.ut_examen_color));
        holder.icTypeActNor.setColorFilter(ContextCompat.getColor(context,R.color.ut_examen_color));
        holder.icTypeActEdit.setColorFilter(ContextCompat.getColor(context,R.color.ut_examen_color));
        holder.icSalonNor.setColorFilter(ContextCompat.getColor(context,R.color.ut_examen_color));
        holder.icSalonEd.setColorFilter(ContextCompat.getColor(context,R.color.ut_examen_color));
        holder.icDayTimeNor.setColorFilter(ContextCompat.getColor(context,R.color.ut_examen_color));
        holder.icClock.setColorFilter(ContextCompat.getColor(context,R.color.ut_examen_color));
        holder.icComplete.setColorFilter(ContextCompat.getColor(context,R.color.ut_examen_color));

   /*     holder.icActSave.setColorFilter(ContextCompat.getColor(context,R.color.ut_examen_color));
        holder.icActEdit.setColorFilter(ContextCompat.getColor(context,R.color.ut_examen_color));
        holder.icActDelete.setColorFilter(ContextCompat.getColor(context,R.color.ut_examen_color));*/



        if(listState.get(position)==2){
            holder.icActSave.setVisibility(View.GONE);
            holder.icActDelete.setVisibility(View.GONE);
            holder.icActEdit.setVisibility(View.VISIBLE);

            holder.icMtNor.setVisibility(View.VISIBLE);holder.icMtEdit.setVisibility(View.GONE);
            holder.textMateria.setVisibility(View.VISIBLE);holder.spinnerMateria.setVisibility(View.GONE);

            holder.icTypeActNor.setVisibility(View.VISIBLE);holder.icTypeActEdit.setVisibility(View.GONE);
            holder.textTypeAct.setVisibility(View.VISIBLE);holder.spinnerTypeAct.setVisibility(View.GONE);
                                                            holder.editTextNumAct.setVisibility(View.GONE);

            holder.icSalonNor.setVisibility(View.VISIBLE);holder.icSalonEd.setVisibility(View.GONE);
            holder.textSalon.setVisibility(View.VISIBLE);holder.editTextSalon.setVisibility(View.GONE);

            holder.textDayTime.setClickable(false);
            holder.textClockIni.setClickable(false);holder.textClockEnd.setClickable(false);

            holder.icComplete.setVisibility(View.VISIBLE);
            holder.checkBoxcmplt.setVisibility(View.VISIBLE);holder.textNota.setVisibility(View.VISIBLE);

            //Background
            holder.textClockIni.setBackgroundColor(ContextCompat.getColor(context, R.color.coloGrisClaro));
            holder.textClockIni.setTextColor(ContextCompat.getColor(context, R.color.colorTextSecondary));
            holder.textDayTime.setBackgroundColor(ContextCompat.getColor(context, R.color.coloGrisClaro));
            holder.textDayTime.setTextColor(ContextCompat.getColor(context, R.color.colorTextSecondary));

            //Contenido
            UserTask currentTask;
            currentTask= listUserTask.get(position);

            holder.textMateria.setText(currentTask.getMtName());
            holder.textTypeAct.setText(currentTask.getType());
            holder.textSalon.setText(currentTask.getSalon());
            holder.textDayTime.setText(currentTask.getDayTimeToText());
            holder.textClockIni.setText(currentTask.getHrIniToText());
            holder.textClockEnd.setText(currentTask.getHrEndToText());

            onBindCheckCmplt=true;
            if(currentTask.getCmplt().equals("0")){
                holder.checkBoxcmplt.setChecked(false);
                holder.textNota.setText("  Nota");

            }else if(currentTask.getCmplt().equals("1")){
                holder.checkBoxcmplt.setChecked(true);
                holder.textNota.setText("  Nota: "+currentTask.getNota());
            }
            onBindCheckCmplt=false;



        }else if(listState.get(position)==1){
            holder.icActSave.setVisibility(View.VISIBLE);
            holder.icActDelete.setVisibility(View.VISIBLE);
            holder.icActEdit.setVisibility(View.GONE);

            holder.icMtNor.setVisibility(View.VISIBLE);holder.icMtEdit.setVisibility(View.GONE);
            holder.textMateria.setVisibility(View.VISIBLE);holder.spinnerMateria.setVisibility(View.GONE);

            holder.icTypeActNor.setVisibility(View.GONE);holder.icTypeActEdit.setVisibility(View.VISIBLE);
            holder.textTypeAct.setVisibility(View.GONE);holder.spinnerTypeAct.setVisibility(View.VISIBLE);
                                                         holder.editTextNumAct.setVisibility(View.VISIBLE);

            holder.icSalonNor.setVisibility(View.GONE);holder.icSalonEd.setVisibility(View.VISIBLE);
            holder.textSalon.setVisibility(View.GONE);holder.editTextSalon.setVisibility(View.VISIBLE);

            holder.textDayTime.setClickable(true);
            holder.textClockIni.setClickable(true);holder.textClockEnd.setClickable(true);

            holder.icComplete.setVisibility(View.GONE);
            holder.checkBoxcmplt.setVisibility(View.GONE);
            holder.textNota.setVisibility(View.GONE);


            //Background
            holder.textClockIni.setBackgroundResource(R.drawable.ut_row_clock);
            holder.textClockIni.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.textDayTime.setBackgroundResource(R.drawable.ut_row_clock);
            holder.textDayTime.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));

            //Contenido
            UserTask currentTask;
            currentTask= listUserTask.get(position);

            holder.textMateria.setText(currentTask.getMtName());

            String auxTypeAct=currentTask.getType();
            if(auxTypeAct.substring(0,3).equals("Exa")){
                holder.spinnerTypeAct.setSelection(0); positionSpinnerTipoAct=0;
                holder.editTextNumAct.setText(auxTypeAct.substring(8));

            }else if(auxTypeAct.substring(0,3).equals("Exp")){
                holder.spinnerTypeAct.setSelection(1); positionSpinnerTipoAct=1;
                holder.editTextNumAct.setText(auxTypeAct.substring(12));

            }else if(auxTypeAct.substring(0,3).equals("Tal")){
                holder.spinnerTypeAct.setSelection(2); positionSpinnerTipoAct=2;
                holder.editTextNumAct.setText(auxTypeAct.substring(8));
            }



            holder.editTextSalon.setText(currentTask.getSalon());
            holder.textDayTime.setText(currentTask.getDayTimeToText());
            holder.textClockIni.setText(currentTask.getHrIniToText());
            holder.textClockEnd.setText(currentTask.getHrEndToText());

        }else if(listState.get(position)==0){
            holder.icActSave.setVisibility(View.VISIBLE);
            holder.icActDelete.setVisibility(View.VISIBLE);
            holder.icActEdit.setVisibility(View.GONE);

            holder.icMtNor.setVisibility(View.GONE);holder.icMtEdit.setVisibility(View.VISIBLE);
            holder.textMateria.setVisibility(View.GONE);holder.spinnerMateria.setVisibility(View.VISIBLE);

            holder.icTypeActNor.setVisibility(View.GONE);holder.icTypeActEdit.setVisibility(View.VISIBLE);
            holder.textTypeAct.setVisibility(View.GONE);holder.spinnerTypeAct.setVisibility(View.VISIBLE);
                                                        holder.editTextNumAct.setVisibility(View.VISIBLE);

            holder.icSalonNor.setVisibility(View.GONE);holder.icSalonEd.setVisibility(View.VISIBLE);
            holder.textSalon.setVisibility(View.GONE);holder.editTextSalon.setVisibility(View.VISIBLE);

            holder.textDayTime.setClickable(true);
            holder.textClockIni.setClickable(true);holder.textClockEnd.setClickable(true);

            holder.icComplete.setVisibility(View.GONE);
            holder.checkBoxcmplt.setVisibility(View.GONE);holder.textNota.setVisibility(View.GONE);


            //Background
            holder.textClockIni.setBackgroundResource(R.drawable.ut_row_clock);
            holder.textClockIni.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.textDayTime.setBackgroundResource(R.drawable.ut_row_clock);
            holder.textDayTime.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));

            //Contenido
            UserTask currentTask;
            currentTask= listUserTask.get(position);

            holder.textMateria.setText(currentTask.getMtName());

            if(ItemChangeOnFirstAdd) {

                for(int i=0;i<listUserMateria.size();i++){
                    if(listUserMateria.get(i).getCod().equals(currentTask.getCod())){
                        holder.spinnerMateria.setSelection(i);
                    }
                }

                String auxTypeAct = currentTask.getType();
                if (auxTypeAct.substring(0, 3).equals("Exa")) {
                    holder.spinnerTypeAct.setSelection(0);
                    positionSpinnerTipoAct = 0;
                    holder.editTextNumAct.setText(auxTypeAct.substring(8));
                }else if(auxTypeAct.substring(0,3).equals("Exp")){
                    holder.spinnerTypeAct.setSelection(1); positionSpinnerTipoAct=1;
                    holder.editTextNumAct.setText(auxTypeAct.substring(12));

                }else if(auxTypeAct.substring(0,3).equals("Tal")){
                    holder.spinnerTypeAct.setSelection(2); positionSpinnerTipoAct=2;
                    holder.editTextNumAct.setText(auxTypeAct.substring(8));
                }
                holder.editTextSalon.setText(currentTask.getSalon());
            }

            holder.textDayTime.setText(currentTask.getDayTimeToText());
            holder.textClockIni.setText(currentTask.getHrIniToText());
            holder.textClockEnd.setText(currentTask.getHrEndToText());
        }
    }

    @Override
    public int getItemCount() {
        return listUserTask.size();
    }




    public class RMMTViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView icActEdit, icActSave, icActDelete;

        //Row Materia cod
        ImageView icMtNor, icMtEdit;
        TextView textMateria;
        Spinner spinnerMateria; ArrayList<String> categoriesMT; ArrayAdapter<String> adapterSpinnerMT;

        //row Type Actividad
        ImageView icTypeActNor,icTypeActEdit;
        TextView textTypeAct;
        Spinner spinnerTypeAct;     ArrayList<String> categoriesTA;
        EditText editTextNumAct;

        //Row Salon
        ImageView icSalonNor,icSalonEd;
        TextView textSalon;
        EditText editTextSalon;

        //Row Calendar DAYTIME
        ImageView icDayTimeNor;
        TextView textDayTime;

        //Row Calendar HrINI/END
        ImageView icClock;
        TextView textClockIni,textClockEnd;

        //Row Complete
        ImageView icComplete;
        CheckBox checkBoxcmplt;
        TextView textNota;

        public void setListUserMateriaUpdate(ArrayList<Materia> listUserMateriaUpdate){
            categoriesMT= new ArrayList<>();

            for(int i=0;i<listUserMateriaUpdate.size();i++){
                categoriesMT.add(listUserMateriaUpdate.get(i).getTitulo());
            }

            adapterSpinnerMT = new
                    ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, categoriesMT);
            adapterSpinnerMT.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinnerMateria.setAdapter(adapterSpinnerMT);
            spinnerMateria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    positionSpinnerMateria = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }


        public RMMTViewHolder(View itemView) {
            super(itemView);


            icActEdit=(ImageView)itemView.findViewById(R.id.actionEdit);
            icActSave=(ImageView)itemView.findViewById(R.id.actionSave);
            icActDelete=(ImageView)itemView.findViewById(R.id.actionDelete);

            icActEdit.setOnClickListener(this);icActSave.setOnClickListener(this);
            icActDelete.setOnClickListener(this);





            //Row Materia cod
            icMtNor=(ImageView)itemView.findViewById(R.id.icMateriaNor);
            icMtEdit=(ImageView)itemView.findViewById(R.id.icMateriaEdit);
            textMateria=(TextView)itemView.findViewById(R.id.textMateria);
            spinnerMateria=(Spinner)itemView.findViewById(R.id.spinnerMateria);

            categoriesMT= new ArrayList<>();

            for(int i=0;i<listUserMateria.size();i++){
                categoriesMT.add(listUserMateria.get(i).getTitulo());
            }

            adapterSpinnerMT = new
                    ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, categoriesMT);
            adapterSpinnerMT.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinnerMateria.setAdapter(adapterSpinnerMT);
            spinnerMateria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    positionSpinnerMateria = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });






            //Row Type ACt
            icTypeActNor=(ImageView)itemView.findViewById(R.id.icTypeActNor);
            icTypeActEdit=(ImageView)itemView.findViewById(R.id.icTypeActEdit);
            textTypeAct=(TextView)itemView.findViewById(R.id.textTypeAct);
            spinnerTypeAct=(Spinner)itemView.findViewById(R.id.spinnerTypeAct);
            editTextNumAct=(EditText)itemView.findViewById(R.id.editTextNumAct);
            editTextNumAct.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        editTextNumAct.setText("1");
                        editTextNumAct.selectAll();
                    }
                }
            });

            categoriesTA= new ArrayList<>();
            categoriesTA.add("Examen");categoriesTA.add("Exposicion");
            categoriesTA.add("Taller");

            ArrayAdapter<String> adapterSpinnerTA = new
                    ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, categoriesTA);
            adapterSpinnerTA.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinnerTypeAct.setAdapter(adapterSpinnerTA);
            spinnerTypeAct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    positionSpinnerTipoAct = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //Row salon
            icSalonNor=(ImageView)itemView.findViewById(R.id.icSalonNor);
            icSalonEd=(ImageView)itemView.findViewById(R.id.icSalonEdit);
            textSalon=(TextView)itemView.findViewById(R.id.textSalon);
            editTextSalon=(EditText)itemView.findViewById(R.id.editTextSalon);
            editTextSalon.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus)
                        editTextSalon.setText("822");
                        editTextSalon.selectAll();
                }
            });
            //Row Daytime
            icDayTimeNor=(ImageView)itemView.findViewById(R.id.icDayTime);
            textDayTime=(TextView)itemView.findViewById(R.id.textDayTime);
            textDayTime.setOnClickListener(this);

            //Row HR INI END
            icClock=(ImageView)itemView.findViewById(R.id.icClock);
            textClockIni=(TextView)itemView.findViewById(R.id.textClockIni);
            textClockEnd=(TextView)itemView.findViewById(R.id.textClockEnd);
            textClockIni.setOnClickListener(this);


            //ROw Complete
            icComplete=(ImageView)itemView.findViewById(R.id.icComplete);
            checkBoxcmplt=(CheckBox)itemView.findViewById(R.id.checkBoxCmplt);
            textNota=(TextView)itemView.findViewById(R.id.textNota);

            checkBoxcmplt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!onBindCheckCmplt) {
                        if (isChecked) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(context);

                            LayoutInflater inflater;
                            TextView textNomb, textCIoProfCod;
                            final EditText editTextNomb, editTextCIoProfCod;
                            ImageView imgCurrentType;

                            inflater = ((ActBase) context).getLayoutInflater();
                            View view = inflater.inflate(R.layout.dialog_user_regi, null);

                            textNomb = (TextView) view.findViewById(R.id.textNomb);
                            textCIoProfCod = (TextView) view.findViewById(R.id.textCIoProfCod);
                            editTextNomb = (EditText) view.findViewById(R.id.editTextNomb);
                            editTextCIoProfCod = (EditText) view.findViewById(R.id.editTextCIoProfCod);
                            imgCurrentType = (ImageView) view.findViewById(R.id.imgCurrentType);

                            textNomb.setVisibility(View.GONE);
                            editTextNomb.setVisibility(View.GONE);


                            imgCurrentType.setImageResource(R.drawable.ic_estudiante_launch);
                            textCIoProfCod.setText("Nota");

                            editTextCIoProfCod.setInputType(InputType.TYPE_CLASS_NUMBER);
                            editTextCIoProfCod.setText("");
                            editTextCIoProfCod.setFilters(new InputFilter[] { new InputFilter.LengthFilter(2) });


                            imgCurrentType.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));

                            builder.setView(view)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            String auxNtSTR = "";
                                            int auxNota = 0;
                                            auxNtSTR = editTextCIoProfCod.getText().toString();

                                            if (!auxNtSTR.equals(""))
                                                auxNota = Integer.parseInt(auxNtSTR);

                                            if (auxNota > 20)
                                                auxNota = 20;


                                            setCmplt(getAdapterPosition(), "1", auxNota);


                                        }
                                    })
                                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            setCmplt(getAdapterPosition(), "0", 0);
                                        }
                                    });

                            builder.show();
                        } else {
                            setCmplt(getAdapterPosition(), "0", 0);

                        }
                    }
                }
            });


        }

        @Override
        public void onClick(View v) {
            if(v==v.findViewById(R.id.actionEdit)){
                itemEditionHasSet(getAdapterPosition(),1);

            }else if(v==v.findViewById(R.id.actionSave)){
               UserTask currentUT= listUserTask.get(getAdapterPosition());

                if(listState.get(getAdapterPosition())==0){
                currentUT.setMtName(listUserMateria.get(positionSpinnerMateria).getTitulo());}
                currentUT.setCod(listUserMateria.get(positionSpinnerMateria).getCod());
                currentUT.setType(categoriesTA.get(positionSpinnerTipoAct) + " #" + editTextNumAct.getText());
                currentUT.setSalon(editTextSalon.getText().toString());
                //Faltan: daytime y HR INI END


                if(listState.get(getAdapterPosition())==0){ ItemChangeOnFirstAdd=false;
                    MiAplicativo.getWritableDatabase().insertUserTask(currentUT);
                    currentUT.setId(MiAplicativo.getWritableDatabase().getLastUserTaskId());
                } else if (listState.get(getAdapterPosition())==1){
                    MiAplicativo.getWritableDatabase().updateSingleUserTask(currentUT);
                }

                fragmentBaseMMTask.updateMainListOnLoad();
                listUserTask.set(getAdapterPosition(),currentUT);
                itemSaveHasSet(getAdapterPosition(), 2);

            }else if(v==v.findViewById(R.id.actionDelete)){

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Borrar User Task");
                builder.setMessage("Seguro que desea eliminar este Task?");
                builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(listState.get(getAdapterPosition())==1){
                            int uIdUserTask = listUserTask.get(getAdapterPosition()).getId();
                            MiAplicativo.getWritableDatabase().deleteSigleUserMateriaTask(uIdUserTask);
                            fragmentBaseMMTask.updateMainListOnLoad();
                        }
                        itemDeleteHasSet(getAdapterPosition());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

            }

            if(v==v.findViewById(R.id.textDayTime)){
                UserTask currentUT= listUserTask.get(getAdapterPosition());
                if(listState.get(getAdapterPosition())==0){
                currentUT.setMtName(listUserMateria.get(positionSpinnerMateria).getTitulo());
                currentUT.setCod(listUserMateria.get(positionSpinnerMateria).getCod());}
                currentUT.setType(categoriesTA.get(positionSpinnerTipoAct) + " #" + editTextNumAct.getText());
                currentUT.setSalon(editTextSalon.getText().toString());
                listUserTask.set(getAdapterPosition(),currentUT);

                clickCallBack.onHVCalendarSelected(getAdapterPosition());



            }else  if(v==v.findViewById(R.id.textClockIni)){
                UserTask currentUT= listUserTask.get(getAdapterPosition());
                if(listState.get(getAdapterPosition())==0){
                    currentUT.setMtName(listUserMateria.get(positionSpinnerMateria).getTitulo());
                currentUT.setCod(listUserMateria.get(positionSpinnerMateria).getCod());}
                currentUT.setType(categoriesTA.get(positionSpinnerTipoAct) + " #" + editTextNumAct.getText());
                currentUT.setSalon(editTextSalon.getText().toString());
                listUserTask.set(getAdapterPosition(),currentUT);
                clickCallBack.onHVTimeSelected(getAdapterPosition(),"NoNeedIt");
            }

        }
    }
}
