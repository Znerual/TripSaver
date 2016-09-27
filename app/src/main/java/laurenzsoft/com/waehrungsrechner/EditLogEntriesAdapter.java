package laurenzsoft.com.waehrungsrechner;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Laurenz on 22.08.2016.
 */
public class EditLogEntriesAdapter extends SimpleCursorAdapter {
    public EditLogEntriesAdapter(Context context, Cursor cursor) {
        super(context,R.layout.log_entry,cursor,new String[]{Database.KEY_COMMENT,Database.KEY_LOGDATE, Database.KEY_AMOUNT, Database.KEY_CHANGEFACTOR},new int[] {R.id.kommentarTextView, R.id.changeFactorNameTextView, R.id.mengeTextView, R.id.umrechnungsfaktorTextView},0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.log_entry,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        DateFormat dateAndTimeFormat = new SimpleDateFormat("dd.MM.yyyy' at 'HH:mm");
        TextView dateAndTimeTextView, amountTextView, changeFactorTextView, commentTextView;

        commentTextView = (TextView) view.findViewById(R.id.kommentarTextView);
        dateAndTimeTextView = (TextView) view.findViewById(R.id.changeFactorNameTextView);
        amountTextView = (TextView) view.findViewById(R.id.mengeTextView);
        changeFactorTextView = (TextView) view.findViewById(R.id.umrechnungsfaktorTextView);

        String comment = cursor.getString(cursor.getColumnIndexOrThrow(Database.KEY_COMMENT));
        long date = cursor.getLong(cursor.getColumnIndexOrThrow(Database.KEY_LOGDATE));
        float amount = cursor.getFloat(cursor.getColumnIndexOrThrow(Database.KEY_AMOUNT));
        float changeFactor = cursor.getFloat(cursor.getColumnIndexOrThrow(Database.KEY_CHANGEFACTOR));

        commentTextView.setText(comment);
        dateAndTimeTextView.setText(dateAndTimeFormat.format(new Date(date)));
        amountTextView.setText(String.valueOf(amount));
        changeFactorTextView.setText(String.valueOf(changeFactor));
    }
}
