package org.pacemaker.main;

import java.util.List;
import org.pacemaker.http.Request;
import org.pacemaker.http.Response;
import org.pacemaker.http.Rest;
import org.pacemaker.models.JsonParser;
import org.pacemaker.models.MyActivity;
import org.pacemaker.models.User;
import android.content.Context;

public class PacemakerAPI
{
  public static void getUsers(Context context, Response<User> response, String dialogMesssage)
  {
    new GetUsers(context, response, dialogMesssage).execute();
  }

  public static void createUser(Context context, Response<User> response, String dialogMesssage, User user)
  {
    new CreateUser(context, response, dialogMesssage).execute(user);
  }

  public static void getActivities(Context context, User user, Response<MyActivity> response, String dialogMesssage)
  {
    new GetActivities(context, user, response, dialogMesssage).execute();
  }

  public static void createActivity(Context context, User user, Response<MyActivity> response, String dialogMesssage, MyActivity activity)
  {
    new CreateActivity(context, user, response, dialogMesssage).execute(activity);
  }
}

class GetUsers extends Request
{
  public GetUsers(Context context, Response<User> callback, String message)
  {
    super(context, callback, message);
  }

  @Override
  protected List<User> doRequest(Object... params) throws Exception
  {
    String response = Rest.get("/api/users");
    List<User> userList = JsonParser.json2Users(response);
    return userList;
  }
}

class CreateUser extends Request
{
  public CreateUser(Context context, Response<User> callback, String message)
  {
    super(context, callback, message);
  }

  @Override
  protected User doRequest(Object... params) throws Exception
  {
    String response = Rest.post("/api/users", JsonParser.user2Json(params[0]));
    return JsonParser.json2User(response);
  }
}

class GetActivities extends Request
{
  private User user;

  public GetActivities(Context context, User user, Response<MyActivity> callback, String message)
  {
    super(context, callback, message);
    this.user = user;
  }

  @Override
  protected List<MyActivity> doRequest(Object... params) throws Exception
  {
    String response =  Rest.get("/api/users/" + user.id + "/activities");
    List<MyActivity> ActivityList = JsonParser.json2Activities(response);
    return ActivityList;
  }
}

class CreateActivity extends Request
{
  private User user;

  public CreateActivity(Context context, User user, Response<MyActivity> callback, String message)
  {
    super(context, callback, message);
    this.user = user;
  }

  @Override
  protected MyActivity doRequest(Object... params) throws Exception
  {
    String response = Rest.post ("/api/users/" + user.id + "/activities", JsonParser.activity2Json(params[0]));
    return JsonParser.json2Activity(response);
  }
}