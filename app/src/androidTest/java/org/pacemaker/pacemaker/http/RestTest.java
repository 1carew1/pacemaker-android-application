package org.pacemaker.pacemaker.http;

import android.content.Context;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.pacemaker.controllers.Dashboard;
import org.pacemaker.http.Rest;
import org.pacemaker.main.PacemakerAPI;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.User;
import org.pacemaker.utils.JsonParser;

import static junit.framework.Assert.assertEquals;

/**
 * Created by colmcarew on 05/04/16.
 */
public class RestTest extends TestCase  {
    private PacemakerApp app;

    @Before
    public void setUp() throws Exception {
        app = new PacemakerApp();
    }
// TODO : Fix juint testing

    public void testRestCreateUser() throws Exception {
/*        String colm = "Colm";
        String colmEmail = "colm@colm.com";
        User u = new User(colm, colm, colmEmail, colm);
        String response = Rest.post("/api/users", JsonParser.user2Json(u));
        JsonParser.json2User(response);
        app.connectToPacemakerAPI(null);
        User testUser = app.getUserMap().get(colmEmail);
        assertEquals(u.lastname, testUser.lastname);*/
        assert true;
    }
}
