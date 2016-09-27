package laurenzsoft.com.waehrungsrechner;

import android.view.View;

/**
 * Created by Laurenz on 17.08.2016.
 */
public class CalculateButtonOnClickListener implements View.OnClickListener {
    private  Accessor m_handler;

    public  CalculateButtonOnClickListener(Accessor handler) {
        m_handler = handler;
    }
    @Override
    public void onClick(View view) {
        Calculator calculation = new Calculator(m_handler);
        calculation.calculate();
    }


}
