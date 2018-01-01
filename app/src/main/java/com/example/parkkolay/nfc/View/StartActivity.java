package com.example.parkkolay.nfc.View;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.parkkolay.nfc.BaseClass;
import com.example.parkkolay.nfc.Model.Model;
import com.example.parkkolay.nfc.Model.Sqlite.DBHelper;
import com.example.parkkolay.nfc.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.parkkolay.nfc.Model.Sqlite.DBHelper.NFC_CARDS_COLUMN_ID;
import static com.example.parkkolay.nfc.Model.Sqlite.DBHelper.NFC_CARDS_TABLE_NAME;
import static com.example.parkkolay.nfc.Model.UtilityMethods.bytesToString;

public class StartActivity extends BaseClass {
    private NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    IntentFilter ndefIntent;
    TextView CardIDTextView;
    List<RadioButton> RadioButtons=new ArrayList<RadioButton>();
    EditText CarId;
    Tag tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        CarId=(EditText)findViewById(R.id.CarID);
        CardIDTextView=(TextView)findViewById(R.id.CardIDTextView);
        model.getUnFinishedFields();
//        model.existCardToTable("asd","adda","asdawd","asdw");
        InitNFC();

    }
    @Override
    public void onResume() {
        super.onResume();
        model.ifTarifeNameNotExistedBefore("tarife1");
        if (mNfcAdapter != null){
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
        }
        if(!model.ifTarifeTableIsNotEmpty()){
            Intent TarifeActivityintent = new Intent(getApplicationContext(), TarifeActivity.class);
            startActivity(TarifeActivityintent);
        }


        createView();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        tag=null;
        String action = intent.getAction();
        if(!model.ifTarifeTableIsNotEmpty()){
            Intent TarifeActivityintent = new Intent(getApplicationContext(), TarifeActivity.class);
            startActivity(TarifeActivityintent);
        }

         tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(tag!=null){
            CardIDTextView.setText(bytesToString(tag.getId()));
            Log.e("Card IDD",bytesToString(tag.getId()));
            if(model.ifCardExistedBefore(NFC_CARDS_TABLE_NAME,bytesToString(tag.getId())) && !model.ifExistedCardHaveExitTime(NFC_CARDS_TABLE_NAME,bytesToString(tag.getId())) ){
                Intent CalculateActivity = new Intent(getApplicationContext(), CalculateActivity.class);
                Log.e("CalculateActivity","if is true for intent");
                CalculateActivity.putExtra("CARD_ID", bytesToString(tag.getId()));
                startActivity(CalculateActivity);
            }
        }






    }

    private void InitNFC(){
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this,
                0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        ndefIntent = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[] { ndefIntent };
        } catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }
        mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };
    }

    RadioGroup radioGroupView;
    public void createView(){
         radioGroupView = (RadioGroup) findViewById(R.id.TarifeRadioGroup);
        LayoutInflater inflater =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        radioGroupView.removeAllViews();
        View[] myView = null;
        int ii=0;
        List<String> strings=new ArrayList<String>();
        strings=model.readTarifesFromTarifeTable();
        RadioButton radioButton;

        RadioButton radioButtonView;
        for(String i : strings){
             radioButtonView = new  RadioButton(this);
            radioButtonView.setId(ii);
            ii++;
            radioButtonView.setText(i);

            radioButtonView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.e("compoundButton.getId()", String.valueOf(compoundButton.getId()));
                    radioGroupView.clearCheck();
                }
            });
         //   RadioButtons.add(radioButton);
         /*   Log.e("i",i);
            if(myView.getParent()!=null)
                ((ViewGroup)myView.getParent()).removeView(myView); // <- fix

            */
            radioGroupView.addView(radioButtonView);
        }

       //radioGroupView.addView(myView);


    }

    public void addTarife(View view) {
        Intent TarifeActivityintent = new Intent(getApplicationContext(), TarifeActivity.class);
        startActivity(TarifeActivityintent);
    }

    public void getRAdioGroup(View view) {
        WitchRadioButtonSelected();
    }
    public String WitchRadioButtonSelected() {
        Log.e("getCheckedRadioButtonId", String.valueOf(radioGroupView.getCheckedRadioButtonId()));
        RadioButton r = (RadioButton)  radioGroupView.getChildAt(radioGroupView.getCheckedRadioButtonId());
        if(r!=null){
            String selectedtext = r.getText().toString();

            Log.e("getCheckedRadioButtonId", selectedtext);
            return selectedtext;
        }
        return null;
    }
    public void AddToDataBase(View view) {
        if(!(CarId.getText().toString().equals(""))&& tag!=null && WitchRadioButtonSelected()!=null){
            model.existCardToTable(NFC_CARDS_TABLE_NAME,
                    bytesToString(tag.getId()),
                    model.readPriceFromTarifeTable(WitchRadioButtonSelected()),
                    CarId.getText().toString());
           ////////Make SUre to added to data base;

        }else {
            Log.e("AddToDataBase","LackInInput");
        }

    }

    public void ADDService(View view) {
        Intent ADDServiceForParking = new Intent(getApplicationContext(), ADDServiceForParking.class);
        startActivity(ADDServiceForParking);
    }

    public void getFinishidFields(View view) {
        Intent FinishedFields = new Intent(getApplicationContext(), FinishedFields.class);
        startActivity(FinishedFields);
    }
    public void getUnFinishidFields(View view) {
        Intent UnFinishidFields = new Intent(getApplicationContext(), UnFinishedFields.class);
        startActivity(UnFinishidFields);
    }
}
