package org.pacemaker.controllers;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.pacemaker.R;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.User;
import org.pacemaker.utils.PacemakerENUMs;

public class ShowFriend extends AppCompatActivity {
    private static final String TAG = "ShowFriendActivity";

    //Application, LoggedinUser + Friend Selected
    private PacemakerApp app;
    private User myFriend;
    private User loggedInUser;

    //views
    private TextView friendName;
    private Button addOrRemove;
    private Button compareWorkouts;
    private ImageView profilePhoto;
    //Vary activity based on this value
    private String weAreFriends;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friend);

        //get application + logged in user
        app = (PacemakerApp) getApplication();
        loggedInUser = app.getLoggedInUser();
        //associate views
        friendName = (TextView) findViewById(R.id.friendName);
        addOrRemove = (Button) findViewById(R.id.addOrRemoveFriend);
        compareWorkouts = (Button) findViewById(R.id.compareWorkouts);
        profilePhoto = (ImageView) findViewById(R.id.profilePhoto);

        //Set this button invisible whilst the user if not your friend
        compareWorkouts.setVisibility(View.INVISIBLE);

        //obtain the friend + the friend status via JSON
        final Gson gS = new Gson();
        String userJson = getIntent().getStringExtra("MyFriend");
        String weAreFriendsJson = getIntent().getStringExtra("WeAreFriendsCheck");
        myFriend = gS.fromJson(userJson, User.class);
        weAreFriends = gS.fromJson(weAreFriendsJson, String.class);


        //Edit activity based on result
        friendName.setText(myFriend.firstname + " " + myFriend.lastname);
        if (weAreFriends.equalsIgnoreCase(PacemakerENUMs.NOTHING.toString())) {
            Log.i(TAG, "Not Friends");
            addOrRemove.setText("Add Friend");
            addOrRemove.getBackground().setColorFilter(Color.parseColor("#33ccff"), PorterDuff.Mode.MULTIPLY);
            addOrRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    app.addFriend(ShowFriend.this, myFriend.id);
                    finish();
                }
            });
        } else if (weAreFriends.equalsIgnoreCase(PacemakerENUMs.FRIENDS.toString())) {
            Log.i(TAG, "Friends");
            addOrRemove.setText("Unfriend");
            addOrRemove.getBackground().setColorFilter(Color.parseColor("#cc3300"), PorterDuff.Mode.MULTIPLY);
            addOrRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    app.unFriend(ShowFriend.this, myFriend.id);
                    finish();
                }
            });
            //If this user wants you to see their activities - add compare workouts
            if (myFriend.isFriendViewable) {
                compareWorkouts.setVisibility(View.VISIBLE);
                compareWorkouts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "Compare Workouts clicked - Going to compare workouts");
                        Intent compareWorkouts = new Intent(ShowFriend.this, CompareWorkouts.class);
                        String userJson = gS.toJson(myFriend);
                        compareWorkouts.putExtra(PacemakerENUMs.MYFRIEND.toString(), userJson);
                        startActivity(compareWorkouts);
                    }
                });
            }
        } else if (weAreFriends.equalsIgnoreCase(PacemakerENUMs.PENDING.toString())) {
            Log.i(TAG, "Pending Friends");
            addOrRemove.setText("Accept");
            addOrRemove.getBackground().setColorFilter(Color.parseColor("#33ccff"), PorterDuff.Mode.MULTIPLY);
            addOrRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    app.acceptFriend(ShowFriend.this, myFriend.id);
                    finish();
                }
            });
        }
        //Set their profile photo
        org.pacemaker.utils.ImageUtils.setUserImage(profilePhoto, myFriend.profilePhoto);
    }
}
