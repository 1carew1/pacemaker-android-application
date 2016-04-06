package org.pacemaker.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.pacemaker.R;
import org.pacemaker.http.Response;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.User;
import org.pacemaker.utils.FriendsAdapter;
import org.pacemaker.utils.PacemakerENUMs;

import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity implements Response<User> {

    public static final String TAG = "UserList";

    private PacemakerApp app;
    private ListView friendsListView;
    private TextView pageTitle;
    private FriendsAdapter friendsAdapter;
    private List<User> friendsList = new ArrayList<>();
    private User loggedInUser;
    private User selectedUser;
    private String areWeFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        app = (PacemakerApp) getApplication();
        loggedInUser = app.getLoggedInUser();
        final String loggedInUserString = loggedInUser.firstname + " " + loggedInUser.lastname;
        Log.i(TAG, "In on create for FRIENDS List View for " + loggedInUserString);

        friendsListView = (ListView) findViewById(R.id.friendsListView);


        friendsAdapter = new FriendsAdapter(this, friendsList);
        friendsListView.setAdapter(friendsAdapter);

        Gson gS = new Gson();
        String target = getIntent().getStringExtra("FriendsOrNot");
        areWeFriends = gS.fromJson(target, String.class);

        String pageTitleString = "";
        if (areWeFriends.equals(PacemakerENUMs.FRIENDS.toString())) {
            app.getFriends(this, this);
            pageTitleString = "FRIENDS of " + loggedInUserString;
        }  else  if (areWeFriends.equals(PacemakerENUMs.NOTHING.toString())){
            app.getUsersWhoAreNotFriends(this, this);
            pageTitleString = "Users List";
        }  else  if (areWeFriends.equals(PacemakerENUMs.PENDING.toString())) {
            app.getPendingFriends(this, this);
            pageTitleString = "Pending Friends";
        }

        pageTitle = (TextView) findViewById(R.id.pageTitle);
        Log.i(TAG, "Setting text of pageTitle");
        pageTitle.setText(pageTitleString);


        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                if (areWeFriends.equalsIgnoreCase(PacemakerENUMs.FRIENDS.toString())) {
                    selectedUser = loggedInUser.friendsList.get(position);

                } else  if (areWeFriends.equalsIgnoreCase(PacemakerENUMs.NOTHING.toString())) {
                    selectedUser = loggedInUser.notFriendsList.get(position);
                }
                Log.i(TAG, selectedUser.toString());
                listItemPressed(selectedUser, areWeFriends);
            }
        });

    }

    public void listItemPressed(User friend, String weAreFriends) {

        Gson gS = new Gson();
        String userJson = gS.toJson(friend);
        String areWeFriends = gS.toJson(weAreFriends);
        Intent showFriend = new Intent(this, ShowFriend.class);
        showFriend.putExtra("MyFriend", userJson);
        showFriend.putExtra("WeAreFriendsCheck", areWeFriends);
        startActivity(showFriend);
    }

    //Using this to refresh the list when returned to from another activity
    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        this.recreate();
    }


    @Override
    public void setResponse(List<User> aList) {
        friendsAdapter.friendsList = aList;
        friendsAdapter.notifyDataSetChanged();
    }

    @Override
    public void setResponse(User anObject) {
    }

    @Override
    public void errorOccurred(Exception e) {
        Toast toast = Toast.makeText(this, "Error Retrieving Activities...\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT);
        toast.show();
        Log.v("Getting Activities", e.getLocalizedMessage());
    }


}