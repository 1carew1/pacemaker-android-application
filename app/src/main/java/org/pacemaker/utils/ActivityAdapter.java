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

    /**
     * Constructor
     *
     * @param context
     * @param activities
     */
    public ActivityAdapter(Context context, List<MyActivity> activities) {
        super(context, R.layout.activity_row_layout, activities);
        this.context = context;
        this.activities = activities;
    }

    /**
     * Method used to set the view
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_row_layout, parent, false);
        MyActivity activity = activities.get(position);
        TextView startTime = (TextView) view.findViewById(R.id.startTime);
        TextView type = (TextView) view.findViewById(R.id.type);
        //String used to get the day + date ie Sat Mar 12 rather than the entire date string
        String dateStringForActivity = activity.startTime.replaceAll("(.*?)\\d{2}\\:.+", "$1") + " - ";

        startTime.setText(dateStringForActivity);

        type.setText(activity.kind);
        return view;
    }

    @Override
    public int getCount() {
        return activities.size();
    }
}