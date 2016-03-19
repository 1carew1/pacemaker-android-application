package org.pacemaker.controllers;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.pacemaker.R;
import org.pacemaker.models.MyActivity;


public class ShowMyActivity extends Activity {

    private MyActivity selectedActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my);

        //Get the MyActivity from the Activity List
        Gson gS = new Gson();
        String target = getIntent().getStringExtra("SelectedActivity");
        selectedActivity = gS.fromJson(target, MyActivity.class);
        Toast toast = Toast.makeText(this, selectedActivity.toString(), Toast.LENGTH_SHORT);
        toast.show();

        //Create a way of parsing the date
        String dateString = selectedActivity.startTime;
        //Temporary Fix to parse dates
        // TODO : Implement better method of date parsing
        if (dateString.contains("+")) {
            dateString = dateString.replaceAll("\\+\\d{2}\\:\\d{2}", "");
        }
        String dateFormatter = "EEE MMM dd HH:mm:ss z yyyy";


        DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormatter);
        DateTime dt = formatter.parseDateTime(dateString);

        //Set the date in the date picker
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.updateDate(dt.getYear(), dt.getMonthOfYear() - 1, dt.getDayOfMonth());

    }

}
