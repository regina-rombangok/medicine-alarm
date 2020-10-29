package com.example.maiajam.medcinealram.data.model;

import java.util.Date;

/**
 * Created by maiAjam on 9/5/2017.
 */

public class Medcine {


    int medcineId ;
    String MedcineDesc ;
    String MedcindeName ;
    int NoTime;
    String MedFirstAlarm ;

    int MedDose ;
    Date MedStartDate ;



   public Medcine()
    {
    }

    public Medcine(String Med_name,String Med_Desc,String firstAlarm,Date startDate ,int noTime,int dose)
    {

        this.MedcindeName = Med_name ;
        this.MedcineDesc = Med_Desc ;
      this.MedDose = dose ;
        this.MedFirstAlarm = firstAlarm ;
        this.MedStartDate = startDate ;
        this.NoTime = noTime ;


    }

    public Medcine(int id,String Med_name,String Med_Desc,String firstAlarm,Date startDate ,int noTime,int dose)
    {

        this.medcineId = id ;
        this.MedcindeName = Med_name ;
        this.MedcineDesc = Med_Desc ;
        this.MedDose = dose ;
        this.MedFirstAlarm = firstAlarm ;
        this.MedStartDate = startDate ;
        this.NoTime = noTime ;


    }

    public int  getMedcineId()
    {
        return  medcineId ;
    }



    public void setMedcindeName(String MedName)
    {
        MedcindeName = MedName ;
    }

    public String getMedcindeName()
    {
        return this.MedcindeName;
    }


    public void setMedcineDesc(String MedDesc)
    {
        MedcineDesc = MedDesc ;
    }

    public String getMedcineDesc()
    {
        return this.MedcineDesc;
    }




    public String getFirstAlarm()
   {
      return this.MedFirstAlarm ;
   }

   public void setMedFirstAlarm(String firstAlarm)
   {
       this.MedFirstAlarm = firstAlarm ;
   }

   public void setStartDate(Date startDate )
   {
       this.MedStartDate = startDate ;
   }

   public Date getStartDate()
   {
       return this.MedStartDate ;
   }

   //
    public void setNumber(int n)
    {
        NoTime =n ;
    }

    public int getNoTime()
    {
        return this.NoTime;
    }
//

    public int getMedDose()
    {
       return this.MedDose;
    }

    public void setMedDose(int Meddose)
    {
        this.MedDose = Meddose ;
    }

}

