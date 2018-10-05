package com.example.tsantos_feelsbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

public class DisplayComment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaycomment);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                save(v);

            }

        });

    }

    public void cancel(View view){
        finish();
    }

    public void save(View view){
        EditText temp = (EditText)findViewById(R.id.comment);
        String comment = temp.getText().toString();

        Intent mainIntent = new Intent();
        mainIntent.putExtra("comment",comment);
        setResult(RESULT_OK, mainIntent);
        finish();

    }
}
