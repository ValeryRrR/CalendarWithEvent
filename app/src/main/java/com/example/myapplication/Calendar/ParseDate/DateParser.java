package com.example.myapplication.Calendar.ParseDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateParser {

    public static String formatDate(String Date, String fromPattern, String toPattern){
        SimpleDateFormat dateFormat = new SimpleDateFormat(fromPattern, Locale.getDefault());
        SimpleDateFormat out = new SimpleDateFormat(toPattern, Locale.getDefault());
        String formattedDate = "";

        try {
            Date res = dateFormat.parse(Date);
            formattedDate = out.format(res);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }
}
