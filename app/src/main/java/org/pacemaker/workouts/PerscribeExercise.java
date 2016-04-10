package org.pacemaker.workouts;

import org.pacemaker.models.MyActivity;

import java.util.List;

/**
 * Created by colmcarew on 10/04/16.
 */
public interface PerscribeExercise {
    String workout(List<MyActivity> userActivities);
}
