package com.example.text;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddText extends AppCompatActivity {

    private EditText ettext;
     private ImageView c_img;
    private NotesDB notesDB;
    private SQLiteDatabase dbWriter;
    private EditText etclass;
    private File phoneFile;
    private String importent;
    private String AAA;
    private Button inpath,depath;
    private Switch aSwitch;
    ActivityResultLauncher<String> stringActivityResultLauncher;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_add_text );
       // int mod=getIntent ().getIntExtra ( "mod",0 );
        Button out=(Button) findViewById ( R.id.add_out );
        Button input=(Button) findViewById ( R.id.add_input );
        inpath=(Button) findViewById ( R.id.add_path );
        depath=(Button) findViewById ( R.id.add_imgde );
        etclass=(EditText)findViewById ( R.id.et_class );
        aSwitch=(Switch)findViewById ( R.id.switch_imp );
        ettext =(EditText) findViewById ( R.id.et );
        c_img =findViewById ( R.id.add_img );
        notesDB=new NotesDB ( this );
        depath.setVisibility ( View.INVISIBLE );
        dbWriter=notesDB.getWritableDatabase ();
        importent="no";


//////////////////////////////////////////////////////////////////////////////////
        aSwitch.setOnCheckedChangeListener ( new CompoundButton.OnCheckedChangeListener ( ) {
            @Override
            public void onCheckedChanged (CompoundButton compoundButton, boolean b) {
                if(b){
                    importent="yes";
                }else {
                    importent="no";
                }
            }
        } );

        depath.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                phoneFile=null;
                c_img.setImageBitmap ( null);
                depath.setVisibility ( View.INVISIBLE );
                inpath.setVisibility ( View.VISIBLE );
            }
        } );

        inpath.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                stringActivityResultLauncher.launch ( "image/*" );
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
                AAA=etclass.getText ().toString ();
                if(AAA.length ()>=16){
                    Toast.makeText ( AddText.this," 标题字数过多了哦",Toast.LENGTH_SHORT ).show ();
                    return;
                }
                if(!etclass.getText ().toString ().equals ( "" )) {
                    addDB ();
                    finish ();
                }else {
                    Toast.makeText ( AddText.this,"还没设置标题哦",Toast.LENGTH_SHORT ).show ();
                    return;
                }
               // Toast.makeText ( AddText.this, phoneFile.getPath (), Toast.LENGTH_SHORT ).show ( );
                    finish ();
            }
        } );
    }
    public void addDB(){
        ContentValues cv=new ContentValues ( );
        cv.put ( NotesDB.CONTENT,ettext.getText ().toString ());
        cv.put ( NotesDB.TIME,getTime () );
        cv.put ( NotesDB.CLASS,etclass.getText ().toString () );
        cv.put ( NotesDB.IMPORTENT,importent );
        if (phoneFile==null){cv.put ( NotesDB.PATH,"isnull");}
        else { cv.put ( NotesDB.PATH,phoneFile.getAbsolutePath ());}
        dbWriter.insert ( NotesDB.TABLE_NAME,null,cv );

    }
    private String getTime(){
        SimpleDateFormat format=new SimpleDateFormat ("yy.MM.dd HH:mm");
        Date date =new Date (  );
        String str =format.format ( date );
        return str;
    }
    @Nullable
    @Override
    public View onCreateView (@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        View root = super.onCreateView ( parent, name, context, attrs );
        stringActivityResultLauncher = registerForActivityResult ( new ActivityResultContracts.GetContent (), (uri) -> {
            if (uri==null){return;}
            depath.setVisibility ( View.VISIBLE );
            inpath.setVisibility ( View.INVISIBLE );
            phoneFile=new File (getPath ( AddText.this,uri ));
            Bitmap bitmap =BitmapFactory.decodeFile ( phoneFile.getAbsolutePath () );
            c_img.setImageBitmap ( bitmap );
        });
        return root;
    }

    public static String getPath(Context context, Uri srcUri) {
        SimpleDateFormat format=new SimpleDateFormat ("yy.MM.dd HH:mm");
        Date date =new Date (  );
        String str =format.format ( date );
        String path=context.getFilesDir()+"/"+str+".png";//获取本地目录
        File file=new File(path);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);//context的方法获取URI文件输入流
            if (inputStream == null) return "null";
            OutputStream outputStream = new FileOutputStream(file);
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