package org.pacemaker.controllers;

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
import org.pacemaker.utils.MyDatePicker;

import java.util.List;


public class ShowMyActivity extends AppCompatActivity implements Response<MyActivity> {
    private static final String TAG = "UpdateActivity";

    private MyActivity selectedActivity;
    private PacemakerApp app;

    private Button updateActivityButton;
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

        updateActivityButton = (Button) findViewById(R.id.createActivityButton);
        activityType = (TextView) findViewById(R.id.activityType);
        activityLocation = (TextView) findViewById(R.id.activityLocation);
        activityDurationHour = (NumberPicker) findViewById(R.id.durationPickerHour);
        activityDurationMinute = (NumberPicker) findViewById(R.id.durationPickerMinute);
        activityTimeHour = (NumberPicker) findViewById(R.id.timePickerHour);
        activityTimeMinute = (NumberPicker) findViewById(R.id.timePickerMinute);
        distancePicker = (NumberPicker) findViewById(R.id.distnacePicker);
        datePicker = (MyDatePicker) findViewById(R.id.datePicker);

        //Get the MyActivity from the Activity List
        Gson gS = new Gson();
        String target = getIntent().getStringExtra("SelectedActivity");
        selectedActivity = gS.fromJson(target, MyActivity.class);

        //Create a way of parsing the date
        String dateString = selectedActivity.startTime;
        //Temporary Fix to parse dates
        // TODO : Implement better method of date parsing
        //TODO : Fix this for time parsing + Date parsing
        if (dateString.contains("+")) {
            dateString = dateString.replaceAll("\\+\\d{2}\\:\\d{2}", "");
        }
        if (dateString.contains("IST")) {
            dateString = dateString.replaceAll("IST", "GMT");
        }
        String dateFormatter = "EEE MMM dd HH:mm:ss z yyyy";


        DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormatter);
        DateTime dt = formatter.parseDateTime(dateString);

        //Set the date in the date picker
        datePicker.updateDate(dt.getYear(), dt.getMonthOfYear() - 1, dt.getDayOfMonth());

        activityType.setText(selectedActivity.kind);
        activityLocation.setText(selectedActivity.location);

        int durationHour = Integer.parseInt(selectedActivity.duration.replaceAll(":\\d{1,2}", ""));
        int durationMinute = Integer.parseInt(selectedActivity.duration.replaceAll("\\d{1,2}:", ""));

        distancePicker.setMinValue(0);
        distancePicker.setMaxValue(200);
        distancePicker.setValue((int) selectedActivity.distance);
        activityDurationHour.setMinValue(0);
        activityDurationHour.setMaxValue(23);
        activityDurationHour.setValue(durationHour);
        activityDurationMinute.setMinValue(0);
        activityDurationMinute.setMaxValue(59);
        activityDurationMinute.setValue(durationMinute);
        activityTimeHour.setMinValue(0);
        activityTimeHour.setMaxValue(23);
        activityTimeHour.setValue(dt.getHourOfDay());
        activityTimeMinute.setMinValue(0);
        activityTimeMinute.setMaxValue(59);
        activityTimeMinute.setValue(dt.getMinuteOfHour());

        //Remove the list view option as the same xml is being used for Edit + Create
        updateActivityButton.setText("Update Activity");
    }

    public void listActivityButtonPressed(View view) {
        //Do nothing if this button is pressed
    }


    public void createActivityButtonPressed(View view) {
        selectedActivity = org.pacemaker.utils.ActivtyUtils.changeActivity(this, selectedActivity,
                distancePicker.getValue(), activityType.getText().toString(), activityLocation.getText().toString(),
                activityDurationHour.getValue(), activityDurationMinute.getValue(),
                datePicker, activityTimeHour.getValue(), activityTimeMinute.getValue());

        if (selectedActivity.kind.equals("IncorrectChange")) {

        } else {
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
