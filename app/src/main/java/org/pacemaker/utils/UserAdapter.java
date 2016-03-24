package org.pacemaker.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.pacemaker.R;
import org.pacemaker.models.MyActivity;
import org.pacemaker.models.User;

import java.util.List;

/**
 * Created by colmcarew on 24/03/16.
 */
public class UserAdapter extends ArrayAdapter<User> {
    private Context context;
    public List<User> users;

    public UserAdapter(Context context, List<User> users) {
        super(context, R.layout.user_row_layout, users);
        this.context = context;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_row_layout, parent, false);
        User user = users.get(position);

//        TextView startTime = (TextView) view.findViewById(R.id.startTime);
//        TextView type = (TextView) view.findViewById(R.id.type);
//        startTime.setText(activity.startTime);
//        type.setText(activity.kind);

        return view;
    }

    @Override
    public int getCount() {
        return users.size();
    }
}