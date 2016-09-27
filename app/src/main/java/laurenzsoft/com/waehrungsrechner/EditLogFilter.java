package laurenzsoft.com.waehrungsrechner;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditLogFilter extends AppCompatActivity implements  DatePickerFragment.TheListener, TimePickerFragment.TheListener {
   protected LogFilter m_logFilter = null;
    private ToggleButton dateAfter, dateBefore, timeAfter, timeBefore;
    private  CheckBox showHidden, showComments, showAmount, showAmountCalculated, showChangeFactor, showIncome, showOutcome, showTime;
    private  TextView dateTextView, timeTextView;
    private Button speichernButton;



    private  enum BUTTONS {DANACH, DAVOR, FRUEHER, SPAETER}
    private BUTTONS selectedButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_log_filter);
        m_logFilter = getIntent().getParcelableExtra("LogFilter");


        speichernButton = (Button) findViewById(R.id.speichernButton);

        dateAfter = (ToggleButton) findViewById(R.id.datumNachToggle);
        dateBefore = (ToggleButton) findViewById(R.id.datumVorToggle);

        timeAfter = (ToggleButton) findViewById(R.id.zeitSpaeterToggle);
        timeBefore  = (ToggleButton) findViewById(R.id.zeitFrueherToggle);

        showHidden = (CheckBox) findViewById(R.id.versteckteCheckBox);
        showComments = (CheckBox) findViewById(R.id.zeigeKommentarCheckBox);
        showAmount = (CheckBox) findViewById(R.id.mengeCheckBox);
        showAmountCalculated = (CheckBox) findViewById(R.id.umgerechnetCheckBox);
        showChangeFactor = (CheckBox) findViewById(R.id.umrechnungsfaktorCheckBox);
        showIncome = (CheckBox) findViewById(R.id.einnahmenCheckBox);
        showOutcome = (CheckBox) findViewById(R.id.ausgabenCheckBox);
        showTime = (CheckBox) findViewById(R.id.zeitCheckBox);

        dateTextView = (TextView) findViewById(R.id.datumTextView);
        timeTextView = (TextView) findViewById(R.id.zeitTextView);
        updateGUI();

        CompoundButton.OnCheckedChangeListener checkChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (compoundButton == dateAfter) {
                        selectedButton = BUTTONS.DANACH;
                        timeAfter.setEnabled(true);
                        timeAfter.setChecked(false);
                    } else if (compoundButton == dateBefore) {
                        selectedButton = BUTTONS.DAVOR;
                        timeBefore.setEnabled(true);
                        timeBefore.setChecked(false);
                    }
                    DialogFragment df = new DatePickerFragment();
                    df.show(getSupportFragmentManager(),"datePicker");
                } else {
                    if (compoundButton == dateAfter) {
                        timeAfter.setChecked(false);
                        timeAfter.setEnabled(false);
                        m_logFilter.setStartDate(null);
                    } else if (compoundButton == dateBefore){
                        timeBefore.setChecked(false);
                        timeBefore.setEnabled(false);
                        m_logFilter.setEndDate(null);
                    }
                }
            updateGUI();
            }
        };

        dateAfter.setOnCheckedChangeListener(checkChangeListener);
        dateBefore.setOnCheckedChangeListener(checkChangeListener);

        CompoundButton.OnCheckedChangeListener checkTimeChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    DialogFragment df = new TimePickerFragment();
                    df.show(getSupportFragmentManager(),"timePicker");
                }
                updateGUI();
            }
        };

        timeAfter.setOnCheckedChangeListener(checkTimeChangeListener);
        timeBefore.setOnCheckedChangeListener(checkTimeChangeListener);

        showHidden.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                m_logFilter.setShowHidden(b);
            }
        });
        showComments.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                m_logFilter.setShowComment(b);
            }
        });
        showAmount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                m_logFilter.setShowAmount(b);
            }
        });
        showAmountCalculated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                m_logFilter.setShowAmountCalculated(b);
            }
        });
        showChangeFactor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                m_logFilter.setShowChangeFactor(b);
            }
        });
        showIncome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                m_logFilter.setShowIncome(b);
                if (!b) {
                    if (!showOutcome.isChecked()) {
                        showOutcome.setChecked(true);
                        m_logFilter.setShowOutcome(true);
                    }
                }
            }
        });
        showOutcome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                m_logFilter.setShowOutcome(b);
                if (!b) {
                    if (!showIncome.isChecked()) {
                        showIncome.setChecked(true);
                        m_logFilter.setShowIncome(true);
                    }
                }
            }
        });
        showTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                m_logFilter.setShowTime(b);
            }
        });
        speichernButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("LogFilter", (Parcelable) m_logFilter);
                setResult(MainActivity.RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void returnDate(Date date) {
        Log.d("Datum ausgewählt", date.toString() + " " + selectedButton.toString());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 0);
        switch (selectedButton) {
            case DANACH:
                m_logFilter.setStartDate(cal.getTime());
                break;
            case DAVOR:
                m_logFilter.setEndDate(date);
                break;
        }
        updateGUI();
    }
    @Override
    public void returnTime(int hour, int minute) {
        Log.d("Zeit ausgewählt", String.valueOf(hour) + ":" +String.valueOf(minute)+ " " + selectedButton.toString());
        Calendar cal = Calendar.getInstance();
        switch (selectedButton){
            case SPAETER:
                m_logFilter.setStartTime(hour, minute);
            case FRUEHER:
                m_logFilter.setEndTime(hour, minute);
        }

        updateGUI();
    }
    private void updateGUI() {
        Resources res = getResources();
        Format date = new SimpleDateFormat("dd.MM");
        Format time = new SimpleDateFormat("HH:mm");
        if (m_logFilter.getStartDate() != null) {
            dateAfter.setChecked(true);
            timeAfter.setEnabled(true);
            dateTextView.setText(String.format(res.getString(R.string.dateNach),date.format(m_logFilter.getStartDate())));
            timeTextView.setText(String.format(res.getString(R.string.timeNach),time.format(m_logFilter.getStartDate())));
        }
        if (m_logFilter.getEndDate() != null) {
            dateBefore.setChecked(true);
            timeBefore.setEnabled(true);
            if (m_logFilter.getStartDate() != null) {
                dateTextView.setText(String.format(res.getString(R.string.dateNachUndVor),date.format(m_logFilter.getStartDate()), date.format(m_logFilter.getEndDate())));
                timeTextView.setText(String.format(res.getString(R.string.timeNachUndVor),time.format(m_logFilter.getStartDate()),time.format(m_logFilter.getEndDate())));
            } else {
                dateTextView.setText(String.format(res.getString(R.string.dateVor),date.format(m_logFilter.getEndDate())));
                timeTextView.setText(String.format(res.getString(R.string.timeVor),time.format(m_logFilter.getEndDate())));
            }

        }
        showComments.setChecked(m_logFilter.getShowComment());
        showHidden.setChecked(m_logFilter.getShowHidden());
        showAmount.setChecked(m_logFilter.getShowAmount());
        showAmountCalculated.setChecked(m_logFilter.getShowAmountCalculated());
        showChangeFactor.setChecked(m_logFilter.getShowChangeFactor());
        showIncome.setChecked(m_logFilter.getShowIncome());
        showOutcome.setChecked(m_logFilter.getShowOutcome());

    }
}
