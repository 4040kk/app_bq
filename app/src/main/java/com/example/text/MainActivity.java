package com.example.text;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCUser;
import cn.leancloud.types.LCNull;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends Activity {

    private Boolean A=true;
    private Button login,getlc,putlc,bedone,find_in,user_out;
    private FloatingActionButton add;
    private ListView lv,lv_todo,find_list;;
    private MyAdapter adapter;
    private NotesDB notesDB;
    private BeDoneDB beDoneDB;
    private BeDone_Adapter adapter_todo;
    private SQLiteDatabase dbReader,dbReader_todo,dbWriter,dbWriter_note;
    private Cursor cursor,cursor_todo;
    private String ViewOP;
    private Boolean SeeTime;
    private String set_findtext,nowuser,nowkey;
    private Switch aSwitch;
    private EditText find_text;
    private TextView username;
    private int get_BeDb_ID;
    private LCUser author;
    private String name ;
    private ProgressBar wait;
    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        init();
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

        find_list.setOnItemClickListener ( new AdapterView.OnItemClickListener ( ) {
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

        user_out.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                Toast.makeText ( MainActivity.this, "退出成功", Toast.LENGTH_SHORT ).show ( );
                LCUser.logOut ();
                name=null;
                username.setText ( "登录" );
            }

        } );

        login.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
               Intent intent=new Intent (MainActivity.this,LoginActivity1.class);
                startActivityForResult ( intent,1);
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


        find_in.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                find_list.setVisibility ( View.VISIBLE );
                onResume ();
                find_text.setText ( "" );
            }
        } );

        putlc.setOnClickListener ( new View.OnClickListener ( ) {

            @Override
            public void onClick (View view) {
                if (name==null){Toast.makeText ( MainActivity.this, "用户未登录", Toast.LENGTH_SHORT ).show ( );return;}
                wait.setVisibility ( View.VISIBLE );
                A=true;
                int co;
                delet();
                delay ( 100 );
                String text="null";
                cursor_todo=dbReader_todo.query ( BeDoneDB.TABLE_NAME,null, null,null,null,null,null  );
                while (cursor_todo.moveToNext ()){
                    co = cursor_todo.getColumnIndex ( "content" );
                    if (co > -1) text = cursor_todo.getString (co);
                    LCObject todo=new LCObject ( "TODO" );
                    todo.put ( "TEXT",text);
                    todo.put ( "user",name );
                    Log.d ( "text1111",text);
                    todo.saveInBackground ().subscribe ( new Observer<LCObject> ( ) {
                        @Override
                        public void onSubscribe (Disposable d) {
                        }
                        @Override
                        public void onNext (LCObject lcObject) {
                           if (A) {Toast.makeText ( MainActivity.this, "上传成功", Toast.LENGTH_SHORT ).show ( );}
                            A=false;
                            onResume();
                        }

                        @Override
                        public void onError (Throwable e) {
                            if (A){Toast.makeText ( MainActivity.this, "上传失败", Toast.LENGTH_SHORT ).show ( );}
                            A=false;
                        }

                        @Override
                        public void onComplete ( ) {


                        }
                    } );
                }
                cursor=dbReader.query ( NotesDB.TABLE_NAME,null,null,null,null,null,null );
                String classno=null;
                A=true;
                while (cursor.moveToNext ()){
                    co = cursor.getColumnIndex ( "content" );
                    if (co > -1) text = cursor.getString (co);
                    co = cursor.getColumnIndex ( "class" );
                    if (co > -1) classno = cursor.getString (co);
                    LCObject note=new LCObject ( "NOTE" );
                    note.put ( "CONTENT",text);
                    note.put ( "user",name );
                    note.put ( "CLASS",classno );
                    //Log.d ( "text1111",text);
                    note.saveInBackground ().subscribe ( new Observer<LCObject> ( ) {
                        @Override
                        public void onSubscribe (Disposable d) {

                        }
                        @Override
                        public void onNext (LCObject lcObject) {
                            if (A){ Toast.makeText ( MainActivity.this, "上传成功", Toast.LENGTH_SHORT ).show ( );}
                                A=false;
                        }

                        @Override
                        public void onError (Throwable e) {
                            Toast.makeText ( MainActivity.this, e.toString (), Toast.LENGTH_SHORT ).show ( );
                        }

                        @Override
                        public void onComplete ( ) {

                        }
                    } );
                    A=false;
                    Toast.makeText ( MainActivity.this, "上传成功", Toast.LENGTH_SHORT ).show ( );
                }
                wait.setVisibility ( View.INVISIBLE );
                onResume();}} );
        getlc.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                if (name==null){Toast.makeText ( MainActivity.this, "用户未登录", Toast.LENGTH_SHORT ).show ( );return;}
                wait.setVisibility ( View.VISIBLE );
                   // author = LCUser.getCurrentUser();
                   // name =author.getUsername ();
                dbWriter.delete ( BeDoneDB.TABLE_NAME,null,null );
                onResume();
                LCQuery<LCObject> query = new LCQuery<> ("TODO");
                query.whereEqualTo("user", name);
                query.findInBackground().subscribe(new Observer<List<LCObject>>() {
                    public void onSubscribe(Disposable disposable) {}
                    public void onNext(List<LCObject> students) {
                        for (int i = 0; i < students.size(); i++) {
                            System.out.println(students.get(i) );
                            if (students.get(i).getString ( "TEXT" )!=null){
                            // Toast.makeText ( MainActivity.this,students.get(i).getString ( "TEXT" ) , Toast.LENGTH_SHORT ).show ( );
                                ContentValues cv=new ContentValues ( );
                                cv.put (BeDoneDB.CONTENT,students.get(i).getString ( "TEXT" ));
                                cv.put ( BeDoneDB.TIME,getTime () );
                                 dbWriter.insert ( BeDoneDB.TABLE_NAME, null, cv );
                                onResume();
                            }
                        }
                        Toast.makeText ( MainActivity.this, "同步成功", Toast.LENGTH_SHORT ).show ( );
                    }
                    public void onError(Throwable throwable) {
                        Toast.makeText ( MainActivity.this, throwable.toString (), Toast.LENGTH_SHORT ).show ( );
                    }
                    public void onComplete() {}
                });

                dbWriter_note.delete ( NotesDB.TABLE_NAME,null,null );
                LCQuery<LCObject> query1 = new LCQuery<> ("NOTE");
                query1.whereEqualTo("user", name);
                query1.findInBackground().subscribe(new Observer<List<LCObject>>() {
                    public void onSubscribe(Disposable disposable) {}
                    public void onNext(List<LCObject> students) {
                        for (int i = 0; i < students.size(); i++) {
                            if (students.get(i).getString ( "CLASS" )!=null){
                                ContentValues cv=new ContentValues ( );
                                cv.put (NotesDB.CLASS,students.get(i).getString ( "CLASS" ));
                                cv.put (NotesDB.CONTENT,students.get(i).getString ( "CONTENT" ));
                                cv.put (NotesDB.IMPORTENT,students.get(i).getString ( "IMPORTENT" ));
                                cv.put ( NotesDB.PATH,students.get ( i ).getString ( "PATH" ) );
                                cv.put ( NotesDB.TIME,getTime () );
                                dbWriter_note.insert ( NotesDB.TABLE_NAME, null, cv );
                                onResume();
                            }
                        }
                    }
                    public void onError(Throwable throwable) {
                        Toast.makeText ( MainActivity.this, "同步失败", Toast.LENGTH_SHORT ).show ( );
                    }
                    public void onComplete() {}
                });
                wait.setVisibility ( View.INVISIBLE );
            }} );



    }

    private String getTime(){
        SimpleDateFormat format=new SimpleDateFormat ("yy.MM.dd HH:mm");
        Date date =new Date (  );
        String str =format.format ( date );
        return str;
    }

    public void init(){
        notesDB =new NotesDB ( this );
        beDoneDB=new BeDoneDB ( this );
        dbReader =notesDB.getReadableDatabase ();
        dbReader_todo=beDoneDB.getReadableDatabase ();
        dbWriter=beDoneDB.getWritableDatabase ();
        dbWriter_note=notesDB.getWritableDatabase ();
        login=(Button)findViewById ( R.id.Login );
        bedone=(Button)findViewById ( R.id.bedonemain_bedone );
        lv=(ListView) findViewById ( R.id.main_list);
        lv_todo=(ListView)findViewById ( R.id.main_todo_list );
        aSwitch=(Switch)findViewById ( R.id.switch1 );
        find_list=(ListView)findViewById ( R.id.find_text );
        find_list.setVisibility ( View.INVISIBLE );
        find_in=(Button) findViewById ( R.id.find_in );
        add=(FloatingActionButton)findViewById ( R.id.main_add );
        user_out=(Button)findViewById ( R.id.user_out );
        find_text=(EditText)findViewById ( R.id.main_find0);
        getlc=(Button)findViewById ( R.id.get_LC );
        putlc=(Button)findViewById ( R.id.put_LC );
        username=(TextView)findViewById ( R.id.textView2 );
        wait=(ProgressBar)findViewById ( R.id.main_wait );
        wait.setVisibility ( View.INVISIBLE );
        set_findtext="";
        ViewOP ="0";
        SeeTime=false;
        try {
            author=LCUser.getCurrentUser ();
            name= author.getUsername ( );
            username.setText ( name );
        }catch (Exception e){


        }
    }

    public void selectDB(){
        cursor=dbReader.query( NotesDB.TABLE_NAME, null,null,null,null,null,null );
        adapter=new MyAdapter ( this,cursor,set_findtext,"0",SeeTime );
        lv.setAdapter (adapter);
        cursor_todo=dbReader_todo.query ( BeDoneDB.TABLE_NAME,null, null,null,null,null,null  );
        adapter_todo=new BeDone_Adapter ( this,cursor_todo );
        lv_todo.setAdapter ( adapter_todo );
        cursor=dbReader.query( NotesDB.TABLE_NAME, null,null,null,null,null,null );
        adapter=new MyAdapter ( MainActivity.this,cursor,find_text.getText ().toString (),"1",SeeTime );
        find_list.setAdapter (adapter);

    }

    @Override
    protected void onResume(){
        super.onResume ();
        selectDB ();
    }
    private void delay(int ms){
        try {
            Thread.currentThread();
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void delet(){
        LCQuery<LCObject> query = new LCQuery<> ("TODO");
        query.whereEqualTo("user", name);
        query.findInBackground().subscribe(new Observer<List<LCObject>>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(List<LCObject> students) {
                for (int i = 0; i < students.size(); i++) {
                    System.out.println(students.get(i) );
                    if (students.get(i).getString ( "objectId" )!=null){
                        LCObject todo = LCObject.createWithoutData("TODO", students.get(i).getString ( "objectId" ));
                       // Toast.makeText ( MainActivity.this, students.get(i).getString ( "objectId" ), Toast.LENGTH_SHORT ).show ( );
                        todo.deleteInBackground().subscribe(new Observer<LCNull>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {}

                            @Override
                            public void onNext(LCNull response) {

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                System.out.println("failed to delete a todo: " + e.getMessage());
                            }

                            @Override
                            public void onComplete() {}
                        });
                    }
                }

            }
            public void onError(Throwable throwable) {
                Toast.makeText ( MainActivity.this, throwable.toString (), Toast.LENGTH_SHORT ).show ( );
            }
            public void onComplete() {}
        });

        LCQuery<LCObject> query1 = new LCQuery<> ("NOTE");
        query1.whereEqualTo("user", name);
        query1.findInBackground().subscribe(new Observer<List<LCObject>>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(List<LCObject> students) {
                for (int i = 0; i < students.size(); i++) {
                    System.out.println(students.get(i) );
                    if (students.get(i).getString ( "objectId" )!=null){
                        LCObject notes = LCObject.createWithoutData("NOTE", students.get(i).getString ( "objectId" ));
                        // Toast.makeText ( MainActivity.this, students.get(i).getString ( "objectId" ), Toast.LENGTH_SHORT ).show ( );
                        notes.deleteInBackground().subscribe(new Observer<LCNull>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {}

                            @Override
                            public void onNext(LCNull response) {
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                System.out.println("failed to delete a todo: " + e.getMessage());
                            }

                            @Override
                            public void onComplete() {}
                        });
                    }
                }

            }
            public void onError(Throwable throwable) {
                Toast.makeText ( MainActivity.this, throwable.toString (), Toast.LENGTH_SHORT ).show ( );
            }
            public void onComplete() {  }
        });
    }
    @Override
    protected void  onActivityResult(int requestCode,int resultCode,Intent data){
        if (resultCode==1){
            author=LCUser.getCurrentUser ();
            name= author.getUsername ( );
            Log.d ("user" ,author.getUsername ().toString () );
            username.setText ( name );
            init ();
        }


    }
}