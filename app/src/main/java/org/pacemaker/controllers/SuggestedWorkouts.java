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
import org.pacemaker.workouts.PrescribeExercise;

import java.util.List;

public class SuggestedWorkouts extends AppCompatActivity implements Response<MyActivity> {

    private static final String TAG = "SuggestedWorkouts";
    //Application + Logged in user
    private PacemakerApp app;
    private User loggedInUser;
    //Workout to Prescribe to User
    private PrescribeExercise suggestedWorkout = new LoseFat();
    ;
    //List of Users finished Activities
    private List<MyActivity> finishedActivities = null;
    //View to display suggested workout
    private TextView suggestedWorkoutTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_workouts);
        //associate view
        suggestedWorkoutTextView = (TextView) findViewById(R.id.suggestedWorkoutTextView);
        //get application + loggied in user
        app = (PacemakerApp) getApplication();
        loggedInUser = app.getLoggedInUser();
        //get all of the user's activities
        app.getActivities(this, this, loggedInUser);

    }

    /**
     * On click method for lose fat button
     * @param view
     */
    public void lostFatWorkout(View view) {
        //Make the suggested workout to be lost fat
        suggestedWorkout = new LoseFat();
        Log.v(TAG, "Getting user workout for losing weight");
        //Run this method to output it to screen
        calculateTheBestFitnessPlan();
    }

    /**
     * On click for build muscle button
     * @param view
     */
    public void buildMuscleWorkout(View view) {
        //User wants to build muscle
        suggestedWorkout = new BuildMuscle();
        Log.v(TAG, "Getting user workout for building muscle");
        calculateTheBestFitnessPlan();
    }

    /**
     * On click for improve fitness button
     * @param view
     */
    public void increaseFitnessWorkout(View view) {
        //User wants to improve their fitness
        suggestedWorkout = new ImproveFitness();
        Log.v(TAG, "Getting user workout for increasing fitness");
        calculateTheBestFitnessPlan();
    }

    /**
     * Method used to calculate the workout + display to user
     */
    public void calculateTheBestFitnessPlan() {
        String suggestedWorkoutString = suggestedWorkout.workout(finishedActivities);
        suggestedWorkoutTextView.setText(suggestedWorkoutString);
    }

    /**
     * When app.getActivities is used, this method is ran upon completion
     * and the users finished activities are obtained from all of their activities
     * @param aList
     */
    @Override
    public void setResponse(List<MyActivity> aList) {
        Log.i(TAG, "Getting All Finished Activities");
        finishedActivities = ActivtyUtils.finishedActivities(loggedInUser.activities);
    }

    /**
     * Not used here but can be if only 1 MyActivity object is being returned
     * @param anObject
     */
    @Override
    public void setResponse(MyActivity anObject) {
    }

    /**
     * In the event of a GET error log + toast to user
     * @param e
     */
    @Override
    public void errorOccurred(Exception e) {
        Toast errorRetrivingActivitesToast = Toast.makeText(this, "Error Retrieving Activities...\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT);
        errorRetrivingActivitesToast.show();
        Log.v(TAG, e.getLocalizedMessage());
    }
}
