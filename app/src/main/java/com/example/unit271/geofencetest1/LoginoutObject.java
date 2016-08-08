package com.example.unit271.geofencetest1;

import java.util.HashMap;

/**
 * Created by unit271 on 7/1/16.
 */
public class LoginoutObject {
    private String Action, Location, Time;

    public LoginoutObject(){

    }

    public void setAction(String Action){
        this.Action = Action;
    }

    public void setLocation(String Location){
        this.Location = Location;
    }

    public void setTime(String Time){
        this.Time = Time;
    }

    //***************************************************************************

    public String getAction(){
        return Action;
    }

    public String getLocation(){
        return Location;
    }

    public String getTime(){
        return Time;
    }

}
