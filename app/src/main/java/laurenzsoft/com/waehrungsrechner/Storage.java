package laurenzsoft.com.waehrungsrechner;

import android.Manifest;
import android.app.Activity;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.io.IOException;

/**
 * Created by Laurenz on 24.09.2016.
 */

public class Storage {
    public static File getExternalStorageFile(String fileName) {
        if (isExternalStorageWritable()) {

            File ext = Environment.getExternalStorageDirectory();
            File fullPath = new File(ext,"/TripSaver/");
            if (!fullPath.exists()) {
                    fullPath.mkdirs();
            }
            File file = new File(fullPath, fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return file;
        } else {
           return new File(App.get().getFilesDir(),fileName);
        }
    }
    public static File getInternalStorageFile(String fileName) {
        return new File(App.get().getFilesDir(), fileName);
    }
    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
