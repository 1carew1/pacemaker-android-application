package org.pacemaker.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.pacemaker.R;
import org.pacemaker.main.PacemakerApp;


public class Welcome extends AppCompatActivity {
    public static final String TAG = "WelcomeActivitys";
    //application
    PacemakerApp app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //get the application + generate the list of users
        app = (PacemakerApp) getApplication();
        app.connectToPacemakerAPI(this);
    }

    /**
     * When the login button is pressed start the login activity
     *
     * @param view
     */
    public void loginPressed(View view) {
        startActivity(new Intent(this, Login.class));
    }

    /**
     * When the signup button is pressed start the signup activity
     *
     * @param view
     */
    public void signupPressed(View view) {
        startActivity(new Intent(this, Signup.class));
    }

    /**
     * When this activity is returned to generate the user list again
     */
    @Override
    public void onRestart() {
        super.onRestart();
        app.connectToPacemakerAPI(this);
    }
}
