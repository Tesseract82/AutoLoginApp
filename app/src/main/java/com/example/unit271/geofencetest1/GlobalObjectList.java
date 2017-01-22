package com.example.unit271.geofencetest1;

import android.provider.Settings;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by unit271 on 1/21/17.
 */
public class GlobalObjectList<T> {

    public static final DatabaseReference allPeopleRef = FirebaseDatabase.getInstance().getReference();
    private ArrayList<T> genericList = new ArrayList<>();
    private PersonObject currentPerson = new PersonObject();

    public GlobalObjectList(String childPath, PersonInfoChangedCallback personInfoChangedCallback, Class<T> genericClassType, String teamID){
        listenToFirebase(childPath, personInfoChangedCallback, genericClassType, teamID);
    }

    public void listenToFirebase(String path, final PersonInfoChangedCallback personInfoChangedCallback, final Class<T> typeClass, final String teamID){
        ValueEventListener genericListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot genericDataSnapshot : dataSnapshot.getChildren()){
                    if(genericDataSnapshot.getValue() != null){
                        T genericObject = genericDataSnapshot.getValue(typeClass);
                        if(typeClass == PersonObject.class) {
                            ((PersonObject) genericObject).setPersonName(genericDataSnapshot.getKey());
                            if(genericDataSnapshot.getKey().equals(teamID)){
                                currentPerson = (PersonObject) genericObject;
                            }
                        }
                        genericList.add(genericObject);
                    }
                }
                personInfoChangedCallback.sendLocalBroadcast();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                personInfoChangedCallback.sendLocalBroadcast();
            }
        };

        allPeopleRef.child(path).addValueEventListener(genericListener);

    }

    public interface PersonInfoChangedCallback {
        void sendLocalBroadcast();
    }

    public ArrayList<T> getGenericList(){
        return genericList;
    }
}
