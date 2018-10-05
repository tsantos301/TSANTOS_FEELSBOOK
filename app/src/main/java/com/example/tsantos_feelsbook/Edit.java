package com.example.tsantos_feelsbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Date;

public class Edit extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ArrayList<Feeling> feelingsArray;
    int position;
    Spinner spinner;
    Date newDate;
    String newFeelingName;
    String newComment;
    private static final String FILENAME = "file.sav";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        loadFromFile();

        Intent intent = getIntent();
        //int position = intent.getIntExtra("index",-1);
        position = intent.getIntExtra("index",-1);
        Log.d("new",String.valueOf(position));

        displayRecord(position);

        spinner = (Spinner) findViewById(R.id.emotionSpinner);
        spinner.setOnItemSelectedListener(this);


        Button saveButton = (Button) findViewById(R.id.saveButton2);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(v);

            }

        });
    }


    public void cancel(View view) {
        finish();
    }

    public void delete(View view){
        feelingsArray.remove(position);
        saveInFile();
        finish();

    }

    public void save(View view) {



        EditText temp = (EditText)findViewById(R.id.editComment);
        String newComment = temp.getText().toString();

        EditText temp2 = (EditText)findViewById(R.id.editDate);
        String temp3 = temp2.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");//EEEEE MMMMM yyyy HH:mm:ss.SSSZ

        try{
            newDate = sdf.parse(temp3);
            Log.d("date",String.valueOf(newDate));

            Log.d("deee",String.valueOf(feelingsArray.get(0).getFeelingName()));

            Feeling updateFeeling = new Feeling(newFeelingName,newDate,newComment);
            feelingsArray.set(position,updateFeeling);
            Log.d("new",updateFeeling.getFeelingName());
            //Log.d("new",String.valueOf(updateFeeling.getFeelingDate()));
            //Log.d("new",updateFeeling.getFeelingComment());
            saveInFile();
            finish();
        }
        catch(ParseException a){
            a.printStackTrace();
            Toast toast = Toast.makeText(Edit.this,"Invalid Date Entered!",Toast.LENGTH_LONG);
            View v = toast.getView();
            v.setBackgroundResource(R.drawable.red);
            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 300);
            toast.show();

        }



    }

    public void displayRecord(int position){

        Feeling selectedFeeling = feelingsArray.get(position);
        Log.d("new",String.valueOf(selectedFeeling));
        String oldFeelingName = selectedFeeling.getFeelingName();

        Date oldFeelingDate = selectedFeeling.getFeelingDate();
        String oldFeelingDateString = Feeling.dateToString(oldFeelingDate);

        String oldFeelingComment = selectedFeeling.getFeelingComment();
        Log.d("display",oldFeelingComment);
        setContentView(R.layout.activity_edit);

        TextView commentFeild = (TextView) findViewById(R.id.editComment);
        commentFeild.setText(oldFeelingComment);

        // TO-DO implement default selection of spinner as what was already selected.
        Spinner spinner = (Spinner) findViewById(R.id.emotionSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Feeling_Names, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        if(oldFeelingName.matches("Joy"))spinner.setSelection(0);
        else if(oldFeelingName.matches("Sadness"))spinner.setSelection(1);
        else if(oldFeelingName.matches("Anger"))spinner.setSelection(2);
        else if(oldFeelingName.matches("Surprise"))spinner.setSelection(3);
        else if(oldFeelingName.matches("Fear"))spinner.setSelection(4);
        else if(oldFeelingName.matches("Love"))spinner.setSelection(5);


        TextView date = (TextView) findViewById(R.id.editDate);
        date.setText(oldFeelingDateString);




    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
       newFeelingName = (String)parent.getItemAtPosition(pos);

       //Log.d("deee",newFeelingName);


    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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

    protected void onResume() {
        super.onResume();
        loadFromFile();
    }

    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
       loadFromFile();
    }
}