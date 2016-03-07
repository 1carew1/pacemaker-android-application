package org.pacemaker.controllers;

import org.pacemaker.R;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.main.PacemakerAPI;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Welcome extends Activity
{
  PacemakerApp app;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_welcome);
    app = (PacemakerApp) getApplication();
    app.connectToPacemakerAPI(this);
  }

  public void loginPressed (View view)
  {
    startActivity (new Intent(this, Login.class));
  }

  public void signupPressed (View view)
  {
    startActivity (new Intent(this, Signup.class));
  }
}
