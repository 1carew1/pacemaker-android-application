package org.pacemaker.utils;

import android.content.Context;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.pacemaker.models.MyActivity;

/**
 * Created by colmcarew on 27/03/16.
 */
public class ActivtyUtils {
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
}
