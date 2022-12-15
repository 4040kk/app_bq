package com.example.text;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class todoet extends AppCompatActivity {


    private EditText ed_todo;
    private EditText ed_content;
    private BeDoneDB beDoneDB;
    private SQLiteDatabase dbWriter;
    private Button out,input;
    private Boolean Alarm_of;
    private int month,day;
    private TextView Alarm_t;
    private EditText Alarm_m,Alarm_d;
    private Switch Alarm_sw;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_todoeditor );
        ed_content= (EditText)findViewById ( R.id.text_todo );
        out=(Button) findViewById ( R.id.backTodo );
        input=(Button)findViewById ( R.id.add_todo );
        out=(Button) findViewById ( R.id.backTodo );
        Alarm_t=(TextView)findViewById ( R.id.Alarm_T );
        Alarm_m=(EditText)findViewById ( R.id.Alarm_m );
        Alarm_d=(EditText)findViewById ( R.id.Alarm_d );
        Alarm_sw=(Switch)findViewById ( R.id.Alarm_sw );
        Alarm_m.setVisibility ( View.INVISIBLE );
        Alarm_t.setVisibility ( View.INVISIBLE );
        Alarm_d.setVisibility ( View.INVISIBLE );
        Alarm_of=false;
        month=-1002;
        day=-1002;
        beDoneDB=new BeDoneDB ( this );
        dbWriter=beDoneDB.getWritableDatabase ();

        Alarm_sw.setOnCheckedChangeListener ( new CompoundButton.OnCheckedChangeListener ( ) {
            @Override
            public void onCheckedChanged (CompoundButton compoundButton, boolean b) {
                if(b){
                    Alarm_m.setVisibility ( View.VISIBLE );
                    Alarm_d.setVisibility ( View.VISIBLE );
                    Alarm_t.setVisibility ( View.VISIBLE );
                    onResume();
                }else {
                    Alarm_m.setVisibility ( View.INVISIBLE );
                    Alarm_t.setVisibility ( View.INVISIBLE );
                    Alarm_d.setVisibility ( View.INVISIBLE );
                }
                    Alarm_of=b;
            }
        } );

        out.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                finish ();
            }
        } );

        input.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                if (Alarm_of){
                    try {
                        month=Integer.parseInt(Alarm_m.getText ().toString ());
                        day=Integer.parseInt(Alarm_d.getText ().toString ());
                    }catch (Exception e){
                        return;
                    }
                    if (month>12||month<1||day>31||day<1){
                        Toast.makeText ( todoet.this, "输入日期不是有效数字", Toast.LENGTH_SHORT ).show ( );
                        month=-1002;
                        return;
                    }
                }
                if (ed_content.getText ().toString ().length ()==0){
                    Toast.makeText ( todoet.this, "请输入内容", Toast.LENGTH_SHORT ).show ( );
                    return;
                }
                    addDB ();
                    finish ();
            }
        } );

    }
    public void addDB(){
        ContentValues cv=new ContentValues ( );
        cv.put (BeDoneDB.CONTENT,ed_content.getText ().toString ());
        cv.put ( BeDoneDB.TIME,month+"."+day );
       // Toast.makeText ( this, month+"."+day , Toast.LENGTH_SHORT ).show ( );
        //cv.put ( BeDoneDB.CLASS,ed_todo.getText ().toString () );
        long insert = dbWriter.insert ( BeDoneDB.TABLE_NAME, null, cv );
        //Toast.makeText ( todoet.this,insert+"",Toast.LENGTH_SHORT ).show ();
    }

    @Override
    protected void onResume(){
        super.onResume ();
    }
}