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
    //application
    private PacemakerApp app;
    //friends adapter
    private FriendsAdapter friendsAdapter;
    //Logged in user, selected user + list of all friends
    private User loggedInUser;
    private User selectedUser;
    private List<User> friendsList = new ArrayList<>();
    //Use to determine activity behaviour
    private String areWeFriends;
    //Views
    private ListView friendsListView;
    private TextView pageTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        //Get application + logged in user
        app = (PacemakerApp) getApplication();
        loggedInUser = app.getLoggedInUser();
        //set user name
        final String loggedInUserString = loggedInUser.firstname + " " + loggedInUser.lastname;
        Log.i(TAG, "In on create for Friends List View for " + loggedInUserString);
        //associate view
        friendsListView = (ListView) findViewById(R.id.friendsListView);
        //create friends adapter
        friendsAdapter = new FriendsAdapter(this, friendsList);
        friendsListView.setAdapter(friendsAdapter);
        //Set up Gson and see if this is the friends list, not friends list or pending friends list
        Gson gS = new Gson();
        String target = getIntent().getStringExtra(PacemakerENUMs.FRIENDSORNOT.toString());
        areWeFriends = gS.fromJson(target, String.class);

        String pageTitleString = "";
        //if we are friends
        if (areWeFriends.equals(PacemakerENUMs.FRIENDS.toString())) {
            app.getFriends(this, this);
            pageTitleString = "Friends of " + loggedInUserString;
            //if we are not friends
        } else if (areWeFriends.equals(PacemakerENUMs.NOTHING.toString())) {
            app.getUsersWhoAreNotFriends(this, this);
            pageTitleString = "Users List";
            //if I was added as a friend but have not yet accepted
        } else if (areWeFriends.equals(PacemakerENUMs.PENDING.toString())) {
            app.getPendingFriends(this, this);
            pageTitleString = "Pending Friends";
        }
        //set the page title
        pageTitle = (TextView) findViewById(R.id.pageTitle);
        Log.i(TAG, "Setting text of pageTitle");
        pageTitle.setText(pageTitleString);

        //set the on click listener for each item in the list view
        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //checking friends status
                if (areWeFriends.equalsIgnoreCase(PacemakerENUMs.FRIENDS.toString())) {
                    selectedUser = loggedInUser.friendsList.get(position);
                } else if (areWeFriends.equalsIgnoreCase(PacemakerENUMs.NOTHING.toString())) {
                    selectedUser = loggedInUser.notFriendsList.get(position);
                } else if (areWeFriends.equalsIgnoreCase(PacemakerENUMs.PENDING.toString())) {
                    selectedUser = loggedInUser.pendingFriendsList.get(position);
                }
                Log.i(TAG, selectedUser.toString());
                listItemPressed(selectedUser, areWeFriends);
            }
        });
    }

    /**
     * Method used to start next activity showing the user by themselves
     * via also placing the areWeFriends object into the next activity
     *
     * @param friend
     * @param weAreFriends
     */
    public void listItemPressed(User friend, String weAreFriends) {
        Gson gS = new Gson();
        String userJson = gS.toJson(friend);
        String areWeFriends = gS.toJson(weAreFriends);
        Intent showFriend = new Intent(this, ShowFriend.class);
        showFriend.putExtra("MyFriend", userJson);
        showFriend.putExtra("WeAreFriendsCheck", areWeFriends);
        startActivity(showFriend);
    }

    /**
     * Use this to refresh the list when returned to from another activity
     */
    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        this.recreate();
    }


    /**
     * Set the friends list when the app.getFriends (or other variant) is finished
     * and set the user list to be that returned list
     *
     * @param aList
     */
    @Override
    public void setResponse(List<User> aList) {
        friendsAdapter.friendsList = aList;
        friendsAdapter.notifyDataSetChanged();
    }

    /**
     * Used if only one user was to be returned
     *
     * @param anObject
     */
    @Override
    public void setResponse(User anObject) {
    }

    /**
     * If GET error occurs, log it and toast to user
     *
     * @param e
     */
    @Override
    public void errorOccurred(Exception e) {
        Toast toast = Toast.makeText(this, "Error Retrieving Users...\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT);
        toast.show();
        Log.v(TAG, e.getLocalizedMessage());
    }


}