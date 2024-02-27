package com.appwalied.quran.islamicstory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appwalied.quran.ahades.azkarmodel;
import com.appwalied.quran.sahaba.item_story;

import java.util.ArrayList;

public class DatabaseAsstes5 {
    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;
    private  static DatabaseAsstes5 instance;

    private DatabaseAsstes5(Context context){
        this.openHelper=new sqlitenewstory(context);
    }
    public static DatabaseAsstes5 getInstance(Context context){
        if (instance==null){instance=new DatabaseAsstes5(context);}
        return instance;
    }
    public void opean(){this.database=this.openHelper.getWritableDatabase();}
    public void close(){if (this.database!=null){this.database.close();}}


    public boolean insertdata(azkarmodel azkarmodel){
        ContentValues values=new ContentValues();
        values.put(sqlitenewstory.HEADER,azkarmodel.getName());
        values.put(sqlitenewstory.TITLE,azkarmodel.getTitle());
        long result =database.insert(sqlitenewstory.DB_table,null,values);
        return result !=-1;
    }

    public boolean updatedata(azkarmodel azkarmodel){
        ContentValues values=new ContentValues();
        values.put(sqlitenewstory.HEADER,azkarmodel.getName());
        values.put(sqlitenewstory.TITLE,azkarmodel.getFadl());

        String args[]={String.valueOf(azkarmodel.getId())};
        int result=database.update(sqlitenewstory.DB_table,values,"id=?",args);;
        return result >0;
    }
    public ArrayList<item_story> getalldata(){
        ArrayList<item_story>liststory=new ArrayList<>();
        database=openHelper.getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM "+sqlitenewstory.DB_table,null);
        if (cursor!=null && cursor.moveToFirst()){
            do {
                String header=cursor.getString(cursor.getColumnIndex(sqlitenewstory.HEADER));
                String title=cursor.getString(cursor.getColumnIndex(sqlitenewstory.TITLE));
                item_story data=new item_story(header,title);
                liststory.add(data);


            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return liststory;
    }
}


