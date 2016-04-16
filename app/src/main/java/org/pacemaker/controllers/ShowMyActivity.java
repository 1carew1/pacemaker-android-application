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
import org.pacemaker.utils.PacemakerENUMs;

import java.util.List;


public class ShowMyActivity extends AppCompatActivity implements Response<MyActivity> {
    private static final String TAG = "UpdateActivity";

    //Activity From the Activity List
    private MyActivity selectedActivity;
    //Application containing global data
    private PacemakerApp app;

    //Views Associated with the XML File for this activity
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

        //Get the application
        app = (PacemakerApp) getApplication();

        //Assign the correct view to the private variables
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
        String target = getIntent().getStringExtra(PacemakerENUMs.SELECTEDACTIVITY.toString());
        selectedActivity = gS.fromJson(target, MyActivity.class);

        String dateString = selectedActivity.startTime;
        DateTime dt = org.pacemaker.utils.ActivtyUtils.stringToDatetime(dateString);
        //Set the date in the date picker
        datePicker.updateDate(dt.getYear(), dt.getMonthOfYear() - 1, dt.getDayOfMonth());
        //Set the text of the fields
        activityType.setText(selectedActivity.kind);
        activityLocation.setText(selectedActivity.location);

        //Obtain the duration values
        int durationHour = Integer.parseInt(selectedActivity.duration.replaceAll(":\\d{1,2}", ""));
        int durationMinute = Integer.parseInt(selectedActivity.duration.replaceAll("\\d{1,2}:", ""));

        //Set the max and min values of each of the pickers
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

        //Change Create Activity Button to say Update Activity
        updateActivityButton.setText("Update Activity");
    }


    /**
     * When the create activity button is pressed - update the activity with the new information
     *
     * @param view
     */
    public void createActivityButtonPressed(View view) {
        selectedActivity = org.pacemaker.utils.ActivtyUtils.changeActivity(this, selectedActivity,
                distancePicker.getValue(), activityType.getText().toString(), activityLocation.getText().toString(),
                activityDurationHour.getValue(), activityDurationMinute.getValue(),
                datePicker, activityTimeHour.getValue(), activityTimeMinute.getValue());

        //Make Sure the fields are filled in correctly
        if (!selectedActivity.kind.equals(PacemakerENUMs.INCORRECTCHANGE.toString())) {
            app.updateActivity(this, selectedActivity, this);
            Toast finishToast = Toast.makeText(ShowMyActivity.this, "Activity Updated", Toast.LENGTH_SHORT);
            finishToast.show();
            finish();
        }
    }


    /**
     * If a list is being returned from the request then the data can be processed here
     *
     * @param aList
     */
    @Override
    public void setResponse(List<MyActivity> aList) {
    }

    /**
     * Once the actiity is created + returned, this method can be used to process the data
     *
     * @param anObject
     */
    @Override
    public void setResponse(MyActivity anObject) {
    }

    /**
     * Toast + Log the exception if the Activity Cannot be updated
     *
     * @param e
     */
    @Override
    public void errorOccurred(Exception e) {
        Toast toast = Toast.makeText(this, "Failed to update Activity", Toast.LENGTH_SHORT);
        Log.i(TAG, e.getLocalizedMessage());
        toast.show();
    }


}
