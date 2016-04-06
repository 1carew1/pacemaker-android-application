package org.pacemaker.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.pacemaker.http.Request;
import org.pacemaker.http.Response;
import org.pacemaker.http.Rest;
import org.pacemaker.models.Friends;
import org.pacemaker.utils.JsonParser;
import org.pacemaker.models.MyActivity;
import org.pacemaker.models.User;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;

public class PacemakerAPI {
    public static final String TAG = "PacemakerAPI";

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

    public static void getFriends(Context context, User user, Response<User> response, String dialogMesssage) {
        new GetFriends(context, user, response, dialogMesssage).execute();
    }

    public static void getUsersWhoAreNotFriends(Context context, User user, Response<User> response, String dialogMesssage) {
        new GetUsersWhoAreNotFriends(context, user, response, dialogMesssage).execute();
    }

    public static void getPendingFriends(Context context, User user, Response<User> response, String dialogMesssage) {
        new GetPendingFriendsThatAddedMe(context, user, response, dialogMesssage).execute();
    }

    public static void addFriend(Context context, User user, Long friendId, Response<User> response, String dialogMesssage) {
        new AddFriend(context, user, friendId, response, dialogMesssage).execute();
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

    public GetFriends(Context context, User user, Response<User> callback, String message) {
        super(context, callback, message);
        this.user = user;
    }

    @Override
    protected List<User> doRequest(Object... params) throws Exception {
        GetUsers getUsers = new GetUsers(null, null, null);
        List<User> listOfAllUsers = getUsers.doRequest();
        // Update to API level 19
        Map<Long, User> listOfAllUsersMap = new ArrayMap<Long, User>();
        for (User u : listOfAllUsers) {
            listOfAllUsersMap.put(u.id, u);
        }
        String response = Rest.get("/api/users/" + user.id + "/friends");
        List<Friends> friendsList = JsonParser.json2Friends(response);
        user.friendObjecList = friendsList;

        List<User> allFriends = new ArrayList<>();
        for (Friends f : friendsList) {
            // For the time being only add accepted friends
            if (f.accepted.equalsIgnoreCase("Yes")) {
                User u = listOfAllUsersMap.get(f.friendId);
                allFriends.add(u);
            }
        }
        user.friendsList = allFriends;
        return allFriends;
    }
}

class GetUsersWhoAreNotFriends extends Request {
    private User user;

    public GetUsersWhoAreNotFriends(Context context, User user, Response<User> callback, String message) {
        super(context, callback, message);
        this.user = user;
    }

    @Override
    protected List<User> doRequest(Object... params) throws Exception {
        GetUsers getUsers = new GetUsers(null, null, null);
        List<User> listOfAllUsers = getUsers.doRequest();

        GetFriends getFriends = new GetFriends(null, user, null, null);
        List<User> listOfAllFriends = getFriends.doRequest();

        GetPendingFriendsThatAddedMe pendingFriends = new GetPendingFriendsThatAddedMe(null, user, null, null);
        List<User> listOfPendingFriends = pendingFriends.doRequest();

        GetPendingFriendsThatIAdded pendingFriendsIAdded = new GetPendingFriendsThatIAdded(null, user, null, null);
        List<User> listOfPendingFriendsIAdded = pendingFriendsIAdded.doRequest();

        listOfAllFriends.addAll(listOfPendingFriends);
        listOfAllFriends.addAll(listOfPendingFriendsIAdded);

        if (listOfAllFriends.isEmpty() || listOfAllFriends == null) {
            listOfAllFriends = new ArrayList<>();
        }
        List<User> notFriends = new ArrayList<>();
        if (listOfAllUsers.isEmpty() || listOfAllUsers == null) {
            //Do Nothing
        } else {
            for (User u : listOfAllUsers) {
                if (listOfAllFriends.contains(u) || user.equals(u)) {
                    //Do nothing
                } else {
                    notFriends.add(u);
                }
            }
        }
        user.notFriendsList = notFriends;

        return notFriends;
    }
}

class AddFriend extends Request {
    public static final String TAG = "AddFriendInPacemakerAPI";
    private User user;
    private long friendId;

    public AddFriend(Context context, User user, Long friendId, Response<User> callback, String message) {
        super(context, callback, message);
        this.user = user;
        this.friendId = friendId;
    }

    @Override
    protected Object doRequest(Object... params) throws Exception {
        String addFriend = "/api/users/" + user.id + "/friends/" + friendId;
        Log.i(TAG, addFriend);
        String response = Rest.post(addFriend, "{}");
        return new User();
    }
}

class GetPendingFriendsThatAddedMe extends Request {
    private User user;

    public GetPendingFriendsThatAddedMe(Context context, User user, Response<User> callback, String message) {
        super(context, callback, message);
        this.user = user;
    }

    @Override
    protected List<User> doRequest(Object... params) throws Exception {
        GetUsers getUsers = new GetUsers(null, null, null);
        List<User> listOfAllUsers = getUsers.doRequest();
        // Update to API level 19
        Map<Long, User> listOfAllUsersMap = new ArrayMap<Long, User>();
        for (User u : listOfAllUsers) {
            listOfAllUsersMap.put(u.id, u);
        }
        String response = Rest.get("/api/users/" + user.id + "/friendsWhoAddedMe");
        List<Friends> friendsList = JsonParser.json2Friends(response);
        user.friendObjecList = friendsList;

        List<User> allFriends = new ArrayList<>();
        for (Friends f : friendsList) {
            User u = listOfAllUsersMap.get(f.userId);
            allFriends.add(u);
        }
        user.friendsList = allFriends;
        return allFriends;
    }
}


class GetPendingFriendsThatIAdded extends Request {
    private User user;

    public GetPendingFriendsThatIAdded(Context context, User user, Response<User> callback, String message) {
        super(context, callback, message);
        this.user = user;
    }

    @Override
    protected List<User> doRequest(Object... params) throws Exception {
        GetUsers getUsers = new GetUsers(null, null, null);
        List<User> listOfAllUsers = getUsers.doRequest();
        // Update to API level 19
        Map<Long, User> listOfAllUsersMap = new ArrayMap<Long, User>();
        for (User u : listOfAllUsers) {
            listOfAllUsersMap.put(u.id, u);
        }
        String response = Rest.get("/api/users/" + user.id + "/friendsWhoIAdded");
        List<Friends> friendsList = JsonParser.json2Friends(response);
        user.friendObjecList = friendsList;

        List<User> allFriends = new ArrayList<>();
        for (Friends f : friendsList) {
            User u = listOfAllUsersMap.get(f.friendId);
            allFriends.add(u);
        }
        user.friendsList = allFriends;
        return allFriends;
    }
}

