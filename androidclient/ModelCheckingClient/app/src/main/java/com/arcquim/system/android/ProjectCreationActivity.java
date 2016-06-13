package com.arcquim.system.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.arcquim.gwt.shared.ProjectDescriptor;
import com.arcquim.gwt.shared.Record;
import com.arcquim.gwt.shared.RecordEncoderDecoder;
import com.arcquim.system.android.orchestrator.Orchestrator;

public class ProjectCreationActivity extends AppCompatActivity implements View.OnClickListener{

    private Record record;
    private EditText editText;
    private boolean resumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        record = RecordEncoderDecoder.decode(getIntent().getStringExtra(InterActivityConstants.RECORD_KEY));
        setContentView(R.layout.activity_project_creation);
        editText = (EditText) findViewById(R.id.projectTitleEditText);
        Button createButton = (Button) findViewById(R.id.createButton);
        createButton.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            case R.id.createButton:
                String title = editText.getText().toString();
                if (title != null && !title.isEmpty()) {
                    Record recordToCreate = new Record();
                    recordToCreate.setClientDescriptor(record.getClientDescriptor());
                    ProjectDescriptor projectDescriptor = new ProjectDescriptor();
                    projectDescriptor.setTitle(title);
                    recordToCreate.setProjectDescriptor(projectDescriptor);
                    Orchestrator.getInstance().go(recordToCreate, Orchestrator.DesiredActionType.CREATE_PROJECT);
                    super.onBackPressed();
                }
        }
    }
}

