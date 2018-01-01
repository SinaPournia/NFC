package com.example.parkkolay.nfc.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.parkkolay.nfc.BaseClass;
import com.example.parkkolay.nfc.R;

public class FinishedFields extends BaseClass {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_fields);
        TextView textView= (TextView) findViewById(R.id.textview);
        textView.setText(model.getFinishedFields().toString());
    }
}
