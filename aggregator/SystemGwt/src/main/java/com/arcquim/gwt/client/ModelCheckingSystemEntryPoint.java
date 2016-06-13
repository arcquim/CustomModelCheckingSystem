package com.arcquim.gwt.client;

import com.arcquim.gwt.client.orchestrator.Orchestrator;
import com.arcquim.gwt.client.resources.RequiredResourcesBundle;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.History;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class ModelCheckingSystemEntryPoint implements EntryPoint {
    
    private static final String DEFAULT_ITEM = "";

    @Override
    public void onModuleLoad() {
        Orchestrator.getInstance().ensureInjected();
        RequiredResourcesBundle.INSTANCE.requiredCssResources().ensureInjected();
        Orchestrator.getInstance().doStart(null);
    }
    
}
