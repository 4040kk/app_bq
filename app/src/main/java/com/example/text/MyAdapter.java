package com.example.text;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

public class MyAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private AbsoluteLayout layout;
    private String stlect;
    private String kay;
    private Boolean seetime;
    private String[] strings=new String[1000];
    private String[] xtstrings=new String[1000];
    private Boolean INTI=true;
   public MyAdapter(Context context,Cursor cursor,String select,String trun1,Boolean SeeTime){
       this.context=context;
       this.cursor=cursor;
       this.kay=trun1;
       this.stlect=select;
        this.seetime=SeeTime;
    }

    @Override
    public int getCount ( ) {
        return cursor.getCount ();
    }

    @Override
    public Object getItem (int Position) {
        return cursor.getPosition ();
    }

    @Override
    public long getItemId (int Position) {
        return Position;
    }


    public View getView (int Position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=LayoutInflater.from (context );
        layout =(AbsoluteLayout) inflater.inflate ( R.layout.cell,null );
        TextView contenttv=(TextView) layout.findViewById ( R.id.list_content );
        TextView timetv=(TextView) layout.findViewById ( R.id.list_time );
        TextView classtv=(TextView)layout.findViewById ( R.id.list_class );
        ImageView importent=(ImageView)layout.findViewById ( R.id.importent );


        cursor.moveToPosition ( Position );
        boolean ptxt =false;

        int columnIndex = cursor.getColumnIndex ( "content" );
        String content = "null";
        if (columnIndex > -1) content = cursor.getString (columnIndex);
        columnIndex=cursor.getColumnIndex ( "time" );
        String time="null";
        if(columnIndex>-1) time =cursor.getString ( columnIndex );

        columnIndex=cursor.getColumnIndex ( "class" );
        String classc="null";
        if(columnIndex>-1) classc =cursor.getString ( columnIndex );

        columnIndex=cursor.getColumnIndex ( "path" );
        String url="null";
        if(columnIndex>-1) url =cursor.getString ( columnIndex );

        columnIndex=cursor.getColumnIndex ( "importent" );
        String IMP="null";
        if(columnIndex>-1) IMP=cursor.getString ( columnIndex );

        if (IMP.equals ( "yes" )){importent.setVisibility ( View.VISIBLE );}
        else{importent.setVisibility ( View.INVISIBLE );}

        if (kay.equals ( "1" )){
            int i=0;
            if (INTI==true){
                while (cursor.moveToNext ()){
                    int index1=cursor.getColumnIndex ( "class" );
                    if(index1>=-1){
                        String classname1=cursor.getString ( index1 );
                        strings[i]=classname1;
                        i++;
                    }
                }
                xtstrings=ssxtString (stlect,strings);
                INTI=false;
            }
            i=0;
            ptxt =false;
          for (;i<xtstrings.length;i++){
                if (xtstrings[i]==null){break;}
              if (classc.equals (xtstrings[i])){
                  ptxt=true;
                  break;
              }
          }
          if(ptxt){
              classtv.setText ( classc );
              contenttv.setText ( content);
          }else {

              classtv.setText ( " " );
              contenttv.setText ( " ");

          }
          ptxt=false;

        }


        if(kay.equals ( "0" )){
           // Toast.makeText ( context, classc, Toast.LENGTH_SHORT ).show ( );
            contenttv.setText ( content);
            // timetv.setText ( time );
            classtv.setText ( classc );
            if(seetime){timetv.setText ( time );}
            //imgiv.setImageBitmap ( getImageThumbnail ( url,200,200) );
        }


        return layout;
    }
    public static String Stringgetone(String s, int x){

        return s.substring(x,x+1);
    }
    public static String[] ssxtString(String ss,String[] strings){

        String[] xtstring=new String[100];
        int xt=0;
        int xtsz=0;
        for (int i=0;i<strings.length;i++){
            if (strings[i]==null){break;}
            for(int sswz=0 ;sswz<ss.length();sswz++){
                for (int i1=0;i1<strings[i].length();i1++){
                    if (Stringgetone(strings[i],i1). equals(Stringgetone(ss,sswz))){
                        xt++;
                    }
                }
            }
            if (xt>=ss.length()){

                xtstring[xtsz]=strings[i];
                xtsz++;
            }
            xt=0;
        }

        return xtstring;
    }}
