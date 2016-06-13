package com.arcquim.system.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.arcquim.gwt.shared.ClientDescriptor;
import com.arcquim.gwt.shared.ProjectDescriptor;
import com.arcquim.gwt.shared.Record;
import com.arcquim.gwt.shared.RecordEncoderDecoder;
import com.arcquim.system.android.orchestrator.Orchestrator;

import java.util.List;

public class ProjectsActivity extends AppCompatActivity {
    private ListView projectsListView;
    //get list of projects from the server instead of this
    private String[] projectsArray;
    private Record record;
    private List<ProjectDescriptor> projectDescriptorList;
    private boolean resumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createMenuItems();
        setContentView(R.layout.activity_projects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent projectCreationIntent = new Intent(getApplicationContext(), ProjectCreationActivity.class);
                projectCreationIntent.putExtra(InterActivityConstants.RECORD_KEY, RecordEncoderDecoder.encode(record));
                startActivity(projectCreationIntent);
            }
        });

        initListView();
    }

    private void initListView() {
        projectsListView = (ListView)findViewById(R.id.projectsListView);

        //fill this adapter with data from the server
        ListAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, projectsArray);

        projectsListView.setAdapter(adapter);

        projectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent projectIntent = new Intent(getApplicationContext(), ProjectActivity.class);
                Record projectRecord = new Record();
                projectRecord.setClientDescriptor(record.getClientDescriptor());
                projectRecord.setProjectDescriptor(projectDescriptorList.get(position));
                Orchestrator.getInstance().go(projectRecord, Orchestrator.DesiredActionType.VIEW_PROJECT);
                startActivity(projectIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createMenuItems() {
        record = Orchestrator.getInstance().get();
        if (record == null || record.getClientDescriptor() == null) {
            projectsArray = new String[0];
            return;
        }
        ClientDescriptor clientDescriptor = record.getClientDescriptor();
        projectDescriptorList = clientDescriptor.getProjects();
        if (projectDescriptorList == null) {
            projectsArray = new String[0];
            return;
        }
        projectsArray = new String[projectDescriptorList.size()];
        for (int i = 0; i < projectDescriptorList.size(); i++) {
            projectsArray[i] = projectDescriptorList.get(i).getTitle();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (resumed) {
            Orchestrator.getInstance().go(record, Orchestrator.DesiredActionType.VIEW_PROJECTS);
            record = Orchestrator.getInstance().get();
            createMenuItems();
            initListView();
        }
        else {
            resumed = true;
        }
    }
}
