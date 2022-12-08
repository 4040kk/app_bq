package com.example.text;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class todoet extends AppCompatActivity {


    private EditText ed_todo;
    private EditText ed_content;
    private BeDoneDB beDoneDB;
    private SQLiteDatabase dbWriter;
    private Button out,input;
    private String AAA;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_todoeditor );
        ed_content= (EditText)findViewById ( R.id.text_todo );
        out=(Button) findViewById ( R.id.backTodo );
        input=(Button)findViewById ( R.id.add_todo );
        out=(Button) findViewById ( R.id.backTodo );
        beDoneDB=new BeDoneDB ( this );
        dbWriter=beDoneDB.getWritableDatabase ();

        out.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                finish ();
            }
        } );

        input.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                    addDB ();
                    finish ();
            }
        } );

    }
    public void addDB(){
        ContentValues cv=new ContentValues ( );
        cv.put (BeDoneDB.CONTENT,ed_content.getText ().toString ());
        cv.put ( BeDoneDB.TIME,getTime () );
        //cv.put ( BeDoneDB.CLASS,ed_todo.getText ().toString () );
        long insert = dbWriter.insert ( BeDoneDB.TABLE_NAME, null, cv );
        //Toast.makeText ( todoet.this,insert+"",Toast.LENGTH_SHORT ).show ();
    }
    private String getTime(){
        SimpleDateFormat format=new SimpleDateFormat ("yy.MM.dd HH:mm");
        Date date =new Date (  );
        String str =format.format ( date );
        return str;
    }
}