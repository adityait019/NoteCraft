package com.example.notecraft.models;

import static com.example.notecraft.models.DateExtraction.*;
//import static com.example.notecraft.models.DateExtraction.generateDateString;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.sql.Date;

public class Note implements Serializable {
    private String description;
    private long timeStamp;
    private String id;

    public Note(String description, String id,long timeStamp) {
        this.id = id;
        this.description = description;
        this.timeStamp=timeStamp;
    }

    public Note() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getFormattedTime() {
        return DateExtraction.generateDateString(timeStamp);
    }
}
