package laurenzsoft.com.waehrungsrechner;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Laurenz on 24.08.2016.
 */
public class Bilanzrechner {
    private  final String fileName = "bilanz.html";
    private  final String BilanzFilterFileName = "bilanzfilter.job";
    private Database m_db = null;
    private Context m_context;
    public Bilanzrechner(Context context) {
        m_context = context;
        m_db = DatabaseHolder.getInstance().getDatabase();
       loadBilanzFilter();
    }
    public void printBilanz(LogFilter logFilter) {
        List<BilanzEntry> entries = logFilter.getBilanzEntries(m_db);
        File path = Storage.getExternalStorageFile(fileName);
        writeHtml(path,entries);

    }
    public void exportBilanz(LogFilter logFilter) {
        List<BilanzEntry> entries = logFilter.getBilanzEntries(m_db);
        File path = Storage.getExternalStorageFile(fileName);
        writeHtml(path,entries);
        Toast toast =Toast.makeText(m_context,String.format(m_context.getResources().getString(R.string.speicherpfad), path.toString()), Toast.LENGTH_LONG);
        toast.show();
    }

    private void writeHtml(File path, List<BilanzEntry> entries) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Resources res = m_context.getResources();
        double totalIncomeSum = 0, totalOutcomeSum = 0;
        String oldDate = "";
        double daylyIncomeSum = 0, daylyOutcomeSum = 0;
        int dayCount = 0;
        try {
            PrintWriter fileWriter = new PrintWriter(new FileWriter(path));
            fileWriter.print(HTML.getHeader());
            for (BilanzEntry entry: entries) {
                Date dummyDate = new Date();
                dummyDate.setTime(entry.getDate());
                String logDate = dateFormat.format(dummyDate);
                if (!logDate.contentEquals(oldDate)) {
                    if (!oldDate.contentEquals("")) {
                        fileWriter.print("<p><div class=\"bilanz\"><span class=\"attributeName\">" + res.getString(R.string.einnahmen) + ":</span> <span class=\"attribute\">" + daylyIncomeSum + "</span><span class=\"attributeName\">" + res.getString(R.string.ausgaben) + ":</span> <span class=\"attribute\"> " + daylyOutcomeSum + "</span></div>");
                        daylyIncomeSum = 0;
                        daylyOutcomeSum = 0;
                    }

                    fileWriter.print("</p><h1>" + logDate + "</h1>");
                    dayCount++;
                    oldDate = logDate;
                }
                if (entry.getOutcome() == 1) {
                    daylyOutcomeSum += entry.getAmount();
                    totalOutcomeSum += entry.getAmount();
                } else {
                    daylyIncomeSum += entry.getAmount();
                    totalIncomeSum += entry.getAmount();
                }



            }
            if (!oldDate.contentEquals("")) {
                fileWriter.print("<p><div class=\"bilanz\"><span class=\"attributeName\">" + res.getString(R.string.einnahmen)+  ":</span> <span class=\"attribute\">" + daylyIncomeSum + "</span><span class=\"attributeName\">" + res.getString(R.string.ausgaben) + ":</span> <span class=\"attribute\"> "+ daylyOutcomeSum + "</span></div>");
            }
            fileWriter.print("<br><div class=\"text\">" + res.getString(R.string.gesammteEinnahmen)+  ": " + totalIncomeSum + "<br>" +  res.getString(R.string.gesammteAusgaben) + ": "+ totalOutcomeSum + "<br>");
            fileWriter.print(String.format(res.getString(R.string.result),(totalIncomeSum - totalOutcomeSum) , dayCount));
            fileWriter.print(String.format(res.getString(R.string.averageExpenses), (totalOutcomeSum / (float) dayCount), (totalIncomeSum / (float) dayCount), (totalIncomeSum - totalOutcomeSum) / (float) dayCount));
            fileWriter.print("</div></body></html>");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        openBilanz(path);
    }
    private void openBilanz(File path) {
        Intent intent = new Intent(m_context,Browser.class);
        intent.putExtra("url",path.toString());
        m_context.startActivity(intent);
    }
    private void loadBilanzFilter() {};

}
