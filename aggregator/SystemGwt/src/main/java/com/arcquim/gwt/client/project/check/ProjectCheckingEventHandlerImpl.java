package com.arcquim.gwt.client.project.check;

import com.arcquim.gwt.client.event.ProjectCheckingEvent;
import com.arcquim.gwt.client.event.handler.ProjectCheckingEventHandler;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class ProjectCheckingEventHandlerImpl implements ProjectCheckingEventHandler {
    
    private final ProjectCheckCompositeWidget widget;

    public ProjectCheckingEventHandlerImpl(ProjectCheckCompositeWidget widget) {
        this.widget = widget;
    }

    @Override
    public void handleProjectCheckingEvent(ProjectCheckingEvent event) {
        widget.showCheckResultMessage(event.getRecord());
    }
    
}
