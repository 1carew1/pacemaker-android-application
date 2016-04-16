package org.pacemaker.controllers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.pacemaker.R;
import org.pacemaker.main.PacemakerApp;
import org.pacemaker.models.User;
import org.pacemaker.utils.ImageUtils;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Settings extends AppCompatActivity {
    //Static variables
    public static final int GET_FROM_GALLERY = 3;
    public static final String TAG = "SettingsActivity";
    //application + logged in user
    private PacemakerApp app;
    private User loggedInUser;
    //views
    private ImageView userImage;
    private Button submit;
    //bitmap used for image
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        app = (PacemakerApp) getApplication();
        loggedInUser = app.getLoggedInUser();

        userImage = (ImageView) findViewById(R.id.uploadedImage);
        submit = (Button) findViewById(R.id.submitButton);
        //Make submit invisible until photo selected
        submit.setVisibility(View.INVISIBLE);
        //Set the photo to be user user photo
        ImageUtils.setUserImage(userImage, loggedInUser.profilePhoto);
    }

    /**
     * Method used to select local image
     * @param view
     */
    public void displayLocalImage(View view) {
        Log.i(TAG, "User picking photo");
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
    }

    /**
     * Method used to upload photo to production - currently not functional
     * @param view
     */
    public void submitPhoto(View view) {
        try {
            ImageUtils.uploadUserProfilePhoto(Settings.this, bitmap, loggedInUser.id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast submitToast = Toast.makeText(Settings.this, "We are sorry, this feature is currently in development and not yet completed", Toast.LENGTH_SHORT);
        submitToast.show();
    }


    /**
     * Method used for get users local image + dusplaying it on screen
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "About to get the photo and display on screen");
        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                //Set Photo to be the new photo
                userImage.setImageBitmap(bitmap);
                submit.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                Log.i(TAG, "File Not Found Exception " + e.getLocalizedMessage());
            } catch (IOException e) {
                Log.i(TAG, "IO Exception " + e.getLocalizedMessage());
            }
        }
    }

}
