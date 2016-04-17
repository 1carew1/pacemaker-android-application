package org.pacemaker.models;

import static com.google.common.base.Objects.toStringHelper;

import com.google.common.base.Objects;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MyActivity {
    public Long id;
    public String kind;
    public String location;
    public double distance;
    public String startTime;
    public String duration;
    public List<Location> routes = new ArrayList<>();

    /**
     * Default Constructor
     */
    public MyActivity() {
    }

    /**
     * Constructor
     *
     * @param type
     * @param location
     * @param distance
     * @param duration
     */
    public MyActivity(String type, String location, double distance, String duration) {
        this.kind = type;
        this.location = location;
        this.distance = distance;
        this.duration = duration;
    }

    /**
     * Constructor
     *
     * @param type
     * @param location
     * @param distance
     * @param startTime
     * @param duration
     */
    public MyActivity(String type, String location, double distance, DateTime startTime, String duration) {
        this.kind = type;
        this.location = location;
        this.distance = distance;
        this.duration = duration;
        this.startTime = startTime.toDate().toString();
    }

    /**
     * To string method
     *
     * @return
     */
    @Override
    public String toString() {
        return toStringHelper(this).addValue(id)
                .addValue(kind)
                .addValue(location)
                .addValue(distance)
                .addValue(duration)
                .toString();
    }

    /**
     * Equals method
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyActivity activity = (MyActivity) o;

        if (Double.compare(activity.distance, distance) != 0) return false;
        if (id != null ? !id.equals(activity.id) : activity.id != null) return false;
        if (kind != null ? !kind.equals(activity.kind) : activity.kind != null) return false;
        if (location != null ? !location.equals(activity.location) : activity.location != null)
            return false;
        if (startTime != null ? !startTime.equals(activity.startTime) : activity.startTime != null)
            return false;
        return !(duration != null ? !duration.equals(activity.duration) : activity.duration != null);

    }

    /**
     * Hash code method
     *
     * @return
     */
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (kind != null ? kind.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        temp = Double.doubleToLongBits(distance);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        return result;
    }
}