package org.pacemaker.controllers;

import org.pacemaker.R;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.User;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Signup extends Activity {
    // TODO : Employ a check for Email + Password + Make sure user does not exist
    private PacemakerApp app;
    private List<String> listOfAllUserEmails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        app = (PacemakerApp) getApplication();
    }

    public void registerPressed(View view) {
        TextView firstName = (TextView) findViewById(R.id.firstName);
        TextView lastName = (TextView) findViewById(R.id.lastName);
        TextView email = (TextView) findViewById(R.id.Email);
        TextView password = (TextView) findViewById(R.id.Password);
        listOfAllUserEmails = app.getUserEmails();

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
            startActivity(new Intent(this, Login.class));
        }
    }
}
