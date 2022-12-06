package com.example.text;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Find_AC extends Activity {

    private Button in,out;
    private String re="1";
    private EditText et;
    private String edtext;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_find);
        et=(EditText) findViewById ( R.id.find_et );
        out=(Button) findViewById ( R.id.bedonemain_main );
        in=(Button) findViewById ( R.id.bedonemain_bedone );



        in.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {

                edtext=et.getText ().toString ();
                if(edtext.length ()==0){re="0";}
                Intent intent=new Intent ();
                intent.putExtra ( "key",re );
                intent.putExtra ( "content", edtext);
                setResult ( RESULT_OK,intent );
                finish ();

            }
        } );

        out.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick (View view) {
                re="0";
                Intent intent=new Intent ();
                intent.putExtra ( "key",re );
                setResult ( RESULT_OK,intent );
                finish ();
            }
        } );

    }
}