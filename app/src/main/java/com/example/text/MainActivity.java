package com.example.text;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

//import com.example.text.databinding.ActivityMain2Binding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

public class MainActivity extends Activity {


    private Button login,findText,bedone;
    private ListView lv,lv_todo;
    private MyAdapter adapter;
    private NotesDB notesDB;
    private BeDoneDB beDoneDB;
    private BeDone_Adapter adapter_todo;
    private SQLiteDatabase dbReader,dbReader_todo,dbWriter;
    private Cursor cursor,cursor_todo;
    private String ViewOP;
    private Boolean SeeTime;
    private String set_findtext;
    private Switch aSwitch;
    private int get_BeDb_ID;
    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate ( savedInstanceState );
       setContentView ( R.layout.activity_main );
        notesDB =new NotesDB ( this );
        beDoneDB=new BeDoneDB ( this );
        dbReader =notesDB.getReadableDatabase ();
        dbReader_todo=beDoneDB.getReadableDatabase ();
        dbWriter=beDoneDB.getWritableDatabase ();
        login=(Button)findViewById ( R.id.Login );
        bedone=(Button)findViewById ( R.id.bedonemain_bedone );
        lv=(ListView) findViewById ( R.id.main_list);
        lv_todo=(ListView)findViewById ( R.id.main_todo_list );
        FloatingActionButton add=(FloatingActionButton)findViewById ( R.id.main_add );
        findText=(Button) findViewById ( R.id.main_find );
        aSwitch=(Switch)findViewById ( R.id.switch1 );
        set_findtext="";
        ViewOP ="0";
        SeeTime=false;

        //////////////////////////////////////////////////////////////////////////
        lv.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition ( position);
                Intent i=new Intent (MainActivity.this,Content.class);
                int ColumnIndex=cursor.getColumnIndex ( NotesDB.ID );
                if(ColumnIndex>-1){i.putExtra ( NotesDB.ID,cursor.getInt ( ColumnIndex ) );}
                ColumnIndex=cursor.getColumnIndex ( NotesDB.CONTENT );
                if(ColumnIndex>-1){i.putExtra ( NotesDB.CONTENT,cursor.getString ( ColumnIndex ) );}
                ColumnIndex=cursor.getColumnIndex ( NotesDB.TIME );
                if(ColumnIndex>-1){i.putExtra ( NotesDB.TIME,cursor.getString ( ColumnIndex ) );}
                ColumnIndex=cursor.getColumnIndex ( NotesDB.CLASS );
                if(ColumnIndex>-1){i.putExtra ( NotesDB.CLASS,cursor.getString ( ColumnIndex ) );}
                ColumnIndex=cursor.getColumnIndex ( NotesDB.PATH );
                if(ColumnIndex>-1){i.putExtra ( NotesDB.PATH,cursor.getString ( ColumnIndex ) );}
                ColumnIndex=cursor.getColumnIndex ( NotesDB.IMPORTENT );
                if(ColumnIndex>-1){i.putExtra ( NotesDB.IMPORTENT,cursor.getString ( ColumnIndex ) );}

                startActivity ( i );
            }
        } );

        lv_todo.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
            @Override
            public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {
                cursor_todo.moveToPosition ( i );
                int Co=cursor_todo.getColumnIndex ( BeDoneDB.ID );
                if(Co>-1){get_BeDb_ID=cursor_todo.getInt ( Co );}
                dbWriter.delete ( beDoneDB.TABLE_NAME,"_id="+get_BeDb_ID,null);
                onResume();
            }
        } );


        login.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
               Intent intent=new Intent (MainActivity.this,LoginActivity1.class);
                startActivity ( intent );
            }
        } );

        add.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {

                Intent intent=new Intent (MainActivity.this,AddText.class);
                startActivity ( intent );
            }
        } );

        aSwitch.setOnCheckedChangeListener ( new CompoundButton.OnCheckedChangeListener ( ) {
            @Override
            public void onCheckedChanged (CompoundButton compoundButton, boolean b) {
                if(b){
                    SeeTime=b;
                }else {
                    SeeTime=b;
                }
                onResume();
            }
        } );

        bedone.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                Intent intent=new Intent (MainActivity.this,BeDone.class);
                startActivity ( intent );
            }
        } );


        findText.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                Intent a=new Intent (MainActivity.this,Find_AC.class);
                startActivityForResult (a,0);
            }
        } );

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult ( requestCode,resultCode,data );
        if(resultCode==RESULT_OK){
            set_findtext=data.getStringExtra ( "content" );
            ViewOP=data.getStringExtra ( "key" );
            onResume();
        }

    }

    public void selectDB(){
        cursor=dbReader.query( NotesDB.TABLE_NAME, null,null,null,null,null,null );
        adapter=new MyAdapter ( this,cursor,set_findtext,ViewOP,SeeTime );
        lv.setAdapter (adapter);
        cursor_todo=dbReader_todo.query ( BeDoneDB.TABLE_NAME,null, null,null,null,null,null  );
        adapter_todo=new BeDone_Adapter ( this,cursor_todo );
        lv_todo.setAdapter ( adapter_todo );
    }

    @Override
    protected void onResume(){
        super.onResume ();
        selectDB ();
    }
}