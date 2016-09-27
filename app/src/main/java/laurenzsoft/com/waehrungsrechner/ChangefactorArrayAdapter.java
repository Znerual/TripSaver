package laurenzsoft.com.waehrungsrechner;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Laurenz on 30.08.2016.
 */
public class ChangefactorArrayAdapter extends ArrayAdapter<ChangeFactor> {
    private Context m_context;
    private List<ChangeFactor> m_values;
    public ChangefactorArrayAdapter(Context context, int textViewResourceId, List<ChangeFactor> objects) {
        super(context, textViewResourceId, objects);
        m_context = context;
        m_values = objects;
    }
    @Override
    public int getCount() {
        return m_values.size();
    }
    @Override
    public ChangeFactor getItem(int position) {
        return m_values.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(m_context);
        label.setTextSize(18);
        label.setText(m_values.get(position).getName());
        label.setHeight(70);
        label.setGravity(Gravity.LEFT|Gravity.CENTER);
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(m_context);
        label.setTextSize(18);
        label.setText(m_values.get(position).getName());
        label.setHeight(80);
        label.setGravity(Gravity.LEFT|Gravity.CENTER);
        return label;
    }
}
