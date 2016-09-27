package laurenzsoft.com.waehrungsrechner;

import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Laurenz on 19.08.2016.
 */
public class LogFilter implements Parcelable, Serializable{
    private Date m_startDate = null, m_endDate = null;
    private boolean m_showHidden = false, m_showComment = true, m_showAmount = true, m_showAmountCalculated = true, m_showChangeFactor = true, m_showIncome = true, m_showOutcome = true, m_showTime = true;
    public  LogFilter() {
    };

    protected LogFilter(Parcel in) {
        m_showHidden = in.readByte() != 0;
        m_showComment = in.readByte() != 0;
        m_showAmount = in.readByte()!= 0;
        m_showAmountCalculated = in.readByte()!= 0;
        m_showChangeFactor = in.readByte()!= 0;
        m_showOutcome = in.readByte()!= 0;
        m_showIncome= in.readByte()!= 0;
        m_showTime = in.readByte()!= 0;
        long startDateLong = in.readLong();
        long endDateLong = in.readLong();
        if (startDateLong != 0) {
            m_startDate = new Date();
            m_startDate.setTime(startDateLong);
        }
        if (endDateLong != 0) {
            m_endDate = new Date();
            m_endDate.setTime(endDateLong);
        }

    }

    public static final Creator<LogFilter> CREATOR = new Creator<LogFilter>() {
        @Override
        public LogFilter createFromParcel(Parcel in) {
            return new LogFilter(in);
        }

        @Override
        public LogFilter[] newArray(int size) {
            return new LogFilter[size];
        }
    };
    public Date getStartDate() {return m_startDate;}
    public  Date getEndDate() { return m_endDate;}
    public boolean getShowHidden() {return m_showHidden;}
    public boolean getShowComment() {return  m_showComment;}
    public  boolean getShowAmount() {return m_showAmount;}
    public  boolean getShowAmountCalculated() {return m_showAmountCalculated;}
    public  boolean getShowChangeFactor() {return m_showChangeFactor;}
    public  boolean getShowOutcome() {return m_showOutcome;}
    public  boolean getShowIncome() {return m_showIncome;}
    public  boolean getShowTime() {return m_showTime;}

    public void setStartDate(Date startDate) {
        m_startDate = startDate;
    }
    public  void setEndDate(Date endDate) {
        m_endDate = endDate;
    }
    public  void setShowHidden(boolean show) {
        m_showHidden = show;
    }
    public  void setShowComment(boolean comment) {
        m_showComment = comment;
    }
    public  void setShowAmount(boolean amount) {
        m_showAmount = amount;
    }
    public  void setShowAmountCalculated(boolean amountCalc) {m_showAmountCalculated = amountCalc;}
    public  void setShowChangeFactor(boolean changeFactor) {
        m_showChangeFactor = changeFactor;
    }
    public  void setShowIncome(boolean income) {
        m_showIncome = income;
    }
    public  void setShowOutcome(boolean outcome) {
        m_showOutcome = outcome;
    }
    public void setShowTime(boolean time) {m_showTime = time;}

    public void setStartTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(m_startDate);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE, minute);
        m_startDate = calendar.getTime();
    }
    public  void setEndTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(m_endDate);
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE, minute);
        m_endDate = calendar.getTime();
    }
    public List<LogEntry> getEntries(Database db) {
        return db.getEntries(generateQuery());
    }
    public List<BilanzEntry> getBilanzEntries(Database db) {
        List<String> query = new ArrayList<String>();
        List<String> arguments = new ArrayList<String>();
        if (m_startDate != null) {
            query.add(Database.KEY_LOGDATE+" > ? ");
            arguments.add(String.valueOf(m_startDate.getTime()));
        }
        if (m_endDate != null) {
            query.add(Database.KEY_LOGDATE+" < ? ");
            arguments.add(String.valueOf(m_endDate.getTime()));
        }
        query.add(Database.KEY_SHOW +" = 1");
        String s_query = TextUtils.join(" AND ", query);

        String[] a_arguments = arguments.toArray(new String[arguments.size()]);
        return db.getBilanzEntries(s_query,a_arguments);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (m_showHidden ? 1 : 0));
        parcel.writeByte((byte) (m_showComment ? 1 : 0));
        parcel.writeByte((byte) (m_showAmount ? 1 : 0));
        parcel.writeByte((byte) (m_showAmountCalculated ? 1 : 0));
        parcel.writeByte((byte) (m_showChangeFactor ? 1 : 0));
        parcel.writeByte((byte) (m_showOutcome ? 1 : 0));
        parcel.writeByte((byte) (m_showIncome ? 1 : 0));
        parcel.writeByte((byte) (m_showTime ? 1 : 0));
        parcel.writeLong(m_startDate != null ? m_startDate.getTime() : 0);
        parcel.writeLong(m_endDate != null ? m_endDate.getTime():0);
    }
    public FilterQuery generateQuery() {
        List<String> query = new ArrayList<String>();
        List<String> arguments = new ArrayList<String>();
        FilterQuery result = new FilterQuery();

        if (m_startDate != null) {
            query.add(Database.KEY_LOGDATE+" > ? ");
            arguments.add(String.valueOf(m_startDate.getTime()));
        }
        if (m_endDate != null) {
            query.add(Database.KEY_LOGDATE+" < ? ");
            arguments.add(String.valueOf(m_endDate.getTime()));
        }
        if (!m_showHidden) {
            query.add(Database.KEY_SHOW+" = 1");
        }
        if (!(m_showIncome && m_showOutcome)) {
            if (m_showIncome) {
                query.add(Database.KEY_OUTCOME + " = 0");
            } else if (m_showOutcome) {
                query.add(Database.KEY_OUTCOME + " = 1");
            }
        }


        result.m_query = TextUtils.join(" AND ", query);
        result.m_arguments = arguments.toArray(new String[arguments.size()]);
        return  result;
    }

}
