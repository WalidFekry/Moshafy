package com.appwalied.quran.sahaba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appwalied.quran.ahades.azkarmodel;

import java.util.ArrayList;

public class DatabaseAsstes4 {
    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;
    private  static DatabaseAsstes4 instance;

    private DatabaseAsstes4(Context context){
        this.openHelper=new sqlitestory(context);
    }
    public static DatabaseAsstes4 getInstance(Context context){
        if (instance==null){instance=new DatabaseAsstes4(context);}
        return instance;
    }
    public void opean(){this.database=this.openHelper.getWritableDatabase();}
    public void close(){if (this.database!=null){this.database.close();}}


    public boolean insertdata(azkarmodel azkarmodel){
        ContentValues values=new ContentValues();
        values.put(sqlitestory.HEADER,azkarmodel.getName());
        values.put(sqlitestory.TITLE,azkarmodel.getTitle());
        long result =database.insert(sqlitestory.DB_table,null,values);
        return result !=-1;
    }

    public boolean updatedata(azkarmodel azkarmodel){
        ContentValues values=new ContentValues();
        values.put(sqlitestory.HEADER,azkarmodel.getName());
        values.put(sqlitestory.TITLE,azkarmodel.getFadl());

        String args[]={String.valueOf(azkarmodel.getId())};
        int result=database.update(sqlitestory.DB_table,values,"id=?",args);;
        return result >0;
    }
    public ArrayList<item_story> getalldata(){
        ArrayList<item_story>liststory=new ArrayList<>();
        database=openHelper.getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM "+sqlitestory.DB_table,null);
        if (cursor!=null && cursor.moveToFirst()){
            do {
                String header=cursor.getString(cursor.getColumnIndex(sqlitestory.HEADER));
                String title=cursor.getString(cursor.getColumnIndex(sqlitestory.TITLE));
                item_story data=new item_story(header,title);
                liststory.add(data);


            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return liststory;
    }
}

