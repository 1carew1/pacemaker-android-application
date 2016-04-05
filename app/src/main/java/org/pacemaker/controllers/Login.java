package org.pacemaker.controllers;

import org.pacemaker.R;
import org.pacemaker.database.SQLLiteDataSource;
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
    private PacemakerApp app;
    private SQLLiteDataSource datasource;

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
        datasource = new SQLLiteDataSource(this);



        boolean loggedIn = app.loginUser(userEmail, userPassword);
        if (loggedIn) {
            datasource.saveUser(app.getLoggedInUser());
            startActivity(new Intent(this, Dashboard.class));
        } else {
            Toast toast = Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    //Using this to refresh the list when returned to from another activity
    @Override
    public void onRestart() {
        super.onRestart();
        app.logout();
        datasource.deleteUsersFromDb();
        //When BACK BUTTON is pressed or another activity is finished, the activity on the stack is restarted
        //Finish the activity
        finish();
    }
}