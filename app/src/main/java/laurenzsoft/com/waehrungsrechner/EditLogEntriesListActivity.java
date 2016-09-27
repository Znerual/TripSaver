package laurenzsoft.com.waehrungsrechner;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class EditLogEntriesListActivity extends ListActivity {
    private final int EDITEDENTRY = 1235;
    private EditLogEntriesAdapter m_adapter;
    private Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_adapter = new EditLogEntriesAdapter(this,null );
        setListAdapter(m_adapter);
        db = DatabaseHolder.getInstance().getDatabase();

        m_adapter.changeCursor(db.getCursorForAllEntries());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        LogEntry logEntry =  db.createEntryFromCursor((Cursor) m_adapter.getItem(position));
        Log.d("ListItemClicked", String.valueOf(logEntry.getId()));
        Intent intent = new Intent(this, EditLogEntriesActivity.class);
        intent.putExtra("id",logEntry.getId());
        startActivityForResult(intent, EDITEDENTRY);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITEDENTRY) {
            if (resultCode == RESULT_OK) {

                m_adapter.changeCursor(db.getCursorForAllEntries());
            }
        }
    }

}
