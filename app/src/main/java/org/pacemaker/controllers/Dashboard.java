package org.pacemaker.controllers;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

import com.google.gson.Gson;

import org.pacemaker.R;
import org.pacemaker.database.SQLLiteDataSource;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.User;
import org.pacemaker.utils.ImageUtils;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SQLLiteDataSource datasource;
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

     */


    //TODO : Add in a message in the dashboard telling the user of their next activity
    //TODO : Add and unfriend people
    //TODO : add profile photos upload + deletion
    // TODO : Add a settings page
    //TODO : Look into storing data + autologin
    public static final String TAG = "Dashboard";

    private TextView messageToUser;
    private TextView userName;
    private ImageView profilePhoto;
    private User loggedInUser;
    private PacemakerApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        datasource = new SQLLiteDataSource(this);
        searchDatabase();

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

        app = (PacemakerApp) getApplication();
        loggedInUser = app.getLoggedInUser();
        messageToUser = (TextView) findViewById(R.id.dashboardMessage);
        userName = (TextView) findViewById(R.id.userName);
        profilePhoto = (ImageView) findViewById(R.id.userPhoto);
        String messageToUserString = "Hello " + loggedInUser.firstname + " and welcome to your dashboard";
        messageToUser.setText(messageToUserString);
        userName.setText(loggedInUser.firstname + " " + loggedInUser.lastname);
        //Make username bold
        userName.setTypeface(null, Typeface.BOLD);
        ImageUtils.setUserImage(profilePhoto, loggedInUser.profilePhoto);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i = new Intent(this, Welcome.class);
        boolean willIStartTheActivity = true;
        boolean areWeFriends = true;

        Gson gS = new Gson();


        if (id == R.id.activitiesList) {
            i = new Intent(this, ActivitiesList.class);
            willIStartTheActivity = true;
        } else if (id == R.id.createActivity) {
            i = new Intent(this, CreateActivity.class);
            willIStartTheActivity = true;
        } else if (id == R.id.friendsList) {
            areWeFriends = true;
            i = new Intent(this, UserList.class);
            String target = gS.toJson(areWeFriends);
            i.putExtra("FriendsOrNot", target);
            willIStartTheActivity = true;
        } else if (id == R.id.notFriendsList) {
            areWeFriends = false;
            i = new Intent(this, UserList.class);
            String target = gS.toJson(areWeFriends);
            i.putExtra("FriendsOrNot", target);
            willIStartTheActivity = true;
        } else if (id == R.id.logoutFromDashboard) {
            willIStartTheActivity = false;
            app.logout();
            finish();
        } else if (id == R.id.progressReports) {
            i = new Intent(this, ProgressReports.class);
            willIStartTheActivity = true;
        } else if (id == R.id.suggestedWorkouts) {
            i = new Intent(this, SuggestedWorkouts.class);
            willIStartTheActivity = true;
        } else if (id == R.id.compareWorkouts) {
            i = new Intent(this, CompareWorkouts.class);
            willIStartTheActivity = true;
        } else {
            willIStartTheActivity = false;
            app.logout();
            finish();
        }
        if (willIStartTheActivity) {
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void searchDatabase() {


        Cursor cursor = datasource.getUser();
        if (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(SQLLiteDataSource.FIRST_NAME));
            name += " " + cursor.getString(cursor.getColumnIndex(SQLLiteDataSource.LAST_NAME));
            Toast.makeText(Dashboard.this, name + " has been saved to db", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Dashboard.this, "Person not found", Toast.LENGTH_SHORT).show();
        }


    }
}
