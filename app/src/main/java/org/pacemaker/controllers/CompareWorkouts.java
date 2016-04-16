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
    //Application
    private PacemakerApp app;
    //Logged in User + Friend to compate
    private User loggedInUser;
    private User myFriend;
    //Views associated with xml
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
    //Activities of each User
    private List<MyActivity> loggedInUserActivities = new ArrayList<>();
    private List<MyActivity> friendsActivities = new ArrayList<>();
    //Used to check if the first user activities are obtained
    private Boolean firstUsersActivities = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_workouts);
        //Get the applications
        app = (PacemakerApp) getApplication();
        //Get the logged in users
        loggedInUser = app.getLoggedInUser();
        //Associate the variables with the correct Views
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

        //Create an adapter for both the friends activities and the logged in users activities
        myActivityAdapter = new ActivityAdapter(this, loggedInUserActivities);
        friendActivityAdapter = new ActivityAdapter(this, friendsActivities);
        //set the adapter on each
        mainUserActivitiesListView.setAdapter(myActivityAdapter);
        friendsActivitiesListView.setAdapter(friendActivityAdapter);

        Gson gS = new Gson();
        Log.i(TAG, "Getting frien from JSON");
        String userJson = getIntent().getStringExtra(PacemakerENUMs.MYFRIEND.toString());
        //obtain the user from the the last Android Activity using JSON
        myFriend = gS.fromJson(userJson, User.class);

        //Set the text of the header text views
        mainUserActivitiesTextView.setText("Your Activities");
        friendsActivitiesTextView.setText(myFriend.firstname + "'s Activities");
        //Get the activities of each user
        app.getActivities(this, this, loggedInUser);
        app.getActivities(this, this, myFriend);
    }

    /**
     * Method called each time the app.getActivities is finished
     * @param aList
     */
    @Override
    public void setResponse(List<MyActivity> aList) {
        //When the logged in user's activities are obtained set them
        if (firstUsersActivities) {
            //Set to false so this part of the method does not happen again
            firstUsersActivities = false;
            loggedInUserActivities.addAll(loggedInUser.activities);
            myActivityAdapter.activities = aList;
            myActivityAdapter.notifyDataSetChanged();
        //When getActivities is called for the friend, set their activities + begin comparison
        } else {
            friendsActivities.addAll(myFriend.activities);
            friendActivityAdapter.activities = aList;
            friendActivityAdapter.notifyDataSetChanged();

            //Set the textviews to contain the progress of each user for that time period
            org.pacemaker.utils.ActivtyUtils.userProgressInLast7Days(mainUserLastWeekOfWorkouts, loggedInUserActivities);
            org.pacemaker.utils.ActivtyUtils.userProgressInLast7Days(friendLastWeekOfWorkouts, friendsActivities);

            org.pacemaker.utils.ActivtyUtils.userProgressInLastMonth(mainUserLastMonthOfWorkouts, loggedInUserActivities);
            org.pacemaker.utils.ActivtyUtils.userProgressInLastMonth(friendLasMonthOfWorkouts, friendsActivities);

            org.pacemaker.utils.ActivtyUtils.userProgressOverall(mainUserOverallWorkouts, loggedInUserActivities);
            org.pacemaker.utils.ActivtyUtils.userProgressOverall(friendOverallWorkouts, friendsActivities);
        }

    }

    /**
     * Method used if only 1 activity was being returned
     * @param anObject
     */
    @Override
    public void setResponse(MyActivity anObject) {
    }

    /**
     * If error occurs retrieving activities log it + toast to user
     * @param e
     */
    @Override
    public void errorOccurred(Exception e) {
        String errorString = "Error Retrieving Activities...\n" + e.getLocalizedMessage();
        Toast toast = Toast.makeText(this, errorString, Toast.LENGTH_SHORT);
        toast.show();
        Log.v(TAG, errorString);
    }


}
