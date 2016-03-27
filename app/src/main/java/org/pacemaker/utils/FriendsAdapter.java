package org.pacemaker.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import org.pacemaker.R;
import org.pacemaker.http.Rest;
import org.pacemaker.models.User;

import java.util.List;

/**
 * Created by colmcarew on 24/03/16.
 */
public class FriendsAdapter extends ArrayAdapter<User> {
    private Context context;
    public List<User> friendsList;

    public FriendsAdapter(Context context, List<User> friendsList) {
        super(context, R.layout.activity_row_layout, friendsList);
        this.context = context;
        this.friendsList = friendsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View view = inflater.inflate(R.layout.friend_row_layout, parent, false);
        User friend = friendsList.get(position);


        TextView friendName = (TextView) view.findViewById(R.id.friendName);
        friendName.setText(friend.firstname + " " + friend.lastname);

        ImageView imageView = (ImageView) view.findViewById(R.id.profilePhoto);

        // Ion library used to display image
        org.pacemaker.utils.ImageUtils.setUserImage(imageView, friend.profilePhoto);
        return view;
    }

    @Override
    public int getCount() {
        return friendsList.size();
    }
}