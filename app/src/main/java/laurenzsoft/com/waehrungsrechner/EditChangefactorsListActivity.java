package laurenzsoft.com.waehrungsrechner;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Laurenz on 30.08.2016.
 */
public class EditChangefactorsListActivity extends ListActivity {
    private final int EDITCHANGEFACTOR = 1435;
    private ChangefactorArrayAdapter m_adapter;
    private Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = DatabaseHolder.getInstance().getDatabase();
        m_adapter = new ChangefactorArrayAdapter(this,R.layout.changefactor_entry,db.getAllChangefactors());
        setListAdapter(m_adapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ChangeFactor cf = m_adapter.getItem(position);
        Log.d("ListItemClicked", String.valueOf(cf.getId()));
        Intent intent = new Intent(this, EditChangeFactorActivity.class);
        intent.putExtra("edit", true);
        intent.putExtra("id",cf.getId());

        startActivityForResult(intent, EDITCHANGEFACTOR);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITCHANGEFACTOR) {
            if (resultCode == RESULT_OK) {

                m_adapter = new ChangefactorArrayAdapter(this,R.layout.changefactor_entry,db.getAllChangefactors());
                setListAdapter(m_adapter);
                setResult(RESULT_OK, null);
            }
        }
    }

}
