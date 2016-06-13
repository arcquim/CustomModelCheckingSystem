package com.arcquim.gwt.client.resources;

import com.google.gwt.resources.client.CssResource;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public interface RequiredCssResources extends CssResource {
    
    @ClassName("welcome-label")
    String welcomeLabelStyle();
    
    @ClassName("main-panel")
    String mainPanelStyle();
    
    @ClassName("add-button")
    String addButtonStyle();
    
    @ClassName("top-system-label")
    String systemLabelStyle();
    
    @ClassName("add-button-empty")
    String addButtonEmptyStyle();
    
    @ClassName("empty-no-projects-label")
    String noProjectLabelStyle();
    
    @ClassName("empty-create-project-label")
    String createFirstProjectLabelStyle();
    
    @ClassName("empty-projects-panel")
    String emptyProjectsPanelStyle();
    
    @ClassName("back-to-previous-label")
    String backButtonStyle();
    
    @ClassName("check-project-title-label")
    String checkProjectTitleLabelStyle();
}
