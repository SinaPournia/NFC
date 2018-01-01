package com.example.parkkolay.nfc.View;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.parkkolay.nfc.BaseClass;
import com.example.parkkolay.nfc.Model.Model;
import com.example.parkkolay.nfc.Model.Sqlite.DBHelper;
import com.example.parkkolay.nfc.R;

import java.util.ArrayList;
import java.util.List;

public class TarifeActivity extends BaseClass {
    ArrayAdapter<String> dataAdapter;
    Button addButton;
    Button hesabButton;
    EditText TarifeName;
    List<Spinner> leftSpinners=new ArrayList<Spinner>();
    List<Spinner> righttSpinners=new ArrayList<Spinner>();
    List<EditText> editTexts=new ArrayList<EditText>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarife);
        addButton=(Button)findViewById(R.id.AddButton) ;
        TarifeName=(EditText)findViewById(R.id.TArifeNameEditText);
         model.createTarifeTable();

      /*  String price="{\"tarife\": [{\"Start\":0,\"Finish\":1,\"Price\":3},{\"Start\":1,\"Finish\":2,\"Price\":4},{\"Start\":2,\"Finish\":10,\"Price\":5},{\"Start\":10,\"Finish\":11,\"Price\":8},{\"Start\":11,\"Finish\":12,\"Price\":8},{\"Start\":12,\"Finish\":24,\"Price\":9}]  }";
        Log.e("Price", model.getJsonFromTarifeFiyatlari(price).toString());
         Log.e("LastPrice", model.calculatePrice(model.getJsonFromTarifeFiyatlari(price),5).toString());
      Log.e("ifTarifeTableIsNotEmpty", String.valueOf(model.ifTarifeTableIsNotEmpty()));
      model.insertNewTarifeInTarifeTable("TarifeTest","PriceTEst");*/
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (righttSpinners.get(righttSpinners.size()-1).getSelectedItem() !=null && !(Integer.parseInt(righttSpinners.get(righttSpinners.size()-1).getSelectedItem().toString())== 24) && checkIfEditTextNotNullAndBiggerThanLastOne()  ){
                    createView(Integer.parseInt(righttSpinners.get(righttSpinners.size()-1).getSelectedItem().toString()));
                    Log.e(editTexts.get(editTexts.size()-1).getText().toString(),editTexts.get(editTexts.size()-1).getText().toString());

                }

                if((righttSpinners.size()>=2) ){
                    if((righttSpinners.get(righttSpinners.size()-2) !=null)){
                        righttSpinners.get(righttSpinners.size()-2).setEnabled(false);
                        leftSpinners.get(leftSpinners.size()-2).setEnabled(false);
                        leftSpinners.get(leftSpinners.size()-1).setEnabled(false);
                        editTexts.get(editTexts.size()-2).setEnabled(false);
                    }

                }

            }
        });
        hesabButton=(Button)findViewById(R.id.hesab) ;
        hesabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TarifeName.getText().toString().equals("")&& model.ifTarifeNameNotExistedBefore(TarifeName.getText().toString().toUpperCase()) && checkIfEditTextNotNullAndBiggerThanLastOne()){
                    model.insertNewTarifeInTarifeTable(TarifeName.getText().toString().toUpperCase(),generateTarifeString());
                    Intent TarifeActivityintent = new Intent(getApplicationContext(), StartActivity.class);
                    startActivity(TarifeActivityintent);
                }else{
                Log.e("TarifeName","Null Or ExistedBefore");
                }

            }
        });
        createView(0);
        leftSpinners.get(0).setEnabled(false);

    }





    public  ArrayAdapter<String> generateSpinnerData(Integer input){
        List<String> categories = new ArrayList<String>();
        for(int i = input;i<=24;i++){
            categories.add(String.valueOf(i));

        }
        return dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
    }
    public void createView(int LeftspinnerInput){
        LinearLayout mContainerView = (LinearLayout) findViewById(R.id.parentView);
        LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.spinner_item, null);
        leftSpinners.add( (Spinner) myView.findViewById(R.id.spinnerLeft));
        righttSpinners.add( (Spinner) myView.findViewById(R.id.spinnerRight));
        editTexts.add( (EditText) myView.findViewById(R.id.editText));
        ArrayAdapter<String> stringArrayAdapterLeft= generateSpinnerData(LeftspinnerInput);
        ArrayAdapter<String> stringArrayAdapterRight= generateSpinnerData(LeftspinnerInput+1);
        stringArrayAdapterLeft.remove("24");
        leftSpinners.get(leftSpinners.size()-1).setAdapter(stringArrayAdapterLeft);
        righttSpinners.get(righttSpinners.size()-1).setAdapter(stringArrayAdapterRight);
        setListenerAndGiveInputForRightSpinner();
        mContainerView.addView(myView);
    }
    public void setListenerAndGiveInputForRightSpinner(){
        final Boolean[] defaultitem = {true};
        leftSpinners.get(leftSpinners.size()-1).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int x, long l) {

                if(defaultitem[0] !=true){
                    righttSpinners.get(righttSpinners.size()-1).setEnabled(true);

                    int input=Integer.parseInt(leftSpinners.get(leftSpinners.size()-1).getSelectedItem().toString())+1;
                    righttSpinners.get(righttSpinners.size()-1).setAdapter(generateSpinnerData(input));

                }
                Log.e("ItemSelected =",x + " " + l );
                defaultitem[0] =false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                righttSpinners.get(righttSpinners.size()-1).setEnabled(false);
                Log.e("onNothingSelected ="," onNothingSelected"  );

            }


        });
    }
    private Boolean checkIfEditTextNotNullAndBiggerThanLastOne(){
       if(ifLastEditTextIsNotNull()){
           if(iflastEditTextBiggerThanOnetolast()){
              return true;

           }else{
               return false;
           }

        }else {
           Log.e("checkIfEditTextNotNull","editTexts.size()-1)==null");
           return false;
       }


    }
    private Boolean ifLastEditTextIsNotNull(){
        if((!editTexts.get(editTexts.size()-1).getText().toString().equals(""))){
                return true;

    }else{
            return false;
        }}
    private Boolean iflastEditTextBiggerThanOnetolast(){
        if(editTexts.size()>=2){
            if(Integer.parseInt(editTexts.get(editTexts.size()-1).getText().toString())
                    >= Integer.parseInt(editTexts.get(editTexts.size()-2).getText().toString())) {
                return true;
            }else{
                return false;
            }
        }else{
            return true;
        }
    }

    private String generateTarifeString(){
        if(ifLastEditTextIsNotNull()&& iflastEditTextBiggerThanOnetolast()){
        StringBuilder sb = new StringBuilder();
            sb.append("{\"tarife\": [");
        for(int i=0;editTexts.size()-1>=i;i++){
            sb.append("{");
            sb.append("\"Start\":");
            sb.append(leftSpinners.get(i).getSelectedItem().toString());
            sb.append(",");
            sb.append("\"Finish\":");
            sb.append(righttSpinners.get(i).getSelectedItem().toString());
            sb.append(",");
            sb.append("\"Price\":");
            sb.append(editTexts.get(i).getText().toString());
            if(editTexts.size()-2>=i){
                sb.append("},");
                Log.e("},","},");
            }else{
                sb.append("}");
                Log.e("}","}");
            }

        }
            sb.append("]  }");
            Log.e("generateTarifeString",sb.toString());
             model.getJsonFromTarifeFiyatlari(sb.toString());
return sb.toString();

    }else{
            Log.e("ifLastEditTextIsNotNull","=null");

            return null;
        }
        }

}
