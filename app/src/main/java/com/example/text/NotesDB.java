package com.example.text;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class NotesDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME="notes";
    public static final String CONTENT="content";
    public static final String ID="_id";
    public static final String TIME="time";
    public static final String CLASS="class";
    public static final String PATH="path";
    public static final String IMPORTENT="importent";


    public NotesDB (Context context){
        super(context,"notes",null,1);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
       // Toast.makeText ( mContext,"OK1",Toast.LENGTH_SHORT ).show ();
        db.execSQL ("CREATE TABLE "+TABLE_NAME+"("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +CONTENT+" TEXT NOT NULL,"
                +CLASS+" TEXT NOT NULL,"
                +PATH+" TEXT NOT NULL,"
                +IMPORTENT+" TEXT NOT NULL,"
                +TIME+" TEXT NOT NULL)"
        );

       // Toast.makeText ( mContext,"OK1",Toast.LENGTH_SHORT ).show ();
    }

    @Override
    public void onUpgrade (SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
