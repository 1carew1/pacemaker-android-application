package org.pacemaker.pacemaker.utils;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Duration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pacemaker.models.MyActivity;
import org.pacemaker.utils.ActivtyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by colmcarew on 17/04/16.
 */
public class ActivityUtilsTest extends TestCase {

    List<MyActivity> activityList = new ArrayList<>();
    MyActivity activity1 = new MyActivity("type", "location", 10d, (new DateTime()).plusDays(7), "1:00");
    MyActivity activity2 = new MyActivity("type", "location", 10d, (new DateTime()).minusDays(7), "1:00");

    @Before
    public void setUp() throws Exception {
        activityList.add(activity1);
        activityList.add(activity2);
    }

    @After
    public void tearDown() throws Exception {
        activityList = null;
        activity1 = null;
        activity2 = null;
    }

    @Test
    public void testActivitySorting() {
        activityList = ActivtyUtils.sortActivitiesByDate(activityList);
        assertEquals(activity1, activityList.get(1));
        assertEquals(activity2, activityList.get(0));
    }

    @Test
    public void testFinishedActivityTest() {
        List<MyActivity> finishedActivities = ActivtyUtils.finishedActivities(activityList);
        assertTrue(finishedActivities.contains(activity2));
        assertTrue(!finishedActivities.contains(activity1));
    }

    @Test
    public void testStringToDateTime() {
        String dateTimeString = "Sun Mar 13 19:00:00 GMT+00:00 2016";
        DateTime dt = ActivtyUtils.stringToDatetime(dateTimeString);
        assertEquals(dt.getDayOfWeek(), 7);
        assertEquals(dt.getDayOfMonth(), 13);
        assertEquals(dt.get(DateTimeFieldType.monthOfYear()), 3);
    }

    @Test
    public void testActivityDuration() {
        String duration = "2:00";
        Duration d = ActivtyUtils.activityDuration(duration);
        assertEquals(d.getMillis(), 7200000);
    }

    @Test
    public void testActivitiesInLastXDays() {
        MyActivity activity3 = new MyActivity("type", "location", 10d, (new DateTime()).minusDays(2), "1:00");
        MyActivity activity4 = new MyActivity("type", "location", 10d, (new DateTime()).minusDays(9), "1:00");
        activityList.add(activity3);
        activityList.add(activity4);
        activityList = ActivtyUtils.activitiesInLastXDays(activityList, 8);
        assertTrue(activityList.contains(activity3));
        assertTrue(activityList.contains(activity2));
        assertTrue(!activityList.contains(activity1));
        assertTrue(!activityList.contains(activity4));
    }

    @Test
    public void testKmAndTimeAsStringFromActivities() {
        String response = ActivtyUtils.kmAndTimeAsStringFromActivities(activityList);
        assertTrue(!response.isEmpty());
    }

    @Test
    public void testSubtractOneIfGreaterThanZero() {
        int intTest = 12;
        intTest = ActivtyUtils.subtractOneIfGreaterThanZero(intTest);
        assertEquals(intTest, 11);
        intTest = 0;
        intTest = ActivtyUtils.subtractOneIfGreaterThanZero(intTest);
        assertEquals(intTest, 0);
    }

    @Test
    public void testRoundDoubleToTwoDecimalPlaces() {
        Double d = 12.5555;
        d = ActivtyUtils.roundDoubleToTwoDecimalPlaces(d);
        assertEquals("12.56", d + "");
    }
}
