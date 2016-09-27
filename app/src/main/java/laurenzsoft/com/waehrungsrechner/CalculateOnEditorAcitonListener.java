package laurenzsoft.com.waehrungsrechner;


import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Laurenz on 18.08.2016.
 */
public class CalculateOnEditorAcitonListener implements EditText.OnEditorActionListener {
    private  Accessor m_handler;
    public CalculateOnEditorAcitonListener(Accessor handler) {
        m_handler = handler;
    }
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            Calculator calculation = new Calculator(m_handler);
            calculation.calculate();
            return true;
        }
        return false;
    }

}
