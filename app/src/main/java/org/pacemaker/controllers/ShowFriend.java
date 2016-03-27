package org.pacemaker.controllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.pacemaker.R;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.User;

public class ShowFriend extends AppCompatActivity {

    private User myFriend;
    private PacemakerApp app;


    private TextView friendName;
    private ImageView profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friend);


        app = (PacemakerApp) getApplication();

        friendName = (TextView) findViewById(R.id.friendName);
        profilePhoto = (ImageView) findViewById(R.id.profilePhoto);

        Gson gS = new Gson();
        String target = getIntent().getStringExtra("MyFriend");
        myFriend = gS.fromJson(target, User.class);

        friendName.setText(myFriend.firstname + " " + myFriend.lastname);
        org.pacemaker.utils.ImageUtils.setUserImage(profilePhoto, myFriend.profilePhoto);
    }
}
