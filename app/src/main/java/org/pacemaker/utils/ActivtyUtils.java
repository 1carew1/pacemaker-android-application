package org.pacemaker.utils;

import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.pacemaker.models.MyActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by colmcarew on 27/03/16.
 */
public class ActivtyUtils {
    private static final String TAG = "ActivityUtils";

    /**
     * Purpose of this method is to create an activity or update a current activity
     *
     * @param context
     * @param selectedActivity
     * @param distance
     * @param type
     * @param location
     * @param durationHour
     * @param durationMinute
     * @param datePicker
     * @param activityTimeHour
     * @param activityTimeMinute
     * @return
     */
    public static MyActivity changeActivity(Context context, MyActivity selectedActivity,
                                            int distance, String type, String location,
                                            int durationHour, int durationMinute,
                                            DatePicker datePicker, int activityTimeHour, int activityTimeMinute) {
        if (type.isEmpty() || location.isEmpty()) {
            Toast errorToast = Toast.makeText(context, "Please Make sure everything is filled in correctly", Toast.LENGTH_SHORT);
            errorToast.show();
            //If nothing is filled in correctly, make the kind of the activity incorrectchange
            selectedActivity.kind = PacemakerENUMs.INCORRECTCHANGE.toString();
        } else {
            String duration = subtractOneIfGreaterThanZero(durationHour) + ":" + subtractOneIfGreaterThanZero(durationMinute);
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth() + 1; //Month starts from 0
            int year = datePicker.getYear();
            int hour = subtractOneIfGreaterThanZero(activityTimeHour);
            int minutes = subtractOneIfGreaterThanZero(activityTimeMinute);
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

    /**
     * The purpose of this method is to take in a string in the correct format
     * and output the DateTime
     *
     * @param dateString
     * @return
     */
    public static DateTime stringToDatetime(String dateString) {
        //Temporary Fix to parse dates
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

    /**
     * This method takes in a list of activities and returns a list of all those activities before now
     *
     * @param allActivities
     * @return
     */
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

    /**
     * Sort Activities by the date ascending - take in an Activity List and return an
     * Activity list
     *
     * @param allActivities
     * @return
     */
    public static List<MyActivity> sortActivitiesByDate(List<MyActivity> allActivities) {
        Collections.sort(allActivities, new DateTimeComparator());
        return allActivities;
    }

    /**
     * The purpose of this method is to input a string in a HH:MM format and return
     * a JODA Time Duration
     *
     * @param durationString
     * @return
     */
    public static Duration activityDuration(String durationString) {
        PeriodFormatter hoursMinutes = new PeriodFormatterBuilder().appendHours()
                .appendSeparator(":")
                .appendMinutes()
                .toFormatter();
        Period p = hoursMinutes.parsePeriod(durationString);
        Duration timeDuration = p.toStandardDuration();
        return timeDuration;
    }

    /**
     * The purpose of this is go get a list of Activities completed within X Number of days
     *
     * @param allActivities
     * @param numberOfDays
     * @return
     */
    public static List<MyActivity> activitiesInLastXDays(List<MyActivity> allActivities, int numberOfDays) {
        DateTime now = new DateTime();
        DateTime oneWeekAgo = new DateTime().minusDays(numberOfDays);
        Log.i(TAG, "The current time is : " + now.toDate().toString());
        Log.i(TAG, "One week ago : " + oneWeekAgo.toDate().toString());
        List<MyActivity> finishedActivities = new ArrayList<>();
        if (allActivities != null && !allActivities.isEmpty()) {
            for (MyActivity activity : allActivities) {
                DateTime dt = stringToDatetime(activity.startTime);
                if (dt.isBefore(now) && dt.isAfter(oneWeekAgo)) {
                    Log.i(TAG, "Activty on : " + dt.toDate().toString() + " to " + activity.kind + " has been added");
                    finishedActivities.add(activity);
                }
            }
            finishedActivities = sortActivitiesByDate(finishedActivities);
        }

        return finishedActivities;
    }

    /**
     * The purpose of this method is to give a performance evaluation based on the inputted activities
     *
     * @param userActivities
     * @return
     */
    public static String kmAndTimeAsStringFromActivities(List<MyActivity> userActivities) {
        String kmAndTime = "No activities have been completed";

        if (userActivities != null && !userActivities.isEmpty() && userActivities.size() > 0) {
            Log.i(TAG, "Going through " + userActivities.size() + " activities in ActivityUtils");
            Double totalDurationMilliSeconds = 0d;
            Double totalDistance = 0d;
            for (MyActivity userActivity : userActivities) {
                totalDurationMilliSeconds += (ActivtyUtils.activityDuration(userActivity.duration).getMillis());
                totalDistance += userActivity.distance;
            }
            double totalDurationHours = totalDurationMilliSeconds / (1000 * 60 * 60);
            double avgKmPerHour = totalDistance / totalDurationHours;
            totalDistance = roundDoubleToTwoDecimalPlaces(totalDistance);
            totalDurationHours = roundDoubleToTwoDecimalPlaces(totalDurationHours);
            avgKmPerHour = roundDoubleToTwoDecimalPlaces(avgKmPerHour);
            kmAndTime = totalDistance + "km has been traversed in " + totalDurationHours +
                    " hours giving an average of " + avgKmPerHour + "km/hr";
        }
        return kmAndTime;
    }

    /**
     * This method obtains the perfoamnce evaluation string and set the text of the TextView
     *
     * @param lastWeekOfWorkoutsResults
     * @param listOfActivities
     * @return
     */
    public static String userProgressInLast7Days(TextView lastWeekOfWorkoutsResults, List<MyActivity> listOfActivities) {
        String userProgress = kmAndTimeAsStringFromActivities(ActivtyUtils.activitiesInLastXDays(listOfActivities, 7)) + " in the last week";
        lastWeekOfWorkoutsResults.setText(userProgress);
        return userProgress;
    }

    /**
     * Sets the text of the textview to be the users evaluation
     *
     * @param lastMonthOfWorkoutsResults
     * @param listOfActivities
     * @return
     */
    public static String userProgressInLastMonth(TextView lastMonthOfWorkoutsResults, List<MyActivity> listOfActivities) {
        String userProgress = kmAndTimeAsStringFromActivities(ActivtyUtils.activitiesInLastXDays(listOfActivities, 31)) + " in the last month";
        lastMonthOfWorkoutsResults.setText(userProgress);
        return userProgress;
    }

    /**
     * Sets the text of the textview to be the users evaluation
     *
     * @param lastWeekOfWorkoutsResults
     * @param allActivities
     * @return
     */
    public static String userProgressOverall(TextView lastWeekOfWorkoutsResults, List<MyActivity> allActivities) {
        String userProgress = kmAndTimeAsStringFromActivities(finishedActivities(allActivities)) + " in this app's history";
        lastWeekOfWorkoutsResults.setText(userProgress);
        return userProgress;
    }

    /**
     * The reason for this method is that the NumberPickers in the XML due to being resized
     * do not show the correct selected number so if they are edited
     * 1 need to be subtracted from each number edited to give the correct time
     *
     * @param number
     * @return
     */
    public static int subtractOneIfGreaterThanZero(int number) {
        int numberToReturn = 0;
        if (number - 1 > 0) {
            numberToReturn = number - 1;
        }

        return numberToReturn;
    }

    /**
     * A method used for rounding a double to 2 decimal places
     *
     * @param d
     * @return
     */
    public static double roundDoubleToTwoDecimalPlaces(double d) {
        DecimalFormat toDecimalFormTwoPlaces = new DecimalFormat("#.##");
        return Double.valueOf(toDecimalFormTwoPlaces.format(d));
    }
}
