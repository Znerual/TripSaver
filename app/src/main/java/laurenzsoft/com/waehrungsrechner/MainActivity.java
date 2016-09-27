package laurenzsoft.com.waehrungsrechner;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "Umrechnungsfaktor";
    public static final int GETLOGFILTER = 1234;
    public static final int ADDCHANGEFACTOR = 1432;
    public static final int EDITCHANGEFACTOR = 1433;
    private Accessor g_handler = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger logger = new Logger(this);

        Button calculateButton;
        EditText euroIn, otherIn, changeFactorIn;
        AutoCompleteTextView commentIn;
        RadioButton euroSelection, otherSelection;
        Spinner changeFactorSelection;
        final CheckBox income, outcome;

        euroIn = (EditText) findViewById(R.id.euroIn);
        otherIn = (EditText) findViewById(R.id.andereIn);
        changeFactorIn = (EditText) findViewById(R.id.changeFactorIn);
        commentIn = (AutoCompleteTextView) findViewById(R.id.kommentarEditEditText);

        calculateButton = (Button) findViewById(R.id.calculateButton);

        euroSelection = (RadioButton) findViewById(R.id.selectionEuro);
        otherSelection = (RadioButton) findViewById(R.id.selectionOther);

        income = (CheckBox) findViewById(R.id.gutschriftCheckBox);
        outcome = (CheckBox) findViewById(R.id.lastschriftCheckBox);

        changeFactorSelection = (Spinner) findViewById(R.id.spinner);

        Button clearOtherButton = (Button) findViewById(R.id.clearAndere);
        Button clearEuroButton = (Button) findViewById(R.id.clearEuro);


       g_handler = new Accessor(euroIn, otherIn, changeFactorIn, commentIn, euroSelection, income, outcome,changeFactorSelection, this, logger);

        euroIn.setOnFocusChangeListener( new EditTextFocusChangedListener(euroSelection, otherSelection));
        otherIn.setOnFocusChangeListener(new EditTextFocusChangedListener(otherSelection, euroSelection));
        euroIn.setOnEditorActionListener(new CalculateOnEditorAcitonListener(g_handler));
        otherIn.setOnEditorActionListener(new CalculateOnEditorAcitonListener(g_handler));


        euroSelection.setOnClickListener( new RadioButtonSelectedOnClickListener(otherSelection));
        otherSelection.setOnClickListener( new RadioButtonSelectedOnClickListener(euroSelection));

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.autocomplete, null, new String[]{Database.KEY_COMMENT}, new int[] {R.id.autocompleteTextView},0);
        commentIn.setAdapter(adapter);
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                if (charSequence != null) {
                    return DatabaseHolder.getInstance().getDatabase().getCursorForComments(charSequence.toString());
                }
                return null;
            }
        });
        adapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
               return cursor.getString(cursor.getColumnIndexOrThrow(Database.KEY_COMMENT));
            }
        });
        clearOtherButton.setOnClickListener(new ClearOnClickListener(otherIn, ""));
        clearEuroButton.setOnClickListener(new ClearOnClickListener(euroIn, ""));

        income.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    outcome.setChecked(false);
                } else {
                    outcome.setChecked(true);
                }
            }
        });
        outcome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    income.setChecked(false);
                } else {
                    income.setChecked(true);
                }
            }
        });

        calculateButton.setOnClickListener(new CalculateButtonOnClickListener(g_handler));

        changeFactorSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ChangeFactor selected = (ChangeFactor) adapterView.getItemAtPosition(i);
                Log.d("spinnerClick",selected.getName());
                g_handler.getChangeFactorField().setText(String.valueOf(selected.getChangeFactor()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do if empty
            }
        });
        ChangefactorArrayAdapter changeFactorAdapter;
        List<ChangeFactor> changeFactors = DatabaseHolder.getInstance().getDatabase().getAllChangefactors();
        changeFactorAdapter = new ChangefactorArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, changeFactors);
        changeFactorSelection.setAdapter(changeFactorAdapter);
        changeFactorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        float umrechnungsfaktor = settings.getFloat("umrechnungsfaktor",1f);
        changeFactorIn.setText(String.valueOf(umrechnungsfaktor));

        if (settings.getBoolean("firstStart", true)) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstStart", false);
            editor.commit();
            startTutorial();
        }

    }
    private void startTutorial() {
        g_handler.setUIState(false);
        continueTutorial(0);
    }
    private void getPermission() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                100);
    }
    private void continueTutorial(int i) {
        switch (i) {
            case 0:
                Dialog tutorialStart = new Dialog(this, Dialog.ICON.INFO,R.string.willkommen, R.string.tutorial1, true, g_handler);
                tutorialStart.setPositiveButton(R.string.weiter, new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int which) {continueTutorial(1);}});
                tutorialStart.show();
                break;
            case 1:
                Dialog tutorial1 = new Dialog(this, Dialog.ICON.INFO,R.string.tutorial, R.string.tutorial2, true, g_handler);
                tutorial1.setPositiveButton(R.string.weiter, new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int which) {continueTutorial(2);}});
                tutorial1.show();
                break;
            case 2:
                Dialog tutorial2 = new Dialog(this, Dialog.ICON.INFO,R.string.tutorial, R.string.tutorial3, true, g_handler);
                tutorial2.setPositiveButton(R.string.weiter, new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int which) {continueTutorial(3);}});
                tutorial2.show();
                break;
            case 3:
                Dialog tutorial3 = new Dialog(this, Dialog.ICON.INFO,R.string.tutorial, R.string.tutorial4, true, g_handler);
                tutorial3.setPositiveButton(R.string.weiter, new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int which) {continueTutorial(4);}});
                tutorial3.show();
                break;
            case 4:
                Dialog tutorial4 = new Dialog(this, Dialog.ICON.INFO,R.string.tutorial, R.string.tutorial5, false, g_handler);
                tutorial4.setPositiveButton(R.string.start, new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int which) {g_handler.setUIState(true);}});
                tutorial4.show();
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bilanzrechner bilanz = null;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logButton:
                getPermission();
                g_handler.getLogger().printLog();
                return true;
            case R.id.bilanzButton:
                getPermission();
                bilanz = new Bilanzrechner(this);
                bilanz.printBilanz(g_handler.getLogger().getLogFilter());
                return true;
            case R.id.editLogButton:
                Intent editLogIntent = new Intent(this, EditLogEntriesListActivity.class);
                startActivity(editLogIntent);
                return true;
            case R.id.editChangeFactorButton:
                Intent editChangeIntent = new Intent(this, EditChangefactorsListActivity.class);
                startActivityForResult(editChangeIntent, EDITCHANGEFACTOR);
                return true;
            case R.id.addChangeFactorButton:
                Intent newChangeFactorIntent = new Intent(this, EditChangeFactorActivity.class);
                newChangeFactorIntent.putExtra("edit", false);
                startActivityForResult(newChangeFactorIntent, ADDCHANGEFACTOR);
                return true;
            case R.id.exportLogButton:
                getPermission();
                g_handler.getLogger().exportLog();
                return true;
            case R.id.exportBilanzButton:
                getPermission();
                bilanz = new Bilanzrechner(this);
                bilanz.exportBilanz(g_handler.getLogger().getLogFilter());
                return true;
            case R.id.filterLogButton:
                Intent editLogFilterIntent = new Intent(this, EditLogFilter.class);
                editLogFilterIntent.putExtra("LogFilter", (Parcelable) g_handler.getLogger().getLogFilter());
                startActivityForResult(editLogFilterIntent, GETLOGFILTER);
                return  true;
            case R.id.clearCommentFieldCheck:
                item.setChecked(!item.isChecked());
                g_handler.setClearComment(item.isChecked());
                return  true;
            case R.id.showFAQButton:
                faq.show(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GETLOGFILTER) {
            if (resultCode == RESULT_OK) {
                LogFilter logFilter = data.getParcelableExtra("LogFilter");
                g_handler.getLogger().setLogFilter(logFilter);
            }
        } else if (requestCode == ADDCHANGEFACTOR || requestCode == EDITCHANGEFACTOR) {
            if (resultCode == RESULT_OK) {
                ChangefactorArrayAdapter changeFactorAdapter;
                List<ChangeFactor> changeFactors = DatabaseHolder.getInstance().getDatabase().getAllChangefactors();
                changeFactorAdapter = new ChangefactorArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, changeFactors);
                g_handler.getSpinner().setAdapter(changeFactorAdapter);
                changeFactorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }
        }
    }
    @Override
    protected  void onDestroy() {
        super.onDestroy();
        g_handler.getLogger().saveLogFilter();
    }
}
