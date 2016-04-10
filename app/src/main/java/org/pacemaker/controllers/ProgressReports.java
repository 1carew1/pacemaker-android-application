package org.pacemaker.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.pacemaker.R;
import org.pacemaker.http.Response;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.MyActivity;
import org.pacemaker.models.User;
import org.pacemaker.utils.ActivityAdapter;

import java.util.List;

public class ProgressReports extends AppCompatActivity implements Response<MyActivity> {
    private static final String TAG = "ProgressReports";

    private List<MyActivity> finishedActivities;
    private PacemakerApp app;
    private User loggedInUser;
    private ActivityAdapter activitiesAdapter;
    private ListView finishedActivitiesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_reports);
        finishedActivitiesListView = (ListView) findViewById(R.id.finishedActivitiesListView);
        app = (PacemakerApp) getApplication();
        loggedInUser = app.getLoggedInUser();
        app.getActivities(this, this);
;
    }

    @Override
    public void setResponse(List<MyActivity> aList) {
        finishedActivities = org.pacemaker.utils.ActivtyUtils.finishedActivities(loggedInUser.activities);
        activitiesAdapter = new ActivityAdapter(this, finishedActivities);
        finishedActivitiesListView.setAdapter(activitiesAdapter);
    }

    @Override
    public void setResponse(MyActivity anObject) {
    }

    @Override
    public void errorOccurred(Exception e) {
        Toast toast = Toast.makeText(this, "Error Retrieving Activities...\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT);
        toast.show();
        Log.v(TAG, e.getLocalizedMessage());
    }
}
