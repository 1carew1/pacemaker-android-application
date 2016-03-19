package org.pacemaker.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import org.pacemaker.models.MyActivity;
import org.pacemaker.models.User;

import java.util.ArrayList;
import java.util.List;


public class ActivitiesList extends android.app.Activity implements Response<MyActivity> {

    public static final String TAG = "PacemakerPart1";

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

        app.getActivities(this, this);

        activitiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {


                selectedActivity = loggedInUser.activities.get(position);

                Log.i(TAG, selectedActivity.toString());
                listItemPressed(selectedActivity);
            }
        });

        //Create a hold item listener
        activitiesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
//                                Toast.makeText(ActivitiesList.this, "Activity on the " + selectedActivity.startTime +
//                                        " to " + selectedActivity.kind + " has been deleted"
//                                        , Toast.LENGTH_SHORT).show();
                                //Refresh the Activity
                                onRestart();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                Toast.makeText(ActivitiesList.this, "Activity Not Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                return false;
            }
        });

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
        Log.v("Getting Activities", e.getLocalizedMessage());
    }


    public void listItemPressed(MyActivity selectedActivity) {

        Gson gS = new Gson();
        String target = gS.toJson(selectedActivity);
        Intent showActivity = new Intent(this, ShowMyActivity.class);
        showActivity.putExtra("SelectedActivity", target);
        startActivity(showActivity);
    }

    public void createActivityButtonPressed(View view) {
        Intent i = new Intent(ActivitiesList.this, CreateActivity.class);
        startActivity(i);
    }
}

class ActivityAdapter extends ArrayAdapter<MyActivity> {
    private Context context;
    public List<MyActivity> activities;

    public ActivityAdapter(Context context, List<MyActivity> activities) {
        super(context, R.layout.activity_row_layout, activities);
        this.context = context;
        this.activities = activities;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_row_layout, parent, false);
        MyActivity activity = activities.get(position);
        TextView startTime = (TextView) view.findViewById(R.id.startTime);
        TextView type = (TextView) view.findViewById(R.id.type);

        startTime.setText(activity.startTime);

        type.setText(activity.kind);
        return view;
    }

    @Override
    public int getCount() {
        return activities.size();
    }
}