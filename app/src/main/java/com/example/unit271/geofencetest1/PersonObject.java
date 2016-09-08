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

    public PersonObject(){

    }

    public void setPersonName(String personName){
        this.personName = personName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    //**********************************************************************************************

    public String getPersonName(){
        return personName;
    }

    public String getPassword(){
        return password;
    }
}
