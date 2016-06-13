package com.arcquim.system.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.arcquim.gwt.shared.Record;
import com.arcquim.gwt.shared.RecordEncoderDecoder;

public class CheckProjectModelActivity extends AppCompatActivity implements View.OnClickListener{
    private Button nextButton;

    private Record record;
    private EditText editText;
    private boolean resumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        record = RecordEncoderDecoder.decode(getIntent().getStringExtra(InterActivityConstants.RECORD_KEY));
        setContentView(R.layout.activity_check_project_model);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(record.getProjectDescriptor().getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText = (EditText) findViewById(R.id.modelEditText);
        nextButton = (Button)findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.nextButton:
                String model = editText.getText().toString();
                if (model != null && !model.isEmpty()) {
                    Intent addAtomicPredicateIntent = new Intent(this, AddAtomicPredicateActivity.class);
                    record.getProjectDescriptor().setProgram(model);
                    addAtomicPredicateIntent.putExtra(InterActivityConstants.RECORD_KEY, RecordEncoderDecoder.encode(record));
                    startActivity(addAtomicPredicateIntent);
                }
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
