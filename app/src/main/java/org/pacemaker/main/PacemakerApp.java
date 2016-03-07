package org.pacemaker.main;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pacemaker.http.Response;
import org.pacemaker.models.MyActivity;
import org.pacemaker.models.User;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


public class PacemakerApp extends Application implements Response<User>
{
  private Map<String, User>             users         = new HashMap<String, User>();
  private Map<String, List<MyActivity>> activities    = new HashMap<String, List<MyActivity>>();
  private User                          loggedInUser;
  private boolean                       connected     = false;

  public void connectToPacemakerAPI(Context context)
  {
    PacemakerAPI.getUsers(context, this, "Retrieving list of users");
  }

  @Override
  public void setResponse(List<User> aList)
  {
    connected = true;
    for (User user : aList)
    {
      users.put(user.email, user);
    }
  }

  @Override
  public void setResponse(User user)
  {
    connected = true;
    users.put(user.email, user);
    activities.put(user.email, new ArrayList<MyActivity>());
  }

  @Override
  public void errorOccurred(Exception e)
  {
    connected = false;
    Toast toast = Toast.makeText(this, "Failed to connect to Pacemaker Service", Toast.LENGTH_SHORT);
    toast.show();
  }

  public void registerUser(Context context, User user)
  {
    PacemakerAPI.createUser(context, this, "Registering new user", user);
  }

  public boolean loginUser(String email, String password)
  {
    loggedInUser = users.get(email);
    if (loggedInUser != null && !loggedInUser.password.equals(password))
    {
      loggedInUser = null;
    }
    return loggedInUser != null;
  }

  public void logout()
  {
    loggedInUser = null;
  }

  public void createActivity (Context context, MyActivity activity, Response<MyActivity> responder)
  {
    if (loggedInUser != null)
    {
      PacemakerAPI.createActivity(context, loggedInUser, responder, "Creating activity...", activity);
    }
  }

  public void getActivities(Context context, Response<MyActivity> responder)
  {
    PacemakerAPI.getActivities(context, loggedInUser, responder, "Retrieving Activities...");
  }

  @Override
  public void onCreate()
  {
    super.onCreate();
    Log.v("Pacemaker", "Pacemaker App Started");
  }
}

