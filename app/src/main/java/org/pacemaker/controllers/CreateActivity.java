package org.pacemaker.controllers;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import org.pacemaker.R;
import org.pacemaker.http.Response;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.MyActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import java.util.List;

public class CreateActivity extends android.app.Activity implements Response<MyActivity> {
  private PacemakerApp app;

  private Button createActivityButton;
  private TextView activityType;
  private TextView activityLocation;
  private NumberPicker distancePicker;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create);

    app = (PacemakerApp) getApplication();

    createActivityButton = (Button) findViewById(R.id.createActivityButton);
    activityType = (TextView) findViewById(R.id.activityType);
    activityLocation = (TextView) findViewById(R.id.activityLocation);
    distancePicker = (NumberPicker) findViewById(R.id.numberPicker);

    distancePicker.setMinValue(0);
    distancePicker.setMaxValue(20);
  }

  public void createActivityButtonPressed(View view) {
    double distance = distancePicker.getValue();
    MyActivity activity = new MyActivity(activityType.getText().toString(), activityLocation.getText().toString(), distance);

    app.createActivity(this, activity, this);
  }

  @Override
  public void setResponse(List<MyActivity> aList) {
  }

  @Override
  public void setResponse(MyActivity anObject) {
  }

  @Override
  public void errorOccurred(Exception e) {
    Toast toast = Toast.makeText(this, "Failed to create Activity", Toast.LENGTH_SHORT);
    toast.show();
  }

  public void listActivityButtonPressed (View view)
  {
    Log.v("Pacemaker", "List Activities Button Pressed");
    startActivity (new Intent(this, ActivitiesList.class));
  }
}
