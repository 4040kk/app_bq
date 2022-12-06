package com.example.text;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BeDone_Adapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private AbsoluteLayout layout;
    private int position;
    private View view;
    private ViewGroup viewGroup;

    public BeDone_Adapter(Context context,Cursor cursor){
        this.context=context;
        this.cursor=cursor;
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

        LayoutInflater inflater=LayoutInflater.from ( context );
        layout=(AbsoluteLayout) inflater.inflate ( R.layout.bedone_cell,null );
        TextView classtv=(TextView)layout.findViewById ( R.id.bedonelist_class );
        cursor.moveToPosition ( Position );

        //columnIndex=cursor.getColumnIndex ( "time" );
        //String time="null";
        //if(columnIndex>-1) time =cursor.getString ( columnIndex );
        int columnIndex=cursor.getColumnIndex ( "content" );
        String content="null";
        if(columnIndex>-1) content =cursor.getString ( columnIndex );
        //timetv.setText ( time);
        classtv.setText ( content );
        return layout;
    }
}
