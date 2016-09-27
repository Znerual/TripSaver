package laurenzsoft.com.waehrungsrechner;

import android.content.res.Resources;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Laurenz on 18.08.2016.
 */
public class LogEntry {
    private  int m_id;
    private Date m_date;
    private  double m_amount, m_amountCalculated, m_changeFactor;
    private  boolean m_show, m_outcome;
    private  String m_comment;

    public  LogEntry() {
        m_id = -1;
        m_date = new Date();
        m_date.setTime(0);
        m_amount = 0;
        m_amountCalculated = 0;
        m_changeFactor = 1.0;
        m_show = true;
        m_outcome = true;
        m_comment = "";
    };

    public LogEntry(int id, long date, double amount, double amountCalculated, double changeFactor, boolean show, boolean outcome, String comment) {
        m_id = id;
        m_date = new Date(date);
        m_amount = amount;
        m_amountCalculated = amountCalculated;
        m_changeFactor = changeFactor;
        m_show = show;
        m_outcome = outcome;
        m_comment = comment;
    }
    public int getId() {
        return m_id;
    }
    public long getDate(){
        return m_date.getTime();
    };
    public double getAmount() {
        return  m_amount;
    };
    public  double getAmountCalculated() {
        return  m_amountCalculated;
    }
    public  double getChangeFactor() {
        return m_changeFactor;
    }
    public int getShow() {
        return m_show? 1:0;
    }
    public int getOutcome() {
        return m_outcome ? 1: 0;
    }
    public  String getComment() {
        if (m_comment != null) {
            return m_comment;
        } else {
            return  "";
        }

    }
    public  void setId(int id) {
        m_id = id;
    }
    public void setDate(long date) {
        m_date = new Date(date);
    };
    public void setAmount(double amount) {
        m_amount = amount;
    };
    public  void setAmountCalculated(double amountCalculated) {
        m_amountCalculated = amountCalculated;
    };
    public void setChangeFactor(double changeFactor) {
        m_changeFactor = changeFactor;
    };
    public  void setShow(int show) {
        m_show = (show == 1);
    };
    public  void setOutcome(int outcome) {
        m_outcome = (outcome == 1);
    };
    public void setComment(String comment) {
        m_comment = comment;
    };
    @Override
    public String toString() {
        return "Entry [id=" + m_id + ", logdate="+m_date + ", amount=" + m_amount + ", amountcalculated=" + m_amountCalculated + ", changefactor=" + m_changeFactor + ", show=" + m_show + ", outcome=" + m_outcome + ", comment=" + m_comment + "]";
    }
    public String toHTML(LogFilter logFilter, Resources res) {
        List<String> htmlCode = new ArrayList<String>();
        htmlCode.add("<div>");
        if (logFilter.getShowAmount()) {
            htmlCode.add("<span class=\"attributeName\">"+ res.getString(R.string.Input) +":</span><span class=\"attribute\"> " + m_amount + "</span> | ");
        }
        if (logFilter.getShowChangeFactor()) {
            htmlCode.add("<span class=\"attributeName\">"+ res.getString(R.string.umrechnungsfaktor) +":</span><span class=\"attribute\"> " + m_changeFactor + "</span> | ");
        }
        if (logFilter.getShowAmountCalculated()) {
            htmlCode.add("<span class=\"attributeName\">" + res.getString(R.string.Output) + ":</span><span class=\"attribute\"> " + m_amountCalculated + "</span>");
        }
        htmlCode.add("</div>");
        if (!m_comment.contentEquals("") && logFilter.getShowComment()) {
            htmlCode.add("<div style=...> <span class=\"attributeName\">"+ res.getString(R.string.Kommentar) +":</span><span class=\"attribute\"> " + m_comment + "</span></div>");
        }
        return TextUtils.join("", htmlCode);


    }
}
