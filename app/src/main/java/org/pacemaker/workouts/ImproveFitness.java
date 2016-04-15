package org.pacemaker.workouts;

import org.pacemaker.models.MyActivity;

import java.util.List;

/**
 * Created by colmcarew on 15/04/16.
 */
public class ImproveFitness implements PerscribeExercise {
    @Override
    public String workout(List<MyActivity> userActivities) {
        String suggestedWorkout = "Run 3 times a week for an hour and begin light weights";
        List<MyActivity> activitiesInLastWeek = org.pacemaker.utils.ActivtyUtils.activitiesInLastXDays(userActivities, 7);
        if (activitiesInLastWeek != null && !activitiesInLastWeek.isEmpty() && activitiesInLastWeek.size() >= 2) {
            suggestedWorkout = "Doing well with current workout for improving fitness - ensure to exercise at least 3 times a week";
        }
        return suggestedWorkout;
    }
}