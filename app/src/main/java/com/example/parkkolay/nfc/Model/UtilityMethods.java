package com.example.parkkolay.nfc.Model;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by parkkolay on 30.11.2017.
 */

public class UtilityMethods {
    public static String bytesToString(byte[] src) {
       return new BigInteger(src).toString();
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
