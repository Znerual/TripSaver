package laurenzsoft.com.waehrungsrechner;

/**
 * Created by Laurenz on 22.08.2016.
 */
public class DatabaseHolder {
    private static DatabaseHolder instance;
    private static Database gDb;
    public static DatabaseHolder getInstance() {
        if(instance == null) instance = getSync();
        return instance;
    }

    private static synchronized DatabaseHolder getSync() {
        if(instance == null) instance = new DatabaseHolder();
        return instance;
    }

    private DatabaseHolder(){
        // here you can directly access the Application context calling
        gDb = new Database(App.get());
    }
    public Database getDatabase() {
        return gDb;
    }
}
