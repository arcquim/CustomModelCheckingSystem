package com.arcquim.system.android.orchestrator;

import com.arcquim.gwt.shared.Record;
import com.arcquim.system.android.transport.Requester;
import com.arcquim.system.android.transport.ResponseReceivedHandler;

public class Orchestrator implements ResponseReceivedHandler {

    private static final Orchestrator INSTANCE = new Orchestrator();

    public enum DesiredActionType {
        CREATE_PROJECT,
        VIEW_PROJECTS,
        VIEW_PROJECT,
        EDIT_PROJECT,
        CHECK_PROJECT
    }

    public static Orchestrator getInstance() {
        return INSTANCE;
    }

    private Record record;
    private RequesterFactory requesterFactory;

    private Orchestrator() {
        requesterFactory = new RequesterFactory();
    }

    public Record get() {
        return record;
    }

    public void go(Record sourceRecord, DesiredActionType desiredActionType) {
        if (sourceRecord == null || desiredActionType == null) {
            return;
        }
        Requester requester = requesterFactory.getRequester(desiredActionType, this);
        if (requester.getRequestMethod() == Requester.RequestMethod.GET) {
            requester.setQueryParametersString(sourceRecord.getProjectDescriptor().getId().toString());
        }
        requester.sendRequest(sourceRecord);
    }

    @Override
    public void onSuccess(Record record) {
        this.record = record;
    }

    @Override
    public void onFailure(Record record) {
        this.record = record;
    }

}
