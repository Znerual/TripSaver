package laurenzsoft.com.waehrungsrechner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Filter;

/**
 * Created by Laurenz on 18.08.2016.
 */
public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "LogDB";
    private  static final String TABLE_LOG = "Log";
    private static final String TABLE_CHANGEFACTORS = "Change";

    //COMMON Column names
    public static final String KEY_ID = "_id";
    public static final String KEY_CHANGEFACTOR = "changefactor";

    //LOG Column names
    public static final String KEY_LOGDATE = "logdate";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_AMOUNTCALCULATED = "amountcalculated";
    public static final String KEY_SHOW = "show";
    public static final String KEY_OUTCOME = "outcome";
    public static final String KEY_COMMENT = "comment";

    //Changefactor column names
    public static final String KEY_NAME="name";

    private static final String[] LOG_COLUMNS = {KEY_ID, KEY_LOGDATE, KEY_AMOUNT, KEY_AMOUNTCALCULATED, KEY_CHANGEFACTOR, KEY_SHOW, KEY_OUTCOME, KEY_COMMENT};
    private static final String[] CHANGEFACTOR_COLUMNS = {KEY_ID, KEY_NAME, KEY_CHANGEFACTOR};
    private static final String[] BILANZ_COLUMNS = {KEY_ID, KEY_LOGDATE, KEY_AMOUNT, KEY_OUTCOME};

    private static final String CREATE_LOG_TABLE = "CREATE TABLE " +TABLE_LOG +" ( " + KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ KEY_LOGDATE + " INTEGER, " + KEY_AMOUNT + " REAL, " + KEY_AMOUNTCALCULATED + " REAL, " + KEY_CHANGEFACTOR + " REAL, " + KEY_SHOW + " INTEGER, " + KEY_OUTCOME+ " INTEGER, " + KEY_COMMENT + " TEXT )";
    private static final String CREATE_CHANGEFACTOR_TABLE = "CREATE TABLE " + TABLE_CHANGEFACTORS + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " TEXT, " + KEY_CHANGEFACTOR + " REAL )";

    public  Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_LOG_TABLE);
        sqLiteDatabase.execSQL(CREATE_CHANGEFACTOR_TABLE);
        ChangeFactor cf = new ChangeFactor();
        cf.setName("EUR-USD");
        cf.setChangeFactor(1.12);
        ContentValues values = createValues(cf);
        sqLiteDatabase.insert(TABLE_CHANGEFACTORS, null, values);
        cf.setName("EUR-VND");
        cf.setChangeFactor(25000);
        values = createValues(cf);
        sqLiteDatabase.insert(TABLE_CHANGEFACTORS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LOG );
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " +  TABLE_CHANGEFACTORS );
        this.onCreate(sqLiteDatabase);
    }
    public void addEntry(LogEntry entry) {
        Log.d("addEntry", entry.toString());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = createValues(entry);
        db.insert(TABLE_LOG, null, values);
        db.close();
    }
    public void addEntry(ChangeFactor entry) {
        Log.d("addEntry", entry.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = createValues(entry);
        db.insert(TABLE_CHANGEFACTORS, null, values);
        db.close();
    }
    public LogEntry getEntryById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOG, LOG_COLUMNS,KEY_ID + " = ?", new String[] {String.valueOf(id)},null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        LogEntry entry = null;
        entry = createEntryFromCursor(cursor);

        Log.d("getEntry("+id+")", entry.toString());
        return entry;

    }
    public ChangeFactor getChangeFactorById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHANGEFACTORS, CHANGEFACTOR_COLUMNS,KEY_ID + " = ?", new String[] {String.valueOf(id)},null,null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        ChangeFactor entry = createChangeFactorFromCursor(cursor);
        Log.d("getEntry("+id+")", entry.toString());
        return entry;
    }
    public List<LogEntry> getEntries(FilterQuery filter) {
        List<LogEntry> entries = new LinkedList<LogEntry>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        cursor = db.query(TABLE_LOG, LOG_COLUMNS, filter.m_query, filter.m_arguments,null, null, null);
        LogEntry entry = null;
        if (cursor.moveToFirst()) {
            do {
                entry = createEntryFromCursor(cursor);
                entries.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d("getEntries",entries.toString() +" for the Query " + filter.m_query);
        return  entries;
    }
    public List<BilanzEntry> getBilanzEntries(String query, String[] arguments) {
        List<BilanzEntry> entries = new LinkedList<BilanzEntry>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        //brauche nicht alle Columns
        cursor = db.query(TABLE_LOG, BILANZ_COLUMNS, query, arguments,null, null, null);
        BilanzEntry entry = null;
        if (cursor.moveToFirst()) {
            do {
                entry = createBilanzEntryFromCursor(cursor);
                entries.add(entry);
            } while (cursor.moveToNext());
        }
        Log.d("getBilanzEntries",entries.toString() +" for the Query " + query);
       cursor.close();
        db.close();
        return  entries;
    }
    @Deprecated
    public List<LogEntry> getAllEntries(boolean onlyVisibleEntries) {
        List<LogEntry> entries = new LinkedList<LogEntry>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (onlyVisibleEntries) {
            cursor = db.query(TABLE_LOG, LOG_COLUMNS, "show = 1", null,null, null, null);
        } else {
            cursor = db.query(TABLE_LOG, LOG_COLUMNS, null, null,null, null, null);
        }

        LogEntry entry = null;
        if (cursor.moveToFirst()) {
            do {
               entry = createEntryFromCursor(cursor);
                entries.add(entry);
            } while (cursor.moveToNext());
        }
        Log.d("getAllEntries",entries.toString());
        cursor.close();
        db.close();
        return  entries;
    }
    public List<ChangeFactor> getAllChangefactors() {
        List<ChangeFactor> changeFactorList = new ArrayList<ChangeFactor>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CHANGEFACTORS, CHANGEFACTOR_COLUMNS,null,null,null,null,null);
        ChangeFactor entry = null;
        if (cursor.moveToFirst()) {
            do {
                entry = createChangeFactorFromCursor(cursor);
                changeFactorList.add(entry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return changeFactorList;
    }
    public Cursor getCursorForAllEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        return  db.query(TABLE_LOG, LOG_COLUMNS, null, null,null, null, null);
    }
    public Cursor getCursorForComments(String start) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_LOG, new String[] {KEY_ID, KEY_COMMENT}, KEY_COMMENT +" <> '' AND " + KEY_COMMENT + " LIKE ?"  , new String[] {"%" + start + "%"}, null, null, null );
    }
    public  int update(LogEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = createValues(entry);
        int i = db.update(TABLE_LOG, values, KEY_ID +" = ?", new String[] {String.valueOf(entry.getId())});
        db.close();
        Log.d("update", entry.toString());
        return i;
    }
    public int update(ChangeFactor entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = createValues(entry);
        int i = db.update(TABLE_CHANGEFACTORS, values, KEY_ID +" = ?", new String[] {String.valueOf(entry.getId())});
        db.close();
        Log.d("update", entry.toString());
        return i;
    }
    public  void deleteEntry(LogEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_LOG, KEY_ID+" = ?", new String[]{ String.valueOf(entry.getId())});
        db.close();
        Log.d("delete", entry.toString());
    }
    public  void deleteEntry(ChangeFactor entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHANGEFACTORS, KEY_ID+" = ?", new String[]{ String.valueOf(entry.getId())});
        db.close();
        Log.d("delete", entry.toString());
    }
    public LogEntry createEntryFromCursor(Cursor cursor) {
        LogEntry entry = new LogEntry();
        entry.setId(Integer.parseInt(cursor.getString(0)));
        entry.setDate(Long.parseLong(cursor.getString(1)));
        entry.setAmount(Double.parseDouble(cursor.getString(2)));
        entry.setAmountCalculated(Double.parseDouble(cursor.getString(3)));
        entry.setChangeFactor(Double.parseDouble(cursor.getString(4)));
        entry.setShow(Integer.parseInt(cursor.getString(5)));
        entry.setOutcome(Integer.parseInt(cursor.getString(6)));
        entry.setComment(cursor.getString(7));
        return  entry;
    }
    public ChangeFactor createChangeFactorFromCursor(Cursor cursor) {
        ChangeFactor entry = new ChangeFactor();
        entry.setId(Integer.parseInt(cursor.getString(0)));
        entry.setName(cursor.getString(1));
        entry.setChangeFactor(Double.parseDouble(cursor.getString(2)));
        return entry;
    }
    public BilanzEntry createBilanzEntryFromCursor(Cursor cursor) {
        BilanzEntry entry = new BilanzEntry();
        entry.setId(Integer.parseInt(cursor.getString(0)));
        entry.setDate(Long.parseLong(cursor.getString(1)));
        entry.setAmount(Double.parseDouble(cursor.getString(2)));
        entry.setOutcome(Integer.parseInt(cursor.getString(3)));
        return entry;
    }
    private ContentValues createValues(LogEntry entry) {
        ContentValues values = new ContentValues();
        values.put(KEY_LOGDATE, entry.getDate());
        values.put(KEY_AMOUNT, entry.getAmount());
        values.put(KEY_AMOUNTCALCULATED, entry.getAmountCalculated());
        values.put(KEY_CHANGEFACTOR, entry.getChangeFactor());
        values.put(KEY_SHOW, entry.getShow());
        values.put(KEY_OUTCOME, entry.getOutcome());
        values.put(KEY_COMMENT, entry.getComment());
        return  values;
    }
    private ContentValues createValues(ChangeFactor entry) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, entry.getName());
        values.put(KEY_CHANGEFACTOR, entry.getChangeFactor());
        return values;
    }
}
