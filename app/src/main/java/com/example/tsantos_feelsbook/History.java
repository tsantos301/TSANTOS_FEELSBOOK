package com.example.tsantos_feelsbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class History extends AppCompatActivity {
    ArrayList<String> stringFeelingsArray = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    public ArrayList<Feeling> feelingsArray;
    private static final String FILENAME = "file.sav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadFromFile();

        //        for (Feeling each : feelingsArray){
//            String date  = each.dateToString(each.getFeelingDate());
//            stringFeelingsArray.add(each.getFeelingName()+ " - " + date);
//        }

        for (Feeling each : feelingsArray){
            stringFeelingsArray.add(each.getFeelingName()+ " - " + String.valueOf(each.getFeelingDate()));
        }

        setContentView(R.layout.activity_history);

        ListView lv = (ListView) findViewById(R.id.historyList);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editEmotion(view,position,feelingsArray);
            }
        });


        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                stringFeelingsArray );

        lv.setAdapter(arrayAdapter);

    }

    private void editEmotion(View view, int position, ArrayList<Feeling> feelingsArray){
        Intent editIntent = new Intent(this,Edit.class);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",(Serializable)feelingsArray);
        editIntent.putExtra("BUNDLE",args);
        editIntent.putExtra("index",position);
        startActivityForResult(editIntent,1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                loadFromFile();
                stringFeelingsArray.clear();

                for (Feeling each : feelingsArray){

                    stringFeelingsArray.add(each.getFeelingName()+ " - " + String.valueOf(each.getFeelingDate()));
                }

                arrayAdapter.notifyDataSetChanged();


            }
        }
    }

    public void clear(View view){
        feelingsArray.clear();
        saveInFile();
        finish();
    }

    protected void sort(){
        Collections.sort(this.feelingsArray);
    }

    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        loadFromFile();
        stringFeelingsArray.clear();

        for (Feeling each : feelingsArray){

            stringFeelingsArray.add(each.getFeelingName()+ " - " + String.valueOf(each.getFeelingDate()));
        }
        sort();
        saveInFile();
        arrayAdapter.notifyDataSetChanged();
    }

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson(); //library to save objects
            Type listType = new TypeToken<ArrayList<Feeling>>() {
            }.getType();
            Log.d("LoadFromFile", "Reached");
            feelingsArray = gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            Log.d("eeeMotion List", "Null");
            feelingsArray = new ArrayList<Feeling>();
        }
    }

    //takes a text and date and saves it to our file.
    private void saveInFile() {
        try {
            //creates a file with FILENAME and tells it what it will say in java syntax
            FileOutputStream fos = openFileOutput(FILENAME,
                    Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(feelingsArray, out);

            //important to flush otherwise you will print garbage
            out.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        loadFromFile();
        stringFeelingsArray.clear();

        for (Feeling each : feelingsArray){

            stringFeelingsArray.add(each.getFeelingName()+ " - " + String.valueOf(each.getFeelingDate()));
        }
        sort();
        saveInFile();
        arrayAdapter.notifyDataSetChanged();
    }
}
