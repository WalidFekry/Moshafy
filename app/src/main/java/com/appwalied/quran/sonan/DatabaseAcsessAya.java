package com.appwalied.quran.sonan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseAcsessAya {
    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;
    private  static DatabaseAcsessAya instance;

    private DatabaseAcsessAya(Context context){
        this.openHelper=new sqliteAya(context);
    }
    public static DatabaseAcsessAya getInstance(Context context){
        if (instance==null){instance=new DatabaseAcsessAya(context);}
        return instance;
    }
    public void opean(){this.database=this.openHelper.getWritableDatabase();}
    public void close(){if (this.database!=null){this.database.close();}}


    public boolean insertdata(item_Aya item){
        ContentValues values=new ContentValues();
        values.put(sqliteAya.NAME,item.getName());
        values.put(sqliteAya.TITLE,item.getTitle());


        long result =database.insert(sqliteAya.DB_table,null,values);
        return result !=-1;
    }

    public boolean updatedata(item_Aya item_aya){
        ContentValues values=new ContentValues();
        values.put(sqliteAya.NAME,item_aya.getName());
        values.put(sqliteAya.TITLE,item_aya.getTitle());



        String args[]={String.valueOf(item_aya.getId())};
        int result=database.update(sqliteAya.DB_table,values,"id=?",args);
        return result >0;
    }
    public ArrayList<item_Aya> getalldata(){
        ArrayList<item_Aya>liststory=new ArrayList<>();
        database=openHelper.getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM "+sqliteAya.DB_table,null);
        if (cursor!=null && cursor.moveToFirst()){
            do {
                int id =cursor.getInt(cursor.getColumnIndex(sqliteAya.ID));
                String name=cursor.getString(cursor.getColumnIndex(sqliteAya.NAME));
                String title=cursor.getString(cursor.getColumnIndex(sqliteAya.TITLE));
                int num=cursor.getInt(cursor.getInt(1));
                item_Aya data=new item_Aya(name,title,id,num);
                liststory.add(data);


            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return liststory;
    }
}