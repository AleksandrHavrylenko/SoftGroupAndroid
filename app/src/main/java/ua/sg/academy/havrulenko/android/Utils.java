package ua.sg.academy.havrulenko.android;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {

    public static String saveUserImage(Bitmap bitmap) {
        return saveToInternalStorage(bitmap, MyApplication.getImgPathUsers());
    }

    public static String savePlaceImage(Bitmap bitmap) {
        return saveToInternalStorage(bitmap, MyApplication.getImgPathPlaces());
    }

    private static String saveToInternalStorage(Bitmap bitmap, String folder) {
        File file = new File(folder, System.currentTimeMillis() + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("Utils", "saveToInternalStorage: " + file.getPath());
        return file.getName();
    }
}
