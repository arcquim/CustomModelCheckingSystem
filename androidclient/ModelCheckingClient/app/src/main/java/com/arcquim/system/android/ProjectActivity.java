package com.arcquim.system.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.arcquim.gwt.shared.ProjectDescriptor;
import com.arcquim.gwt.shared.ProjectHistoryDescriptor;
import com.arcquim.gwt.shared.Record;
import com.arcquim.gwt.shared.RecordEncoderDecoder;
import com.arcquim.system.android.orchestrator.Orchestrator;

import java.util.List;

public class ProjectActivity extends AppCompatActivity {

    private static final String ACTIVATE_ITEM_TEXT = "Activate";
    private static final String DEACTIVATE_ITEM_TEXT = "Deactivate";
    private String labelAboutActiviness;
    private boolean active;

    private TextView projectNameTextView;
    private String[] historyArray;
    private ListView historyListView;

    private Record record;
    private List<ProjectHistoryDescriptor> projectHistoryDescriptorList;
    private String projectName;
    private boolean resumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        record = Orchestrator.getInstance().get();

        setContentView(R.layout.activity_project);
        projectName = record.getProjectDescriptor().getTitle();
        active = record.getProjectDescriptor().getActive();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(projectName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        projectNameTextView = (TextView)findViewById(R.id.projectNameTextView);
        projectNameTextView.setText(R.string.historyLabelText);

        initListView();
    }

    private void initListView() {
        createHistoryData();
        historyListView = (ListView)findViewById(R.id.historyListView);

        //fill this adapter with data from the server
        ListAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, historyArray);

        historyListView.setAdapter(adapter);

        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //open HistoryItemActivity
                Intent historyItemIntent = new Intent(getApplicationContext(), HistoryItemActivity.class);
                Record detailedRecord = new Record();
                detailedRecord.setProjectDescriptor(record.getProjectDescriptor());
                detailedRecord.setProjectHistoryDescriptor(projectHistoryDescriptorList.get(position));
                historyItemIntent.putExtra(InterActivityConstants.PROJECT_KEY, RecordEncoderDecoder.encode(detailedRecord));
                startActivity(historyItemIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder;
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                break;

            case R.id.changeTitleMenuItem:
                //do something
                builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.enterNewNameText);
                // Set up the input
                final EditText input = new EditText(this);
                input.setText(projectName);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //validate if it's empty
                        String value = input.getText().toString();
                        if (!value.isEmpty()) {
                            projectName = value;
                            projectNameTextView.setText(projectName);
                            record.getProjectDescriptor().setTitle(projectName);
                            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                            toolbar.setTitle(projectName);
                            Orchestrator.getInstance().go(record, Orchestrator.DesiredActionType.EDIT_PROJECT);
                        };
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                break;
            case R.id.activateMenuItem:
                //do something
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure you want to " + labelAboutActiviness + " " + projectName + "?");

                // Set up the buttons
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        active = !active;
                        record.getProjectDescriptor().setActive(active);
                        Orchestrator.getInstance().go(record, Orchestrator.DesiredActionType.EDIT_PROJECT);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                break;
            case R.id.checkPropertyMenuItem:
                Intent checkProjectModelIntent = new Intent(this, CheckProjectModelActivity.class);
                checkProjectModelIntent.putExtra(InterActivityConstants.RECORD_KEY, RecordEncoderDecoder.encode(record));
                startActivity(checkProjectModelIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.project_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        setActivateMenuItemTitle(menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (resumed) {
            Orchestrator.getInstance().go(record, Orchestrator.DesiredActionType.VIEW_PROJECT);
            record = Orchestrator.getInstance().get();
            initListView();
        }
        else {
            resumed = true;
        }
    }

    private void createHistoryData() {
        ProjectDescriptor projectDescriptor = record.getProjectDescriptor();
        projectHistoryDescriptorList = projectDescriptor.getHistory();
        if (projectHistoryDescriptorList == null) {
            historyArray = new String[0];
        }
        historyArray = new String[projectHistoryDescriptorList.size()];
        for (int i = 0; i < projectHistoryDescriptorList.size(); i++) {
            historyArray[i] = projectHistoryDescriptorList.get(i).getResult();
        }
    }

    private void setActivateMenuItemTitle(Menu menu) {
        MenuItem activateMenuItem = menu.findItem(R.id.activateMenuItem);
        MenuItem checkMenuItem = menu.findItem(R.id.checkPropertyMenuItem);
        if (active) {
            labelAboutActiviness = DEACTIVATE_ITEM_TEXT.toLowerCase();
            activateMenuItem.setTitle(DEACTIVATE_ITEM_TEXT);
        }
        else {
            labelAboutActiviness = ACTIVATE_ITEM_TEXT.toLowerCase();
            activateMenuItem.setTitle(ACTIVATE_ITEM_TEXT);
        }
        checkMenuItem.setVisible(active);
    }
}
