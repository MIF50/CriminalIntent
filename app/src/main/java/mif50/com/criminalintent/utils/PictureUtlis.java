package mif50.com.criminalintent.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public final class PictureUtlis {

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        // read in the dimensions in the desk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        // figure out how much you scale down by
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            if (srcHeight > srcWidth)
                inSampleSize = Math.round(srcWidth / destWidth);
            else
                inSampleSize = Math.round(srcHeight / destHeight);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        // read in and create file bitmap
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }
}
