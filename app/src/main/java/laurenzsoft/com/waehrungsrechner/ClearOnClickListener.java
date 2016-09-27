package laurenzsoft.com.waehrungsrechner;

import android.view.View;
import android.widget.EditText;

/**
 * Created by Laurenz on 17.08.2016.
 */
public class ClearOnClickListener implements View.OnClickListener {
    private  EditText m_editTextToClear;
    private  String m_clearText;
    public ClearOnClickListener(EditText editTextToClear) {
        this.m_editTextToClear = editTextToClear;
        this.m_clearText = "";
    }
    public ClearOnClickListener(EditText editTextToClear, String clearText) {
        this.m_editTextToClear = editTextToClear;
        this.m_clearText = clearText;
    }

    @Override
    public void onClick(View view) {
        m_editTextToClear.setText(m_clearText);
    }
}
