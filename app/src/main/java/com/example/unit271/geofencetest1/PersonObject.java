package com.example.unit271.geofencetest1;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

/**
 * Created by unit271 on 8/8/16.
 */
@IgnoreExtraProperties
public class PersonObject {
    @PropertyName("Password")
    private String password;
    @Exclude
    private String personName;
    @PropertyName("Status")
    private String Status;
    @PropertyName("StartTime")
    private String StartTime;
    @PropertyName("StartDate")
    private String StartDate;
    @PropertyName("TotalRobotics")
    private double TotalRobotics;
    @PropertyName("TotalOutreach")
    private double TotalOutreach;
    @PropertyName("Type")
    private String Type;
    @PropertyName("Activity")
    private String Activity;

    public PersonObject(){

    }

    public void setPersonName(String personName){
        this.personName = personName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setStatus(String Status){
        this.Status = Status;
    }

    public void setStartTime(String StartTime){
        this.StartTime = StartTime;
    }

    public void setStartDate(String StartDate){
        this.StartDate = StartDate;
    }

    public void setTotalRobotics(double TotalRobotics){
        this.TotalRobotics = TotalRobotics;
    }

    public void setTotalOutreach(double TotalOutreach){
        this.TotalOutreach = TotalOutreach;
    }

    public void setType(String Type){
        this.Type = Type;
    }

    public void setActivity(String Activity){
        this.Activity = Activity;
    }

    //**********************************************************************************************

    public String getPersonName(){
        return personName;
    }

    public String getPassword(){
        return password;
    }

    public String getStatus(){
        return Status;
    }

    public String getStartTime(){
        return StartTime;
    }

    public String getStartDate(){
        return StartDate;
    }

    public double getTotalRobotics(){
        return TotalRobotics;
    }

    public double getTotalOutreach(){
        return TotalOutreach;
    }

    public String getType(){
        return Type;
    }

    public String getActivity(){
        return Activity;
    }
}
