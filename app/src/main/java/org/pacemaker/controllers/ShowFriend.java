package org.pacemaker.controllers;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.koushikdutta.ion.Ion;

import org.pacemaker.R;
import org.pacemaker.http.Response;
import org.pacemaker.http.Rest;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.Friends;
import org.pacemaker.models.User;
import org.pacemaker.utils.PacemakerENUMs;

public class ShowFriend extends AppCompatActivity {
    private static final String TAG = "ShowFriendActivity";

    private User myFriend;
    private String weAreFriends;
    private PacemakerApp app;


    private TextView friendName;
    private Button addOrRemove;
    private ImageView profilePhoto;
    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friend);


        app = (PacemakerApp) getApplication();
        loggedInUser = app.getLoggedInUser();

        friendName = (TextView) findViewById(R.id.friendName);
        addOrRemove = (Button) findViewById(R.id.addOrRemoveFriend);
        profilePhoto = (ImageView) findViewById(R.id.profilePhoto);

        Gson gS = new Gson();
        String userJson = getIntent().getStringExtra("MyFriend");
        String weAreFriendsJson = getIntent().getStringExtra("WeAreFriendsCheck");
        myFriend = gS.fromJson(userJson, User.class);
        weAreFriends = gS.fromJson(weAreFriendsJson, String.class);


        friendName.setText(myFriend.firstname + " " + myFriend.lastname);
        if (weAreFriends.equalsIgnoreCase(PacemakerENUMs.NOTHING.toString())) {
            addOrRemove.setText("Add Friend");
            addOrRemove.getBackground().setColorFilter(Color.parseColor("#33ccff"), PorterDuff.Mode.MULTIPLY);
            //TODO : Implement method of adding + removing friends
            addOrRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    app.addFriend(ShowFriend.this, myFriend.id);
                    finish();
                }
            });
        } else if (weAreFriends.equalsIgnoreCase(PacemakerENUMs.FRIENDS.toString())) {
            addOrRemove.setText("Unfriend");
            addOrRemove.getBackground().setColorFilter(Color.parseColor("#cc3300"), PorterDuff.Mode.MULTIPLY);
            addOrRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    app.unFriend(ShowFriend.this, myFriend.id);
                    finish();
                }
            });
        } else if (weAreFriends.equalsIgnoreCase(PacemakerENUMs.PENDING.toString())) {
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

        org.pacemaker.utils.ImageUtils.setUserImage(profilePhoto, myFriend.profilePhoto);
    }
}
