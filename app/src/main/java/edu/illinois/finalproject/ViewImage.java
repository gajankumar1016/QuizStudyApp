package edu.illinois.finalproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by gajan on 12/12/2017.
 */

public class ViewImage {

    /**
     * Opens jpeg image at given URL in suitable photo gallery on user's device.
     * @param urlString url string for downloading jpeg image.
     */
    public static void viewImageInGallery(String urlString, Context context) {
        //Derived from https://stackoverflow.com/questions/5383797/open-an-image-using-uri-in-androids-default-gallery-image-viewer
        Intent viewImageInGalleryIntent = new Intent();
        viewImageInGalleryIntent.setAction(Intent.ACTION_VIEW);
        viewImageInGalleryIntent.setDataAndType(Uri.parse(urlString), "image/jpeg");
        context.startActivity(viewImageInGalleryIntent);
    }
}
