package org.pacemaker.http;

import java.util.List;

/**
 * This is the interface used via the activities for handling the fetching of data via REST
 *
 * @param <T>
 */
public interface Response<T> {
    public void setResponse(List<T> aList);

    public void setResponse(T anObject);

    public void errorOccurred(Exception e);
}