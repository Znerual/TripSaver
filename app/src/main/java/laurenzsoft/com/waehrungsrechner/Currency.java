package laurenzsoft.com.waehrungsrechner;

/**
 * Created by Laurenz on 18.08.2016.
 */

public class Currency {
    public enum Calculation { CALCULATE, CALCULATE_BACK, DONT_CALCULATE};
    private double m_amount, m_changeFactor, m_amountCalculated;
    private Calculation m_changeDirection;
    public Currency(double amount) {
        m_amount = amount;
        m_changeDirection = Calculation.DONT_CALCULATE;
        m_changeFactor = 1.0;
        m_amountCalculated = m_amount;
    };
    public Currency(double amount, double changeFactor, Calculation changeDirection) {
        m_amount = amount;
        m_changeFactor = changeFactor;
        m_changeDirection = (changeFactor == 1.0) ? Calculation.DONT_CALCULATE : changeDirection;
        switch (m_changeDirection) {
            case CALCULATE:
                m_amountCalculated = m_amount * m_changeFactor;
                break;
            case CALCULATE_BACK:
                m_amountCalculated = m_amount / m_changeFactor;
                break;
        }
    }
    public double result() {
        return m_amountCalculated;
    }
    public String resultToString() {
        return String.valueOf(m_amountCalculated);
    }
    public Calculation getCalculation() { return  m_changeDirection;}
    public double getAmount() {return m_amount; }
    public double getAmountCalculated() {return m_amountCalculated; }
    public double getChangeFactor() {return m_changeFactor; }
}
