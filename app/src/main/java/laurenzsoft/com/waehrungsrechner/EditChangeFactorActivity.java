package laurenzsoft.com.waehrungsrechner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditChangeFactorActivity extends AppCompatActivity {
    private Database m_db;
    private ChangeFactor selectedEntry = null;
    private Button saveButton;
    private EditText nameInput, changeFactorInput;
    private int id;
    private boolean edit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_change_factor);
        m_db = DatabaseHolder.getInstance().getDatabase();
        selectedEntry = new ChangeFactor();



        saveButton = (Button) findViewById(R.id.EditChangeFactorSaveButton);
        nameInput = (EditText) findViewById(R.id.EditCFNameEditText);
        changeFactorInput = (EditText) findViewById(R.id.EditCFChangeFactorEditText);
        edit = getIntent().getBooleanExtra("edit",false);
        if (edit) {
            id = getIntent().getIntExtra("id",1);
            selectedEntry =  m_db.getChangeFactorById(id);
            nameInput.setText(selectedEntry.getName());
            changeFactorInput.setText(String.valueOf(selectedEntry.getChangeFactor()));

        }



        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameInput.getText().toString().contentEquals("") || changeFactorInput.getText().toString().contentEquals("")) {
                    Toast toast = Toast.makeText(EditChangeFactorActivity.this, getResources().getString(R.string.leeresFeld), Toast.LENGTH_LONG);
                    toast.show();
                    return;
                } else {
                    selectedEntry.setName(nameInput.getText().toString());
                    selectedEntry.setChangeFactor(Double.parseDouble(changeFactorInput.getText().toString()));
                    if (edit) {
                        m_db.update(selectedEntry);
                    } else {
                        m_db.addEntry(selectedEntry);
                    }
                    setResult(RESULT_OK, null);
                    finish();
                }
            }
        });
    }
}
