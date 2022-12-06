package com.example.text;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.IDN;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Content extends AppCompatActivity {

    private Button Delete,add_p,img_de;
    private Button Back;
    private EditText c_tv;
    private EditText c_tvclass;
    private Content content=this;
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    private String AAA;
    private int AID;
    private ImageView c_img;
    private ImageView images;
    private File file;
    private Boolean nu=false;
    private String imp;
    private Switch aSwitch;
    ActivityResultLauncher<String> stringActivityResultLauncher;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_content );
        Delete=(Button)findViewById ( R.id.c_delete );
        Back=(Button) findViewById ( R.id.c_return );
        add_p=(Button)findViewById ( R.id.add_photo );
        img_de=(Button)findViewById ( R.id.c_imgde );
        c_tv=(EditText) findViewById ( R.id.C_et );
        aSwitch=(Switch)findViewById ( R.id.switch4 );
        c_tvclass=(EditText)findViewById ( R.id.Content_class );
        c_img=(ImageView) findViewById ( R.id.content_path );
        notesDB =new NotesDB ( this );
        dbWriter=notesDB.getWritableDatabase ();
        AAA=c_tvclass.getText ().toString ();
        imp=getIntent ().getStringExtra ( NotesDB.IMPORTENT );
        img_de.setVisibility ( View.INVISIBLE );
        c_tv.setText ( getIntent ().getStringExtra ( NotesDB.CONTENT ));
        c_tvclass.setText (  getIntent ().getStringExtra ( NotesDB.CLASS ));
        AID=getIntent ().getIntExtra ( NotesDB.ID,0 );
        if (imp.equals ( "yes" )){aSwitch.setChecked (true);}else {aSwitch.setChecked (false);}
        Bitmap bitmap=BitmapFactory.decodeFile (  getIntent ().getStringExtra (NotesDB.PATH));
            if (bitmap!=null){
                img_de.setVisibility ( View.VISIBLE );
               add_p.setVisibility ( View.INVISIBLE );
               c_img.setImageBitmap ( bitmap);}
        //////////////////////////////////////////////////////////////////////

        aSwitch.setOnCheckedChangeListener ( new CompoundButton.OnCheckedChangeListener ( ) {
            @Override
            public void onCheckedChanged (CompoundButton compoundButton, boolean b) {
                if(b){
                    imp="yes";
                }else {
                    imp="no";
                }
                update();
            }
        } );

        img_de.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                nu=true;
                file=null;
                update ();
                img_de.setVisibility ( View.INVISIBLE );
                add_p.setVisibility ( View.VISIBLE );
                c_img.setImageBitmap ( null);
            }
        } );

        add_p.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                    nu=true;
                    stringActivityResultLauncher.launch ( "image/*" );
                    update();
            }
        } );

        Delete.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
            delete ();
            finish ();
            }
        });
        Back.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                AAA=c_tvclass.getText ().toString ();
                if(c_tv.getText ().toString ().equals ( "" )&&c_tvclass.getText ().toString ().equals ( "" )){delete ();}else {update();}
                if(AAA.length ()>=16){
                    Toast.makeText ( content," 标题字数过多了哦",Toast.LENGTH_SHORT ).show ();
                    return;
                }
             if (c_tvclass.getText ().toString ().equals ( "" )){
                 Toast.makeText ( content,"还没设置标题哦",Toast.LENGTH_SHORT ).show ();
                 return;
             }else {finish ();}

            }
        } );


    /////////////////////////////////////////////////////////////////////////////
    }
            public  void delete(){
                dbWriter.delete ( NotesDB.TABLE_NAME,"_id="+getIntent ().getIntExtra ( NotesDB.ID,0 ),null);
            }

            public void update(){
                ContentValues cv=new ContentValues ( );
                cv.put ( NotesDB.CONTENT,c_tv.getText ().toString ());
                cv.put ( NotesDB.CLASS,c_tvclass.getText ().toString () );
                cv.put ( NotesDB.IMPORTENT,imp );
                if (nu){
                    if (file!=null){cv.put (NotesDB.PATH, file.getAbsolutePath () );}
                    else { cv.put ( NotesDB.PATH,"isnull");}
                }
                dbWriter.update ( NotesDB.TABLE_NAME,cv,"_id="+getIntent ().getIntExtra ( NotesDB.ID,0 ),null );
            }

            @Nullable
            @Override
            public View onCreateView (@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
                View root = super.onCreateView ( parent, name, context, attrs );
                stringActivityResultLauncher = registerForActivityResult ( new ActivityResultContracts.GetContent (), (uri) -> {
                    if (uri==null){return;}
                    img_de.setVisibility ( View.VISIBLE );
                    add_p.setVisibility ( View.INVISIBLE );
                    file=new File (getPath ( Content.this,uri,AID ));
                    Bitmap bitmap =BitmapFactory.decodeFile ( file.getAbsolutePath () );
                    c_img.setImageBitmap ( bitmap );
                });
                return root;
            }
            public static String getPath(Context context, Uri srcUri,int AID) {
                SimpleDateFormat format=new SimpleDateFormat ("yy.MM.dd HH:mm");
                Date date =new Date (  );
                String str =format.format ( date );
                String path=context.getFilesDir()+"/"+str+AID+".png";//获取本地目录
                File file=new File(path);
                try {
                    InputStream inputStream = context.getContentResolver().openInputStream(srcUri);//context的方法获取URI文件输入流
                    if (inputStream == null) return "null";
                    OutputStream outputStream = new FileOutputStream (file);
                    copyStream(inputStream, outputStream);//调用下面的方法存储
                    inputStream.close();
                    outputStream.close();
                    return path;//成功返回路径
                } catch (Exception e) {
                    e.printStackTrace();
                    return "null";//失败返回路径null
                }
            }
            private static void copyStream(InputStream input, OutputStream output){//文件存储
                final int BUFFER_SIZE = 1024 * 2;
                byte[] buffer = new byte[BUFFER_SIZE];
                BufferedInputStream in = new BufferedInputStream (input, BUFFER_SIZE);
                BufferedOutputStream out = new BufferedOutputStream (output, BUFFER_SIZE);
                int count = 0, n = 0;
                try {
                    while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                        out.write(buffer, 0, n);
                        count += n;
                    }
                    out.flush();
                    out.close();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


}