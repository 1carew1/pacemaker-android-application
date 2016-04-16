package org.pacemaker.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.pacemaker.R;
import org.pacemaker.http.Response;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.MyActivity;
import org.pacemaker.models.User;
import org.pacemaker.utils.ActivityAdapter;
import org.pacemaker.utils.PacemakerENUMs;

import java.util.ArrayList;
import java.util.List;

public class CompareWorkouts extends AppCompatActivity implements Response<MyActivity> {

    private static final String TAG = "CompareWorkouts";
    private User loggedInUser;
    private User myFriend;
    private PacemakerApp app;
    private ListView mainUserActivitiesListView;
    private ListView friendsActivitiesListView;
    private ActivityAdapter myActivityAdapter;
    private ActivityAdapter friendActivityAdapter;
    private TextView mainUserActivitiesTextView;
    private TextView friendsActivitiesTextView;
    private TextView mainUserLastWeekOfWorkouts;
    private TextView friendLastWeekOfWorkouts;
    private TextView mainUserLastMonthOfWorkouts;
    private TextView friendLasMonthOfWorkouts;
    private TextView mainUserOverallWorkouts;
    private TextView friendOverallWorkouts;


    private List<MyActivity> loggedInUserActivities = new ArrayList<>();
    private List<MyActivity> friendsActivities = new ArrayList<>();

    private Boolean firstUsersActivities = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_workouts);

        app = (PacemakerApp) getApplication();
        loggedInUser = app.getLoggedInUser();

        mainUserActivitiesListView = (ListView) findViewById(R.id.mainUserActivitiesListView);
        friendsActivitiesListView = (ListView) findViewById(R.id.friendsActivitiesListView);
        mainUserActivitiesTextView = (TextView) findViewById(R.id.mainUserActivities);
        friendsActivitiesTextView = (TextView) findViewById(R.id.friendsActivities);
        mainUserLastWeekOfWorkouts = (TextView) findViewById(R.id.mainUserLastWeekOfWorkouts);
        friendLastWeekOfWorkouts = (TextView) findViewById(R.id.friendLastWeekOfWorkouts);
        mainUserLastMonthOfWorkouts = (TextView) findViewById(R.id.mainUserLastMonthOfWorkouts);
        friendLasMonthOfWorkouts = (TextView) findViewById(R.id.friendLasMonthOfWorkouts);
        mainUserOverallWorkouts = (TextView) findViewById(R.id.mainUserOverallWorkouts);
        friendOverallWorkouts = (TextView) findViewById(R.id.friendOverallWorkouts);

        myActivityAdapter = new ActivityAdapter(this, loggedInUserActivities);
        friendActivityAdapter = new ActivityAdapter(this, friendsActivities);


        mainUserActivitiesListView.setAdapter(myActivityAdapter);
        friendsActivitiesListView.setAdapter(friendActivityAdapter);

        Gson gS = new Gson();
        Log.i(TAG, "Getting frien from JSON");
        String userJson = getIntent().getStringExtra(PacemakerENUMs.MYFRIEND.toString());
        myFriend = gS.fromJson(userJson, User.class);

        mainUserActivitiesTextView.setText("Your Activities");
        friendsActivitiesTextView.setText(myFriend.firstname + "'s Activities");

        app.getActivities(this, this, loggedInUser);
        app.getActivities(this, this, myFriend);
    }

    @Override
    public void setResponse(List<MyActivity> aList) {
        //Do Something
        if (firstUsersActivities) {

            firstUsersActivities = false;
            loggedInUserActivities.addAll(loggedInUser.activities);
            myActivityAdapter.activities = aList;
            myActivityAdapter.notifyDataSetChanged();
        } else {
            friendsActivities.addAll(myFriend.activities);
            friendActivityAdapter.activities = aList;
            friendActivityAdapter.notifyDataSetChanged();

            org.pacemaker.utils.ActivtyUtils.userProgressInLast7Days(mainUserLastWeekOfWorkouts, loggedInUserActivities);
            org.pacemaker.utils.ActivtyUtils.userProgressInLast7Days(friendLastWeekOfWorkouts, friendsActivities);

            org.pacemaker.utils.ActivtyUtils.userProgressInLastMonth(mainUserLastMonthOfWorkouts, loggedInUserActivities);
            org.pacemaker.utils.ActivtyUtils.userProgressInLastMonth(friendLasMonthOfWorkouts, friendsActivities);

            org.pacemaker.utils.ActivtyUtils.userProgressOverall(mainUserOverallWorkouts, loggedInUserActivities);
            org.pacemaker.utils.ActivtyUtils.userProgressOverall(friendOverallWorkouts, friendsActivities);
        }

    }

    @Override
    public void setResponse(MyActivity anObject) {
    }

    @Override
    public void errorOccurred(Exception e) {
        String errorString = "Error Retrieving Activities...\n" + e.getLocalizedMessage();
        Toast toast = Toast.makeText(this, errorString, Toast.LENGTH_SHORT);
        toast.show();
        Log.v(TAG, errorString);
    }


}
