package org.pacemaker.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

import org.pacemaker.controllers.Settings;
import org.pacemaker.http.Rest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by colmcarew on 27/03/16.
 */
public class ImageUtils {
    public static void setUserImage(ImageView imageView, String profilePhoto) {
        Ion.with(imageView).load(Rest.URL + "/getPicture/" + profilePhoto);
    }

    public static void uploadUserProfilePhoto(Context context, Bitmap bitmap, Long loggedInUserId) throws IOException {
        Log.i(Settings.TAG, "Image is " + bitmap.getWidth() + " width");
        File outputDir = context.getCacheDir();
        File file = File.createTempFile("jpg", "jpg", outputDir);
        Log.i(Settings.TAG, "Writing to temporary File : Size " + file.length());
        OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        os.close();
        Log.i(Settings.TAG, "File : Size " + file.length());
        Log.i(Settings.TAG, "POST to production");

        String url = Rest.URL + "/api/users/" + loggedInUserId + "/profilephoto";
        //TODO : Implement was of post image to production
    }
}
