package com.inpt.jibmaak.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
/** Classe abstraite pour définir un callback en cas de changement de date */
public abstract class DateListener implements View.OnClickListener {
    protected DateFormat format = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.SHORT);
    protected ActivityManageDateDialog manager;
    protected TimePickerDialog timeDialog;
    protected DatePickerDialog dateDialog;
    protected Calendar selected_date;
    protected DateListener(ActivityManageDateDialog manager){
        this.manager = manager;
    }
    /**
     * Methode qui renvoie la date minimale possible
     * @return La date minimale possible selectionnable, null si aucune limite
     */
    public abstract Date getMinDate();

    /**
     * Methode qui renvoie la date maximale possible
     * @return La date maximale possible selectionnable, null si aucune limite
     */
    public abstract Date getMaxDate();

    /**
     * Methode qui renvoie la valeur actuelle
     * @return La date actuelle, null si aucune valeur actuellement
     */
    public abstract Date getCurrentDate();

    /**
     * Methode qui change la valeur actuelle de la date
     * @param date La nouvelle valeur de la date, null si aucune
     */
    public abstract void setCurrentDate(Date date);
    @Override
    public void onClick(View v) {
        Date min_date = getMinDate();
        Date max_date = getMaxDate();
        Date current_date = getCurrentDate();

        dateDialog = new DatePickerDialog(v.getContext());
        Calendar current_calendar = Calendar.getInstance();
        int current_hour = 12,current_minute = 0;
        if (current_date != null){
            current_calendar.setTime(current_date);
            dateDialog.updateDate(current_calendar.get(Calendar.YEAR), current_calendar.get(Calendar.MONTH)
                    , current_calendar.get(Calendar.DAY_OF_MONTH));
            current_hour = current_calendar.get(Calendar.HOUR_OF_DAY);
            current_minute = current_calendar.get(Calendar.MINUTE);
        }
        if (min_date != null)
            dateDialog.getDatePicker().setMinDate(min_date.getTime());
        if (max_date != null)
            dateDialog.getDatePicker().setMaxDate(max_date.getTime());

        timeDialog = new TimePickerDialog(v.getContext(),(view1, hourOfDay, minute) -> {
            selected_date.set(Calendar.HOUR_OF_DAY,hourOfDay);
            selected_date.set(Calendar.MINUTE,minute);
            selected_date.set(Calendar.SECOND,0);
            setCurrentDate(selected_date.getTime());
        },current_hour,current_minute,true);
        timeDialog.setOnCancelListener(dialog -> setCurrentDate(null));
        selected_date = Calendar.getInstance();
        dateDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            selected_date.set(year,month,dayOfMonth);
            timeDialog.show();
        });
        dateDialog.setOnCancelListener(dialog -> setCurrentDate(null));
        manager.addDateDialog(dateDialog,timeDialog);
        dateDialog.show();
    }
}
