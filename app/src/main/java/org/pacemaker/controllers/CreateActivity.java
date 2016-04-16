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
import org.pacemaker.utils.PacemakerENUMs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.List;

public class CreateActivity extends AppCompatActivity implements Response<MyActivity> {
    private static final String TAG = "CreateActivity";

    //Application for Common/Global Data
    private PacemakerApp app;

    //Views Corresponding to XML
    private TextView activityType;
    private TextView activityLocation;
    private NumberPicker activityDurationHour;
    private NumberPicker activityDurationMinute;
    private NumberPicker activityTimeHour;
    private NumberPicker activityTimeMinute;
    private NumberPicker distancePicker;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        app = (PacemakerApp) getApplication();
        //Assign correct View to private variable
        activityType = (TextView) findViewById(R.id.activityType);
        activityLocation = (TextView) findViewById(R.id.activityLocation);
        activityDurationHour = (NumberPicker) findViewById(R.id.durationPickerHour);
        activityDurationMinute = (NumberPicker) findViewById(R.id.durationPickerMinute);
        activityTimeHour = (NumberPicker) findViewById(R.id.timePickerHour);
        activityTimeMinute = (NumberPicker) findViewById(R.id.timePickerMinute);
        distancePicker = (NumberPicker) findViewById(R.id.distnacePicker);
        datePicker = (MyDatePicker) findViewById(R.id.datePicker);

        //Set Max and Min number picker values
        distancePicker.setMinValue(0);
        distancePicker.setMaxValue(200);
        activityDurationHour.setMinValue(0);
        activityDurationHour.setMaxValue(23);
        activityDurationMinute.setMinValue(0);
        activityDurationMinute.setMaxValue(59);
        activityTimeHour.setMinValue(0);
        activityTimeHour.setMaxValue(23);
        activityTimeMinute.setMinValue(0);
        activityTimeMinute.setMaxValue(59);
    }

    /**
     * When create activity button is pressed, some sanity checking + Activity Creation
     * @param view
     */
    public void createActivityButtonPressed(View view) {

        MyActivity activity = new MyActivity();
        activity = org.pacemaker.utils.ActivtyUtils.changeActivity(this, activity,
                distancePicker.getValue(), activityType.getText().toString(), activityLocation.getText().toString(),
                activityDurationHour.getValue(), activityDurationMinute.getValue(),
                datePicker, activityTimeHour.getValue(), activityTimeMinute.getValue());
        //Ensure that all views are filled in correctly
        if (!activity.kind.equals(PacemakerENUMs.INCORRECTCHANGE.toString())) {
            app.createActivity(this, activity, this);
            Toast finishToast = Toast.makeText(CreateActivity.this, "Activity Created", Toast.LENGTH_SHORT);
            finishToast.show();
            finish();
        }
    }


    /**
     * If response activity of this is a list of Activities - Run this method
     * @param aList
     */
    @Override
    public void setResponse(List<MyActivity> aList) {
    }

    /**
     * If response is going to be a single activity use this method
     * @param anObject
     */
    @Override
    public void setResponse(MyActivity anObject) {
    }

    /**
     * Upon and error log it + toast to the user
     * @param e
     */
    @Override
    public void errorOccurred(Exception e) {
        Toast toast = Toast.makeText(this, "Failed to create Activity", Toast.LENGTH_SHORT);
        toast.show();
    }
}
