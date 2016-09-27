package laurenzsoft.com.waehrungsrechner;

import java.util.Date;

/**
 * Created by Laurenz on 24.08.2016.
 */
public class BilanzEntry {
    private int m_id;
    private Date m_date;
    private double m_amount;
    private boolean m_outcome;
    public BilanzEntry() {
        m_id = -1;
        m_date = new Date();
        m_date.setTime(0);
        m_amount = 0;
        m_outcome = true;
    }
    public BilanzEntry(int id, Date date, double amount, boolean outcome) {
        m_id = id;
        m_date = date;
        m_amount = amount;
        m_outcome = outcome;
    }
    public int getId() {return m_id;}
    public long getDate() { return m_date.getTime();}
    public double getAmount() {return m_amount;}
    public int getOutcome() { return m_outcome ? 1 : 0;}
    public void setId(int id) {m_id = id;}
    public void setDate(long date) {m_date = new Date();m_date.setTime(date);}
    public void setAmount(double amount) {m_amount = amount;}
    public void setOutcome(int outcome) {m_outcome = (outcome == 1);}


}
