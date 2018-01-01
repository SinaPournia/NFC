package com.example.parkkolay.nfc.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.parkkolay.nfc.Model.Sqlite.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.parkkolay.nfc.Model.Sqlite.DBHelper.*;
import static com.example.parkkolay.nfc.Model.UtilityMethods.getDateTime;

/**
 * Created by parkkolay on 30.11.2017.
 */

public class Model implements ModelContract {


    DBHelper mDBhelper;

    public Model(DBHelper mDBhelper) {
        this.mDBhelper = mDBhelper;
        createTarifeTable();
        createNewSubTarifeTable();
        createServiceTable();
    }


    @Override
    public void createNewSubTarifeTable() {
        mDBhelper.getWritableDatabase().execSQL(
                " CREATE TABLE IF NOT EXISTS "
                        + NFC_CARDS_TABLE_NAME + "(" + NFC_CARDS_COLUMN_ID + " INTEGER PRIMARY KEY,"
                        + NFC_CARDS_COLUMN_CARD_ID + " TEXT,"
                        + CAR_ID + " TEXT,"
                        + NFC_CARDS_ENTER_AT + " TEXT,"
                        + NFC_CARDS_EXIT_AT + " TEXT,"
                        + SERVICE_NAME_PRICE + " TEXT,"
                        + TARIFE_FIYAT_LISTESI + " TEXT " + ")"
        );
    }

    @Override
    public void createTarifeTable() {
        mDBhelper.getWritableDatabase().execSQL(
                " CREATE TABLE IF NOT EXISTS "
                        + TARIFE_TABLE + "("
                        + TARIFE_ID + " INTEGER PRIMARY KEY,"
                        + TARIFE_NAME + " TEXT,"
                        + TARIFE_PRICE + " TEXT"
                        + ")"
        );
    }

    @Override
    public void createServiceTable() {
        mDBhelper.getWritableDatabase().execSQL(
                " CREATE TABLE IF NOT EXISTS "
                        + SERVICE_TABLE + "("
                        + SERVICE_ID + " INTEGER PRIMARY KEY,"
                        + SERVICE_NAME + " TEXT,"
                        + SERVICE_PRICE + " TEXT"
                        + ")"
        );
    }

    @Override
    public void insertNewServiceInServiceTable(String ServiceName, String price) {
        if(ifServiceNameNotExistedBefore(ServiceName)){


        ContentValues contentValues = new ContentValues();
        contentValues.put(SERVICE_NAME, ServiceName);
        contentValues.put(SERVICE_PRICE, price);

        mDBhelper.getWritableDatabase().insert(SERVICE_TABLE, null, contentValues);
        Log.e("NewServiceInService","Created" + ServiceName);
        }else{
            Log.e("NewServiceInService","ServiceNameNotExistedBefore");
        }
    }

    @Override
    public void insertNewTarifeInTarifeTable(String TarifeName, String price) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TARIFE_NAME, TarifeName);
        contentValues.put(TARIFE_PRICE, price);

        mDBhelper.getWritableDatabase().insert(TARIFE_TABLE, null, contentValues);
    }

    @Override
    public String readPriceFromTarifeTable(String Tarife) {
        Cursor mCursor = mDBhelper.
                getReadableDatabase().
                rawQuery("select " + TARIFE_PRICE + " from '" + TARIFE_TABLE + "' WHERE TARIFE_NAME='" + Tarife + "';", null);

        if (mCursor != null) {
            mCursor.moveToLast();
        }
        Log.e("readPriceFromTarife", mCursor.getString(mCursor.getColumnIndex(TARIFE_PRICE)));
        return mCursor.getString(mCursor.getColumnIndex(TARIFE_PRICE));
    }

    @Override
    public String readPriceFromServiceTable(String ServiceName) {
        Cursor mCursor = mDBhelper.
                getReadableDatabase().
                rawQuery("select " + SERVICE_PRICE + " from '" + SERVICE_NAME + "' WHERE SERVICE_NAME='" + ServiceName + "';", null);

        if (mCursor != null) {
            mCursor.moveToLast();
        }
        Log.e("readPriceFromService", mCursor.getString(mCursor.getColumnIndex(SERVICE_PRICE)));
        return mCursor.getString(mCursor.getColumnIndex(SERVICE_PRICE));
    }

    @Override
    public String readTarifePriceForEachCard(String CardID) {
        Cursor mCursor = mDBhelper.
                getReadableDatabase().
                rawQuery("select " + TARIFE_FIYAT_LISTESI + " from '" + NFC_CARDS_TABLE_NAME + "' WHERE card_id='" + CardID + "';", null);

        if (mCursor != null) {
            mCursor.moveToLast();
        }
        Log.e("readTarifePriceForEach", mCursor.getString(mCursor.getColumnIndex(TARIFE_FIYAT_LISTESI)));
        return mCursor.getString(mCursor.getColumnIndex(TARIFE_FIYAT_LISTESI));
    }

    @Override
    public String readServicePriceForEachCard(String CardID) {
        Cursor mCursor = mDBhelper.
                getReadableDatabase().
                rawQuery("select " + SERVICE_NAME_PRICE + " from '" + NFC_CARDS_TABLE_NAME + "' WHERE card_id='" + CardID + "';", null);

        if (mCursor != null) {
            mCursor.moveToLast();
        }
        Log.e("readServicePriceForEach", mCursor.getString(mCursor.getColumnIndex(SERVICE_NAME_PRICE)));
        return mCursor.getString(mCursor.getColumnIndex(SERVICE_NAME_PRICE));
    }

    @Override
    public boolean ifServicePriceForEachCardIsNotNull(String CardID) {
        SQLiteDatabase db = mDBhelper.getReadableDatabase();
        Cursor mCursor =
                db.rawQuery("SELECT * FROM'" + NFC_CARDS_TABLE_NAME + "' WHERE " + NFC_CARDS_COLUMN_CARD_ID + "='" + CardID + "';", null);
        if (mCursor != null) {
            mCursor.moveToLast();
        }
        if (mCursor.getString(mCursor.getColumnIndex(SERVICE_NAME_PRICE)) != null) {
            Log.e("ifExistedCardHaveExit", CardID + " true " + mCursor.getString(mCursor.getColumnIndex(SERVICE_NAME_PRICE)));
            return true;
        } else {
            Log.e("ifExistedCardHaveExit", CardID + " false " + mCursor.getCount());
            return false;
        }
    }

    @Override
    public boolean ifServiceTableIsNotEmpty() {
        SQLiteDatabase db = mDBhelper.getReadableDatabase();
        Cursor mCursor =
                db.rawQuery("SELECT * FROM'" + SERVICE_TABLE + "';", null);

        if (mCursor.getCount() > 0) {
            Log.e("ServiceTableIsNotEmpty", " true ");
            return true;
        } else {
            Log.e("ServiceTableIsNotEmpty", " false");
            return false;
        }
    }

    @Override
    public boolean ifTarifeTableIsNotEmpty() {
        SQLiteDatabase db = mDBhelper.getReadableDatabase();
        Cursor mCursor =
                db.rawQuery("SELECT * FROM'" + TARIFE_TABLE + "';", null);

        if (mCursor.getCount() > 0) {
            Log.e("ifTarifeTableIsNotEmpty", " true ");
            return true;
        } else {
            Log.e("ifTarifeTableIsNotEmpty", " false");
            return false;
        }
    }

    @Override
    public boolean ifTarifeNameNotExistedBefore(String tarife) {
        SQLiteDatabase db = mDBhelper.getReadableDatabase();
        Cursor mCursor =
                db.rawQuery("SELECT TARIFE_NAME FROM'" + TARIFE_TABLE + "' WHERE " + TARIFE_NAME + "='" + tarife + "';", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if (mCursor.getCount() > 0) {
            Log.e("ifTarifeNameNotExisted", tarife + " false ");
            return false;
        } else {
            Log.e("ifTarifeNameNotExisted", tarife + " true");
            return true;
        }
    }

    @Override
    public boolean ifServiceNameNotExistedBefore(String ServiceName) {
        SQLiteDatabase db = mDBhelper.getReadableDatabase();
        Cursor mCursor =
                db.rawQuery("SELECT SERVICE_NAME FROM'" + SERVICE_TABLE + "' WHERE " + SERVICE_NAME + "='" + ServiceName + "';", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        if (mCursor.getCount() > 0) {
            Log.e("ifServiceNameNotExisted", ServiceName + " false ");
            return false;
        } else {
            Log.e("ifServiceNameNotExisted", ServiceName + " true");
            return true;
        }
    }

    @Override
    public void UpdateServiceNameAndPrice(String OldServiceName,String ServiceName, String ServicePrice) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(SERVICE_NAME, ServiceName);
            contentValues.put(SERVICE_PRICE, ServicePrice);
            mDBhelper.getWritableDatabase().update(SERVICE_TABLE, contentValues, " SERVICE_NAME ='"+OldServiceName+"' ; ", null);

            Log.e("UpdateServiceName", "OldServiceName =" + " " + OldServiceName + "/N" +
                    "ServiceName =" + " " + ServiceName + "/N" +
                    "ServicePrice =" + ServicePrice );



    }

    @Override
    public List<String>  readTarifesFromTarifeTable() {
        Cursor mCursor = mDBhelper.
                getReadableDatabase().
                rawQuery("select " + TARIFE_NAME + " from '" + TARIFE_TABLE +  "';", null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        List<String> strings=new ArrayList<String>();
        for(int i=0;mCursor.getCount()>i;i++){
            Log.e("readTarifesFromTarife", mCursor.getString(mCursor.getColumnIndex(TARIFE_NAME)));
            strings.add(mCursor.getString(mCursor.getColumnIndex(TARIFE_NAME)));
            mCursor.moveToNext();
        }
        return strings;
    }

    @Override
    public List<String> readServicesFromServiceTable() {
        Cursor mCursor = mDBhelper.
                getReadableDatabase().
                rawQuery("select " + SERVICE_NAME + " from '" + SERVICE_TABLE +  "';", null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        List<String> strings=new ArrayList<String>();
        for(int i=0;mCursor.getCount()>i;i++){
            Log.e("readServicesFromService", mCursor.getString(mCursor.getColumnIndex(SERVICE_NAME)));
            strings.add(mCursor.getString(mCursor.getColumnIndex(SERVICE_NAME)));
            mCursor.moveToNext();
        }
        return strings;
    }

    @Override
    public List<String> readPricesFromServiceTable() {
        Cursor mCursor = mDBhelper.
                getReadableDatabase().
                rawQuery("select " + SERVICE_PRICE + " from '" + SERVICE_TABLE +  "';", null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        List<String> strings=new ArrayList<String>();
        for(int i=0;mCursor.getCount()>i;i++){
            Log.e("readPricesFromService", mCursor.getString(mCursor.getColumnIndex(SERVICE_PRICE)));
            strings.add(mCursor.getString(mCursor.getColumnIndex(SERVICE_PRICE)));
            mCursor.moveToNext();
        }
        return strings;
    }



    @Override
    public String readCardEnterDateFromTable(String table, String CardID) {
        Cursor mCursor = mDBhelper.
                getReadableDatabase().
                rawQuery("select * from '" + table + "' WHERE card_id='" + CardID + "';", null);

        if (mCursor != null) {
            mCursor.moveToLast();
        }
        Log.e("readCardEnterDate", mCursor.getString(mCursor.getColumnIndex(NFC_CARDS_ENTER_AT)));
        return mCursor.getString(mCursor.getColumnIndex(NFC_CARDS_ENTER_AT));
    }

    @Override
    public String readCardExitDateFromTable(String table, String CardID) {
        if (ifExistedCardHaveExitTime(table, CardID) && ifCardExistedBefore(table, CardID)) {
            Cursor mCursor =
                    mDBhelper.getReadableDatabase().rawQuery("select * from " + table + " WHERE card_id = '" + CardID + "';", null);
            if (mCursor != null) {
                mCursor.moveToLast();
                Log.e("mCursor", "moveToLast");
            }
            if (mCursor.getString(mCursor.getColumnIndex(NFC_CARDS_EXIT_AT)) != null) {
                Log.e("readCardExitDate", mCursor.getString(mCursor.getColumnIndex(NFC_CARDS_EXIT_AT)));
                return mCursor.getString(mCursor.getColumnIndex(NFC_CARDS_EXIT_AT));
            }

        }
        Log.e("readCardExitDate", "null");
        return "dont have exit time";
    }

    @Override
    public void writeServicesToNFCcardsToTable(String Services, String CardID) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(SERVICE_NAME_PRICE, Services);
            mDBhelper.getWritableDatabase().update(NFC_CARDS_TABLE_NAME, contentValues, "id = (SELECT max(id) FROM NFCcards) AND card_id ='"+CardID+"' ; ", null);

            Log.e("writeServicesToNFCcards", "true");

    }

    @Override
    public void writeCardExitDateToTable(String table, String CardID) {
        if (!ifExistedCardHaveExitTime(table, CardID) && ifCardExistedBefore(table, CardID)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(NFC_CARDS_EXIT_AT, getDateTime());
            mDBhelper.getWritableDatabase().update(table, contentValues, "id = (SELECT max(id) FROM NFCcards) AND card_id ='"+CardID+"' ; ", null);

            Log.e("writeCardExitDate", "true");
        } else {
            Log.e("writeCardExitDate", "false");
        }
    }

    @Override
    public void existCardToTable(String table, String CardID, String TarifeFiyatListesi,String CarNumber) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NFC_CARDS_COLUMN_CARD_ID, CardID);
        contentValues.put(NFC_CARDS_ENTER_AT, getDateTime());
        contentValues.put(CAR_ID, CarNumber);
        contentValues.put(TARIFE_FIYAT_LISTESI, TarifeFiyatListesi);

        mDBhelper.getWritableDatabase().insert(table, null, contentValues);

    }

    @Override
    public boolean ifCardExistedBefore(String table, String CardID) {
        SQLiteDatabase db = mDBhelper.getReadableDatabase();
        Cursor mCursor =
                db.rawQuery("SELECT * FROM'" + table + "' WHERE " + NFC_CARDS_COLUMN_CARD_ID + "='" + CardID + "';", null);

        if (mCursor.getCount() > 0) {
            Log.e("ifCardExistedBefore", CardID + " true ");
            return true;
        } else {
            Log.e("ifCardExistedBefore", CardID + " false");
            return false;
        }
    }

    @Override
    public boolean ifExistedCardHaveExitTime(String table, String CardID) {

        SQLiteDatabase db = mDBhelper.getReadableDatabase();
        Cursor mCursor =
                db.rawQuery("SELECT * FROM'" + table + "' WHERE " + NFC_CARDS_COLUMN_CARD_ID + "='" + CardID + "';", null);
        if (mCursor != null) {
            mCursor.moveToLast();
        }
        if (mCursor.getString(mCursor.getColumnIndex(NFC_CARDS_EXIT_AT)) != null) {
            Log.e("ifExistedCardHaveExit", CardID + " true " + mCursor.getString(mCursor.getColumnIndex(NFC_CARDS_EXIT_AT)));
            return true;
        } else {
            Log.e("ifExistedCardHaveExit", CardID + " false " + mCursor.getCount());
            return false;
        }

    }

    @Override
    public JSONArray getJsonFromTarifeFiyatlari(String TarifeFiyatlari) {
        try {
            JSONObject json = new JSONObject(TarifeFiyatlari);
            JSONArray jsonArray = json.getJSONArray("tarife");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.e("Start =", jsonObject.getString("Start"));
                Log.e("Finish =", jsonObject.getString("Finish"));
                Log.e("Price =", jsonObject.getString("Price"));

            }
            return jsonArray;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONException", e.getMessage().toString());
            return null;
        }
    }

    @Override
    public JSONArray getJsonFromServiceNameAndPrice(String ServiceNamePrice) {
        try {
            JSONObject json = new JSONObject(ServiceNamePrice);
            JSONArray jsonArray = json.getJSONArray("service");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.e("name =", jsonObject.getString("name"));
                Log.e("Price =", jsonObject.getString("Price"));
                Log.e("quantity =", jsonObject.getString("quantity"));

            }
            return jsonArray;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONException", e.getMessage().toString());
            return null;
        }
    }

    @Override
    public String calculatePriceByHour(JSONArray PriceArray, Integer Hour) {
        try {
        for (int i = 0; i < PriceArray.length(); i++) {
            JSONObject jsonObject = PriceArray.getJSONObject(i);
            Log.e("Start =", jsonObject.getString("Start"));
            Log.e("Finish =", jsonObject.getString("Finish"));
            Log.e("Price =", jsonObject.getString("Price"));
            if(Hour<0){
                Log.e("Hour","<0");
                Log.e("Price =", String.valueOf(Integer.parseInt(PriceArray.getJSONObject(PriceArray.length()-1).getString("Price"))*Hour));
                return PriceArray.getJSONObject(0).getString("Price");

            }
            else if(Hour>=Integer.parseInt(jsonObject.getString("Start")) && Hour<=Integer.parseInt(jsonObject.getString("Finish"))){
                Log.e("Price =", String.valueOf(Hour*Integer.parseInt(jsonObject.getString("Price"))));
                return jsonObject.getString("Price");
            }else if (Hour>=24){
                Log.e("Hour",">24");
                Log.e("Price =", String.valueOf(Integer.parseInt(PriceArray.getJSONObject(PriceArray.length()-1).getString("Price"))*Hour));
                return PriceArray.getJSONObject(PriceArray.length()-1).getString("Price");
            }
        }} catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONException", e.getMessage().toString());
            return null;
        }
        return null;
    }

    @Override
    public String calculatePriceByService(JSONArray PriceArray) {
        try {
            Integer totalServicePrice=0;

            for (int i = 0; i < PriceArray.length(); i++) {
                JSONObject jsonObject = PriceArray.getJSONObject(i);
                Integer tempQuantity = Integer.valueOf(jsonObject.getString("quantity"));
                Integer tempPrice = Integer.valueOf(jsonObject.getString("Price"));
                Log.e("name =", jsonObject.getString("name"));
                Log.e("Price =", jsonObject.getString("Price"));
                Log.e("quantity =", jsonObject.getString("quantity"));
                if(tempQuantity>0){
                    totalServicePrice =  (tempQuantity*tempPrice) + totalServicePrice  ;
                    Log.e( "PriceByService", String.valueOf(tempQuantity*tempPrice));
                }

            }
            return String.valueOf(totalServicePrice);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONException", e.getMessage().toString());
            return null;
        }

    }

    public Integer calculateHour(String table, String CardID) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date parsedDateEnter = formatter.parse(readCardEnterDateFromTable(table, CardID));
            Date parsedDateExit = formatter.parse(readCardExitDateFromTable(table, CardID));
            long secs = (parsedDateExit.getTime() - parsedDateEnter.getTime()) / 1000;
            int hours = (int) (secs / 3600);
            return hours;
        } catch (ParseException e) {
            e.printStackTrace();
        }
return null;
    }

    @Override
    public List<String> getFinishedFields() {
        SQLiteDatabase db = mDBhelper.getReadableDatabase();
        Cursor mCursor =
                db.rawQuery("SELECT * FROM'" + NFC_CARDS_TABLE_NAME + "' WHERE " + NFC_CARDS_EXIT_AT + "    IS NOT NULL ;", null);
              //  db.rawQuery("SELECT * FROM'" + NFC_CARDS_TABLE_NAME + "' ;", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        List<String> strings=new ArrayList<String>();
        for(int i=0;mCursor.getCount()>i;i++){

            strings.add("CAR_ID = "
                    + mCursor.getString(mCursor.getColumnIndex(CAR_ID))
                    +"\n "
                    +
                    "NFC_CARDS_ENTER_AT = "
                    + mCursor.getString(mCursor.getColumnIndex(NFC_CARDS_ENTER_AT))+

                            " \n"
                    +
                    "NFC_CARDS_EXIT_AT = " + mCursor.getString(mCursor.getColumnIndex(NFC_CARDS_EXIT_AT)) +   " \n\n\n");

            mCursor.moveToNext();
            Log.e("getFinishedFields", strings.get(i));
        }
        return strings;
    }

    @Override
    public List<String> getUnFinishedFields() {
        SQLiteDatabase db = mDBhelper.getReadableDatabase();
        Cursor mCursor =
                db.rawQuery("SELECT * FROM'" + NFC_CARDS_TABLE_NAME + "' WHERE " + NFC_CARDS_EXIT_AT + "    IS  NULL ;", null);
        //  db.rawQuery("SELECT * FROM'" + NFC_CARDS_TABLE_NAME + "' ;", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        List<String> strings=new ArrayList<String>();
        for(int i=0;mCursor.getCount()>i;i++){

            strings.add("CAR_ID = "
                    + mCursor.getString(mCursor.getColumnIndex(CAR_ID))
                    +"\n "
                    +
                    "NFC_CARDS_ENTER_AT = "
                    + mCursor.getString(mCursor.getColumnIndex(NFC_CARDS_ENTER_AT))+

                     " \n\n\n");

            mCursor.moveToNext();
            Log.e("getFinishedFields", strings.get(i));
        }
        return strings;
    }
}

