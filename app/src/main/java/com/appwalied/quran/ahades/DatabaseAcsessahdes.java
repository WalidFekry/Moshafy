package com.appwalied.quran.ahades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseAcsessahdes {
    private SQLiteDatabase database;
    private SQLiteOpenHelper openHelper;
    private  static DatabaseAcsessahdes instance;

    private DatabaseAcsessahdes(Context context){
        this.openHelper=new sqliteahdes(context);
    }
    public static DatabaseAcsessahdes getInstance(Context context){
        if (instance==null){instance=new DatabaseAcsessahdes(context);}
        return instance;
    }
    public void opean(){this.database=this.openHelper.getWritableDatabase();}
    public void close(){if (this.database!=null){this.database.close();}}


    public boolean insertdata(azkarmodel azkarmodel){
        ContentValues values=new ContentValues();
        values.put(sqliteahdes.NAME,azkarmodel.getName());
        values.put(sqliteahdes.TITLE,azkarmodel.getTitle());
        long result =database.insert(sqliteahdes.DB_table,null,values);
        return result !=-1;
    }

    public boolean updatedata(azkarmodel azkarmodel){
        ContentValues values=new ContentValues();
        values.put(sqliteahdes.NAME,azkarmodel.getName());
        values.put(sqliteahdes.TITLE,azkarmodel.getTitle());

        String args[]={String.valueOf(azkarmodel.getId())};
        int result=database.update(sqliteahdes.DB_table,values,"id=?",args);;
        return result >0;
    }
    public ArrayList<item_ahdes> getalldata(){
        ArrayList<item_ahdes>liststory=new ArrayList<>();
        database=openHelper.getReadableDatabase();
        Cursor cursor=database.rawQuery("SELECT * FROM "+sqliteahdes.DB_table,null);
        if (cursor!=null && cursor.moveToFirst()){
            do {
                int id =cursor.getInt(cursor.getColumnIndex(sqliteahdes.ID));
                String name=cursor.getString(cursor.getColumnIndex(sqliteahdes.NAME));
                String title=cursor.getString(cursor.getColumnIndex(sqliteahdes.TITLE));
                item_ahdes data=new item_ahdes(id,name,title);
                liststory.add(data);


            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return liststory;
    }
}