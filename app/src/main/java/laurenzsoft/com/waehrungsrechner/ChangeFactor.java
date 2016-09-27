package laurenzsoft.com.waehrungsrechner;

/**
 * Created by Laurenz on 29.08.2016.
 */
public class ChangeFactor {
    private int m_id = -1;
    private String m_name = "";
    private double m_changeFactor = 1;
    public ChangeFactor() {}
    public void setId(int id) {
        m_id = id;
    }
    public void setName(String name) {
        m_name = name;
    }
    public void setChangeFactor(double changeFactor) {
        m_changeFactor = changeFactor;
    }
    public int getId() {return m_id;}
    public String getName() {return  m_name;}
    public double getChangeFactor() {return m_changeFactor;}
}
