package com.example.parkkolay.nfc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.parkkolay.nfc.Model.Model;
import com.example.parkkolay.nfc.Model.Sqlite.DBHelper;

/**
 * Created by parkkolay on 4.12.2017.
 */

public class BaseClass extends AppCompatActivity {
    public Model model;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new Model(new DBHelper(this));
        //model.insertNewServiceInServiceTable("Service00","0");
       // model.insertNewServiceInServiceTable("Service01","1");

    }
}
