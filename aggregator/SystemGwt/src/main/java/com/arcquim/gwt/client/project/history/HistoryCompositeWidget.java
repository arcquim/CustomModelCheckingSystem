package com.arcquim.gwt.client.project.history;

import com.arcquim.gwt.client.ApplicationConfiguration;
import com.arcquim.gwt.client.ConfigurableComposite;
import com.arcquim.gwt.client.event.GoBackEvent;
import com.arcquim.gwt.client.resources.RequiredResourcesBundle;
import com.arcquim.gwt.client.util.EventBusWrapper;
import com.arcquim.gwt.shared.ProjectDescriptor;
import com.arcquim.gwt.shared.ProjectHistoryDescriptor;
import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class HistoryCompositeWidget extends ConfigurableComposite {
    
    private static final String SYSTEM_TITLE = "Model Checking System";
    
    private static final String NO_HISTORY_TEXT = "No history found";
    
    private static final String BACK_TO_PROJECTS_PAGE_TEXT = "Back to the projects page";
    
    private static final String PROJECT_LABEL_START = "History of the ";
    private static final String PROJECT_LABEL_END = " project";
    
    private HistoryTable historyTable;
    private Label historyLabel;
    private List<Row> tableRows;
    private List<ProjectHistoryDescriptor> historyDescriptors;
    
    private VerticalPanel verticalPanel;
    private HTML systemTitle;
    private HTML backButton;
    
    private VerticalPanel emptyHistoryPanel;
    private HTML noHistoryFound;
    
    private static final String SYSTEM_LABEL_STYLE = 
            RequiredResourcesBundle.INSTANCE.requiredCssResources().systemLabelStyle();
    
    private static final String NO_HISTORY_LABEL_STYLE = 
            RequiredResourcesBundle.INSTANCE.requiredCssResources().noProjectLabelStyle();
    
    private static final String NO_HISTORY_PANEL_STYLE = 
            RequiredResourcesBundle.INSTANCE.requiredCssResources().emptyProjectsPanelStyle();
    
    private static final String BACK_BUTTON_STYLE = 
            RequiredResourcesBundle.INSTANCE.requiredCssResources().backButtonStyle();
    
    public static class Row {
        Integer rowId;
        Long historyItemId;
        String property;
        String listing;
        String result;
    }

    @Override
    public String getTitle() {
        return ApplicationConfiguration.HISTORY_PAGE;
    }

    @Override
    protected void initialize() {
        super.initialize();
        verticalPanel = new VerticalPanel();
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        initializeSystemTitle();
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
        initializeHistoryLabel();
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        initializeHistoryTable();
        initWidget(verticalPanel);
    }
    
    private void initializeSystemTitle() {
        HorizontalPanel titleContainer = new HorizontalPanel();
        backButton = new HTML(new SafeHtmlBuilder().
                appendEscapedLines(BACK_TO_PROJECTS_PAGE_TEXT).toSafeHtml());
        backButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                EventBusWrapper.getInstance().fireEvent(new GoBackEvent(currentRecord, getTitle()));
            }
        });
        backButton.setStyleName(BACK_BUTTON_STYLE);
        systemTitle = new HTML(new SafeHtmlBuilder().
                appendEscapedLines(SYSTEM_TITLE).toSafeHtml());
        systemTitle.setStyleName(SYSTEM_LABEL_STYLE);
        titleContainer.setWidth("100%");
        titleContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        titleContainer.add(backButton);
        titleContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        titleContainer.add(systemTitle);
        verticalPanel.add(titleContainer);
    }
    
    private void initializeHistoryLabel() {
        historyLabel = new Label(PROJECT_LABEL_START + 
                currentRecord.getProjectDescriptor().getTitle()
                + PROJECT_LABEL_END);
        verticalPanel.add(historyLabel);
    }

    private void initializeHistoryTable() {
        if (currentRecord == null) {
            return;
        }
        ProjectDescriptor projectDescriptor = currentRecord.getProjectDescriptor();
        if (projectDescriptor == null) {
            return;
        }
        historyDescriptors = projectDescriptor.getHistory();
        if (historyDescriptors == null || historyDescriptors.isEmpty()) {
            createEmptyProjectsPanel();
        }
        else {
            createHistoryTable();
        }
    }
    
    private void createEmptyProjectsPanel() {
        emptyHistoryPanel = new VerticalPanel();
        emptyHistoryPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        emptyHistoryPanel.setStyleName(NO_HISTORY_PANEL_STYLE);
        noHistoryFound = new HTML(new SafeHtmlBuilder().
                appendEscapedLines(NO_HISTORY_TEXT).toSafeHtml());
        noHistoryFound.setStyleName(NO_HISTORY_LABEL_STYLE);
        emptyHistoryPanel.add(noHistoryFound);
        verticalPanel.add(emptyHistoryPanel);
    }
    
    private void createHistoryTable() {
        historyTable = new HistoryTable();
        historyTable.createOrderColumn();
        historyTable.createModelColumn();
        historyTable.createPropertyColumn();
        historyTable.createResultColumn();
        historyTable.setData(buildFromProjectHistoryDescriptors());
        historyTable.setTitle(currentRecord.getProjectDescriptor().getTitle());
        verticalPanel.add(historyTable);
    }
    
    private List<Row> buildFromProjectHistoryDescriptors() {
        tableRows = new ArrayList<>();
        int counter = 1;
        for (ProjectHistoryDescriptor descriptor : historyDescriptors) {
            Row row = new Row();
            row.historyItemId = descriptor.getId();
            row.listing = descriptor.getListing();
            row.property = descriptor.getProperty();
            row.result = descriptor.getResult();
            row.rowId = counter++;
            tableRows.add(row);
        }
        return tableRows;
    }
    
}
