package com.example.parkkolay.nfc.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.parkkolay.nfc.BaseClass;
import com.example.parkkolay.nfc.R;

import java.util.ArrayList;
import java.util.List;

public class ADDServiceForParking extends BaseClass {
    Integer OldDataBaseFieldQuantity;
    List<EditText> ServiceNameEditTexts;
    List<EditText> ServicePriceEditTexts;
    List<String> ServiceStrings;
    List<String> PriceStrings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addservice_for_parking);


        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateViewManualy();
                Snackbar.make(view, "ADD Service fafTo Parking (Feature)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createAndUpdateDatabase();
            }
        });
    }
    @Override
    protected void onResume() {
        OldDataBaseFieldQuantity=0;
        PriceStrings=new ArrayList<String>();
        ServiceStrings=new ArrayList<String>();
        ServicePriceEditTexts=new ArrayList<EditText>();
        ServiceNameEditTexts=new ArrayList<EditText>();
        CreateViewFromDataBase();
        super.onResume();
    }

    private void CreateViewManualy() {

        LinearLayout mContainerView = (LinearLayout) findViewById(R.id.ParentView_AddService_To_Parking);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.add_service_for_parking_item, null);
        ServiceNameEditTexts.add((EditText)myView.findViewById(R.id.ServiceNameEditText));
        ServicePriceEditTexts.add((EditText)myView.findViewById(R.id.ServicePriceEditText));
        mContainerView.addView(myView);
    }


   private void CreateViewFromDataBase(){

       LinearLayout mContainerView = (LinearLayout) findViewById(R.id.ParentView_AddService_To_Parking);

       LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);

       ServiceStrings=model.readServicesFromServiceTable();
       PriceStrings=model.readPricesFromServiceTable();
Integer ii=0;
       for(String i : ServiceStrings){
           View myView = inflater.inflate(R.layout.add_service_for_parking_item, null);
           ServiceNameEditTexts.add((EditText)myView.findViewById(R.id.ServiceNameEditText));
           ServicePriceEditTexts.add((EditText)myView.findViewById(R.id.ServicePriceEditText));
           Log.e("i=",i + " "+ PriceStrings.get(ii) +"  " + ii.toString() );
           ServiceNameEditTexts.get(ii).setText(i);
           ServicePriceEditTexts.get(ii).setText(PriceStrings.get(ii));
           ii++;

       Log.e("CreateViewFromDataBase", String.valueOf(mContainerView.getId()));
       mContainerView.addView(myView);
           OldDataBaseFieldQuantity=ii;

   }}
   private void createAndUpdateDatabase(){
           for(int i=0;ServiceNameEditTexts.size()>i;i++){
            /*   Log.e("ServiceStrings.size()", String.valueOf(ServiceStrings.size()));
               Log.e("ServiceNameEditT.size()", String.valueOf(ServiceNameEditTexts.size()));
               Log.e("ServiceStrings", String.valueOf(ServiceStrings.get(i)));
               Log.e("ServiceStrings", String.valueOf(ServiceNameEditTexts.get(i).getText()));
               Log.e("PriceString", String.valueOf(PriceStrings.get(i)));
               Log.e("PriceString", String.valueOf(ServicePriceEditTexts.get(i).getText()));
               Log.e("ServiceStrings.equals()", String.valueOf(ServiceStrings.get(i).equals(ServiceNameEditTexts.get(i).getText().toString())));
               Log.e("PriceStrings.equals()", String.valueOf(PriceStrings.get(i).equals(ServicePriceEditTexts.get(i).getText().toString())));*/
            if(i<ServiceStrings.size() &&
                    (!(ServiceStrings.get(i).equals(ServiceNameEditTexts.get(i).getText().toString())) || !(PriceStrings.get(i).equals(ServicePriceEditTexts.get(i).getText().toString()) ))){
                model.UpdateServiceNameAndPrice(ServiceStrings.get(i).toString(),ServiceNameEditTexts.get(i).getText().toString(),ServicePriceEditTexts.get(i).getText().toString());
                Log.e("Testif","Update");

            }else if(i<ServiceStrings.size() &&
                    ((ServiceStrings.get(i).equals(ServiceNameEditTexts.get(i).getText().toString())) && (PriceStrings.get(i).equals(ServicePriceEditTexts.get(i).getText().toString()) ))){
                Log.e("Testif","Do Nothing");
            //    model.insertNewServiceInServiceTable(ServiceNameEditTexts.get(i).getText().toString(),ServicePriceEditTexts.get(i).getText().toString());

            }else{
                   Log.e("Testif","insert");
                   model.insertNewServiceInServiceTable(ServiceNameEditTexts.get(i).getText().toString(),ServicePriceEditTexts.get(i).getText().toString());

               }
           }
    }

}
