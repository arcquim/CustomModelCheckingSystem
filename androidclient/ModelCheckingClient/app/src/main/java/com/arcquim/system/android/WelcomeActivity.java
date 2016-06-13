package com.arcquim.system.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.arcquim.system.android.orchestrator.Orchestrator;
import com.arcquim.gwt.shared.ClientDescriptor;
import com.arcquim.gwt.shared.Record;
import com.arcquim.system.android.utils.Validator;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String WRONG_EMAIL_TITLE = "Warning";
    private static final String WRONG_EMAIL_MESSAGE = "The email is not valid";
    private static final String OK_BUTTON_TEXT = "Ok";

    private Button enterButton;
    private EditText emailEditTextField;

    private String email;
    private boolean resumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        enterButton =(Button)findViewById(R.id.enterButton);
        enterButton.setOnClickListener(this);

        emailEditTextField = (EditText) findViewById(R.id.emailEditText);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.enterButton:
                email = emailEditTextField.getText().toString();
                if (Validator.getInstance().isEmailValid(email)) {
                    startProjectsActivity();
                }
                else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(WRONG_EMAIL_TITLE);
                    builder.setMessage(WRONG_EMAIL_MESSAGE);

                    // Set up the buttons
                    builder.setNeutralButton(OK_BUTTON_TEXT, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }

        }
    }

    private void startProjectsActivity() {
        Record record = new Record();
        ClientDescriptor clientDescriptor = new ClientDescriptor();
        clientDescriptor.setClientEmail(email);
        record.setClientDescriptor(clientDescriptor);
        Orchestrator.getInstance().go(record, Orchestrator.DesiredActionType.VIEW_PROJECTS);
        Intent projectsIntent = new Intent(this, ProjectsActivity.class);
        startActivity(projectsIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (resumed) {
            emailEditTextField.setText("");
        }
        else {
            resumed = true;
        }
    }
}
