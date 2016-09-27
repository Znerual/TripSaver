package laurenzsoft.com.waehrungsrechner;

/**
 * Created by Laurenz on 18.08.2016.
 */
public class Calculator {
    private Accessor m_handler;
    public Calculator(Accessor handler) {
        m_handler = handler;
    }
    public void calculate() {
        EntryData data = new EntryData();
        data.m_comment = m_handler.getComment();
        data.m_lastschrift = m_handler.getOutcomeCheckBoxChecked();
        if (m_handler.getEuroRadioButtonChecked())  {
            double currency1 = m_handler.getCurrency(m_handler.getCurrencyFieldPrimary(), 0.0);
            double changeFactor = m_handler.getCurrency(m_handler.getChangeFactorField(), 1.0);
            Currency calculation1 = new Currency(currency1, changeFactor, Currency.Calculation.CALCULATE);
            data.m_currency = calculation1;
            m_handler.getLogger().writeLog(data);
            m_handler.getCurrencyFieldSecondary().setText(calculation1.resultToString());
        } else {
            double currency2 = m_handler.getCurrency(m_handler.getCurrencyFieldSecondary(), 0.0);
            double changeFactor = m_handler.getCurrency(m_handler.getChangeFactorField(), 1.0);
            Currency calculation2 = new Currency(currency2, changeFactor, Currency.Calculation.CALCULATE_BACK);
            data.m_currency = calculation2;
            m_handler.getLogger().writeLog(data);
            m_handler.getCurrencyFieldPrimary().setText(calculation2.resultToString());
        }
        m_handler.saveUmrechnungsfaktor();
    }

}
