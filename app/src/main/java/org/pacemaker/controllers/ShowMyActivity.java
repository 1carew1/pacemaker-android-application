package org.pacemaker.controllers;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.pacemaker.R;
import org.pacemaker.http.Response;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.MyActivity;

import java.util.List;


public class ShowMyActivity extends AppCompatActivity implements Response<MyActivity> {
    private static final String TAG = "UpdateActivity";

    private MyActivity selectedActivity;
    private PacemakerApp app;

    private Button updateActivityButton;
    private TextView activityType;
    private TextView activityLocation;
    private TextView activityDuration;
    private NumberPicker distancePicker;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        app = (PacemakerApp) getApplication();

        updateActivityButton = (Button) findViewById(R.id.createActivityButton);
        activityType = (TextView) findViewById(R.id.activityType);
        activityLocation = (TextView) findViewById(R.id.activityLocation);
        activityDuration = (TextView) findViewById(R.id.activityDuration);
        distancePicker = (NumberPicker) findViewById(R.id.numberPicker);
        datePicker = (DatePicker) findViewById(R.id.datePicker);

        //Get the MyActivity from the Activity List
        Gson gS = new Gson();
        String target = getIntent().getStringExtra("SelectedActivity");
        selectedActivity = gS.fromJson(target, MyActivity.class);
        Toast toast = Toast.makeText(this, selectedActivity.routes.toString(), Toast.LENGTH_SHORT);
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
        datePicker.updateDate(dt.getYear(), dt.getMonthOfYear() - 1, dt.getDayOfMonth());

        activityType.setText(selectedActivity.kind);
        activityLocation.setText(selectedActivity.location);
        activityDuration.setText(selectedActivity.duration);

        distancePicker.setMinValue(0);
        distancePicker.setMaxValue(200);
        distancePicker.setValue((int) selectedActivity.distance);

        //Remove the list view option as the same xml is being used for Edit + Create
        updateActivityButton.setText("Update Activity");
    }

    public void listActivityButtonPressed(View view) {
        //Do nothing if this button is pressed
    }


    public void createActivityButtonPressed(View view) {
        double distance = distancePicker.getValue();
        String type = activityType.getText().toString();
        String location = activityLocation.getText().toString();
        String duration = activityDuration.getText().toString();

        if (type.isEmpty() || location.isEmpty() || duration.isEmpty() || !duration.matches("\\d{1,2}\\:\\d{2}")) {
            Toast errorToast = Toast.makeText(this, "Please Make sure everything is filled in correctly", Toast.LENGTH_SHORT);
            errorToast.show();
        } else {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1; //Month starts from 0
            int year = datePicker.getYear();
            int hour = 0;
            int minutes = 0;
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
            String dateTime = year + "-" + month + "-" + day + " " + hour + ":" + minutes;
            DateTime activietyDateTime = formatter.parseDateTime(dateTime);

            selectedActivity.startTime = activietyDateTime.toDate().toString();
            selectedActivity.kind = type;
            selectedActivity.distance = distance;
            selectedActivity.duration = duration;
            selectedActivity.location = location;

            app.updateActivity(this, selectedActivity, this);
            Toast finishToast = Toast.makeText(ShowMyActivity.this, "Activity Updated", Toast.LENGTH_SHORT);
            finishToast.show();
            finish();
        }

    }



    @Override
    public void setResponse(List<MyActivity> aList) {
    }

    @Override
    public void setResponse(MyActivity anObject) {
    }

    @Override
    public void errorOccurred(Exception e) {
        Toast toast = Toast.makeText(this, "Failed to update Activity", Toast.LENGTH_SHORT);
        Log.i(TAG, e.getLocalizedMessage());
        toast.show();
    }


}
