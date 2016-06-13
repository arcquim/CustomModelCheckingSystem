package com.arcquim.gwt.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public interface RequiredResourcesBundle extends ClientBundle {
    
    public static final RequiredResourcesBundle INSTANCE = GWT.create(RequiredResourcesBundle.class);
    
    @Source("required.css")
    RequiredCssResources requiredCssResources();
    
}
