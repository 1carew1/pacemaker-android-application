package org.pacemaker.controllers;

import org.pacemaker.R;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.User;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Signup extends AppCompatActivity {
    private static final String TAG = "SignUp";
    //application
    private PacemakerApp app;
    //list of all current user emails
    private List<String> listOfAllUserEmails;
    //views
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        app = (PacemakerApp) getApplication();
        //associate views
        firstName = (TextView) findViewById(R.id.firstName);
        lastName = (TextView) findViewById(R.id.lastName);
        email = (TextView) findViewById(R.id.Email);
        password = (TextView) findViewById(R.id.Password);
    }

    /**
     * On Click for register button
     *
     * @param view
     */
    public void registerPressed(View view) {
        //Get all user emails
        listOfAllUserEmails = app.getUserEmails();
        //Get new details
        String newUserEmail = email.getText().toString();
        String newUserPassword = password.getText().toString();
        if (listOfAllUserEmails.contains(newUserEmail)) {
            Toast toast = Toast.makeText(Signup.this, "A user with Email : " + newUserEmail + " already exists", Toast.LENGTH_SHORT);
            toast.show();
            // Check if user password + email are filled in
        } else if (newUserPassword.isEmpty() || newUserEmail.isEmpty() || !newUserEmail.matches(".+?@.+\\..+")) {
            Toast toast = Toast.makeText(Signup.this, "Email and Password Must be filled out Correclty", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            User user = new User(firstName.getText().toString(), lastName.getText().toString(), newUserEmail, password.getText().toString());
            app.registerUser(this, user);
            Log.i(TAG, "New user created : " + user.firstname + " " + user.lastname);
            startActivity(new Intent(this, Login.class));
        }
    }
}
