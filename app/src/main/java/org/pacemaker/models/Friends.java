package org.pacemaker.models;

/**
 * Created by colmcarew on 20/03/16.
 */
public class Friends {
    public Long id;
    public Long userId;
    public Long friendId;
    public String accepted = "No";

    /**
     * default constructor
     */
    public Friends() {

    }

    /**
     * Constructor
     * @param userId
     * @param friendId
     */
    public Friends(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }


}
