package com.example.tsantos_feelsbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class Statistics extends AppCompatActivity {

    ArrayList<Feeling> feelings;
    int joyCount,sadnessCount,angerCount,surpriseCount,fearCount,loveCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        Intent histIntent = getIntent();
        Bundle args = histIntent.getBundleExtra("BUNDLE");
        ArrayList<Feeling> feelingsArray = (ArrayList<Feeling>) args.getSerializable("ARRAYLIST");
        getStats(feelingsArray);
    }

    public void getStats(ArrayList<Feeling> feelings){
        for(Feeling each:feelings){

            if(each.getFeelingName().matches("Joy"))joyCount++;
            else if(each.getFeelingName().matches("Sadness"))sadnessCount++;
            else if(each.getFeelingName().matches("Anger"))angerCount++;
            else if(each.getFeelingName().matches("Surprise"))surpriseCount++;
            else if(each.getFeelingName().matches("Fear"))fearCount++;
            else if(each.getFeelingName().matches("Love"))loveCount++;


        }

        setContentView(R.layout.activity_statistics);
        TextView stats = (TextView) findViewById(R.id.histroryText);
        stats.setText("Joy Count: "+ joyCount +"\n"+
                          "Sadness Count: "+ sadnessCount +"\n"+
                          "Anger Count: "+ angerCount +"\n"+
                          "Surprise Count: "+ surpriseCount +"\n"+
                          "Fear Count: "+ fearCount +"\n"+
                          "Love Count: "+ loveCount +"\n");
    }
}


