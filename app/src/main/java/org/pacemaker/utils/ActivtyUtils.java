package org.pacemaker.utils;

import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.pacemaker.models.MyActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by colmcarew on 27/03/16.
 */
public class ActivtyUtils {
    private static final String TAG = "ActivityUtils";

    public static MyActivity changeActivity(Context context, MyActivity selectedActivity,
                                            int distance, String type, String location,
                                            int durationHour, int durationMinute,
                                            DatePicker datePicker, int activityTimeHour, int activityTimeMinute) {
        String duration = durationHour + ":" + durationMinute;

        if (type.isEmpty() || location.isEmpty()) {
            Toast errorToast = Toast.makeText(context, "Please Make sure everything is filled in correctly", Toast.LENGTH_SHORT);
            errorToast.show();
            selectedActivity.kind = "IncorrectChange";
        } else {
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1; //Month starts from 0
            int year = datePicker.getYear();
            int hour = activityTimeHour;
            int minutes = activityTimeMinute;
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
            String dateTime = year + "-" + month + "-" + day + " " + hour + ":" + minutes;
            DateTime activietyDateTime = formatter.parseDateTime(dateTime);

            selectedActivity.startTime = activietyDateTime.toDate().toString();
            selectedActivity.kind = type;
            selectedActivity.distance = distance;
            selectedActivity.duration = duration;
            selectedActivity.location = location;
        }
        return selectedActivity;
    }

    public static DateTime stringToDatetime(String dateString) {
        //Temporary Fix to parse dates
        // TODO : Implement better method of date parsing
        //TODO : Fix this for time parsing + Date parsing
        if (dateString.contains("+")) {
            dateString = dateString.replaceAll("\\+\\d{2}\\:\\d{2}", "");
        }
        if (dateString.contains("IST")) {
            dateString = dateString.replaceAll("IST", "GMT");
        }
        String dateFormatter = "EEE MMM dd HH:mm:ss z yyyy";


        DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormatter);
        DateTime dt = formatter.parseDateTime(dateString);
        return dt;
    }

    public static List<MyActivity> finishedActivities(List<MyActivity> allActivities) {
        DateTime now = new DateTime();
        Log.i(TAG, "The current time is : " + now.toDate().toString());
        List<MyActivity> finishedActivities = new ArrayList<>();
        for (MyActivity activity : allActivities) {
            DateTime dt = stringToDatetime(activity.startTime);
            if (dt.isBefore(now)) {
                Log.i(TAG, "Activty on : " + dt.toDate().toString() + " to " + activity.kind + " has been added");
                finishedActivities.add(activity);
            }
        }
        finishedActivities = sortActivitiesByDate(finishedActivities);
        return finishedActivities;
    }

    public static List<MyActivity> sortActivitiesByDate(List<MyActivity> allActivities) {
        Collections.sort(allActivities, new DateTimeComparator());
        return allActivities;
    }

    public static Duration activityDuration(String duration) {
        PeriodFormatter hoursMinutesSeconds = new PeriodFormatterBuilder().appendHours()
                .appendSeparator(":")
                .appendMinutes()
                .appendSeparator(":")
                .appendSeconds()
                .toFormatter();
        Period p = hoursMinutesSeconds.parsePeriod(duration);
        Duration timeDuration = p.toStandardDuration();
        return timeDuration;
    }
}
