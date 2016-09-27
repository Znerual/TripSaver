package laurenzsoft.com.waehrungsrechner;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

/**
 * Created by Laurenz on 18.08.2016.
 */
public class Accessor {
    private  Context m_context;
    private  EditText m_currencyField1, m_currencyField2, m_changeFactorField, m_commentField;
    private RadioButton m_euroToOther;
    private CheckBox m_incomeField, m_outcomeField;
    private Logger m_log;
    private Spinner m_spinner;
    private boolean m_clearCommentField = false;
    public Accessor(EditText currencyField1, EditText currencyField2, EditText changeFactor, EditText commentField, RadioButton euroToOther,CheckBox incomeField, CheckBox outcomeField, Spinner spinner, Context context, Logger log) {
        m_currencyField1 = currencyField1;
        m_currencyField2 = currencyField2;
        m_changeFactorField = changeFactor;
        m_commentField = commentField;
        m_incomeField = incomeField;
        m_outcomeField = outcomeField;
        m_euroToOther = euroToOther;
        m_spinner = spinner;
        m_context = context;
        m_log = log;
    }
    public Context getContext() {
        return m_context;
    }
    public EditText getCurrencyFieldPrimary() {
        return m_currencyField1;
    }
    public EditText getCurrencyFieldSecondary() {
        return m_currencyField2;
    }
    public EditText getChangeFactorField() {
        return m_changeFactorField;
    }
    public EditText getCommentField() { return  m_commentField;}
    public Spinner getSpinner() { return m_spinner;}
    public String getComment() {
        String comment = m_commentField.getText().toString();
        if (m_clearCommentField) {
            m_commentField.setText("");
        }
        return comment;
    }
    public boolean getEuroRadioButtonChecked() {
        return m_euroToOther.isChecked();
    }
    public boolean getIncomdeCheckboxChecked() { return m_incomeField.isChecked();}
    public boolean getOutcomeCheckBoxChecked() { return m_outcomeField.isChecked();}
    public boolean getClearComment() { return m_clearCommentField;}
    public void setClearComment(boolean clearComment) {m_clearCommentField = clearComment;}
    public Logger getLogger() {
        return m_log;
    }
    public   void saveUmrechnungsfaktor() {
        SharedPreferences settings = m_context.getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        float umrechnungsfaktor = Float.valueOf(m_changeFactorField.getText().toString());
        editor.putFloat("umrechnungsfaktor", umrechnungsfaktor);
        editor.commit();
    }
    public  double getCurrency(EditText currencyField, double standardResult) {
        double result = standardResult;
        try {
            result = Double.parseDouble(currencyField.getText().toString());
            if ( result == 0) {
                result = standardResult;
            }
        } catch (NumberFormatException exc) {
            //Toast and log wrong numberic input
        }
        return result;
    }
    public void setUIState(boolean enabled) {
        m_currencyField1.setEnabled(enabled);
        m_currencyField2.setEnabled(enabled);
        m_changeFactorField.setEnabled(enabled);
        m_commentField.setEnabled(enabled);
        m_spinner.setEnabled(enabled);

    }
}
