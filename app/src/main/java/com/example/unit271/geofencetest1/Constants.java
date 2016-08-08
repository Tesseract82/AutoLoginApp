package com.example.unit271.geofencetest1;

import java.util.HashMap;

/**
 * Created by unit271 on 5/12/16.
 */
public class Constants {
    HashMap<String, Integer> dhs = new HashMap<>();
    HashMap<String, Integer> holmes = new HashMap<>();
    HashMap<String, Integer> harper = new HashMap<>();
    HashMap<String, Integer> emerson = new HashMap<>();
    HashMap<String, Integer> daVJrH = new HashMap<>();
    HashMap<String, Integer> daVH = new HashMap<>();

    public void initializeMaps(){
        dhs.put("AStart", 745);
        dhs.put("AEnd", 1535);
        dhs.put("N1", 845);
        dhs.put("N7", 1432);
        dhs.put("N6N7", 1334);
        dhs.put("WedAStart", 855);
        dhs.put("WedAEnd", 1430);
        dhs.put("WedN1", 855);
        dhs.put("WedN7", 1430);
        dhs.put("WedN6N7", 1210);
        dhs.put("ThuAStart", 745);
        dhs.put("ThuAEnd", 1430);
        dhs.put("ThuN1", 855);
        dhs.put("ThuN7", 1210);
        dhs.put("ThuN6N7", 1210);

        daVH.put("AStart", 750);
        daVH.put("AEnd", 1530);
        daVH.put("N1", 859);
        daVH.put("N7", 1432);
        daVH.put("N6N7", 1335);
        daVH.put("WedAStart", 859);
        daVH.put("WedAEnd", 1430);
        daVH.put("WedN1", 859);
        daVH.put("WedN7", 1430);
        daVH.put("WedN6N7", 1208);
        daVH.put("ThuAStart", 750);
        daVH.put("ThuAEnd", 1430);
        daVH.put("ThuN1", 859);
        daVH.put("ThuN7", 1208);
        daVH.put("ThuN6N7", 1208);

        holmes.put("AStart", 808);
        holmes.put("AEnd", 1520);
        holmes.put("N1", 858);
        holmes.put("N7", 1432);
        holmes.put("WedAStart", 928);
        holmes.put("WedAEnd", 1520);
        holmes.put("WedN1", 1013);
        holmes.put("WedN7", 1430);

        harper.put("AStart", 820);
        harper.put("AEnd", 1530);
        harper.put("N1", 911);
        harper.put("N7", 1432);
        harper.put("WedAStart", 940);
        harper.put("WedAEnd", 1530);
        harper.put("WedN1", 1025);
        harper.put("WedN7", 1430);

        emerson.put("AStart", 805);
        emerson.put("AEnd", 1515);
        emerson.put("N1", 900);
        emerson.put("N7", 1432);
        emerson.put("WedAStart", 925);
        emerson.put("WedAEnd", 1515);
        emerson.put("WedN1", 1010);
        emerson.put("WedN7", 1430);

        daVJrH.put("AStart", 805);
        daVJrH.put("AEnd", 1515);
        daVJrH.put("N1", 900);
        daVJrH.put("N7", 1432);
        daVJrH.put("WedAStart", 925);
        daVJrH.put("WedAEnd", 1515);
        daVJrH.put("WedN1", 1010);
        daVJrH.put("WedN7", 1430);
    }

    public HashMap getSchool(String schoolName) {
        switch(schoolName){
            case "Holmes" : return holmes;
            case "DHS" : return dhs;
            case "Harper" : return harper;
            case "Emerson" : return emerson;
            case "DaVinci JrHigh" : return daVJrH;
            case "DaVinci High" : return daVH;
            case "none" : return null;
        }
        return null;
    }
}
