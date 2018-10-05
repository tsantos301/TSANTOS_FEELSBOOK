package com.example.tsantos_feelsbook;

import android.support.v7.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Date;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity {

    int[] IMAGES ={R.drawable.joy, R.drawable.sad,R.drawable.anger,R.drawable.surprised,R.drawable.fear,R.drawable.love};
    String[] NAMES = {"Joy", "Sadness", "Anger", "Surprise", "Fear", "Love"};
    public static ArrayList<Feeling> feelingsArray = new ArrayList<Feeling>(); // Feelings array is just a bunch of feelings objects.
    public static String currentFeeling;
    private static final String FILENAME = "file.sav";

    //Implementing Logo in title Bar
    //    ActionBar actionBar = getSupportActionBar();
    //    getActionBar().setIcon(R.mipmap.heart_launcher_round);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the list view
        ListView listView = (ListView)findViewById(R.id.listView);

        //Using custom adapter to use Images in the list view
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Date date = new Date();
                currentFeeling = NAMES[position];
                Feeling feeling = new Feeling(currentFeeling,date);
                feelingsArray.add(feeling);
                saveInFile();

                // ~~~~~~~~~~~~~~~Kept for future debugging purposes~~~~~~~~~~~~~~~~~~~~~~~~~~~
                //                for (Feeling each : feelingsArray){
                //                    Log.i("Tyler", each.getFeelingName());
                //                    Log.i("tyler", String.valueOf(each.getFeelingDate()));
                //                    Log.i("tyler", String.valueOf(each.getFeelingComment()));
                //                }

                //One-click confirmation for the user via a toast.
                Toast.makeText(MainActivity.this,"Your Emotion has been saved.",Toast.LENGTH_LONG).show();

            }
        });

        //Long-Click option to allow the user to add the optional comment.
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                currentFeeling=NAMES[position];
                comment(view,position);//pass view v just created into it.

                return true;
            }
        });


    }

    //This is ran right after the long-press activity is run...use startActivityForResult
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {

                String comment = data.getStringExtra("comment");
                Date date = new Date();
                Feeling feeling_with_comment = new Feeling(currentFeeling,date,comment);
                feelingsArray.add(feeling_with_comment);
                saveInFile();
                loadFromFile();

                // ~~~~~~~~~~~~~~~Kept for future debugging purposes~~~~~~~~~~~~~~~~~~~~~~~~~~~
                //                for (Feeling each : feelingsArray){
                //                    Log.i("long", each.getFeelingName());
                //                    Log.i("long", String.valueOf(each.getFeelingDate()));
                //                    Log.i("long", each.getFeelingComment());
                //                }
            }
        }
    }


    public void menu(View view){

        final View view1 = view;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] choices = {"Statistics","History"};

        // Use the Builder class for convenient dialog construction
        builder.setIcon(R.drawable.menu);
        builder.setTitle(R.string.Menu)
                .setItems(choices, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        if(which==0){
                           statistics(view1);
                        }
                        else{
                            history(view1);
                        }
                    }
        });

        // Create the AlertDialog object and return it
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void statistics(View view) {
        /*
        The Intent constructor takes two parameters:
        A Context as its first parameter (this is used because the Activity class is a subclass of Context)
        The Class of the app component to which the system should deliver the Intent (in this case, the activity that should be started).
         */
        Intent statIntent = new Intent(this, Statistics.class);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",(Serializable)feelingsArray); //Used for passing the arrayList between activities
        statIntent.putExtra("BUNDLE",args);
        startActivity(statIntent);

    }

    private void history(View view) {

        Intent histIntent = new Intent(this, History.class);
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",(Serializable)feelingsArray);
        histIntent.putExtra("BUNDLE",args);
        startActivity(histIntent);

    }

    private void comment(View view, int position) {

        Intent intent = new Intent(this, DisplayComment.class); //An intent is an abstract description of an operation to be performed. It can be used with startActivity to launch an Activity
        startActivityForResult(intent,1);
    }

    private void getSelection(ListView listView, int position){
        String selectedFromList = (String)(listView.getItemAtPosition(position));
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void onStart() {
        super.onStart();
        loadFromFile();
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return IMAGES.length;//return the size of the data
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.customlayout,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            TextView textView_name = (TextView) view.findViewById(R.id.textView_name);
            imageView.setImageResource(IMAGES[i]);
            textView_name.setText(NAMES[i]);

            return view;
        }
    }


}
