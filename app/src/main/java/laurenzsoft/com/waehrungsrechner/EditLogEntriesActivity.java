package laurenzsoft.com.waehrungsrechner;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditLogEntriesActivity extends AppCompatActivity {
    private Database m_db;
    private LogEntry selectedEntry;
    private TextView date, time, amountCalculated;
    private EditText amount, changeFactor, comment;
    private CheckBox income, outcome, hidden;
    private Button save, update, delete;
    private Resources res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_log_entries);
        m_db = DatabaseHolder.getInstance().getDatabase();
        int id = getIntent().getIntExtra("id",1);
        selectedEntry =  m_db.getEntryById(id);

        res = getResources();
        date = (TextView) findViewById(R.id.dateTextView);
        time = (TextView) findViewById(R.id.timeTextView);
        amountCalculated = (TextView) findViewById((R.id.outputTextView));

        amount = (EditText) findViewById(R.id.mengeEditText);
        changeFactor = (EditText) findViewById(R.id.editText2);
        comment = (EditText) findViewById(R.id.kommentarEditEditText);

        income = (CheckBox) findViewById(R.id.gutschriftEditLogCheckBox);
        outcome = (CheckBox) findViewById(R.id.lastschriftEditLogCheckBox);
        hidden = (CheckBox) findViewById(R.id.hiddenCheckBox);

        save = (Button) findViewById(R.id.speichernButton);
        update = (Button) findViewById(R.id.updateButton);
        delete = (Button) findViewById(R.id.deleteButton);

        amount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    selectedEntry.setAmount(Double.parseDouble(charSequence.toString()));
                    refreshValues();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {            }
        });
        changeFactor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    selectedEntry.setChangeFactor(Double.parseDouble(charSequence.toString()));
                    refreshValues();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        income.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                outcome.setChecked(!b);
            }
        });
        outcome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                income.setChecked(!b);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGUI();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshValues();
                if (amount.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(EditLogEntriesActivity.this, res.getString(R.string.leeresFeld), Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                if (changeFactor.getText().toString().isEmpty()) {
                    Toast toast = Toast.makeText(EditLogEntriesActivity.this, res.getString(R.string.leeresFeld), Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                selectedEntry.setComment(comment.getText().toString());
                selectedEntry.setAmountCalculated(Double.parseDouble(amountCalculated.getText().toString()));
                selectedEntry.setShow(hidden.isChecked() ? 0 : 1);
                selectedEntry.setOutcome(outcome.isChecked() ? 1 : 0);
                m_db.update(selectedEntry);
                setResult(RESULT_OK, null  );
                finish();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EditLogEntriesActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(res.getString(R.string.loeschen))
                        .setMessage(R.string.sicherloeschen)
                        .setPositiveButton(res.getString(R.string.ja), new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                m_db.deleteEntry(selectedEntry);
                                setResult(RESULT_OK, null  );
                                finish();
                            }

                        })
                        .setNegativeButton(res.getString(R.string.nein), null)
                        .show();
            }
        });
        updateGUI();


    }
    private void updateGUI() {
        DateFormat datumsFormat = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat zeitFormat = new SimpleDateFormat("HH:mm");

        Date logentrydate = new Date(selectedEntry.getDate());

        date.setText(datumsFormat.format(logentrydate));
        time.setText(zeitFormat.format(logentrydate));

        amountCalculated.setText(String.valueOf(selectedEntry.getAmountCalculated()));
        amount.setText(String.valueOf(selectedEntry.getAmount()));
        changeFactor.setText(String.valueOf(selectedEntry.getChangeFactor()));
        if (!selectedEntry.getComment().isEmpty()) {
            comment.setText(selectedEntry.getComment());
        } else {
            comment.setText("");
        }

        if (selectedEntry.getOutcome() == 1) {
            outcome.setChecked(true);
            income.setChecked(false);
        } else {
            income.setChecked(true);
            outcome.setChecked(false);
        }
        hidden.setChecked(selectedEntry.getShow() != 1);
    }
    private void refreshValues() {
        Currency currency = new Currency(selectedEntry.getAmount(), selectedEntry.getChangeFactor(), Currency.Calculation.CALCULATE);
        selectedEntry.setAmountCalculated(currency.result());

    }
}
