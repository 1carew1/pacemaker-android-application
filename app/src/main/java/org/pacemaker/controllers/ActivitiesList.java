package org.pacemaker.controllers;

import java.util.ArrayList;
import java.util.List;

import org.pacemaker.R;
import org.pacemaker.http.Response;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.MyActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ActivitiesList extends android.app.Activity implements Response<MyActivity> {
    private PacemakerApp app;
    private ListView activitiesListView;
    private ActivityAdapter activitiesAdapter;
    private List<MyActivity> activities = new ArrayList<MyActivity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_list);

        app = (PacemakerApp) getApplication();

        activitiesListView = (ListView) findViewById(R.id.activitiesListView);
        activitiesAdapter = new ActivityAdapter(this, activities);
        activitiesListView.setAdapter(activitiesAdapter);

        app.getActivities(this, this);

        activitiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String messageForToast = "List item : " + position + " was pressed";
                Toast toast = Toast.makeText(ActivitiesList.this, messageForToast, Toast.LENGTH_SHORT);
                toast.show();
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
        TextView type = (TextView) view.findViewById(R.id.type);
        TextView location = (TextView) view.findViewById(R.id.location);
        TextView distance = (TextView) view.findViewById(R.id.distance);
        TextView duration = (TextView) view.findViewById(R.id.duration);

        type.setText(activity.kind);
        location.setText(activity.location);
        distance.setText("" + activity.distance);
        duration.setText(activity.duration);
        return view;
    }

    @Override
    public int getCount() {
        return activities.size();
    }
}