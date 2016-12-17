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
    @PropertyName("LastSignInCompetition")
    private String LastSignInCompetition;
    @PropertyName("LastSignInRobotics")
    private String LastSignInRobotics;
    @PropertyName("LastSignInFM")
    private String LastSignInFM;
    @PropertyName("TotalCompetition")
    private int TotalCompetition;
    @PropertyName("TotalRobotics")
    private int TotalRobotics;
    @PropertyName("TotalFM")
    private int TotalFM;

    public PersonObject(){

    }

    public void setPersonName(String personName){
        this.personName = personName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setLastSignInCompetition(String lastSignedInCompetition){
        this.LastSignInCompetition = lastSignedInCompetition;
    }

    public void setLastSignInRobotics(String lastSignedInRobotics){
        this.LastSignInRobotics = lastSignedInRobotics;
    }

    public void setLastSignInFM(String lastSignedInFM){
        this.LastSignInFM = lastSignedInFM;
    }

    public void setTotalCompetition(int TotalCompetition){
        this.TotalCompetition = 0;
        this.TotalCompetition = TotalCompetition;
    }

    public void setTotalRobotics(int TotalRobotics){
        this.TotalRobotics = 0;
        this.TotalRobotics = TotalRobotics;
    }

    public void setTotalFM(int TotalFM){
        this.TotalFM = 0;
        this.TotalFM = TotalFM;
    }

    //**********************************************************************************************

    public String getPersonName(){
        return personName;
    }

    public String getPassword(){
        return password;
    }

    public String getLastSignInCompetition(){
        return LastSignInCompetition;
    }

    public String getLastSignInRobotics(){
        return LastSignInRobotics;
    }

    public String getLastSignInFM(){
        return LastSignInFM;
    }

    public int getTotalCompetition(){
        return TotalCompetition;
    }

    public int getTotalRobotics(){
        return TotalRobotics;
    }

    public int getTotalFM(){
        return TotalFM;
    }
}
