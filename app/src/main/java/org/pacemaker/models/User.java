package org.pacemaker.models;

import java.util.List;

public class User
{
  public Long   id;
  public String firstname;
  public String lastname;
  public String email;
  public String password;
  public List<MyActivity> activities = null;
 
  public User()
  {}
  
  public User(String firstname, String lastname, String email, String password)
  {
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.password = password;
  } 
}