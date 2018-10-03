package com.example.dungtt.spammessagebycode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SQLiteHandle extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "SpamNumber";
    private static final int VERSION = 1;
    public static final String TABLE_NAME = "Spams";

    public static final String KEY_ID = "id";
    public static final String KEY_NUMBER = "number";
    public static final String KEY_COUNT = "count";



    public SQLiteHandle(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String scrip = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s INTEGER)",
                TABLE_NAME, KEY_ID, KEY_NUMBER, KEY_COUNT);
        sqLiteDatabase.execSQL(scrip);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String scrip = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        sqLiteDatabase.execSQL(scrip);
        onCreate(sqLiteDatabase);
    }

    public void addSpamNumber(ContactSpam spam){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put(KEY_NUMBER, spam.getNumberSpam());
        values.put(KEY_COUNT, spam.getCount());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<ContactSpam> getListSpamNum(){
        ArrayList<ContactSpam> list = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while ((cursor.isAfterLast() == false)){
            ContactSpam str = new ContactSpam(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
            list.add(str);
            cursor.moveToNext();
        }
        return list;
    }

    public void updateSpam(ContactSpam  spam){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NUMBER, spam.getNumberSpam());
        values.put(KEY_COUNT, spam.getCount());

        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] {String.valueOf(spam.get_id())});
        db.close();
    }

    public void deletedSpam(ContactSpam spam){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(spam.get_id()) });
        db.close();
    }

    public ContactSpam querySpam(String numSpam){
        ContactSpam contactSpam = null;
        String query = "SELECT * FROM " + TABLE_NAME +" WHERE " + KEY_NUMBER + " = " + numSpam + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while ((cursor.isAfterLast() == false)){
            contactSpam = new ContactSpam(cursor.getInt(0), cursor.getString(1), cursor.getInt(2));
            cursor.moveToNext();
        }
        return contactSpam;
    }




}
