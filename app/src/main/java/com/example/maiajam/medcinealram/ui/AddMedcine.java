package com.example.maiajam.medcinealram.ui;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;

import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;

import com.example.maiajam.medcinealram.data.model.Medcine;
import com.example.maiajam.medcinealram.data.sql.Mysql;
import com.example.maiajam.medcinealram.R;
import com.example.maiajam.medcinealram.helper.Global;
import com.example.maiajam.medcinealram.util.AlarmService;
import com.example.maiajam.medcinealram.util.reciver.AlarmReciver;

import java.text.ParseException;
import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import static android.app.AlarmManager.RTC_WAKEUP;
import static com.example.maiajam.medcinealram.helper.Global.Once_Aday;
import static com.example.maiajam.medcinealram.helper.Global.Twice_Aday;
import static java.time.DayOfWeek.MONDAY;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AddMedcine extends AppCompatActivity implements TimeDoseDialge.AlarmDose_value {


    boolean clicked = false;
    Boolean monCheck, sunCheck, satCheck, TusCheck, wenCheck, TharCheck, FriCheck;
    Boolean everDayCheck = true;
    EditText MedName, MedNote, Med_FDose, Med_SDose, Med_ThDose;
    ImageButton Imdrop;
    Spinner Sp_DayTime;
    int NoTime = 1, Dose;
    EditText FirstAlarma;
    EditText startDate;
    EditText SecondAlarm;
    EditText ThirdAlarm;
    EditText MedMember;
    String Med_name, Med_Note, First_Alarm, Second_Al, Third_Alarm, start_Date, Med_member, Med_Dose, FAlaramChosen;
    RadioButton ch_everyday, ch_specificDay;
    RadioGroup days;
    Mysql db;
    int med_Id = 0;
    Arrays days_OfWeek[];
    Calendar c = Calendar.getInstance();
    Calendar c2 = Calendar.getInstance();
    Calendar c3 = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener startFrom = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.YEAR, year);

            SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yy");

            startDate.setText(sf.format(c.getTime()).toString());

        }
    };

    Bundle extra = new Bundle();
    private OneTimeWorkRequest scheduleReq;
    private Data medcineNote;
    private Calendar cal;
    private Calendar calThirdAlarm;

    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medcine);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_activity_add__medcine));

        extra = getIntent().getExtras();
        if (extra != null) {
            med_Id = extra.getInt("med_id", 0);
        }

        initialViews();
        db = new Mysql(getBaseContext());

        if (med_Id == 0) {
            FirstAlarma.setText(new SimpleDateFormat("hh:mm a").format(Calendar.getInstance().getTime()));
            SecondAlarm.setText(new SimpleDateFormat("hh:mm a").format(getSecondeAlaram(NoTime).getTime()));
            ThirdAlarm.setText(new SimpleDateFormat("hh:mm a").format(getThirdAlaram().getTime()));
            startDate.setText(new SimpleDateFormat("M/dd/yyyy").format(Calendar.getInstance().getTime()).toString());

          /*  Med_FDose.setText("1");
            Med_ThDose.setText("2");
            Med_ThDose.setText("3");
            ;*/

        } else {
            Medcine medcine = new Medcine();
            medcine = db.getMedcine(med_Id);
            Date start = medcine.getStartDate();
            int noTime = medcine.getNoTime();
            final String first = medcine.getFirstAlarm().toString();


            MedName.setText(medcine.getMedcindeName().toString());
            MedNote.setText(medcine.getMedcineDesc().toString());
            startDate.setText(new SimpleDateFormat("M/dd/yyyy").format(start));
            FirstAlarma.setText(first);
            Med_FDose.setText(String.valueOf(medcine.getMedDose()));
            final int M_dose = medcine.getMedDose();


            if (noTime == 2) {

                SecondAlarm.setVisibility(View.VISIBLE);
                Med_SDose.setVisibility(View.VISIBLE);
                Med_SDose.setText(String.valueOf(medcine.getMedDose()));
                Calendar cal = Calendar.getInstance(); // creates calendar
                cal.setTime(new Date(medcine.getFirstAlarm())); // sets calendar time/date
                cal.add(Calendar.HOUR_OF_DAY, 12); // adds one hour
                SecondAlarm.setText(cal.getTime().toString());
            } else if (noTime == 3) {
                SecondAlarm.setVisibility(View.VISIBLE);
                Med_SDose.setVisibility(View.VISIBLE);
                Med_SDose.setText(String.valueOf(medcine.getMedDose()));

                ThirdAlarm.setVisibility(View.VISIBLE);
                Med_ThDose.setText(String.valueOf(medcine.getMedDose()));
                Calendar cal = Calendar.getInstance(); // creates calendar
                try {
                    cal.setTime(new SimpleDateFormat("hh:mm a").parse(medcine.getFirstAlarm())); // sets calendar time/date
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                cal.add(Calendar.HOUR_OF_DAY, 8); // adds one hour
                SecondAlarm.setText(cal.getTime().toString());
                Calendar cal2 = Calendar.getInstance(); // creates calendar
                cal2.setTime(cal.getTime()); // sets calendar time/date
                cal2.add(Calendar.HOUR_OF_DAY, 8); // adds one hour
                ThirdAlarm.setText(cal2.getTime().toString());
            }


        }

        FirstAlarma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle T = new Bundle();
                T.putString("AlarmNo", "FirstAlarm");
                T.putInt("noTime", NoTime);
                DialogFragment timeDialoge = new TimeDoseDialge();
                timeDialoge.setArguments(T);
                timeDialoge.show(getFragmentManager(), "");
            }
        });


        SecondAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle T = new Bundle();
                T.putString("AlarmNo", "SecondAlarm");
                T.putInt("noTime", NoTime);
                DialogFragment timeDialoge = new TimeDoseDialge();
                timeDialoge.setArguments(T);
                timeDialoge.show(getFragmentManager(), "");

            }
        });

        ThirdAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle T = new Bundle();
                T.putString("AlarmNo", "ThirdAlarm");
                T.putInt("noTime", NoTime);
                DialogFragment timeDialoge = new TimeDoseDialge();
                timeDialoge.setArguments(T);
                timeDialoge.show(getFragmentManager(), "");

            }
        });

        startDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new DatePickerDialog(AddMedcine.this, startFrom, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                return true;
            }
        });
     /*  startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddMedcine.this, startFrom, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
*/

        Imdrop = (ImageButton) findViewById(R.id.dropDown);

        Imdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clicked) {
                    clicked = false;
                    Imdrop.setImageResource(R.drawable.ic_dropdown);
                    days.setVisibility(View.GONE);
                    ch_specificDay.setVisibility(View.GONE);
                    ch_everyday.setVisibility(View.GONE);

                } else {
                    clicked = true;
                    Imdrop.setImageResource(R.drawable.ic_dropup);
                    days.setVisibility(View.VISIBLE);
                    ch_specificDay.setVisibility(View.VISIBLE);
                    ch_everyday.setVisibility(View.VISIBLE);
                }

            }
        });


        Sp_DayTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                if (item.equals(getString(R.string.oneTime))) {
                    NoTime = 1;
                    alarmsVisibility(Once_Aday);
                } else if (item.equals(getString(R.string.twice))) {
                    NoTime = 2;
                    alarmsVisibility(Twice_Aday);
                    calculateOtherAlarm(Twice_Aday, getFirstSelectedAlarm());
                    SecondAlarm.setText(formatTime(cal.getTime()));

                } else if (item.equals(getString(R.string.three))) {
                    NoTime = 3;
                    alarmsVisibility(3);
                    calculateOtherAlarm(NoTime, getFirstSelectedAlarm());
                    SecondAlarm.setText(formatTime(cal.getTime()));
                    ThirdAlarm.setText(formatTime(calThirdAlarm.getTime()));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        days.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.ch_everyDay:
                        everDayCheck = true;
                        break;
                    case R.id.ch_daysWeek:
                        everDayCheck = false;
                        openPopUpForDaysOfWeek();
                        break;
                    default:
                        break;
                }
            }
        });


    }

    private String formatTime(Date time) {
        return new SimpleDateFormat("hh:mm a").format(time);
    }

    private void alarmsVisibility(int noTime) {

        SecondAlarm.setVisibility(View.VISIBLE);
        Med_SDose.setVisibility(View.VISIBLE);
        if (noTime == Twice_Aday) {
            ThirdAlarm.setVisibility(View.GONE);
            Med_ThDose.setVisibility(View.GONE);
        } else if (noTime == Global.Three_Times) {
            ThirdAlarm.setVisibility(View.VISIBLE);
            Med_ThDose.setVisibility(View.VISIBLE);
        } else {
            ThirdAlarm.setVisibility(View.GONE);
            Med_ThDose.setVisibility(View.GONE);
            SecondAlarm.setVisibility(View.GONE);
            Med_SDose.setVisibility(View.GONE);
        }
    }

    private void calculateOtherAlarm(int noTime, Date firstSelectedAlarm) {

        cal = Calendar.getInstance();
        cal.setTime(getFirstSelectedAlarm());
        if (noTime == Twice_Aday) {
            cal.add(Calendar.HOUR_OF_DAY, 12); // add 12 hour
        } else if (noTime == Global.Three_Times) {
            calThirdAlarm = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY, 8); // add 12 hour
            calThirdAlarm.add(Calendar.HOUR_OF_DAY, 16);
        }

    }

    private Date getFirstSelectedAlarm() {
        Date datefirstAlarm = new Date();
        try {
            datefirstAlarm = new java.text.SimpleDateFormat().parse(FirstAlarma.getText().toString())
            ;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return datefirstAlarm;
    }

    private void initialViews() {

        MedName = (EditText) findViewById(R.id.ed_medName);
        MedNote = (EditText) findViewById(R.id.ed_medNote);
        FirstAlarma = (EditText) findViewById(R.id.ed_FirstAlarm);
        SecondAlarm = (EditText) findViewById(R.id.ed_secondAlarm);
        ThirdAlarm = (EditText) findViewById(R.id.ed_thirdAlarm);

        ch_everyday = (RadioButton) findViewById(R.id.ch_everyDay);
        ch_specificDay = (RadioButton) findViewById(R.id.ch_daysWeek);
        days = (RadioGroup) findViewById(R.id.Days);

        startDate = (EditText) findViewById(R.id.startPiker_Time);

        Med_SDose = (EditText) findViewById(R.id.ed_SecondDose);
        Med_FDose = (EditText) findViewById(R.id.ed_Dose);
        Med_ThDose = (EditText) findViewById(R.id.ed_ThirdDose);

        Sp_DayTime = (Spinner) findViewById(R.id.Sp_DayTime);
    }

    private void openPopUpForDaysOfWeek() {
        final PopupMenu popupMenu = new PopupMenu(getBaseContext(), ch_specificDay);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.weekdays, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.sun:
                        sunCheck = true;
                    case R.id.Mon:
                        monCheck = true;
                    case R.id.Tus:
                        TusCheck = true;
                    case R.id.Thu:
                        TharCheck = true;
                    case R.id.Wed:
                        wenCheck = true;
                    case R.id.Sat:
                        satCheck = true;
                    case R.id.fri:
                        FriCheck = true;
                    default:
                        return false;

                }

            }
        });
        popupMenu.show();
    }

    private Calendar getSecondeAlaram(int noTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(String.valueOf(cal.getTime())));
        if (noTime == 3)
            cal.add(Calendar.HOUR_OF_DAY, 8);
        else
            cal.add(Calendar.HOUR_OF_DAY, 12);
        return cal;
    }

    private Calendar getThirdAlaram() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(String.valueOf(cal.getTime())));
        cal.add(Calendar.HOUR_OF_DAY, 16);
        return cal;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addmedmenu, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        extra = getIntent().getExtras();
        if (extra != null) {
            med_Id = extra.getInt("med_id", 0);
        }
        if (id == R.id.addmed) {
            if (validateMedicineInfo()) {

                Dose = Integer.parseInt(Med_FDose.getText().toString());
                if (med_Id == 0) {
                    Date startdate = null;
                    try {
                        startdate = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).parse(start_Date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date _firstAlarm = null;
                    try {
                        _firstAlarm = new SimpleDateFormat("hh:mm a", Locale.getDefault()).parse(First_Alarm);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    startAlarm(NoTime, Med_Note, Med_name, Dose, _firstAlarm, startdate);

                    AddMed(Med_name, Med_Note, First_Alarm, Dose, startdate, NoTime);

                    Toast.makeText(getBaseContext(), getResources().getString(R.string.doneAdded).toString(), Toast.LENGTH_LONG).show();
                } else {
                    Date startdate = null;
                    try {
                        startdate = (Date) new SimpleDateFormat("MM/dd", Locale.getDefault()).parse(start_Date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date _firstAlarm = null;
                    try {
                        _firstAlarm = (Date) new SimpleDateFormat("hh:mm a").parse(First_Alarm);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    startAlarm(NoTime, Med_Note, Med_name, Dose, _firstAlarm, startdate);

                    upDateMed(Med_name, Med_Note, First_Alarm, Dose, startdate, NoTime);
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.doneUpdate), Toast.LENGTH_LONG).show();
                }
            }
            finish();
        }
        return true;

    }

    private boolean validateMedicineInfo() {
        Med_name = MedName.getText().toString();
        Med_Note = MedNote.getText().toString();
        First_Alarm = FirstAlarma.getText().toString();
        start_Date = startDate.getText().toString();
        Med_Dose = Med_FDose.getText().toString();


        if (TextUtils.isEmpty(Med_Note)) {
            Toast.makeText(getBaseContext(), getString(R.string.Toast_enterName), Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(Med_name)) {
            Toast.makeText(getBaseContext(), getString(R.string.Toast_enternote), Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(Med_FDose.getText().toString())) {
            Toast.makeText(getBaseContext(), getString(R.string.Toast_enterDose), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void upDateMed(String med_name, String med_note, String first_alarm, int dose, Date startdate, int noTime) {

        Medcine m = new Medcine(med_name, med_note, first_alarm, startdate, noTime, dose);
        db.UpdateMed(m);
        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startAlarm(int noTime, String med_note, String med_name, int med_dose, Date first_alarm, Date start_date) {

        Calendar c_startDate = Calendar.getInstance();
        Calendar c_firstAlarm = Calendar.getInstance();

        Calendar calSunChecked = Calendar.getInstance();
        Calendar C2 = Calendar.getInstance();
        Calendar C3 = Calendar.getInstance();
        Calendar C4 = Calendar.getInstance();
        Calendar C5 = Calendar.getInstance();
        Calendar C6 = Calendar.getInstance();
        Calendar C7 = Calendar.getInstance();
        Calendar cEverDay = Calendar.getInstance();

        c_startDate.setTime(start_date);
        c_firstAlarm.setTime(first_alarm);

        medcineNote = new Data.Builder().putString("med_not", med_note)
                .putString("med_mame", med_name)
                .putString("med_dose", String.valueOf(med_dose))
                .build();

        if (everDayCheck) {
            cEverDay.clear();
            cEverDay.set(Calendar.YEAR, c_startDate.get(Calendar.YEAR));
            cEverDay.set(Calendar.MONTH, c_startDate.get(Calendar.MONTH));
            cEverDay.set(Calendar.DAY_OF_MONTH, c_startDate.get(Calendar.DAY_OF_MONTH));
            cEverDay.set(Calendar.HOUR_OF_DAY, c_firstAlarm.get(Calendar.HOUR_OF_DAY));
            cEverDay.set(Calendar.MINUTE, c_firstAlarm.get(Calendar.MINUTE));

            setRepeatedAlarm(cEverDay, noTime);
        } else if (sunCheck) {
            calSunChecked.clear();
            calSunChecked.set(Calendar.YEAR, c_startDate.get(Calendar.YEAR));
            calSunChecked.set(Calendar.MONTH, c_startDate.get(Calendar.MONTH));
            calSunChecked.set(Calendar.DAY_OF_MONTH, c_startDate.get(Calendar.DAY_OF_MONTH));
            calSunChecked.set(Calendar.HOUR_OF_DAY, c_startDate.get(Calendar.HOUR_OF_DAY));
            calSunChecked.set(Calendar.MINUTE, c_startDate.get(Calendar.MINUTE));
            calSunChecked.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

            setRepeatedAlarm(calSunChecked, noTime);

        } else if (monCheck) {
            C2.clear();
            C2.set(Calendar.YEAR, c_startDate.get(Calendar.YEAR));
            C2.set(Calendar.MONTH, c_startDate.get(Calendar.MONTH));
            C2.set(Calendar.DAY_OF_MONTH, c_startDate.get(Calendar.DAY_OF_MONTH));
            C2.set(Calendar.HOUR_OF_DAY, c_startDate.get(Calendar.HOUR_OF_DAY));
            C2.set(Calendar.MINUTE, c_startDate.get(Calendar.MINUTE));
            C2.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            setRepeatedAlarm(C2, noTime);

        } else if (TusCheck) {
            C3.clear();
            C3.set(Calendar.YEAR, c_startDate.get(Calendar.YEAR));
            C3.set(Calendar.MONTH, c_startDate.get(Calendar.MONTH));
            C3.set(Calendar.DAY_OF_MONTH, c_startDate.get(Calendar.DAY_OF_MONTH));
            C3.set(Calendar.HOUR_OF_DAY, c_startDate.get(Calendar.HOUR_OF_DAY));
            C3.set(Calendar.MINUTE, c_startDate.get(Calendar.MINUTE));
            C3.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);

            setRepeatedAlarm(C3, noTime);

        } else if (wenCheck) {
            C4.clear();
            C4.set(Calendar.YEAR, c_startDate.get(Calendar.YEAR));
            C4.set(Calendar.MONTH, c_startDate.get(Calendar.MONTH));
            C4.set(Calendar.DAY_OF_MONTH, c_startDate.get(Calendar.DAY_OF_MONTH));
            C4.set(Calendar.HOUR_OF_DAY, c_startDate.get(Calendar.HOUR_OF_DAY));
            C4.set(Calendar.MINUTE, c_startDate.get(Calendar.MINUTE));
            C4.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            setRepeatedAlarm(C4, noTime);

        } else if (TharCheck) {
            C5.clear();
            C5.set(Calendar.YEAR, c_startDate.get(Calendar.YEAR));
            C5.set(Calendar.MONTH, c_startDate.get(Calendar.MONTH));
            C5.set(Calendar.DAY_OF_MONTH, c_startDate.get(Calendar.DAY_OF_MONTH));
            C5.set(Calendar.HOUR_OF_DAY, c_startDate.get(Calendar.HOUR_OF_DAY));
            C5.set(Calendar.MINUTE, c_startDate.get(Calendar.MINUTE));
            C5.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);

            setRepeatedAlarm(C5, noTime);
        } else if (FriCheck) {
            C6.clear();
            C6.set(Calendar.YEAR, c_startDate.get(Calendar.YEAR));
            C6.set(Calendar.MONTH, c_startDate.get(Calendar.MONTH));
            C6.set(Calendar.DAY_OF_MONTH, c_startDate.get(Calendar.DAY_OF_MONTH));
            C6.set(Calendar.HOUR_OF_DAY, c_startDate.get(Calendar.HOUR_OF_DAY));
            C6.set(Calendar.MINUTE, c_startDate.get(Calendar.MINUTE));
            C6.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

            setRepeatedAlarm(C6, noTime);
        } else if (satCheck) {

            C7.clear();
            C7.set(Calendar.YEAR, c_startDate.get(Calendar.YEAR));
            C7.set(Calendar.MONTH, c_startDate.get(Calendar.MONTH));
            C7.set(Calendar.DAY_OF_MONTH, c_startDate.get(Calendar.DAY_OF_MONTH));
            C7.set(Calendar.HOUR_OF_DAY, c_startDate.get(Calendar.HOUR_OF_DAY));
            C7.set(Calendar.MINUTE, c_startDate.get(Calendar.MINUTE));


            setRepeatedAlarm(C7, noTime);
        }


    }

    private void setRepeatedAlarm(Calendar time, int noRepeatedTime) {
        if (noRepeatedTime == 1) {
            // Check if the Calendar time is in the past
            if (time.getTimeInMillis() < System.currentTimeMillis()) {
                time.add(Calendar.DAY_OF_YEAR, 1); // it will tell to run to next day
            }
            schuldeAlaram((long) time.getTimeInMillis(), noRepeatedTime);
        } else if (noRepeatedTime == 2) {
            schuldeAlaram((long) time.getTimeInMillis(), noRepeatedTime);
        } else if (noRepeatedTime == 3) {
            schuldeAlaram((long) time.getTimeInMillis(), noRepeatedTime);
        }

    }

    private void schuldeAlaram(long IntilaDelay, int noTime) {
        Intent i = new Intent(this, AlarmService.class);
        i.putExtra("noTime", noTime);
        i.putExtra("IntilaDelay", IntilaDelay);
        i.putExtra("Med_name", Med_name);
        i.putExtra("Med_Dos", Med_Dose);
        i.putExtra("Med_Note", Med_Note);
        i.putExtra("med_Id", med_Id);
        startService(i);
    }

    private void repeatManyTimesInWeek() {
        getTheStartDay();
        if (everDayCheck) {

        } else {
            switch ((getTheStartDay())) {
                case "Wed":
                    break;
                case "Mon":

            }

        }

    }

    private String getTheStartDay() {
        Date startdate = null;
        try {
            startdate = (Date) new SimpleDateFormat("MM/dd", Locale.getDefault()).parse(start_Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
        return simpleDateFormat.format(startdate);

    }

    private void AddMed(String med_name, String med_note, String FirstAlarm, int dose, Date Startddate, int noTime) {
        Medcine NewMed = new Medcine(med_name, med_note, FirstAlarm, Startddate, noTime, dose);
        Mysql MY_db = new Mysql(getBaseContext());

        MY_db.AddMedcine(NewMed);
        MY_db.close();

    }

    @Override
    public void AlarmSet(int hour, int min, int am, int noTime) {

        if (noTime == 1) {
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, min);

            SimpleDateFormat sf = new SimpleDateFormat("hh:mm a");
            FAlaramChosen = sf.format(c.getTime()).toString();
            FirstAlarma.setText(FAlaramChosen);
        } else if (noTime == 2) {
            c.set(Calendar.HOUR, hour);
            c2.set(Calendar.HOUR, hour);
            c.set(Calendar.MINUTE, min);
            c2.set(Calendar.MINUTE, min);

            if (am == 1) {
                c.set(Calendar.AM_PM, Calendar.AM);
                c2.set(Calendar.AM_PM, Calendar.PM);
            } else {
                c.set(Calendar.AM_PM, Calendar.PM);
                c2.set(Calendar.AM_PM, Calendar.AM);
            }

            SimpleDateFormat sf = new SimpleDateFormat("hh:mm a");
            FAlaramChosen = sf.format(c.getTime()).toString();
            FirstAlarma.setText(FAlaramChosen);
            SecondAlarm.setText(sf.format(c2.getTime()).toString());


        } else if (noTime == 3) {
            c.set(Calendar.HOUR, hour);
            c2.set(Calendar.HOUR, hour + 8);
            c3.set(Calendar.HOUR, hour + 16);
            c.set(Calendar.MINUTE, min);
            c2.set(Calendar.MINUTE, min);

            if (am == 1) {
                c.set(Calendar.AM_PM, Calendar.AM);
                c2.set(Calendar.AM_PM, Calendar.PM);
                c3.set(Calendar.AM_PM, Calendar.AM);
            } else {
                c.set(Calendar.AM_PM, Calendar.PM);
                c2.set(Calendar.AM_PM, Calendar.AM);
                c3.set(Calendar.AM_PM, Calendar.PM);
            }

            SimpleDateFormat sf = new SimpleDateFormat("hh:mm a");

            FirstAlarma.setText(sf.format(c.getTime()).toString());
            SecondAlarm.setText(sf.format(c2.getTime()).toString());
            ThirdAlarm.setText(sf.format(c3.getTime()).toString());
        }


    }

    @Override
    public void DoseSet(int Dose, int notime) {

        if (notime == 1) {
            Med_FDose.setText(String.valueOf(Dose));
        } else if (notime == 2) {
            Med_SDose.setText(String.valueOf(Dose));
        } else if (notime == 3) {
            Med_ThDose.setText(String.valueOf(Dose));
        }

    }
}
