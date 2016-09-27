package laurenzsoft.com.waehrungsrechner;

import android.view.View;
import android.widget.RadioButton;

/**
 * Created by Laurenz on 17.08.2016.
 */
public class RadioButtonSelectedOnClickListener implements View.OnClickListener {
    private RadioButton m_otherRadioButton;
    public RadioButtonSelectedOnClickListener(RadioButton otherRadioButton) {
        m_otherRadioButton = otherRadioButton;
    }
    @Override
    public void onClick(View view) {
        m_otherRadioButton.setChecked(false);
        ((RadioButton) view).setChecked(true);
    }
}
