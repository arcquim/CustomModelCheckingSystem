package com.arcquim.system.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.arcquim.gwt.shared.Record;
import com.arcquim.gwt.shared.RecordEncoderDecoder;

public class HistoryItemActivity extends AppCompatActivity {
    private TextView projectNameTextView;
    private TextView modelValueTextView;
    private TextView propertyValueTextView;
    private TextView resultValueTextView;
    private boolean resumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Record record = RecordEncoderDecoder.decode(getIntent().getStringExtra(InterActivityConstants.PROJECT_KEY));
        setContentView(R.layout.activity_history_item);

        String projectName = record.getProjectDescriptor().getTitle();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(projectName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        modelValueTextView = (TextView)findViewById(R.id.modelValueTextView);
        //replace it with real data
        modelValueTextView.setText(record.getProjectHistoryDescriptor().getListing());

        propertyValueTextView = (TextView)findViewById(R.id.propertyValueTextView);
        //replace it with real data
        propertyValueTextView.setText(record.getProjectHistoryDescriptor().getProperty());

        resultValueTextView = (TextView)findViewById(R.id.resultValueTextView);
        resultValueTextView.setText(record.getProjectHistoryDescriptor().getResult());
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
