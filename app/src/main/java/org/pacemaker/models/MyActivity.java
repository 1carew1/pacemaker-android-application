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

    public MyActivity() {
    }

    public MyActivity(String type, String location, double distance, String duration) {
        this.kind = type;
        this.location = location;
        this.distance = distance;
        this.duration = duration;
    }

    public MyActivity(String type, String location, double distance, DateTime startTime, String duration) {
        this.kind = type;
        this.location = location;
        this.distance = distance;
        this.duration = duration;
        this.startTime = startTime.toDate().toString();
    }

    @Override
    public String toString() {
        return toStringHelper(this).addValue(id)
                .addValue(kind)
                .addValue(location)
                .addValue(distance)
                .addValue(duration)
                .toString();
    }

    // 2 activities are equal if their time is equal
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof MyActivity) {
            final MyActivity other = (MyActivity) obj;
            return Objects.equal(startTime, other.startTime)
                    ;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id, this.kind, this.location, this.distance, this.duration);
    }
}