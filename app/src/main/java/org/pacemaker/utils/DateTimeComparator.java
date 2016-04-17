package org.pacemaker.utils;


import org.pacemaker.models.MyActivity;

import java.util.Comparator;

/**
 * Created by Colm Carew on 11/10/15.
 */

/**
 * Comparator used to sort DateTimes in ascending order
 */
public class DateTimeComparator implements Comparator<MyActivity> {

    @Override
    public int compare(MyActivity activity1, MyActivity activity2) {
        int result = 0;
        if (ActivtyUtils.stringToDatetime(activity1.startTime).isBefore(ActivtyUtils.stringToDatetime(activity2.startTime))) {
            result = -1;
        } else if (ActivtyUtils.stringToDatetime(activity1.startTime).isAfter(ActivtyUtils.stringToDatetime(activity2.startTime))) {
            result = 1;
        }
        return result;
    }
}
