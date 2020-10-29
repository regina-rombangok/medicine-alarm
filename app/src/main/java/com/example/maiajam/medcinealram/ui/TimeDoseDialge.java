package com.example.maiajam.medcinealram.ui;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.example.maiajam.medcinealram.R;

/**
 * Created by maiAjam on 9/10/2017.
 */

public class TimeDoseDialge extends DialogFragment implements View.OnClickListener {


    Button AddB, cancelB;
    AlarmDose_value value;
    int noTime, Hour, min, a, dose;
    TimePicker timePicker;
    NumberPicker DosePicker;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.timepicker, container);

        timePicker = (TimePicker) view.findViewById(R.id.alarmPicker);
        DosePicker = (NumberPicker) view.findViewById(R.id.Dose);
        AddB = (Button) view.findViewById(R.id.TimePicker_B_Add);
        cancelB = (Button) view.findViewById(R.id.TimePicker_B_Cancel);

        AddB.setOnClickListener(this);
        cancelB.setOnClickListener(this);

        String alarmNo = getArguments().getString("AlarmNo");

        DosePicker.setMinValue(1);
        DosePicker.setMaxValue(10);
        DosePicker.setValue(1);

        return view;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        try {

            value = (AlarmDose_value) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        if (v == AddB) {
            getSelectedTime();
            getSelectedDose();
            value.AlarmSet(Hour, min, a, noTime);
            value.DoseSet(dose, noTime);
            dismiss();
        } else if (v == cancelB) {
            dismiss();
        }

    }

    private void getSelectedDose() {
        dose = DosePicker.getValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getSelectedTime() {
        noTime = getArguments().getInt("noTime", 1);
        min = timePicker.getMinute();
        Hour = timePicker.getHour();
        int a;
        if (Hour > 12) {
            a = 2;
        } else {
            a = 1;
        }
    }



    public interface AlarmDose_value {
        //if a = 1 --- am if a = 2 ---- pm
        public void AlarmSet(int hour, int min, int a, int Notime);

        public void DoseSet(int dose, int noTime);
    }
}
