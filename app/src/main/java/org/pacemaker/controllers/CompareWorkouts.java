package org.pacemaker.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.pacemaker.R;
import org.pacemaker.http.Response;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.MyActivity;
import org.pacemaker.models.User;
import org.pacemaker.utils.PacemakerENUMs;

import java.util.ArrayList;
import java.util.List;

public class CompareWorkouts extends AppCompatActivity implements Response<MyActivity> {

    private static final String TAG = "CompareWorkouts";
    private User loggedInUser;
    private User myFriend;
    private PacemakerApp app;

    private List<MyActivity> loggedInUserActivities = new ArrayList<>();
    private List<MyActivity> friendsActivities = new ArrayList<>();

    private Boolean whichUser = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_workouts);

        app = (PacemakerApp) getApplication();
        loggedInUser = app.getLoggedInUser();

        Gson gS = new Gson();
        Log.i(TAG, "Getting frien from JSON");
        String userJson = getIntent().getStringExtra(PacemakerENUMs.MYFRIEND.toString());
        myFriend = gS.fromJson(userJson, User.class);

        app.getActivities(this, this, loggedInUser);
        app.getActivities(this, this, myFriend);

        // Toast.makeText(this, myFriend.firstname + " is " + loggedInUser.firstname + "'s friend", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setResponse(List<MyActivity> aList) {
        //Do Something
        if (whichUser) {
            whichUser = false;
            loggedInUserActivities.addAll(loggedInUser.activities);
        } else {
            friendsActivities.addAll(myFriend.activities);
            Toast.makeText(this, loggedInUserActivities.size() + " main user\n" + friendsActivities.size() + " friend", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void setResponse(MyActivity anObject) {
    }

    @Override
    public void errorOccurred(Exception e) {
        Toast toast = Toast.makeText(this, "Error Retrieving Activities...\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT);
        toast.show();
        Log.v(TAG, e.getLocalizedMessage());
    }


}
