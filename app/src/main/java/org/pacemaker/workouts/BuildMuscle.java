package org.pacemaker.workouts;

import org.pacemaker.models.MyActivity;
import org.pacemaker.utils.ActivtyUtils;

import java.util.List;

/**
 * Created by colmcarew on 11/04/16.
 */
public class BuildMuscle implements PrescribeExercise {
    /**
     * Generatre a workout based on the user's previous activities
     *
     * @param userActivities
     * @return
     */
    @Override
    public String workout(List<MyActivity> userActivities) {
//        userActivities = ActivtyUtils.finishedActivities(userActivities);
        String suggestedWorkout = "Warmup Run + 45 minutes of weights";
        return suggestedWorkout;
    }
}
