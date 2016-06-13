package com.arcquim.gwt.client;

import com.arcquim.gwt.shared.Record;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class FlowControl {
    
    private static final FlowControl FLOW_CONTROL_INSTANCE = 
            new FlowControl(ApplicationConfiguration.getInstance());
    
    private final ApplicationConfiguration applicationConfiguration;
    
    private FlowControl(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }
    
    public static FlowControl getInstance() {
        return FLOW_CONTROL_INSTANCE;
    }
    
    public void go(ConfigurableComposite composite) {
        String applicationId = applicationConfiguration.getApplicationId();
        RootPanel.get(applicationId).clear();
        RootPanel.get(applicationId).add(composite);
        //History.newItem(composite.getTitle());
    }
    
    public void go(String token) {
        ConfigurableComposite composite= applicationConfiguration.getPage(token);
        if (composite != null) {
            go(composite);
        }
    }
    
    public void go(String token, Record record) {
        ConfigurableComposite composite= applicationConfiguration.getPage(token, record);
        if (composite != null) {
            go(composite);
        }
    }
    
}
