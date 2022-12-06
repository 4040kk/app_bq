package com.example.text;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BeDoneDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME="bedone";
    public static final String CONTENT="content";
    public static final String ID="_id";
    public static final String TIME="time";
   // public static final String CLASS="class";


    public BeDoneDB (Context context){
        super(context,"BEDONE",null,1);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL ("CREATE TABLE "+TABLE_NAME+"("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +CONTENT+" TEXT NOT NULL,"
              //  +CLASS+" TEXT NOT NULL,"
                +TIME+" TEXT NOT NULL)"
        );

    }

    @Override
    public void onUpgrade (SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
