package org.pacemaker.models;

public class User 
{
  public Long   id;
  public String firstname;
  public String lastname;
  public String email;
  public String password;
 
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