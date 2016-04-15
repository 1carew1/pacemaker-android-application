package org.pacemaker.workouts;

import org.joda.time.Duration;
import org.pacemaker.models.MyActivity;
import org.pacemaker.utils.*;

import java.util.List;

/**
 * Created by colmcarew on 10/04/16.
 */
public class LoseFat implements PerscribeExercise {
    @Override
    public String workout(List<MyActivity> userActivities) {
        String suggestedWorkout = "Light Cardio";
        if (userActivities == null || userActivities.isEmpty()) {
            suggestedWorkout = "Start walking/jogging for 1 hour 3-4 times a week";
        } else {
            Long totalDurationMilliSeconds = 0l;
            Double totalDistance = 0d;
            for (MyActivity userActivity : userActivities) {
                totalDurationMilliSeconds += (ActivtyUtils.activityDuration(userActivity.duration).getMillis());
                totalDistance += userActivity.distance;
            }
            Long totalDurationHours = totalDurationMilliSeconds / (1000 * 60 * 60);
            Double avgKmPerHour = totalDistance / totalDurationHours;
            if (avgKmPerHour >= 10 /* This is just the avg km/hr user has done overall - 10 picked arbitrarily */) {
                suggestedWorkout = "Try some weights and strength building exercises";
            } else {
                suggestedWorkout = "Increase running/walking speed by 0.5-1 km/hr and excersie for same amount of time";
            }
        }
        return suggestedWorkout;
    }
}
