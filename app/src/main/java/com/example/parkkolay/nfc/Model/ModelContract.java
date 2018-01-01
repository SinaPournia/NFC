package com.example.parkkolay.nfc.Model;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by parkkolay on 30.11.2017.
 */

public interface ModelContract {
    public void createNewSubTarifeTable(); // ok
    public void createTarifeTable();// ok
    public void createServiceTable();// ok
    public void insertNewServiceInServiceTable(String ServiceName,String price);// ok
    public void insertNewTarifeInTarifeTable(String TarifeName,String price);// ok
    public String readPriceFromTarifeTable(String Tarife);// ok
    public String readPriceFromServiceTable(String Tarife);// ok
    public String readTarifePriceForEachCard(String CardID);// ok
    public String readServicePriceForEachCard(String CardID);// ok
    public boolean ifServicePriceForEachCardIsNotNull(String CardID);// ok
    public boolean ifServiceTableIsNotEmpty();// ok
    public boolean ifTarifeTableIsNotEmpty();// ok
    public boolean ifTarifeNameNotExistedBefore(String tarife);// ok
    public boolean ifServiceNameNotExistedBefore(String ServiceName);// ok
    public void UpdateServiceNameAndPrice(String OldServiceName,String ServiceName,String ServicePrice);// ok
    public List<String> readTarifesFromTarifeTable();// ok
    public List<String> readServicesFromServiceTable();// ok
    public List<String> readPricesFromServiceTable();// ok
    public String readCardEnterDateFromTable(String table,String CardID);// ok
    public String readCardExitDateFromTable(String table,String CardID);// ok
    public void writeServicesToNFCcardsToTable(String Services, String CardID);// ok
    public void writeCardExitDateToTable(String table,String CardID);// ok
    public void existCardToTable(String table,String CardID,String TarifeFiyatListesi,String CarNumber);//ok
    public boolean ifCardExistedBefore(String table,String CardID);//ok
    public boolean ifExistedCardHaveExitTime(String table,String CardID);//ok
    public JSONArray getJsonFromTarifeFiyatlari(String TarifeFiyatlari);//ok
    public JSONArray getJsonFromServiceNameAndPrice(String ServiceNamePrice);//ok
    public String calculatePriceByHour(JSONArray PriceArray, Integer Hour);// ok
    public String calculatePriceByService(JSONArray PriceArray);// ok
    public Integer calculateHour(String table,String CardID);// ok


    public List<String> getFinishedFields();
    public List<String> getUnFinishedFields();

}
