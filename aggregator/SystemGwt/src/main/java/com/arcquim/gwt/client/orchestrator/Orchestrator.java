package com.arcquim.gwt.client.orchestrator;

import com.arcquim.gwt.client.ApplicationConfiguration;
import com.arcquim.gwt.client.FlowControl;
import com.arcquim.gwt.client.event.CheckProjectEvent;
import com.arcquim.gwt.client.event.CreateProjectEvent;
import com.arcquim.gwt.client.event.GoBackEvent;
import com.arcquim.gwt.client.event.NewProjectAppearedEvent;
import com.arcquim.gwt.client.event.PrepareCheckProjectEvent;
import com.arcquim.gwt.client.event.ProjectCheckingEvent;
import com.arcquim.gwt.client.event.ProjectEditedEvent;
import com.arcquim.gwt.client.event.SystemEnteredEvent;
import com.arcquim.gwt.client.event.ViewProjectHistoryEvent;
import com.arcquim.gwt.client.transport.CheckProjectRequester;
import com.arcquim.gwt.client.transport.CreateProjectRequester;
import com.arcquim.gwt.client.transport.EditProjectRequester;
import com.arcquim.gwt.client.transport.EnterSystemRequester;
import com.arcquim.gwt.client.transport.GetProjectHistoryRequester;
import com.arcquim.gwt.client.util.EventBusWrapper;
import com.arcquim.gwt.shared.ClientDescriptor;
import com.arcquim.gwt.shared.ProjectDescriptor;
import com.arcquim.gwt.shared.Record;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class Orchestrator {
    
    private static final Orchestrator INSTANCE = new Orchestrator();
    
    private Orchestrator() {
        EventBusWrapper.getInstance().addHandler(SystemEnteredEvent.TYPE, 
                new SystemEnteredEventHandlerImpl(new EnterSystemRequester()));
        EventBusWrapper.getInstance().addHandler(ProjectEditedEvent.TYPE, 
                new ProjectEditedEventHandlerImpl(new EditProjectRequester()));
        EventBusWrapper.getInstance().addHandler(PrepareCheckProjectEvent.TYPE, 
                new PrepareCheckProjectEventHandlerImpl());
        EventBusWrapper.getInstance().addHandler(CheckProjectEvent.TYPE, 
                new CheckProjectEventHandlerImpl(new CheckProjectRequester()));
        EventBusWrapper.getInstance().addHandler(ViewProjectHistoryEvent.TYPE, 
                new ViewProjectHistoryEventHandlerImpl(new GetProjectHistoryRequester()));
        EventBusWrapper.getInstance().addHandler(CreateProjectEvent.TYPE, 
                new CreateProjectEventHandlerImpl(new CreateProjectRequester()));
        EventBusWrapper.getInstance().addHandler(GoBackEvent.TYPE, 
                new GoBackEventHandlerImpl());
    }
    
    public static Orchestrator getInstance() {
        return INSTANCE;
    }
    
    public void ensureInjected() {
        
    }
    
    public void doStart(Record record) {
        FlowControl.getInstance().go(ApplicationConfiguration.WELCOME_PAGE, record);
    }
    
    public void doEnterSystem(Record record) {
        if (record != null) {
            ClientDescriptor clientDescriptor = record.getClientDescriptor();
            if (clientDescriptor != null) {
                FlowControl.getInstance().go(ApplicationConfiguration.PROJECTS_PAGE, record);
            }
        }
    }
    
    public void doModelChecking(Record record) {
        EventBusWrapper.getInstance().fireEvent(new ProjectCheckingEvent(record));
    }
    
    public void doPrepareModelChecking(Record record) {
        FlowControl.getInstance().go(ApplicationConfiguration.CHECKING_PAGE, record);
    }
    
    public void doViewProjectHistory(Record record) {
        if (record != null) {
            FlowControl.getInstance().go(ApplicationConfiguration.HISTORY_PAGE, record);
        }
    }
    
    public void doCreateProject(Record record) {
        ProjectDescriptor projectDescriptor = null;
        if (record != null && record.getProjectDescriptor() != null) {
            projectDescriptor = record.getProjectDescriptor();
        }
        EventBusWrapper.getInstance().fireEvent(new NewProjectAppearedEvent(projectDescriptor));
    }
    
    public void doNotCreateProject(Record record) {
        EventBusWrapper.getInstance().fireEvent(null);
    }
    
}
