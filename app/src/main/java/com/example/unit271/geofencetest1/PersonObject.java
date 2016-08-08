package com.example.unit271.geofencetest1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by unit271 on 8/8/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonObject {
    @JsonProperty("Password")
    private String password;
    @JsonIgnore
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
