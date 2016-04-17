package org.pacemaker.main;


import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.pacemaker.http.Response;
import org.pacemaker.models.MyActivity;
import org.pacemaker.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO : Replace this with alternative method Application This is only good for temporary data
//TODO : Save data to device storage ! - Used Shared Preferences or SQL Like DB - Realm!

/**
 * Application used to share data between Activities
 */
public class PacemakerApp extends Application implements Response<User> {
    private static final String TAG = "PacemakerApp";

    //Maps of Users + the logged in user
    private Map<String, User> userMapViaEmail = new HashMap<String, User>();
    private Map<Long, User> userMapViaId = new HashMap<Long, User>();
    private User loggedInUser;
    //boolean to show if app can connect to Pacemaker Web App
    private boolean connected = false;


    /**
     * Connect to the Pacemaker Web App and get all the users
     *
     * @param context
     */
    public void connectToPacemakerAPI(Context context) {
        PacemakerAPI.getUsers(context, this, "Retrieving list of users");
    }

    /**
     * Run this when the list of users is obtained
     *
     * @param aList
     */
    @Override
    public void setResponse(List<User> aList) {
        connected = true;
        for (User user : aList) {
            userMapViaEmail.put(user.email, user);
            userMapViaId.put(user.id, user);
        }
    }

    /**
     * Run this when a single user is obtained
     *
     * @param user
     */
    @Override
    public void setResponse(User user) {
        connected = true;
        userMapViaEmail.put(user.email, user);
        userMapViaId.put(user.id, user);
    }

    /**
     * If an error occurs with the GET log it and toast to the user
     *
     * @param e
     */
    @Override
    public void errorOccurred(Exception e) {
        // connected = false;
        Toast toast = Toast.makeText(this, "Failed to connect to Pacemaker Service", Toast.LENGTH_SHORT);
        toast.show();
        Log.i(TAG, e.getLocalizedMessage());
    }

    /**
     * Method used to create a new user
     *
     * @param context
     * @param user
     */
    public void registerUser(Context context, User user) {
        PacemakerAPI.createUser(context, this, "Registering new user", user);
    }

    /**
     * Method used to login a user
     *
     * @param email
     * @param password
     * @return
     */
    public boolean loginUser(String email, String password) {
        loggedInUser = userMapViaEmail.get(email);
        if (loggedInUser != null && !loggedInUser.password.equals(password)) {
            loggedInUser = null;
        }
        return loggedInUser != null;
    }

    /**
     * Method used to return the logged in user
     *
     * @return
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Method used to get a list of all user emails associated with the Pacemaker Web App
     *
     * @return
     */
    public List<String> getUserEmails() {
        List<String> userEmails = new ArrayList<>();
        userEmails.addAll(userMapViaEmail.keySet());
        return userEmails;
    }

    /**
     * Method used to logout of this application
     */
    public void logout() {
        loggedInUser = null;
    }

    /**
     * Method used to create an activity
     *
     * @param context
     * @param activity
     * @param responder
     */
    public void createActivity(Context context, MyActivity activity, Response<MyActivity> responder) {
        if (loggedInUser != null) {
            PacemakerAPI.createActivity(context, loggedInUser, responder, "Creating activity...", activity);
        }
    }

    /**
     * Method used to update an activity
     *
     * @param context
     * @param activity
     * @param responder
     */
    public void updateActivity(Context context, MyActivity activity, Response<MyActivity> responder) {
        if (loggedInUser != null) {
            PacemakerAPI.updateActivity(context, loggedInUser, responder, "Creating activity...", activity);
        }
    }

    /**
     * Method used to delete an activity
     *
     * @param context
     * @param activity
     * @param responder
     */
    public void deleteActivity(Context context, MyActivity activity, Response<MyActivity> responder) {
        if (loggedInUser != null) {
            PacemakerAPI.deleteActivity(context, loggedInUser, responder, "Creating activity...", activity);
        }
    }

    /**
     * Method used to get all activities of a user
     *
     * @param context
     * @param responder
     * @param userToGetActivitiesOf
     */
    public void getActivities(Context context, Response<MyActivity> responder, User userToGetActivitiesOf) {
        PacemakerAPI.getActivities(context, userToGetActivitiesOf, responder, "Retrieving Activities...");
    }

    /**
     * Method used to get friends of logged in user
     *
     * @param context
     * @param responder
     */
    public void getFriends(Context context, Response<User> responder) {
        PacemakerAPI.getFriends(context, loggedInUser, responder, "Retrieving Friends...");
    }


    /**
     * Method used to get list of users who are not friends
     *
     * @param context
     * @param responder
     */
    public void getUsersWhoAreNotFriends(Context context, Response<User> responder) {
        PacemakerAPI.getUsersWhoAreNotFriends(context, loggedInUser, responder, "Retrieving Friends to Add...");
    }

    /**
     * Method used to get list of users who added the logged in user but have not been accepted yet
     *
     * @param context
     * @param responder
     */
    public void getPendingFriends(Context context, Response<User> responder) {
        PacemakerAPI.getPendingFriends(context, loggedInUser, responder, "Retrieving Pending Friends...");
    }

    /**
     * Method used to add a friend
     *
     * @param context
     * @param friendId
     */
    public void addFriend(Context context, Long friendId) {
        PacemakerAPI.addFriend(context, loggedInUser, friendId, this, "Adding Friend.....");
    }

    /**
     * Method used to accept a friend
     *
     * @param context
     * @param friendId
     */
    public void acceptFriend(Context context, Long friendId) {
        PacemakerAPI.acceptFriend(context, loggedInUser, friendId, this, "Acepting Friend.....");
    }

    /**
     * Method used to unfriend a user
     *
     * @param context
     * @param friendId
     */
    public void unFriend(Context context, Long friendId) {
        PacemakerAPI.unFriend(context, loggedInUser, friendId, this, "Deleting Friend.....");
    }

    /**
     * Method called when application is created
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("Pacemaker", "Pacemaker App Started");
    }

    /**
     * Metho used to get the user map
     *
     * @return
     */
    public Map<String, User> getUserMap() {
        return userMapViaEmail;
    }
}

