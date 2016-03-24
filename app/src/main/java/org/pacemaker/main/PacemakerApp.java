package org.pacemaker.main;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pacemaker.http.Response;
import org.pacemaker.models.Friends;
import org.pacemaker.models.MyActivity;
import org.pacemaker.models.User;

import android.app.Application;
import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;


public class PacemakerApp extends Application implements Response<User> {
    private Map<String, User> userMapViaEmail = new HashMap<String, User>();
    private Map<Long, User> userMapViaId = new HashMap<Long, User>();
    private List<User> friendsOfLoggedInUser = new ArrayList<>();
    private User loggedInUser;
    private boolean connected = false;

    public void connectToPacemakerAPI(Context context) {
        PacemakerAPI.getUsers(context, this, "Retrieving list of users");
    }

    @Override
    public void setResponse(List<User> aList) {
        connected = true;
        for (User user : aList) {
            userMapViaEmail.put(user.email, user);
            userMapViaId.put(user.id, user);
        }
    }

    @Override
    public void setResponse(User user) {
        connected = true;
        userMapViaEmail.put(user.email, user);
        userMapViaId.put(user.id, user);
    }

    @Override
    public void errorOccurred(Exception e) {
        connected = false;
        Toast toast = Toast.makeText(this, "Failed to connect to Pacemaker Service", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void registerUser(Context context, User user) {
        PacemakerAPI.createUser(context, this, "Registering new user", user);
    }

    public boolean loginUser(String email, String password) {
        loggedInUser = userMapViaEmail.get(email);
        if (loggedInUser != null && !loggedInUser.password.equals(password)) {
            loggedInUser = null;
        }
        return loggedInUser != null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public List<String> getUserEmails() {
        List<String> userEmails = new ArrayList<>();
        userEmails.addAll(userMapViaEmail.keySet());
        return userEmails;
    }

    public void logout() {
        loggedInUser = null;
    }

    public void createActivity(Context context, MyActivity activity, Response<MyActivity> responder) {
        if (loggedInUser != null) {
            PacemakerAPI.createActivity(context, loggedInUser, responder, "Creating activity...", activity);
        }
    }

    public void updateActivity(Context context, MyActivity activity, Response<MyActivity> responder) {
        if (loggedInUser != null) {
            PacemakerAPI.updateActivity(context, loggedInUser, responder, "Creating activity...", activity);
        }
    }

    public void deleteActivity(Context context, MyActivity activity, Response<MyActivity> responder) {
        if (loggedInUser != null) {
            PacemakerAPI.deleteActivity(context, loggedInUser, responder, "Creating activity...", activity);
        }
    }

    public void getActivities(Context context, Response<MyActivity> responder) {
        PacemakerAPI.getActivities(context, loggedInUser, responder, "Retrieving Activities...");
    }

    public void getFriends(Context context, Response<User> responder) {
        PacemakerAPI.getFriends(context, loggedInUser, responder, "Retrieving Friends...");
    }

    public List<User> getFriends() {
        return friendsOfLoggedInUser;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("Pacemaker", "Pacemaker App Started");
    }
}

