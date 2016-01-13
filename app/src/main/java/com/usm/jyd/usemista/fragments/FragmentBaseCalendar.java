package com.usm.jyd.usemista.fragments;

import android.content.Context;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.usm.jyd.usemista.R;
import com.usm.jyd.usemista.aplicativo.MiAplicativo;
import com.usm.jyd.usemista.dialogs.GuiaUsuario;
import com.usm.jyd.usemista.events.ClickCallBack;
import com.usm.jyd.usemista.logs.L;
import com.usm.jyd.usemista.objects.HVWeek;
import com.usm.jyd.usemista.objects.HorarioVirtual;
import com.usm.jyd.usemista.objects.Materia;
import com.usm.jyd.usemista.objects.UserTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by der_w on 12/2/2015.
 */
public class FragmentBaseCalendar extends Fragment implements WeekView.MonthChangeListener,
        WeekView.EventClickListener, WeekView.EventLongPressListener  {

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;

    private ClickCallBack clickCallBack;

    private ArrayList<HorarioVirtual> listHV;
    private ArrayList<HVWeek> listHVW;
    private ArrayList<UserTask> listUT;

    public FragmentBaseCalendar(){}

    public static FragmentBaseCalendar newInstance() {
        FragmentBaseCalendar fragment = new FragmentBaseCalendar();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String auxGuiaUsuario = "";
        auxGuiaUsuario = MiAplicativo.getWritableDatabase().getUserGuia("cal");
        if (auxGuiaUsuario.equals("0")) {
            GuiaUsuario guiaUsuario = new GuiaUsuario();
            guiaUsuario.setGuiaUsuario("cal");
            guiaUsuario.show(getChildFragmentManager(),"Dialog");
        }

        setHasOptionsMenu(true);

        listHV= MiAplicativo.getWritableDatabase().getAllHorarioVirtual();
        listHVW=MiAplicativo.getWritableDatabase().getAllHorarioVirtualWeek();
        listUT=MiAplicativo.getWritableDatabase().getAllUserTask();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.act_base_fr_cal_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        setupDateTimeInterpreter(id == R.id.action_week_view);
        switch (id){
            case R.id.action_today:
                mWeekView.goToToday();
                return true;
            case R.id.action_day_view:
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(1);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_three_day_view:
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_THREE_DAY_VIEW;
                    mWeekView.setNumberOfVisibleDays(3);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                }
                return true;
            case R.id.action_week_view:
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.setChecked(!item.isChecked());
                    mWeekViewType = TYPE_WEEK_VIEW;
                    mWeekView.setNumberOfVisibleDays(7);

                    // Lets change some dimensions to best fit the view.
                    mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                    mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_cal, container, false);

        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) rootView.findViewById(R.id.weekView);

        // Show a toast message about the touched event.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        // Set up a date time interpreter to interpret how the date and time will be formatted in
        // the week view. This is optional.
        setupDateTimeInterpreter(true);



        return rootView; //super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                 SimpleDateFormat format = new SimpleDateFormat(" d/M", Locale.getDefault());

                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));

                return weekday.toUpperCase() +"\n"+ format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                 return hour > 11 ? (hour>12?hour-12:hour)  + " PM" : (hour == 0 ? "0 AM" : hour + " AM");

               // return hour > 11 ? hour-12  + " AM" : (hour == 0 ? "12 PM" : hour + " PM");

            }
        });
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
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        L.t(getContext(), ""+event.getName());
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        L.t(getContext(), "Clicked Long "+event.getName());
    }

    private String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d",
                time.get(Calendar.HOUR_OF_DAY),
                time.get(Calendar.MINUTE),
                time.get(Calendar.MONTH) + 1,
                time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<>();
        WeekViewEvent event;

        for(int i=0; i<listUT.size();i++){

            Calendar auxDayTime= Calendar.getInstance();
            auxDayTime.setTime(listUT.get(i).getDayTime());

            Calendar hrIni = (Calendar)auxDayTime.clone();
            Calendar auxIni= Calendar.getInstance();
            auxIni.setTime(listUT.get(i).getHrIni());
            hrIni.set(Calendar.HOUR_OF_DAY, auxIni.get(Calendar.HOUR_OF_DAY));
            hrIni.set(Calendar.MINUTE, auxIni.get(Calendar.MINUTE));


            Calendar hrEnd =(Calendar)auxDayTime.clone();
            Calendar auxEnd=Calendar.getInstance();
            auxEnd.setTime(listUT.get(i).getHrEnd());
            hrEnd.set(Calendar.HOUR_OF_DAY, auxEnd.get(Calendar.HOUR_OF_DAY));
            hrEnd.set(Calendar.MINUTE, auxEnd.get(Calendar.MINUTE));



            if(newMonth==hrIni.get(Calendar.MONTH)) {
                event = new WeekViewEvent(1,  (listUT.get(i).getMtName().length()>=11
                            ?listUT.get(i).getMtName().substring(0,11)
                            :listUT.get(i).getMtName())
                        +"\n"+listUT.get(i).getType()
                        +" - "+listUT.get(i).getSalon()
                        +"\n"+listUT.get(i).getHrIniToText()
                        +" - "+listUT.get(i).getHrEndToText(), hrIni, hrEnd);

                if(listUT.get(i).getType().substring(0,3).equals("Exa")){
                    event.setColor(ContextCompat.getColor(getContext(), R.color.ut_examen_color));
                }else if(listUT.get(i).getType().substring(0,3).equals("Exp")){
                    event.setColor(ContextCompat.getColor(getContext(), R.color.ut_expo_color));
                }else if(listUT.get(i).getType().substring(0,3).equals("Tal")){
                    event.setColor(ContextCompat.getColor(getContext(), R.color.ut_taller_color));
                }else
                event.setColor(ContextCompat.getColor(getContext(), R.color.ut_otro_color));

                events.add(event);
            }


        }


        for(int i=0; i<listHV.size();i++){
            if(listHV.get(i).getCalendar().equals("1")){

                for(int j=0;j<listHVW.size();j++){
                    if(listHV.get(i).getCod().equals(listHVW.get(j).getCod())){

                        if(listHVW.get(j).getWeekDay().equals("Lun")){

                            Calendar stTime=Calendar.getInstance(); Calendar edTime=Calendar.getInstance();
                            stTime.setTime(listHV.get(i).getCalIni()); edTime.setTime(listHV.get(i).getCalEnd());

                                int limitDays;
                                if(stTime.get(Calendar.YEAR)==edTime.get(Calendar.YEAR)){
                                    limitDays=edTime.get(Calendar.DAY_OF_YEAR);
                                }else if(stTime.get(Calendar.YEAR)<edTime.get(Calendar.YEAR)){
                                    limitDays=(365-stTime.get(Calendar.DAY_OF_YEAR))+
                                            edTime.get(Calendar.DAY_OF_YEAR);
                                }else{ limitDays=366;}

                                for(int k=0, inc=1;
                                        k<limitDays; k+=inc){
                                    if(stTime.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY){

                                        Calendar hrIni = (Calendar)stTime.clone();
                                        Calendar auxIni= Calendar.getInstance();
                                        auxIni.setTime(listHVW.get(j).getTimeIni());
                                        hrIni.set(Calendar.HOUR_OF_DAY, auxIni.get(Calendar.HOUR_OF_DAY));
                                        hrIni.set(Calendar.MINUTE,auxIni.get(Calendar.MINUTE));

                                        Calendar hrEnd =(Calendar)stTime.clone();
                                        Calendar auxEnd=Calendar.getInstance();
                                        auxEnd.setTime(listHVW.get(j).getTimeEnd());
                                        hrEnd.set(Calendar.HOUR_OF_DAY, auxEnd.get(Calendar.HOUR_OF_DAY));
                                        hrEnd.set(Calendar.MINUTE, auxEnd.get(Calendar.MINUTE));



                                        boolean paseHV=true;
                                        for(int m=0; m<listUT.size();m++) {
                                            Calendar auxDayTime = Calendar.getInstance();
                                            auxDayTime.setTime(listUT.get(m).getDayTime());

                                            if(hrIni.get(Calendar.DAY_OF_YEAR)==auxDayTime.get(Calendar.DAY_OF_YEAR)
                                                    && listUT.get(m).getType().substring(0,3).equals("Exa")){
                                                paseHV=false; m=listUT.size();
                                            }
                                        }

                                        if(hrIni.get(Calendar.MONTH)==newMonth-1 && paseHV){
                                            event = new WeekViewEvent(1, listHV.get(i).getTitulo()
                                                    +"\n"+listHVW.get(j).getAula()
                                                    +"\n"+listHVW.get(j).getTimeIniToText()
                                                    +" - "+listHVW.get(j).getTimeEndToText()
                                                    , hrIni, hrEnd);
                                            event.setColor(listHV.get(i).getColor());
                                            events.add(event);
                                        }

                                        inc=7;
                                        stTime.add(Calendar.DAY_OF_MONTH,7);
                                    }
                                    else
                                    {
                                        stTime.add(Calendar.DAY_OF_MONTH,1);
                                    }
                                }

                        }
                        else   if(listHVW.get(j).getWeekDay().equals("Mar")){

                            Calendar stTime=Calendar.getInstance(); Calendar edTime=Calendar.getInstance();
                            stTime.setTime(listHV.get(i).getCalIni()); edTime.setTime(listHV.get(i).getCalEnd());



                                int limitDays;
                                if(stTime.get(Calendar.YEAR)==edTime.get(Calendar.YEAR)){
                                    limitDays=edTime.get(Calendar.DAY_OF_YEAR);
                                }else if(stTime.get(Calendar.YEAR)<edTime.get(Calendar.YEAR)){
                                    limitDays=(365-stTime.get(Calendar.DAY_OF_YEAR))+
                                            edTime.get(Calendar.DAY_OF_YEAR);
                                }else{ limitDays=366;}

                                for(int k=0, inc=1;
                                    k<limitDays; k+=inc){
                                    if(stTime.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY){

                                        Calendar hrIni = (Calendar)stTime.clone();
                                        Calendar auxIni= Calendar.getInstance();
                                        auxIni.setTime(listHVW.get(j).getTimeIni());
                                        hrIni.set(Calendar.HOUR_OF_DAY, auxIni.get(Calendar.HOUR_OF_DAY));
                                        hrIni.set(Calendar.MINUTE,auxIni.get(Calendar.MINUTE));

                                        Calendar hrEnd =(Calendar)stTime.clone();
                                        Calendar auxEnd=Calendar.getInstance();
                                        auxEnd.setTime(listHVW.get(j).getTimeEnd());
                                        hrEnd.set(Calendar.HOUR_OF_DAY, auxEnd.get(Calendar.HOUR_OF_DAY));
                                        hrEnd.set(Calendar.MINUTE, auxEnd.get(Calendar.MINUTE));



                                        boolean paseHV=true;
                                        for(int m=0; m<listUT.size();m++) {
                                            Calendar auxDayTime = Calendar.getInstance();
                                            auxDayTime.setTime(listUT.get(m).getDayTime());

                                            if(hrIni.get(Calendar.DAY_OF_YEAR)==auxDayTime.get(Calendar.DAY_OF_YEAR)
                                                    && listUT.get(m).getType().substring(0,3).equals("Exa")){
                                                paseHV=false; m=listUT.size();
                                            }
                                        }

                                        if( hrIni.get(Calendar.MONTH)==newMonth-1 && paseHV){
                                            event = new WeekViewEvent(1, listHV.get(i).getTitulo()
                                                    +"\n"+listHVW.get(j).getAula()
                                                    +"\n"+listHVW.get(j).getTimeIniToText()
                                                    +" - "+listHVW.get(j).getTimeEndToText()
                                                    , hrIni, hrEnd);
                                            event.setColor(listHV.get(i).getColor());
                                            events.add(event);
                                        }

                                        inc=7;
                                        stTime.add(Calendar.DAY_OF_MONTH,7);
                                    }
                                    else
                                    {
                                        stTime.add(Calendar.DAY_OF_MONTH,1);
                                    }
                                }
                        }
                        else   if(listHVW.get(j).getWeekDay().equals("Mie")){

                            Calendar stTime=Calendar.getInstance(); Calendar edTime=Calendar.getInstance();
                            stTime.setTime(listHV.get(i).getCalIni()); edTime.setTime(listHV.get(i).getCalEnd());


                                int limitDays;
                                if(stTime.get(Calendar.YEAR)==edTime.get(Calendar.YEAR)){
                                    limitDays=edTime.get(Calendar.DAY_OF_YEAR);
                                }else if(stTime.get(Calendar.YEAR)<edTime.get(Calendar.YEAR)){
                                    limitDays=(365-stTime.get(Calendar.DAY_OF_YEAR))+
                                            edTime.get(Calendar.DAY_OF_YEAR);
                                }else{ limitDays=366;}

                                for(int k=0, inc=1;
                                    k<limitDays; k+=inc){
                                    if(stTime.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY){

                                        Calendar hrIni = (Calendar)stTime.clone();
                                        Calendar auxIni= Calendar.getInstance();
                                        auxIni.setTime(listHVW.get(j).getTimeIni());
                                        hrIni.set(Calendar.HOUR_OF_DAY, auxIni.get(Calendar.HOUR_OF_DAY));
                                        hrIni.set(Calendar.MINUTE,auxIni.get(Calendar.MINUTE));

                                        Calendar hrEnd =(Calendar)stTime.clone();
                                        Calendar auxEnd=Calendar.getInstance();
                                        auxEnd.setTime(listHVW.get(j).getTimeEnd());
                                        hrEnd.set(Calendar.HOUR_OF_DAY, auxEnd.get(Calendar.HOUR_OF_DAY));
                                        hrEnd.set(Calendar.MINUTE, auxEnd.get(Calendar.MINUTE));


                                        boolean paseHV=true;
                                        for(int m=0; m<listUT.size();m++) {
                                            Calendar auxDayTime = Calendar.getInstance();
                                            auxDayTime.setTime(listUT.get(m).getDayTime());

                                            if(hrIni.get(Calendar.DAY_OF_YEAR)==auxDayTime.get(Calendar.DAY_OF_YEAR)
                                                    && listUT.get(m).getType().substring(0,3).equals("Exa")){
                                                paseHV=false; m=listUT.size();
                                            }
                                        }

                                        if( hrIni.get(Calendar.MONTH)==newMonth-1 && paseHV){
                                            event = new WeekViewEvent(1,listHV.get(i).getTitulo()
                                                    +"\n"+listHVW.get(j).getAula()
                                                    +"\n"+listHVW.get(j).getTimeIniToText()
                                                    +" - "+listHVW.get(j).getTimeEndToText()
                                                    , hrIni, hrEnd);
                                            event.setColor(listHV.get(i).getColor());
                                            events.add(event);
                                        }

                                        inc=7;
                                        stTime.add(Calendar.DAY_OF_MONTH,7);
                                    }
                                    else
                                    {
                                        stTime.add(Calendar.DAY_OF_MONTH,1);
                                    }
                                }

                        }
                        else   if(listHVW.get(j).getWeekDay().equals("Jue")){

                            Calendar stTime=Calendar.getInstance(); Calendar edTime=Calendar.getInstance();
                            stTime.setTime(listHV.get(i).getCalIni()); edTime.setTime(listHV.get(i).getCalEnd());


                                int limitDays;
                                if(stTime.get(Calendar.YEAR)==edTime.get(Calendar.YEAR)){
                                    limitDays=edTime.get(Calendar.DAY_OF_YEAR);
                                }else if(stTime.get(Calendar.YEAR)<edTime.get(Calendar.YEAR)){
                                    limitDays=(365-stTime.get(Calendar.DAY_OF_YEAR))+
                                            edTime.get(Calendar.DAY_OF_YEAR);
                                }else{ limitDays=366;}

                                for(int k=0, inc=1;
                                    k<limitDays; k+=inc){
                                    if(stTime.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY){

                                        Calendar hrIni = (Calendar)stTime.clone();
                                        Calendar auxIni= Calendar.getInstance();
                                        auxIni.setTime(listHVW.get(j).getTimeIni());
                                        hrIni.set(Calendar.HOUR_OF_DAY, auxIni.get(Calendar.HOUR_OF_DAY));
                                        hrIni.set(Calendar.MINUTE,auxIni.get(Calendar.MINUTE));

                                        Calendar hrEnd =(Calendar)stTime.clone();
                                        Calendar auxEnd=Calendar.getInstance();
                                        auxEnd.setTime(listHVW.get(j).getTimeEnd());
                                        hrEnd.set(Calendar.HOUR_OF_DAY, auxEnd.get(Calendar.HOUR_OF_DAY));
                                        hrEnd.set(Calendar.MINUTE, auxEnd.get(Calendar.MINUTE));


                                        boolean paseHV=true;
                                        for(int m=0; m<listUT.size();m++) {
                                            Calendar auxDayTime = Calendar.getInstance();
                                            auxDayTime.setTime(listUT.get(m).getDayTime());

                                            if(hrIni.get(Calendar.DAY_OF_YEAR)==auxDayTime.get(Calendar.DAY_OF_YEAR)
                                                    && listUT.get(m).getType().substring(0,3).equals("Exa")){
                                                paseHV=false; m=listUT.size();
                                            }
                                        }

                                        if( hrIni.get(Calendar.MONTH)==newMonth-1 && paseHV){
                                            event = new WeekViewEvent(1, listHV.get(i).getTitulo()
                                                    +"\n"+listHVW.get(j).getAula()
                                                    +"\n"+listHVW.get(j).getTimeIniToText()
                                                    +" - "+listHVW.get(j).getTimeEndToText()
                                                    , hrIni, hrEnd);
                                            event.setColor(listHV.get(i).getColor());
                                            events.add(event);
                                        }

                                        inc=7;
                                        stTime.add(Calendar.DAY_OF_MONTH,7);
                                    }
                                    else
                                    {
                                        stTime.add(Calendar.DAY_OF_MONTH,1);
                                    }
                                }

                        }
                        else   if(listHVW.get(j).getWeekDay().equals("Vie")){

                            Calendar stTime=Calendar.getInstance(); Calendar edTime=Calendar.getInstance();
                            stTime.setTime(listHV.get(i).getCalIni()); edTime.setTime(listHV.get(i).getCalEnd());


                                int limitDays;
                                if(stTime.get(Calendar.YEAR)==edTime.get(Calendar.YEAR)){
                                    limitDays=edTime.get(Calendar.DAY_OF_YEAR);
                                }else if(stTime.get(Calendar.YEAR)<edTime.get(Calendar.YEAR)){
                                    limitDays=(365-stTime.get(Calendar.DAY_OF_YEAR))+
                                            edTime.get(Calendar.DAY_OF_YEAR);
                                }else{ limitDays=366;}

                                for(int k=0, inc=1;
                                    k<limitDays; k+=inc){
                                    if(stTime.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY){

                                        Calendar hrIni = (Calendar)stTime.clone();
                                        Calendar auxIni= Calendar.getInstance();
                                        auxIni.setTime(listHVW.get(j).getTimeIni());
                                        hrIni.set(Calendar.HOUR_OF_DAY, auxIni.get(Calendar.HOUR_OF_DAY));
                                        hrIni.set(Calendar.MINUTE,auxIni.get(Calendar.MINUTE));

                                        Calendar hrEnd =(Calendar)stTime.clone();
                                        Calendar auxEnd=Calendar.getInstance();
                                        auxEnd.setTime(listHVW.get(j).getTimeEnd());
                                        hrEnd.set(Calendar.HOUR_OF_DAY, auxEnd.get(Calendar.HOUR_OF_DAY));
                                        hrEnd.set(Calendar.MINUTE, auxEnd.get(Calendar.MINUTE));



                                        boolean paseHV=true;
                                        for(int m=0; m<listUT.size();m++) {
                                            Calendar auxDayTime = Calendar.getInstance();
                                            auxDayTime.setTime(listUT.get(m).getDayTime());

                                            if(hrIni.get(Calendar.DAY_OF_YEAR)==auxDayTime.get(Calendar.DAY_OF_YEAR)
                                                    && listUT.get(m).getType().substring(0,3).equals("Exa")){
                                                paseHV=false; m=listUT.size();
                                            }
                                        }

                                        if(hrIni.get(Calendar.MONTH)==newMonth-1 && paseHV){
                                            event = new WeekViewEvent(1,listHV.get(i).getTitulo()
                                                    +"\n"+listHVW.get(j).getAula()
                                                    +"\n"+listHVW.get(j).getTimeIniToText()
                                                    +" - "+listHVW.get(j).getTimeEndToText()
                                                    , hrIni, hrEnd);
                                            event.setColor(listHV.get(i).getColor());
                                            events.add(event);
                                        }

                                        inc=7;
                                        stTime.add(Calendar.DAY_OF_MONTH,7);
                                    }
                                    else
                                    {
                                        stTime.add(Calendar.DAY_OF_MONTH,1);
                                    }
                                }

                        }



                    }
                }
            }
        }


//MANUALMENTEEE AGREGADO
     /*       Calendar hrIni = Calendar.getInstance();
            hrIni.setTime(listHVW.get(0).getTimeIni());
            hrIni.set(Calendar.DAY_OF_MONTH, 7);
            hrIni.set(Calendar.MONTH, 11);

            Calendar AUX = Calendar.getInstance();
            AUX.setTime(listHVW.get(0).getTimeEnd());

            Calendar hrEnd = (Calendar) hrIni.clone();
            hrEnd.set(Calendar.HOUR_OF_DAY, AUX.get(Calendar.HOUR_OF_DAY));
            hrEnd.set(Calendar.MINUTE, AUX.get(Calendar.MINUTE));

       if(newMonth==hrIni.get(Calendar.MONTH)) {
             event = new WeekViewEvent(1, "ingles", hrIni, hrEnd);
            event.setColor(ContextCompat.getColor(getContext(), R.color.event_color_01));
            events.add(event);
        }*/
//MANUALMENTEEE AGREGADO FINNNNN




        return events;
    }
}
