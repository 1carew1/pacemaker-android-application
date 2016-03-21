package org.pacemaker.models;

/**
 * Created by colmcarew on 20/03/16.
 */
public class Location {
    public Long id;
    public float latitude;
    public float longitude;

    /**
     * Default Location Constructor
     */
    public Location() {
    }

    /**
     * Location constructor with parameters
     *
     * @param latitude
     * @param longitude
     */
    public Location(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "[" + latitude +
                "," + longitude +
                ']';
    }
}
