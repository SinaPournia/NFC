package com.example.parkkolay.nfc.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.parkkolay.nfc.BaseClass;
import com.example.parkkolay.nfc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.parkkolay.nfc.Model.Sqlite.DBHelper.CARD_ID;
import static com.example.parkkolay.nfc.Model.Sqlite.DBHelper.NFC_CARDS_TABLE_NAME;
import static com.example.parkkolay.nfc.Model.UtilityMethods.getDateTime;

public class CalculateActivity extends BaseClass {
    TextView enterDate;
    TextView exitDate;
    TextView price;
    TextView Services;
    String cardID;
    @Override
    protected void onResume() {
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            cardID= null;
        } else {
            cardID= extras.getString(CARD_ID);
            Log.e("Calculate"+CARD_ID +"=",cardID);
        }
        enterDate.setText(model.readCardEnterDateFromTable(NFC_CARDS_TABLE_NAME,cardID));
        exitDate.setText(getDateTime());
        price.setText(makeTextforPriceText());
        if(model.ifServicePriceForEachCardIsNotNull(cardID)){
            //   Services.setText((CharSequence) model.getJsonFromServiceNameAndPrice(model.readServicePriceForEachCard(cardID)));
            StringBuilder sb = new StringBuilder();
            JSONArray mjsonarray= model.getJsonFromServiceNameAndPrice(model.readServicePriceForEachCard(cardID));
            for(int i=0;mjsonarray.length()>=i;i++){
                try {
                    JSONObject mjsonObject=mjsonarray.getJSONObject(i);
                    if(!mjsonObject.get("quantity").equals("0")){
                        sb.append("name = ");
                        sb.append(mjsonObject.getString("name"));
                        sb.append(" Price = ");
                        sb.append(mjsonObject.getString("Price"));
                        sb.append(" quantity = ");
                        sb.append(mjsonObject.getString("quantity"));
                        sb.append("\n");
                        Log.e("mjsonarray=",sb.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("JSONException=", e.getMessage().toString());
                }

            }
            sb.append(" HourPrice = ");
            sb.append(getTotalHourPrice());
            Services.setText(sb.toString());
        }else{
            Log.e("Null","model.ifServicePriceForEachCardIsNotNull(cardID)");
        }
        super.onResume();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        enterDate = (TextView)findViewById(R.id.enterDate);
        exitDate = (TextView)findViewById(R.id.exitDate);
        price = (TextView)findViewById(R.id.price);
        Services=(TextView)findViewById(R.id.Services);
        Bundle extras = getIntent().getExtras();

        if(extras == null) {
            cardID= null;
        } else {
            cardID= extras.getString(CARD_ID);
            Log.e("Calculate"+CARD_ID +"=",cardID);
        }
        enterDate.setText(model.readCardEnterDateFromTable(NFC_CARDS_TABLE_NAME,cardID));
        exitDate.setText(getDateTime());
   //    Log.e("calculatePriceByService",model.calculatePriceByService(model.getJsonFromServiceNameAndPrice(model.readServicePriceForEachCard(cardID))));
       // price.setText(makeTextforPriceText());
        if(model.ifServicePriceForEachCardIsNotNull(cardID)){
         //   Services.setText((CharSequence) model.getJsonFromServiceNameAndPrice(model.readServicePriceForEachCard(cardID)));
            model.getJsonFromServiceNameAndPrice(model.readServicePriceForEachCard(cardID));
        }else{
            Log.e("Null","model.ifServicePriceForEachCardIsNotNull(cardID)");
        }
    }

    public void Cancel(View view) {
        Intent TarifeActivityintent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(TarifeActivityintent);
    }

    public void Finish(View view) {
        model.writeCardExitDateToTable(NFC_CARDS_TABLE_NAME,cardID);
        Intent TarifeActivityintent = new Intent(getApplicationContext(), StartActivity.class);
        startActivity(TarifeActivityintent);
    }
    public void ServiceEkle(View view) {
        Intent AddServiceToClient = new Intent(getApplicationContext(), AddServiceToClient.class);
        AddServiceToClient.putExtra(CARD_ID,cardID);
        startActivity(AddServiceToClient);

    }
    private Integer calculateHourCalculator() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date parsedDateEnter = formatter.parse(enterDate.getText().toString());
            Date parsedDateExit = formatter.parse(exitDate.getText().toString());
           // Date parsedDateExit = formatter.parse("2017-12-06 14:15:21");// for manual test
            long secs = (parsedDateExit.getTime() - parsedDateEnter.getTime()) / 1000;
            int hours = (int) (secs / 3600);
            return hours;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

     private String makeTextforPriceText(){
       String jsonString= model.readTarifePriceForEachCard(cardID);

         Integer tempPriceByService=0;
         if(model.ifServicePriceForEachCardIsNotNull(cardID)){
             String tempServicePrice= model.readServicePriceForEachCard(cardID);
             JSONArray tempJsonPriceByService= model.getJsonFromServiceNameAndPrice(tempServicePrice);
              tempPriceByService= Integer.valueOf(model.calculatePriceByService(tempJsonPriceByService));
         }

         JSONArray tempJsonPriceByTarifeFiyatlari=model.getJsonFromTarifeFiyatlari(jsonString);

         Integer tempPriceByhour= Integer.valueOf(model.calculatePriceByHour(tempJsonPriceByTarifeFiyatlari,calculateHourCalculator()));

         Log.e("(PriceByService*Byhour)",String.valueOf(tempPriceByService+tempPriceByhour));
       return String.valueOf(tempPriceByService+tempPriceByhour);
     }
     private String getTotalHourPrice(){
         String jsonString= model.readTarifePriceForEachCard(cardID);
         JSONArray jsonArray=model.getJsonFromTarifeFiyatlari(jsonString);
         Integer tempPriceByhour= Integer.valueOf(model.calculatePriceByHour(jsonArray,calculateHourCalculator()));
        return String.valueOf(tempPriceByhour);
     }
}
