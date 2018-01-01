package com.example.parkkolay.nfc.Model.Sqlite;

/**
 * Created by parkkolay on 30.11.2017.
 */

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String  DATABASE_NAME = "DB.db";
    public static final String  CARD_ID = "CARD_ID";
    public static final String  NFC_CARDS_TABLE_NAME = "NFCcards";
    public static final String  NFC_CARDS_COLUMN_ID = "id";
    public static final String  NFC_CARDS_COLUMN_CARD_ID = "card_id";
    public static final String  NFC_CARDS_ENTER_AT = "START_TIME";
    public static final String  NFC_CARDS_EXIT_AT = "EXIT_TIME";
    public static final String  CAR_ID= "CAR_ID";
    public static final String  TARIFE_FIYAT_LISTESI = "TARIFE_FIYAT_LISTESI";
    public static final String  TARIFE_TABLE= "TARIFE_TABLE";
    public static final String  TARIFE_NAME= "TARIFE_NAME";
    public static final String  TARIFE_PRICE= "TARIFE_PRICE";
    public static final String  TARIFE_ID= "TARIFE_ID";
    public static final String  SERVICE_TABLE= "SERVICE_TABLE";
    public static final String  SERVICE_NAME= "SERVICE_NAME";
    public static final String  SERVICE_PRICE= "SERVICE_PRICE";
    public static final String  SERVICE_ID= "SERVICE_ID";
    public static final String  SERVICE_NAME_PRICE= "SERVICE_NAME_PRICE";
    private HashMap hp;
    private final Context context;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("onCreate","SQLiteDatabase");
      /*  db.execSQL(
                "create table NFC_CARDS_TABLE_NAME " +
                        "(NFC_CARDS_COLUMN_ID integer primary key, NFC_CARDS_COLUMN_CARD_ID text, Start DATETIME,Stop DATETIME)"
        );*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS tableName");
        onCreate(db);
    }

  /*  public boolean insertCards (String cardID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NFC_CARDS_COLUMN_CARD_ID", cardID);
        db.insert(tableName, null, contentValues);
        return true;
    }

  /*  public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }*/

   /* public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }*/

  /*  public boolean updateContact (Integer id, String name, String phone, String email, String street,String place) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("email", email);
        contentValues.put("street", street);
        contentValues.put("place", place);
        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }*/

  /*  public Integer deleteContact (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }*/

  /*  public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }*/
}

