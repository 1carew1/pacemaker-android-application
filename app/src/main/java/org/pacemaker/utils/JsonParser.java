package org.pacemaker.utils;

import org.pacemaker.models.Friends;
import org.pacemaker.models.Location;
import org.pacemaker.models.MyActivity;
import org.pacemaker.models.User;

import java.util.ArrayList;
import java.util.List;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class JsonParser {
    public static JSONSerializer userSerializer = new JSONSerializer().exclude("class")
            .exclude("persistent")
            .exclude("entityId");
    public static JSONSerializer activitySerializer = new JSONSerializer().exclude("class")
            .exclude("persistent")
            .exclude("entityId");

    public static User json2User(String json) {
        return new JSONDeserializer<User>().deserialize(json, User.class);
    }

    public static List<User> json2Users(String json) {
        return new JSONDeserializer<ArrayList<User>>().use("values", User.class)
                .deserialize(json);
    }

    public static String user2Json(Object obj) {
        return userSerializer.serialize(obj);
    }

    public static MyActivity json2Activity(String json) {
        MyActivity activity = new JSONDeserializer<MyActivity>().deserialize(json, MyActivity.class);
        return activity;
    }

    public static String activity2Json(Object obj) {
        return activitySerializer.serialize(obj);
    }

    public static List<MyActivity> json2Activities(String json) {
        return new JSONDeserializer<ArrayList<MyActivity>>().use("values", MyActivity.class).deserialize(json);
    }

    public static List<Location> json2Locations(String json) {
        return new JSONDeserializer<ArrayList<Location>>().use("values", Location.class).deserialize(json);
    }

    public static List<Friends> json2Friends(String json) {
        return new JSONDeserializer<ArrayList<Friends>>().use("values", Friends.class).deserialize(json);
    }

}