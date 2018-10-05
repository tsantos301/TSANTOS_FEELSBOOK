package com.example.tsantos_feelsbook;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Feeling implements Serializable,Comparable<Feeling> {

    //declaring all of the fields
    private String feelingName;
    private String feelingComment;
    private Date feelingDate;

    //constructor
    Feeling(String feeling, Date date){
       this.feelingName = feeling;
       this.feelingDate = date;
       this.feelingComment = "";

    }

    //constructor with comment
    Feeling(String feeling, Date date,String comment){
        this.feelingName = feeling;
        this.feelingDate = date;
        this.feelingComment = comment;
    }


    //for each attribute that is in your class you should have a getter and setter
    public String getFeelingName()
    {
        return this.feelingName;
    }

    public void setFeelingName(String value)
    {
        this.feelingName = value;
    }

    public Date getFeelingDate()
    {
        return this.feelingDate;
    }

    public void setFeelingDate(Date value)
    {
        this.feelingDate = value;
    }

    public String getFeelingComment()
    {
        return this.feelingComment;
    }

    public void setFeelingComment(String value)
    {
        this.feelingComment = value;
    }

    public static String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String dateString = sdf.format(date);
        return dateString;
    }

    @Override
    public int compareTo(@NonNull Feeling feelingName){return feelingDate.compareTo(feelingName.getFeelingDate());}
}
