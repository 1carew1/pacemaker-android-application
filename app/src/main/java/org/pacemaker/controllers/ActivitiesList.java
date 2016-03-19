package org.pacemaker.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
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


                MyActivity userActivity = loggedInUser.activities.get(position);

                Log.i(TAG, userActivity.toString());
                listItemPressed(userActivity);
            }
        });


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
//        TextView location = (TextView) view.findViewById(R.id.location);
//        TextView distance = (TextView) view.findViewById(R.id.distance);
//        TextView duration = (TextView) view.findViewById(R.id.duration);

        startTime.setText(activity.startTime);

        type.setText(activity.kind);
//        location.setText(activity.location);
//        distance.setText("" + activity.distance);
//        duration.setText(activity.duration);
        return view;
    }

    @Override
    public int getCount() {
        return activities.size();
    }
}