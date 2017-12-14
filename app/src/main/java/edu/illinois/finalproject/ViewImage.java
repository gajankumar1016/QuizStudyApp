package edu.illinois.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Class with static method to allow users to click on an ImageButton and open the image in a gallery.
 */
public class ViewImage {

    /**
     * Opens jpeg image at given URL in suitable photo gallery on user's device.
     * @param uriString uri string for downloading/viewing jpeg image.
     */
    public static void viewImageInGallery(String uriString, Context context, PackageManager packageManager) {
        //Derived from https://stackoverflow.com/questions/5383797/open-an-image-using-uri-in-androids-default-gallery-image-viewer
        Intent viewImageInGalleryIntent = new Intent();
        viewImageInGalleryIntent.setAction(Intent.ACTION_VIEW);
        viewImageInGalleryIntent.setDataAndType(Uri.parse(uriString), "image/jpeg");
        if (viewImageInGalleryIntent.resolveActivity(packageManager) != null) {
            context.startActivity(viewImageInGalleryIntent);
        }
    }
}
