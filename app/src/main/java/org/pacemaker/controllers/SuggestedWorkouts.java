package org.pacemaker.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.pacemaker.R;

public class SuggestedWorkouts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_workouts);
        //TODO : Use strategy pattern for walking + running use distance + duration
    }
}
