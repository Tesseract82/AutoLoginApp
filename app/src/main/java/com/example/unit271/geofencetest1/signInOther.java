package com.example.unit271.geofencetest1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class signInOther extends AppCompatActivity {

    public String searchString;
    public ArrayList<String> teamNameList;
    public ArrayList<String> formattedList;
    public ArrayList<String> permanentTeamNameList;
    Firebase dataRef;
    public static String filename = "NumberHolder";
    SharedPreferences teamNumData;
    private ListView teamNameListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_other);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Firebase.setAndroidContext(this);
        setTitle("Make Your Selection");
        teamNumData = getSharedPreferences(filename, 0);
        teamNameList = new ArrayList<String>();
        formattedList = new ArrayList<String>();
        permanentTeamNameList = new ArrayList<String>();
        teamNameList.clear();
        formattedList.clear();
        permanentTeamNameList.clear();
        teamNameListView = (ListView) findViewById(R.id.peopleView);
        dataRef = new Firebase("https://loginapptestcc.firebaseio.com/People");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot personSnapshot: dataSnapshot.getChildren()) {
                    permanentTeamNameList.add(personSnapshot.getKey());
                    teamNameList.add(personSnapshot.getKey());
                }
                dataRef.removeEventListener(this);
                generatePersonList();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void adaptPersonList(){
        TextView searchText = (TextView) findViewById(R.id.searchText);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchString = s.toString();
                teamNameList.clear();
                for(int a = 0; a <= permanentTeamNameList.size() - 1; a++){
                    teamNameList.add(permanentTeamNameList.get(a));
                }
                formatString(searchString);
            }
        });
    }

    public void generatePersonList(){
        teamNameListView.setAdapter(new BaseAdapter() {

            @Override
            public boolean areAllItemsEnabled() {
                return true;
            }

            @Override
            public boolean isEnabled(int position) {
                return true;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return teamNameList.size();
            }

            @Override
            public String getItem(int position) {
                return teamNameList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView ( int position, View convertView, ViewGroup parent){
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if (convertView == null) {
                    convertView = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                }
                TextView personString = (TextView) convertView.findViewById(android.R.id.text1);
                personString.setText(teamNameList.get(position));
                personString.setTextColor(Color.BLACK);

                return convertView;
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        });

        teamNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent signInOther2 = new Intent(getBaseContext(), signInOther2.class);
                signInOther2.putExtra("com.example.unit271.geofencetest1/signInOther", parent.getItemAtPosition(position).toString());
                startActivity(signInOther2);
            }
        });
        adaptPersonList();
    }

    public void formatString(String searchString){
        BaseAdapter teamNameAdapter = (BaseAdapter) teamNameListView.getAdapter();
        for(int a = 0; a <= teamNameList.size() - 1; a++){
            Log.i("FORMATSTRING" + teamNameList.get(a), " " + searchString);
            if(!((teamNameList.get(a)).startsWith(searchString))){
                teamNameList.remove(a);
                a--;
            } else if(searchString.equals("")){
                continue;
            } else {
                continue;
            }
        }
        teamNameAdapter.notifyDataSetChanged();
    }
}
