package com.example.parkkolay.nfc.View;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.parkkolay.nfc.BaseClass;
import com.example.parkkolay.nfc.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.parkkolay.nfc.Model.Sqlite.DBHelper.CARD_ID;

public class AddServiceToClient extends BaseClass {
    Integer OldDataBaseFieldQuantity;
    List<TextView> ServiceNameTextViews;
    List<TextView> ServicePriceTextViews;
    List<EditText> ServiceQuantityTextViews;;
    List<String> ServiceStrings;
    List<String> PriceStrings;
    String cardID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service_to_client);
        Bundle extras = getIntent().getExtras();

        if(extras == null) {
            cardID= null;
        } else {
            cardID= extras.getString(CARD_ID);
            Log.e("Calculate"+CARD_ID +"=",cardID);
        }



    }
    @Override
    protected void onResume() {
        ServiceNameTextViews = new ArrayList<TextView>();
        ServicePriceTextViews= new ArrayList<TextView>();
        ServiceQuantityTextViews= new ArrayList<EditText>();
        ServiceStrings= new ArrayList<String>();
        PriceStrings= new ArrayList<String>();
        CreateViewFromDataBase();
        super.onResume();
    }
    private void CreateViewFromDataBase(){

        LinearLayout mContainerView = (LinearLayout) findViewById(R.id.ParentView_AddService_To_Parking);

        LayoutInflater inflater =(LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ServiceStrings=model.readServicesFromServiceTable();
        PriceStrings=model.readPricesFromServiceTable();
        Integer ii=0;
        for(String i : ServiceStrings){
            View myView = inflater.inflate(R.layout.add_service_to_client_item, null);
            ServiceNameTextViews.add((TextView) myView.findViewById(R.id.ServiceNameTextView));
            ServicePriceTextViews.add((TextView)myView.findViewById(R.id.ServicePriceTextView));
            ServiceQuantityTextViews.add((EditText) myView.findViewById(R.id.ServiceQuantityEditText));
            Log.e("i=",i + " "+ PriceStrings.get(ii) +"  " + ii.toString() );
            ServiceNameTextViews.get(ii).setText(i);
            ServicePriceTextViews.get(ii).setText(PriceStrings.get(ii));
            ServiceQuantityTextViews.get(ii).setText("0");
            ii++;

            Log.e("CreateViewFromDataBase", String.valueOf(mContainerView.getId()));
            mContainerView.addView(myView);
            OldDataBaseFieldQuantity=ii;

        }}
    public void ADDservice(View view){
        generateTarifeString();
        model.writeServicesToNFCcardsToTable( generateTarifeString(),cardID);
        Log.e("addService","Added");

    }
    private String generateTarifeString(){

            StringBuilder sb = new StringBuilder();
            sb.append("{\"service\": [");
            for(int i=0;ServiceStrings.size()-1>=i;i++){
                sb.append("{");
                sb.append("\"name\":");
                sb.append(ServiceStrings.get(i).toString());
                sb.append(",");
                sb.append("\"Price\":");
                sb.append(PriceStrings.get(i).toString());
                sb.append(",");
                sb.append("\"quantity\":");
                sb.append(ServiceQuantityTextViews.get(i).getText().toString());
                if(ServiceStrings.size()-2>=i){
                    sb.append("},");
                    Log.e("},","},");
                }else{
                    sb.append("}");
                    Log.e("}","}");
                }

            }
            sb.append("]  }");
            Log.e("generateServiceString",sb.toString());
            model.writeServicesToNFCcardsToTable(sb.toString(),cardID);
            model.getJsonFromServiceNameAndPrice(sb.toString());
            return sb.toString();


    }
}
