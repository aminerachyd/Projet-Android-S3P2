package com.inpt.jibmaak.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

public interface ManageDateDialogActivity {
    void addDateDialog(DatePickerDialog dateDialog, TimePickerDialog timeDialog);
    void dismissDateDialog();
}
