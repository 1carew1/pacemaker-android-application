package org.pacemaker.controllers;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.pacemaker.R;
import org.pacemaker.http.Response;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.Friends;
import org.pacemaker.models.User;

import java.util.ArrayList;
import java.util.List;

public class FriendsList extends AppCompatActivity implements Response<Friends> {

    public static final String TAG = "FriendsList";

    private PacemakerApp app;
    private ListView friendsListView;
    private TextView pageTitle;
    private FriendsAdapter friendsAdapter;
    private List<Friends> friendsList = new ArrayList<>();
    private User loggedInUser;
    private User selectedFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        app = (PacemakerApp) getApplication();
        loggedInUser = app.getLoggedInUser();
        final String loggedInUserString = loggedInUser.firstname + " " + loggedInUser.lastname;
        Log.i(TAG, "In on create for Friends List View for " + loggedInUserString);

        friendsListView = (ListView) findViewById(R.id.friendsListView);
        pageTitle = (TextView) findViewById(R.id.pageTitle);
        Log.i(TAG, "Setting text of pageTitle");
        pageTitle.setText("Friends of " + loggedInUserString);

        friendsAdapter = new FriendsAdapter(this, friendsList);
        friendsListView.setAdapter(friendsAdapter);

        app.getFriends(this, this);

        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                selectedFriend = app.getFriends().get(position);
                Log.i(TAG, selectedFriend.toString());
                listItemPressed(selectedFriend);
            }
        });

    }

    public void listItemPressed(User friend) {

        Gson gS = new Gson();
        String target = gS.toJson(friend);
//        Intent showFriend = new Intent(this, ShowFriend.class);
//        showFriend.putExtra("SelectedActivity", target);
//        startActivity(showFriend);
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
    public void setResponse(List<Friends> aList) {
        friendsAdapter.friendsList = aList;
        friendsAdapter.notifyDataSetChanged();
    }

    @Override
    public void setResponse(Friends anObject) {
    }

    @Override
    public void errorOccurred(Exception e) {
        Toast toast = Toast.makeText(this, "Error Retrieving Activities...\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT);
        toast.show();
        Log.v("Getting Activities", e.getLocalizedMessage());
    }


}

class FriendsAdapter extends ArrayAdapter<Friends> {
    private Context context;
    public List<Friends> friendsList;

    public FriendsAdapter(Context context, List<Friends> friendsList) {
        super(context, R.layout.activity_row_layout, friendsList);
        this.context = context;
        this.friendsList = friendsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.friend_row_layout, parent, false);
        Friends friend = friendsList.get(position);


        TextView friendName = (TextView) view.findViewById(R.id.friendName);
        friendName.setText(friend.accepted);
        return view;
    }

    @Override
    public int getCount() {
        return friendsList.size();
    }
}