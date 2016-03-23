package org.pacemaker.controllers;

import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.pacemaker.R;
import org.pacemaker.http.Response;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.MyActivity;
import org.pacemaker.utils.MyDatePicker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.List;

public class CreateActivity extends AppCompatActivity implements Response<MyActivity> {
    private static final String TAG = "CreateActivity";

    private PacemakerApp app;

    private Button createActivityButton;
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

        createActivityButton = (Button) findViewById(R.id.createActivityButton);
        activityType = (TextView) findViewById(R.id.activityType);
        activityLocation = (TextView) findViewById(R.id.activityLocation);
        activityDuration = (TextView) findViewById(R.id.activityDuration);
        distancePicker = (NumberPicker) findViewById(R.id.numberPicker);
        datePicker = (MyDatePicker) findViewById(R.id.datePicker);


        distancePicker.setMinValue(0);
        distancePicker.setMaxValue(200);
    }

    public void createActivityButtonPressed(View view) {
        double distance = distancePicker.getValue();
        String type = activityType.getText().toString();
        String location = activityLocation.getText().toString();
        String duration = activityDuration.getText().toString();

        if (type.isEmpty() || location.isEmpty() || duration.isEmpty() || !duration.matches("\\d{1,2}\\:\\d{2}")) {
            Toast errorToast = Toast.makeText(CreateActivity.this, "Please Make sure everything is filled in correctly", Toast.LENGTH_SHORT);
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

            MyActivity activity = new MyActivity(type, location, distance, activietyDateTime, duration);

            app.createActivity(this, activity, this);
            Toast finishToast = Toast.makeText(CreateActivity.this, "Activity Created", Toast.LENGTH_SHORT);
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
        Toast toast = Toast.makeText(this, "Failed to create Activity", Toast.LENGTH_SHORT);
        toast.show();
    }
}
