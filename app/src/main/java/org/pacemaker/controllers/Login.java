package org.pacemaker.controllers;

import org.pacemaker.R;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.User;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    PacemakerApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void signinPressed(View view) {
        app = (PacemakerApp) getApplication();


        TextView email = (TextView) findViewById(R.id.loginEmail);
        TextView password = (TextView) findViewById(R.id.loginPassword);
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String PREF_USER_NAME = "userEmail";
        String PREF_PASSWORD = "userPassword";

        if(prefs.getString(PREF_USER_NAME, "NONE").isEmpty()) {
            boolean loggedIn = app.loginUser(userEmail, userPassword);
            if (loggedIn) {
//            edit.clear();
                prefs
                        .edit()
                        .putString(PREF_USER_NAME, userEmail)
                        .putString(PREF_PASSWORD, userPassword)
                        .apply();

                startActivity(new Intent(this, Dashboard.class));
            } else {
                Toast toast = Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            startActivity(new Intent(this, Dashboard.class));
        }



    }

    //Using this to refresh the list when returned to from another activity
    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed or another activity is finished, the activity on the stack is restarted
        //Finish the activity
        finish();
    }
}