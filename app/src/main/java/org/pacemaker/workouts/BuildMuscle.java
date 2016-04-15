package org.pacemaker.workouts;

import org.pacemaker.models.MyActivity;

import java.util.List;

/**
 * Created by colmcarew on 11/04/16.
 */
public class BuildMuscle implements PerscribeExercise {
    @Override
    public String workout(List<MyActivity> userActivities) {
        //TODO : Improve this to be more based upon the users pervious activities
        String suggestedWorkout = "Warmup Run + 45 minutes of weights";
        return suggestedWorkout;
    }
}
