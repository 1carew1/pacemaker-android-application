package org.pacemaker.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.pacemaker.R;
import org.pacemaker.models.MyActivity;

import java.util.List;

/**
 * Created by colmcarew on 24/03/16.
 */
public class ActivityAdapter extends ArrayAdapter<MyActivity> {
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