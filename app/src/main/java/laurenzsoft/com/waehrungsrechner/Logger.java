package laurenzsoft.com.waehrungsrechner;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Laurenz on 18.08.2016.
 */
public class Logger {
    private  final String fileName = "protocol.html";
    private  final String LogFilterFileName = "logfilter.job";
    private Database m_db = null;
    private Context m_context;
    private LogFilter m_logFilter;
    public  Logger(Context context) {
        m_context = context;
        m_logFilter = new LogFilter();
        m_db = DatabaseHolder.getInstance().getDatabase();
        loadLogFilter();
    }
    public void writeLog(EntryData data) {
        if (data.m_currency.getAmount() == 0 || data.m_currency.getChangeFactor() == 0) {
            return;
        }
        LogEntry entry = new LogEntry();
        entry.setDate(new Date().getTime());
        if (data.m_currency.getCalculation() == Currency.Calculation.CALCULATE) {
            entry.setAmount(data.m_currency.getAmount());
            entry.setAmountCalculated(data.m_currency.getAmountCalculated());
        } else if (data.m_currency.getCalculation() == Currency.Calculation.CALCULATE_BACK) {
            entry.setAmount(data.m_currency.getAmountCalculated());
            entry.setAmountCalculated(data.m_currency.getAmount());
        } else {
            entry.setAmount(data.m_currency.getAmount());
            entry.setAmountCalculated(data.m_currency.getAmount());
        }

        entry.setChangeFactor(data.m_currency.getChangeFactor());
        entry.setComment(data.m_comment);
        entry.setOutcome(data.m_lastschrift ? 1:0);
        m_db.addEntry(entry);
    }
    public void writeComment(LogEntry entry, String comment) {
        entry.setComment(comment);
        m_db.update(entry);
    }
    public void hideLogEntry(LogEntry entry) {
        entry.setShow(0);
        m_db.update(entry);
    }
    public void showLogEntry(LogEntry entry) {
        entry.setShow(1);
        m_db.update(entry);
    }
    public void markAsIncome(LogEntry entry) {
        entry.setOutcome(0);
        m_db.update(entry);
    }
    public  void markAsOutcome(LogEntry entry) {
        entry.setOutcome(1);
        m_db.update(entry);
    }
    public void printLog() {

        List<LogEntry> entries = m_logFilter.getEntries(m_db);
        File path = Storage.getExternalStorageFile(fileName);

        Log.d("printLog",path.toString());

        try {
            writeHtml(path, entries);
            openLog(path);
            Log.d("openLog",path.toString());
        } catch (IOException e) {
            e.printStackTrace();
            assert true;
        }


    }
    public  void exportLog() {
        List<LogEntry> entries = m_logFilter.getEntries(m_db);
        File path = Storage.getExternalStorageFile(fileName);
        Log.d("exportLog",path.toString());
        Toast toast =Toast.makeText(m_context,String.format(m_context.getResources().getString(R.string.speicherpfad), path.toString()), Toast.LENGTH_LONG);
        toast.show();
        try {
            writeHtml(path, entries);
        } catch (IOException e) {
            e.printStackTrace();
            assert true;
        }
    }
    public void deleteLogEntry(LogEntry entry) {
        m_db.deleteEntry(entry);
    }
    private  void openLog(File path) {
        Intent intent = new Intent(m_context,Browser.class);
        intent.putExtra("url",path.toString());
        m_context.startActivity(intent);
    }
    private void writeHtml(File path, List<LogEntry> entries) throws FileNotFoundException, IOException {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Resources res = m_context.getResources();
        PrintWriter fileWriter = new PrintWriter(new FileWriter(path));
        fileWriter.print(HTML.getHeader());

        String oldDate = "";
        for (int i =0; i< entries.size();i++) {
            Date currentDateObject = new Date(entries.get(i).getDate());
            String currentDate = dateFormat.format(currentDateObject);
            if (!oldDate.contentEquals(currentDate)) {
                fileWriter.print("</p><h1>"+dateFormat.format(currentDateObject) + "</h1>");
                oldDate = currentDate;
            }
            String zeile = m_logFilter.getShowTime() ? "<div class=\"time\">" + timeFormat.format(currentDateObject) +"</div>": "";
            fileWriter.print(zeile + entries.get(i).toHTML(m_logFilter, res));

        }
        fileWriter.print("</body></html>");
        fileWriter.flush();
        fileWriter.close();
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    public LogFilter getLogFilter() {
        return m_logFilter;
    }
    public  void setLogFilter(LogFilter logFilter) {
        m_logFilter = logFilter;
    }
    public void saveLogFilter() {
        FileOutputStream fos = null;
        try {
            fos = m_context.openFileOutput(LogFilterFileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(m_logFilter);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void loadLogFilter() {
        FileInputStream fis = null;
        try {
            fis = m_context.openFileInput(LogFilterFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            m_logFilter =(LogFilter) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public Database getDatabase() {
        return  m_db;
    }
 }
