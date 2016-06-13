package com.arcquim.system.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.arcquim.gwt.shared.Record;
import com.arcquim.gwt.shared.RecordEncoderDecoder;

import java.util.ArrayList;
import java.util.List;

public class AddAtomicPredicateActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String NO_PREDICATES_MESSAGE = "Specify one predicate at least";
    private static final String NO_PREDICATES_TITLE = "Info";
    private static final String OK_BUTTON_TEXT = "Ok";

    private Button addButton;
    private Button nextButton;
    //private ListView listView;
    private TableLayout tableLayout;
    private EditText predicateEditText;
    private List<String> data;
    private ArrayAdapter adapter;

    private List<String> predicates;
    private int position = 0;
    private Record record;
    private boolean resumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_atomic_predicate);
        record = RecordEncoderDecoder.decode(getIntent().getStringExtra(InterActivityConstants.RECORD_KEY));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(record.getProjectDescriptor().getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addButton = (Button)findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        nextButton = (Button)findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);

        predicateEditText = (EditText)findViewById(R.id.predicateEditText);

        tableLayout = (TableLayout)findViewById(R.id.tableLayout);

        predicates = new ArrayList<>();

        //listView = (ListView)findViewById(R.id.predicatesListView);

        /*data = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, data);

        listView.setAdapter(adapter);*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addButton:
                String nextPredicate = predicateEditText.getText().toString();
                if (nextPredicate == null || nextPredicate.isEmpty()) {
                    return;
                }
                predicates.add(nextPredicate);
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
                predicateTextView.setText(nextPredicate);
                predicateTextView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                tr.addView(predicateTextView);
                tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                tableLayout.setVisibility(View.VISIBLE);
                predicateEditText.setText("");
                break;
            case R.id.nextButton:
                if (predicates.isEmpty()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(NO_PREDICATES_TITLE);
                    builder.setMessage(NO_PREDICATES_MESSAGE);

                    // Set up the buttons
                    builder.setNeutralButton(OK_BUTTON_TEXT, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                    return;
                }
                Intent checkPropertyIntent = new Intent(this, CheckPropertyActivity.class);
                record.getProjectDescriptor().setAtomicPredicates(predicates);
                checkPropertyIntent.putExtra(InterActivityConstants.RECORD_KEY, RecordEncoderDecoder.encode(record));
                startActivity(checkPropertyIntent);
                break;
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
}
