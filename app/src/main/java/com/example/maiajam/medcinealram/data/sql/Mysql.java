package com.example.maiajam.medcinealram.data.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.maiajam.medcinealram.data.model.Medcine;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

/**
 * Created by maiAjam on 9/5/2017.
 */

public class Mysql extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "medcine.db";


    private static final String Key_TableMed = "MedInfo";

    private static final String Key_Id = "id";
    private static final String Key_Med_desc = "MedDesc";
    private static final String Key_MedName = "MedName";
    private static final String Key_MedFirstAlarm = "MedFirstAlarm";
    private static final String Key_MedStartDate = "MedStartDate";
    private static final String Key_NumTime = "NumTime";

    private static final String Key_MedDose = "MedDose";



    private static final String SQL_CREATE_MedInfo =  "CREATE TABLE " + Key_TableMed + " ( " + Key_Id +  " INTEGER PRIMARY KEY," +

            Key_MedName + " TEXT," + Key_Med_desc + " TEXT," + Key_MedStartDate + " TEXT," + Key_MedFirstAlarm + " TEXT," + Key_NumTime + " INTEGER," + Key_MedDose + " INTEGER ) ";




    public Mysql(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

       db.execSQL(SQL_CREATE_MedInfo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

      db.execSQL("DROP TABLE IF EXISTS " + Key_TableMed);

        onCreate(db);
    }



    public void AddMedcine(Medcine medcine)
    {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();

        values.put(Key_MedName,medcine.getMedcindeName());
        values.put(Key_Med_desc,medcine.getMedcineDesc());
        values.put(Key_MedFirstAlarm,medcine.getFirstAlarm());

        values.put(Key_MedDose,medcine.getMedDose());
        values.put(Key_NumTime,medcine.getNoTime());
        values.put(Key_MedStartDate,String.valueOf(medcine.getStartDate()));
        db.insert(Key_TableMed,null,values);

        db.close();
    }




    public Medcine getMedcine(int med_Id)
    {
        String selectQ = "SELECT * FROM MedInfo WHERE id = '" + med_Id + "';";;
        SQLiteDatabase db = this.getReadableDatabase();
        Medcine medcine = new Medcine() ;
        Cursor cursor = db.rawQuery(selectQ,null);

        if (cursor.moveToFirst())
        {
            medcine= new Medcine(cursor.getString(1),cursor.getString(2),cursor.getString(4),new Date(cursor.getString(3)),parseInt(cursor.getString(5)),parseInt(cursor.getString(6)));
        }

        return medcine ;

    }

    public List<Medcine> allMedcine()
    {
        List<Medcine> medcineList = new ArrayList<>();
        String selectQ = "SELECT * FROM " + Key_TableMed ;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQ,null);

        if(cursor != null) {
            if (cursor.moveToFirst()) {

                do {
                    int id = parseInt(cursor.getString(0));
                    String medName = cursor.getString(1);
                    String medDesc = cursor.getString(2);
                    Date startDate = new Date(cursor.getString(3));
                    String firstAlarm = cursor.getString(4);
                    int noTime = parseInt(cursor.getString(5));
                    int Dose = parseInt(cursor.getString(6));

                    Medcine medcine = new Medcine(id, medName, medDesc, firstAlarm, startDate, noTime, Dose);
                    medcineList.add(medcine);
                } while (cursor.moveToNext());
            }

        }
        cursor.close();
        db.close();
        return medcineList ;
    }


    public void delete(int medid) {

        SQLiteDatabase db = this.getWritableDatabase();
        String addQuery = "DELETE from MedInfo  WHERE id == '" + medid + "' ;";

        Log.d(TAG, "DeleteMedcine: " + addQuery);
        db.execSQL(addQuery);
        db.close();
    }

    public void UpdateMed(Medcine medcine) {

        SQLiteDatabase db = this.getWritableDatabase();
        String addQuery = "UPDATE MedInfo SET MedName = '" + medcine.getMedcindeName() +
                "', MedDesc = '" + medcine.getMedcineDesc() +
                "', MedFirstAlarm = '" + String.valueOf(medcine.getFirstAlarm()) +
                "', MedStartDate = '" + String.valueOf(medcine.getStartDate()) +
                "', NumTime = '" + String.valueOf(medcine.getNoTime()) +
                "', MedDose = '" + String.valueOf(medcine.getMedcineDesc())  + ";";
        Log.d(TAG, "updatemedcine: " + addQuery);
        db.execSQL(addQuery);
        db.close();
        
    }

}


