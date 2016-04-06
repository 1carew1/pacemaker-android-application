package org.pacemaker.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    public Long id;
    public String firstname;
    public String lastname;
    public String email;
    public String password;
    public String profilePhoto = "images/profilePhotos/default.jpg";
    public List<MyActivity> activities = new ArrayList<>();
    public List<User> friendsList = new ArrayList<>();
    public List<User> notFriendsList = new ArrayList<>();
    public List<User> pendingFriendsList = new ArrayList<>();
    public List<Friends> friendObjecList = new ArrayList<>();

    public User() {
    }

    public User(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (firstname != null ? !firstname.equals(user.firstname) : user.firstname != null)
            return false;
        if (lastname != null ? !lastname.equals(user.lastname) : user.lastname != null)
            return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        return !(password != null ? !password.equals(user.password) : user.password != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}