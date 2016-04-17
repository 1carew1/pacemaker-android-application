package org.pacemaker.workouts;

import org.pacemaker.models.MyActivity;

import java.util.List;

/**
 * Created by colmcarew on 10/04/16.
 */

/**
 * Interface used for the strategy pattern which is used for generating workouts
 */
public interface PrescribeExercise {
    String workout(List<MyActivity> userActivities);
}
