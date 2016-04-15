package org.pacemaker.controllers;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import org.pacemaker.models.MyActivity;
import org.pacemaker.models.User;
import org.pacemaker.utils.ActivityAdapter;

import java.util.ArrayList;
import java.util.List;


public class ActivitiesList extends AppCompatActivity implements Response<MyActivity> {

    public static final String TAG = "ActivitiesList";

    private PacemakerApp app;
    private ListView activitiesListView;
    private TextView textView;
    private ActivityAdapter activitiesAdapter;
    private List<MyActivity> activities = new ArrayList<MyActivity>();
    private User loggedInUser;
    private MyActivity selectedActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_list);

        app = (PacemakerApp) getApplication();

        loggedInUser = app.getLoggedInUser();
        final String loggedInUserString = loggedInUser.firstname + " " + loggedInUser.lastname;
        Log.i(TAG, "In on create for Activities List View for " + loggedInUserString);

        activitiesListView = (ListView) findViewById(R.id.activitiesListView);

        textView = (TextView) findViewById(R.id.textView);

        Log.i(TAG, "Setting text of textView");
        textView.setText("Activities of " + loggedInUserString);

        activitiesAdapter = new ActivityAdapter(this, activities);

        activitiesListView.setAdapter(activitiesAdapter);


        app.getActivities(this, this, loggedInUser);

        activitiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                      @Override
                                                      public void onItemClick(AdapterView<?> parent, View view, int position,
                                                                              long id) {


                                                          selectedActivity = loggedInUser.activities.get(position);

                                                          Log.i(TAG, selectedActivity.toString());
                                                          listItemPressed(selectedActivity);
                                                      }
                                                  }

        );

        //Create a hold item listener
        activitiesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()

                                                      {
                                                          @Override
                                                          public boolean onItemLongClick(AdapterView<?> parent, View view,
                                                                                         int position, long id) {

                                                              // Prevent the on click listener executing when
                                                              // the on hold listener is executing
                                                              activitiesListView.setOnItemClickListener(null);

                                                              selectedActivity = loggedInUser.activities.get(position);

                                                              new AlertDialog.Builder(parent.getContext())
                                                                      .setTitle("Delete Activity")
                                                                      .setMessage("Are you sure you want to delete this activity?")
                                                                      .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                                          public void onClick(DialogInterface dialog, int which) {
                                                                              //Delete the activity
                                                                              app.deleteActivity(ActivitiesList.this, selectedActivity, ActivitiesList.this);
                                                                              onRestart();
                                                                          }
                                                                      })
                                                                      .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                                          public void onClick(DialogInterface dialog, int which) {
                                                                              // Put back the click listener for the item
                                                                              Toast.makeText(ActivitiesList.this, "Activity Not Deleted", Toast.LENGTH_SHORT).show();
                                                                              activitiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                                  @Override
                                                                                  public void onItemClick(AdapterView<?> parent, View view, int position,
                                                                                                          long id) {
                                                                                      selectedActivity = loggedInUser.activities.get(position);
                                                                                      Log.i(TAG, selectedActivity.toString());
                                                                                      listItemPressed(selectedActivity);
                                                                                  }
                                                                              });
                                                                          }
                                                                      })
                                                                      .show();
                                                              return false;
                                                          }
                                                      }

        );

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
    public void setResponse(List<MyActivity> aList) {
        activitiesAdapter.activities = aList;
        activitiesAdapter.notifyDataSetChanged();
    }

    @Override
    public void setResponse(MyActivity anObject) {
    }

    @Override
    public void errorOccurred(Exception e) {
        Toast toast = Toast.makeText(this, "Error Retrieving Activities...\n" + e.getLocalizedMessage(), Toast.LENGTH_SHORT);
        toast.show();
        Log.v(TAG, e.getLocalizedMessage());
    }


    public void listItemPressed(MyActivity selectedActivity) {

        Gson gS = new Gson();
        String target = gS.toJson(selectedActivity);
        Intent showActivity = new Intent(this, ShowMyActivity.class);
        showActivity.putExtra("SelectedActivity", target);
        startActivity(showActivity);
    }

}

