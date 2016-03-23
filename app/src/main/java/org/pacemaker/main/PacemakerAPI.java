package org.pacemaker.main;

import java.util.List;

import org.pacemaker.http.Request;
import org.pacemaker.http.Response;
import org.pacemaker.http.Rest;
import org.pacemaker.models.Friends;
import org.pacemaker.utils.JsonParser;
import org.pacemaker.models.MyActivity;
import org.pacemaker.models.User;

import android.content.Context;

public class PacemakerAPI {
    public static void getUsers(Context context, Response<User> response, String dialogMesssage) {
        new GetUsers(context, response, dialogMesssage).execute();
    }

    public static void createUser(Context context, Response<User> response, String dialogMesssage, User user) {
        new CreateUser(context, response, dialogMesssage).execute(user);
    }

    public static void getActivities(Context context, User user, Response<MyActivity> response, String dialogMesssage) {
        new GetActivities(context, user, response, dialogMesssage).execute();
    }

    public static void createActivity(Context context, User user, Response<MyActivity> response, String dialogMesssage, MyActivity activity) {
        new CreateActivity(context, user, response, dialogMesssage).execute(activity);
    }

    public static void updateActivity(Context context, User user, Response<MyActivity> response, String dialogMesssage, MyActivity activity) {
        new UpdateActivity(context, user, activity, response, dialogMesssage).execute(activity);
    }

    public static void deleteActivity(Context context, User user, Response<MyActivity> response, String dialogMesssage, MyActivity activity) {
        new DeleteActivity(context, user, activity, response, dialogMesssage).execute(activity);
    }

    public static void getFriends(Context context, User user, Response<Friends> response, String dialogMesssage) {
        new GetFriends(context, user, response, dialogMesssage).execute();
    }
}

class GetUsers extends Request {
    public GetUsers(Context context, Response<User> callback, String message) {
        super(context, callback, message);
    }

    @Override
    protected List<User> doRequest(Object... params) throws Exception {
        String response = Rest.get("/api/users");
        List<User> userList = JsonParser.json2Users(response);
        return userList;
    }
}

class CreateUser extends Request {
    public CreateUser(Context context, Response<User> callback, String message) {
        super(context, callback, message);
    }

    @Override
    protected User doRequest(Object... params) throws Exception {
        String response = Rest.post("/api/users", JsonParser.user2Json(params[0]));
        return JsonParser.json2User(response);
    }
}

class GetActivities extends Request {
    private User user;

    public GetActivities(Context context, User user, Response<MyActivity> callback, String message) {
        super(context, callback, message);
        this.user = user;
    }

    @Override
    protected List<MyActivity> doRequest(Object... params) throws Exception {
        String response = Rest.get("/api/users/" + user.id + "/activities");
        List<MyActivity> activityList = JsonParser.json2Activities(response);
        //This is used to give each activity the correct locations
        for (MyActivity activity : activityList) {
            String getLocationsString = Rest.get("/api/users/" + user.id + "/activities/" + activity.id + "/routes");
            activity.routes = JsonParser.json2Locations(getLocationsString);
        }
        user.activities = activityList;
        return activityList;
    }
}

class CreateActivity extends Request {
    private User user;

    public CreateActivity(Context context, User user, Response<MyActivity> callback, String message) {
        super(context, callback, message);
        this.user = user;
    }

    @Override
    protected MyActivity doRequest(Object... params) throws Exception {
        String response = Rest.post("/api/users/" + user.id + "/activities", JsonParser.activity2Json(params[0]));
        return JsonParser.json2Activity(response);
    }
}

class UpdateActivity extends Request {
    private User user;
    private MyActivity activity;

    public UpdateActivity(Context context, User user, MyActivity activity, Response<MyActivity> callback, String message) {
        super(context, callback, message);
        this.user = user;
        this.activity = activity;
    }

    @Override
    protected MyActivity doRequest(Object... params) throws Exception {

        Rest.put("/api/users/" + user.id + "/activities/" + activity.id, JsonParser.activity2Json(params[0]));
        return activity;
    }
}

class DeleteActivity extends Request {
    private User user;
    private MyActivity activity;

    public DeleteActivity(Context context, User user, MyActivity activity, Response<MyActivity> callback, String message) {
        super(context, callback, message);
        this.user = user;
        this.activity = activity;
    }

    @Override
    protected MyActivity doRequest(Object... params) throws Exception {

        Rest.delete("/api/users/" + user.id + "/activities/" + activity.id);
        return activity;
    }
}

class GetFriends extends Request {
    private User user;

    public GetFriends(Context context, User user, Response<Friends> callback, String message) {
        super(context, callback, message);
        this.user = user;
    }

    @Override
    protected List<Friends> doRequest(Object... params) throws Exception {
        String response = Rest.get("/api/users/" + user.id + "/friends");
        List<Friends> friendsList = JsonParser.json2Friends(response);
        user.friendsList = friendsList;
        return friendsList;
    }
}