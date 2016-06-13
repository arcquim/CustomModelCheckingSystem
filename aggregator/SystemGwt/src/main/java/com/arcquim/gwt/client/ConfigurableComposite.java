package com.arcquim.gwt.client;

import com.arcquim.gwt.client.resources.RequiredResourcesBundle;
import com.arcquim.gwt.shared.Record;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public abstract class ConfigurableComposite extends Composite {
    
    protected Record currentRecord;
    
    private static final String MAIN_PANEL_STYLE = 
            RequiredResourcesBundle.INSTANCE.requiredCssResources().mainPanelStyle();
    
    public void setCurrentRecord(Record record) {
        this.currentRecord = record;
        initialize();
    }

    @Override
    protected void initWidget(Widget widget) {
        widget.setWidth("90%");
        widget.setStyleName(MAIN_PANEL_STYLE);
        super.initWidget(widget);
    }
    
    protected void initialize() {
        
    }
    
}
