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
    //Application
    private PacemakerApp app;
    //Logged in user
    private User loggedInUser;
    //views
    private List<MyActivity> finishedActivities;
    private ActivityAdapter activitiesAdapter;
    private ListView finishedActivitiesListView;
    private TextView lastWeekOfWorkoutsResults;
    private TextView lastMonthOfWorkoutsResults;
    private TextView allTimeWorkoutResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_reports);

        //associate views with variables
        finishedActivitiesListView = (ListView) findViewById(R.id.finishedActivitiesListView);
        lastWeekOfWorkoutsResults = (TextView) findViewById(R.id.lastWeekOfWorkoutsResults);
        lastMonthOfWorkoutsResults = (TextView) findViewById(R.id.lastMonthOfWorkoutsResults);
        allTimeWorkoutResults = (TextView) findViewById(R.id.overallWorkoutResults);

        //get the application, user and the user's activities
        app = (PacemakerApp) getApplication();
        loggedInUser = app.getLoggedInUser();
        app.getActivities(this, this, loggedInUser);
    }

    /**
     * Method called with app.getActivities finishes
     * @param aList
     */
    @Override
    public void setResponse(List<MyActivity> aList) {
        Log.i(TAG, "Getting All Finished Activities");
        finishedActivities = ActivtyUtils.finishedActivities(loggedInUser.activities);
        //Set the adapter when the activities are obtained
        activitiesAdapter = new ActivityAdapter(this, finishedActivities);
        finishedActivitiesListView.setAdapter(activitiesAdapter);

        //Display the evaluation for each time frame
        Log.i(TAG, "Getting Activities performace In Last Week");
        org.pacemaker.utils.ActivtyUtils.userProgressInLast7Days(lastWeekOfWorkoutsResults, finishedActivities);

        Log.i(TAG, "Getting Activities performace In Last Month");
        org.pacemaker.utils.ActivtyUtils.userProgressInLastMonth(lastMonthOfWorkoutsResults, finishedActivities);

        Log.i(TAG, "Getting Activities performace for all finished activities");
        org.pacemaker.utils.ActivtyUtils.userProgressOverall(allTimeWorkoutResults, finishedActivities);
    }

    /**
     * Method if only 1 object is obtained
     * @param anObject
     */
    @Override
    public void setResponse(MyActivity anObject) {
    }

    /**
     * If error occurs retrieving activities, log it and toast to user
     * @param e
     */
    @Override
    public void errorOccurred(Exception e) {
        Toast errorRetrivingActivitesToast = Toast.makeText(this, "Error Retrieving Activities...\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT);
        errorRetrivingActivitesToast.show();
        Log.v(TAG, e.getLocalizedMessage());
    }
}
