package laurenzsoft.com.waehrungsrechner;

import android.view.View;
import android.widget.RadioButton;

/**
 * Created by Laurenz on 17.08.2016.
 */
public class EditTextFocusChangedListener implements View.OnFocusChangeListener {
    private  RadioButton m_selectionButton, m_otherButton;
    public  EditTextFocusChangedListener(RadioButton selectionButton, RadioButton otherButton) {
        m_selectionButton = selectionButton;
        m_otherButton = otherButton;
    }
    @Override
    public void onFocusChange(View view, boolean b) {
        if (view != null) {
            if (b) {
                m_selectionButton.setChecked(b);
                m_otherButton.setChecked(!b);
            }
        }


    }
}
