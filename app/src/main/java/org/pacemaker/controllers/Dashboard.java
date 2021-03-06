package org.pacemaker.controllers;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.pacemaker.R;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.User;
import org.pacemaker.utils.ImageUtils;
import org.pacemaker.utils.PacemakerENUMs;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /*
    Implement/ed Patterns:
   Creational
        Builder - Used when deleting an activity - Dialog Builder
        Dependency Injection - Will be using shared preferences for this - http://google.github.io/dagger/
        Singleton - Used as the Application

   Structural
        Adapter - Used anywhere we have an Adapter
        Facade - Ion is Facade and this application does not care how Ion pulls the image - Also see http://square.github.io/retrofit/

  Behavioral
        Command - To be added - See https://github.com/greenrobot/EventBus
        Observer - To be added - https://github.com/ReactiveX/RxAndroid
        Model View Controller - Android, Models are User, View are the xml layout and Controllers are the Activities
        memento - putExtra

  Architectural Pattern
        Half sync half async

     */


    //TODO : Add in a message in the dashboard telling the user of their next activity
    //TODO : add profile photos upload + deletion in settings pages
    //TODO : Look into storing data + autologin - realm

    public static final String TAG = "DashboardActivity";
    //Application
    private PacemakerApp app;
    //Logged in user
    private User loggedInUser;
    //views
    private TextView messageToUser;
    private TextView userName;
    private ImageView profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Boiler plate code for navigation drawer
        setSupportActionBar(toolbar);

        // Use this if you want to create an email button bottom right of the page
        // Alos need to uncomment from app_bar_dashboard.xml
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Get the application
        app = (PacemakerApp) getApplication();
        //get the logged in user
        loggedInUser = app.getLoggedInUser();
        //associate the correct views with variables
        messageToUser = (TextView) findViewById(R.id.dashboardMessage);
        userName = (TextView) findViewById(R.id.userName);
        profilePhoto = (ImageView) findViewById(R.id.userPhoto);

        String messageToUserString = "Hello " + loggedInUser.firstname + " and welcome to your dashboard";
        messageToUser.setText(messageToUserString);
        userName.setText(loggedInUser.firstname + " " + loggedInUser.lastname);
        //Make username bold
        userName.setTypeface(null, Typeface.BOLD);
        //Set the profile photo
        ImageUtils.setUserImage(profilePhoto, loggedInUser.profilePhoto);

    }

    /**
     * When back button is pressed exit the drawer
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * add items ot actionbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    /**
     * When settings is selected run this method
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsActivity = new Intent(this, Settings.class);
            startActivity(settingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * When one of the navigation items are selected, run this method
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i = new Intent(this, Welcome.class);
        boolean willIStartTheActivity = true;
        String areWeFriends;

        Gson gS = new Gson();

        //List of activities to start based on selection
        if (id == R.id.activitiesList) {
            i = new Intent(this, ActivitiesList.class);
        } else if (id == R.id.createActivity) {
            i = new Intent(this, CreateActivity.class);
        } else if (id == R.id.friendsList) {
            areWeFriends = PacemakerENUMs.FRIENDS.toString();
            i = new Intent(this, UserList.class);
            String target = gS.toJson(areWeFriends);
            i.putExtra(PacemakerENUMs.FRIENDSORNOT.toString(), target);
        } else if (id == R.id.notFriendsList) {
            areWeFriends = PacemakerENUMs.NOTHING.toString();
            i = new Intent(this, UserList.class);
            String target = gS.toJson(areWeFriends);
            i.putExtra(PacemakerENUMs.FRIENDSORNOT.toString(), target);
        } else if (id == R.id.pendingFriends) {
            areWeFriends = PacemakerENUMs.PENDING.toString();
            i = new Intent(this, UserList.class);
            String target = gS.toJson(areWeFriends);
            i.putExtra(PacemakerENUMs.FRIENDSORNOT.toString(), target);
        } else if (id == R.id.logoutFromDashboard) {
            willIStartTheActivity = false;
            app.logout();
            finish();
        } else if (id == R.id.progressReports) {
            i = new Intent(this, ProgressReports.class);
        } else if (id == R.id.suggestedWorkouts) {
            i = new Intent(this, SuggestedWorkouts.class);
        } else {
            willIStartTheActivity = false;
            app.logout();
            finish();
        }
        if (willIStartTheActivity) {
            startActivity(i);
        }
        //CLose Drawer when finished
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
