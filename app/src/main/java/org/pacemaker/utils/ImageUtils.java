package org.pacemaker.utils;

import android.content.Context;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

import org.pacemaker.http.Rest;

/**
 * Created by colmcarew on 27/03/16.
 */
public class ImageUtils {
    public static void setUserImage(ImageView imageView, String profilePhoto) {
        Ion.with(imageView).load(Rest.URL + "/getPicture/" + profilePhoto);
    }
}
