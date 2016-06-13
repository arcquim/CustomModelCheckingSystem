package com.arcquim.system.android;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.arcquim.gwt.shared.Record;
import com.arcquim.gwt.shared.RecordEncoderDecoder;
import com.arcquim.system.android.orchestrator.Orchestrator;


public class CheckPropertyActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String EMAIL_SENT_TITLE = "Info";
    private static final String MODEL_IS_CHECKING_MESSAGE = "The property is checking on the model. " +
            "The result will be sent to ";
    private static final String USER_BUSY_MESSAGE = "Please wait; another property is verifying by ";
    private static final String OK_BUTTON_TEXT = "Ok";

    private TableLayout tableLayout;
    private Record record;
    private EditText editText;
    private boolean resumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        record = RecordEncoderDecoder.decode(getIntent().getStringExtra(InterActivityConstants.RECORD_KEY));
        setContentView(R.layout.activity_check_property);
        Button checkButton = (Button) findViewById(R.id.checkButton);
        checkButton.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(record.getProjectDescriptor().getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText = (EditText) findViewById(R.id.propertyEditText);

        String[] predicates = new String[record.getProjectDescriptor().getAtomicPredicates().size()];
        predicates = record.getProjectDescriptor().getAtomicPredicates().toArray(predicates);

        tableLayout = (TableLayout)findViewById(R.id.tableLayout);

        int position = 0;
        for (String predicate : predicates){
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView numberTextView =  new TextView(this);
            numberTextView.setGravity(1);
            numberTextView.setText(String.valueOf(position));
            position++;
            numberTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            tr.addView(numberTextView);

            TextView predicateTextView =  new TextView(this);
            predicateTextView.setGravity(1);
            predicateTextView.setText(predicate);
            predicateTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            tr.addView(predicateTextView);
            tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.checkButton:
                String property = editText.getText().toString();
                if (property == null || property.isEmpty()) {
                    return;
                }
                record.getProjectDescriptor().setProperty(property);
                Orchestrator.getInstance().go(record, Orchestrator.DesiredActionType.CHECK_PROJECT);
                Record answerRecord = Orchestrator.getInstance().get();
                if (answerRecord == null) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(EMAIL_SENT_TITLE);
                    builder.setMessage(USER_BUSY_MESSAGE + record.getClientDescriptor().getClientEmail());

                    // Set up the buttons
                    builder.setNeutralButton(OK_BUTTON_TEXT, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(EMAIL_SENT_TITLE);
                    builder.setMessage(MODEL_IS_CHECKING_MESSAGE + record.getClientDescriptor().getClientEmail());

                    // Set up the buttons
                    builder.setNeutralButton(OK_BUTTON_TEXT, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                break;
        }
    }
}
