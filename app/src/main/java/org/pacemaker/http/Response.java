package org.pacemaker.http;

import java.util.List;

public interface Response<T>
{
  public void setResponse(List<T> aList);
  public void setResponse(T anObject);
  public void errorOccurred (Exception e);
}