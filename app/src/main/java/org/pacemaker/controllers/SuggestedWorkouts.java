package org.pacemaker.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.pacemaker.R;
import org.pacemaker.http.Response;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.MyActivity;
import org.pacemaker.models.User;
import org.pacemaker.utils.ActivtyUtils;
import org.pacemaker.workouts.BuildMuscle;
import org.pacemaker.workouts.ImproveFitness;
import org.pacemaker.workouts.LoseFat;
import org.pacemaker.workouts.PerscribeExercise;

import java.util.List;

public class SuggestedWorkouts extends AppCompatActivity implements Response<MyActivity> {

    private static final String TAG = "SuggestedWorkouts";

    private PerscribeExercise suggestedWorkout;
    private PacemakerApp app;
    private List<MyActivity> finishedActivities = null;
    private User loggedInUser;
    private TextView suggestedWorkoutTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_workouts);
        suggestedWorkout = new LoseFat();
        suggestedWorkoutTextView = (TextView) findViewById(R.id.suggestedWorkoutTextView);
        app = (PacemakerApp) getApplication();
        loggedInUser = app.getLoggedInUser();
        app.getActivities(this, this, loggedInUser);

    }

    public void perscribeMyWorkoutClick(View view) {
        //TODO : Add popup and list here asking what workout they want and have multiple startegies in place for this

        suggestedWorkout = new LoseFat();
        Log.v(TAG, "Getting user workout for losing weight");
        calculateTheBestFitnessPlan();
    }

    public void buildMuscle(View view) {
        suggestedWorkout = new BuildMuscle();
        Log.v(TAG, "Getting user workout for building muscle");
        calculateTheBestFitnessPlan();
    }

    public void increaseFitness(View view) {
        suggestedWorkout = new ImproveFitness();
        Log.v(TAG, "Getting user workout for increasing fitness");
        calculateTheBestFitnessPlan();
    }

    public void calculateTheBestFitnessPlan() {
        String suggestedWorkoutString = suggestedWorkout.workout(finishedActivities);
        suggestedWorkoutTextView.setText(suggestedWorkoutString);
//        Toast newWorkoutToast = Toast.makeText(SuggestedWorkouts.this, suggestedWorkoutString, Toast.LENGTH_SHORT);
//        newWorkoutToast.show();
    }

    @Override
    public void setResponse(List<MyActivity> aList) {
        Log.i(TAG, "Getting All Finished Activities");
        finishedActivities = ActivtyUtils.finishedActivities(loggedInUser.activities);
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
}
