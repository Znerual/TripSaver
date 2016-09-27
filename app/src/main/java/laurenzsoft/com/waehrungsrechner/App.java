package laurenzsoft.com.waehrungsrechner;

import android.app.Application;

/**
 * Created by Laurenz on 22.08.2016.
 */
public class App extends Application {
    private static App instance;
    public static App get() { return instance; }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
