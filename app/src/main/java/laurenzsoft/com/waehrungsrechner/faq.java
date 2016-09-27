package laurenzsoft.com.waehrungsrechner;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Laurenz on 31.08.2016.
 */
public class faq {
    public static void show(Context context) {
        File path = Storage.getInternalStorageFile("faq.html");
        try {
            OutputStream output = new FileOutputStream(path);
            InputStream input = App.get().getResources().openRawResource(R.raw.faq);
            int read = 0;
            while ((read = input.read()) != -1) {
                output.write(read);
            }
            output.flush();
            output.close();
            input.close();
            Intent intent = new Intent(context,Browser.class);
            intent.putExtra("url",path.toString());
            context.startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
