package com.example.text;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;


public class BeDone extends AppCompatActivity {
    private ListView lv;
    private SQLiteDatabase dbReader;
    private SQLiteDatabase dbWriter;
    private Cursor cursor;
    private BeDone_Adapter adapter;
    private BeDoneDB beDoneDB;
    private Button main,todoBack,todoComplete;
    private FloatingActionButton betodo;
    private EditText bedocontent;
    private long ID;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_be_done );
        lv=(ListView) findViewById ( R.id.be_donglv );
        beDoneDB =new BeDoneDB ( this );
        dbReader =beDoneDB.getReadableDatabase ();
        dbWriter=beDoneDB.getReadableDatabase ();
        betodo=(FloatingActionButton)findViewById ( R.id.betodo_add );
        todoBack=(Button)findViewById ( R.id.todo_back );
        todoComplete=(Button)findViewById ( R.id.todo_complete );
        main=(Button)findViewById ( R.id.bedonemain_main );
        bedocontent=(EditText) findViewById ( R.id.bedone_content ) ;
        bedocontent.setVisibility ( View.INVISIBLE );
        todoComplete.setVisibility ( View.INVISIBLE );
        todoBack.setVisibility ( View.INVISIBLE );
        lv.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition ( position);
                int ColumnIndex=cursor.getColumnIndex ( BeDoneDB.CONTENT );
                String bedoS=null;
                if(ColumnIndex>-1){bedoS= cursor.getString ( ColumnIndex );}
                ColumnIndex=cursor.getColumnIndex ( BeDoneDB.ID );
                if(ColumnIndex>-1){ID=cursor.getInt ( ColumnIndex );}
                bedocontent.setVisibility ( View.VISIBLE );
                todoBack.setVisibility ( View.VISIBLE );
                todoComplete.setVisibility ( View.VISIBLE );
                bedocontent.setText (bedoS );
                ///////////////////////////////////////////////////////////////////////////////////////////////////////
            }
        } );

        todoBack.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                update ();
                bedocontent.setText ( "" );
                bedocontent.setVisibility ( View.INVISIBLE );
                todoComplete.setVisibility ( View.INVISIBLE );
                todoBack.setVisibility ( View.INVISIBLE );
                onResume();
            }
        } );

        todoComplete.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                delete ();
                bedocontent.setVisibility ( View.INVISIBLE );
                todoComplete.setVisibility ( View.INVISIBLE );
                todoBack.setVisibility ( View.INVISIBLE );
                onResume();
            }
        } );

        betodo.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                Intent intent=new Intent ( BeDone.this,todoet.class );
                startActivity ( intent );
            }
        } );

        main.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                finish ();
            }
        } );

        LCQuery<LCObject> query =new LCQuery<> ( "todo" );


    }
    public void selectDB(){
        cursor=dbReader.query( BeDoneDB.TABLE_NAME, null,null,null,null,null,null );

        adapter=new BeDone_Adapter ( this,cursor );
        lv.setAdapter (adapter);
    }

    @Override
    protected void onResume(){
        super.onResume ();
        selectDB ();
    }

    public  void delete(){
        dbWriter.delete ( BeDoneDB.TABLE_NAME,"_id="+ID,null);
    }

    public void update(){
        ContentValues cv=new ContentValues ( );
        cv.put ( BeDoneDB.CONTENT,bedocontent.getText ().toString ());
        dbWriter.update ( BeDoneDB.TABLE_NAME,cv,"_id="+ID,null );
    }
}