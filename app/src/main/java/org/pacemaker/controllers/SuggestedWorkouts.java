package org.pacemaker.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.pacemaker.R;
import org.pacemaker.workouts.BuildMuscle;
import org.pacemaker.workouts.LoseFat;
import org.pacemaker.workouts.PerscribeExercise;

public class SuggestedWorkouts extends AppCompatActivity {

    private static final String TAG = "SuggestedWorkouts";

    private PerscribeExercise suggestedWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_workouts);
        //TODO : Use strategy pattern for walking + running use distance + duration
        suggestedWorkout = new LoseFat();
    }

    public void perscribeMyWorkoutClick(View view) {
        //TODO : Add popup and list here asking what workout they want and have multiple startegies in place for this

        suggestedWorkout = new LoseFat();
        Log.v(TAG, "Getting user workout for losing weight");
        toastTheExercise();
    }

    public void buildMuscle(View view) {
        suggestedWorkout = new BuildMuscle();
        Log.v(TAG, "Getting user workout for building muscle");
        toastTheExercise();
    }

    public void toastTheExercise() {
        //TODO : Get list of user activities - remember they may not have clicked the activities first
        Toast newWorkoutToast = Toast.makeText(SuggestedWorkouts.this, suggestedWorkout.workout(null), Toast.LENGTH_SHORT);
        newWorkoutToast.show();
    }
}
