package org.pacemaker.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.pacemaker.R;
import org.pacemaker.http.Response;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.MyActivity;
import org.pacemaker.models.User;
import org.pacemaker.utils.ActivityAdapter;
import org.pacemaker.utils.ActivtyUtils;
import org.pacemaker.workouts.LoseFat;

import java.util.List;

public class ProgressReports extends AppCompatActivity implements Response<MyActivity> {
    private static final String TAG = "ProgressReports";

    private List<MyActivity> finishedActivities;
    private PacemakerApp app;
    private User loggedInUser;
    private ActivityAdapter activitiesAdapter;
    private ListView finishedActivitiesListView;
    private TextView lastWeekOfWorkoutsResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_reports);
        finishedActivitiesListView = (ListView) findViewById(R.id.finishedActivitiesListView);

        app = (PacemakerApp) getApplication();
        loggedInUser = app.getLoggedInUser();
        app.getActivities(this, this);

    }

    @Override
    public void setResponse(List<MyActivity> aList) {
        Log.i(TAG, "Getting All Finished Activities");
        finishedActivities = ActivtyUtils.finishedActivities(loggedInUser.activities);
        activitiesAdapter = new ActivityAdapter(this, finishedActivities);
        finishedActivitiesListView.setAdapter(activitiesAdapter);

        Log.i(TAG, "Getting Activities In Last Week");
        lastWeekOfWorkoutsResults = (TextView) findViewById(R.id.lastWeekOfWorkoutsResults);
        String userProgress = ActivtyUtils.kmAndTimeAsStringFromActivities(ActivtyUtils.activitiesInLastXDays(finishedActivities, 7)) + " in the last week";
        lastWeekOfWorkoutsResults.setText(userProgress);
    }

    @Override
    public void setResponse(MyActivity anObject) {
    }

    @Override
    public void errorOccurred(Exception e) {
        Toast errorRetrivingActivitesToast = Toast.makeText(this, "Error Retrieving Activities...\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT);
        errorRetrivingActivitesToast.show();
        Log.v(TAG, e.getLocalizedMessage());
    }

    public void perscribeMyWorkoutClick(View view) {
        //TODO : Add popup and list here asking what workout they want and have multiple startegies in place for this
        LoseFat loseFat = new LoseFat();
        Log.v(TAG, "Getting user workout");
        Toast newWorkoutToast = Toast.makeText(ProgressReports.this, loseFat.workout(finishedActivities), Toast.LENGTH_SHORT);
        newWorkoutToast.show();
    }
}
