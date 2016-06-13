package com.arcquim.gwt.client.project;

import com.arcquim.gwt.client.ApplicationConfiguration;
import com.arcquim.gwt.client.ConfigurableComposite;
import com.arcquim.gwt.client.event.CreateProjectEvent;
import com.arcquim.gwt.client.event.GoBackEvent;
import com.arcquim.gwt.client.event.NewProjectAppearedEvent;
import com.arcquim.gwt.client.event.PrepareCheckProjectEvent;
import com.arcquim.gwt.client.event.ProjectEditedEvent;
import com.arcquim.gwt.client.event.ViewProjectHistoryEvent;
import com.arcquim.gwt.client.resources.RequiredResourcesBundle;
import com.arcquim.gwt.client.util.EventBusWrapper;
import com.arcquim.gwt.shared.ClientDescriptor;
import com.arcquim.gwt.shared.ProjectDescriptor;
import com.github.gwtbootstrap.client.ui.Alert;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class ProjectsCompositeWidget extends ConfigurableComposite {
    
    private static final String CREATION_BUTTON_TEXT = "Create Project";
    
    private static final String CREATE_POPUP_LABEL_TEXT = "Enter the name of new project";
    private static final String OK_POPUP_BUTTON_TEXT = "Create";
    private static final String CANCEL_POPUP_BUTTON_TEXT = "Cancel";
    
    private static final String TABLE_TITLE = "Projects of ";
    
    private static final String SYSTEM_TITLE = "Model Checking System";
    
    private static final String NO_PROJECTS_TEXT = "No projects found";
    private static final String TRY_TO_CREATE = "Create your first";
    
    private static final String BACK_TO_START_PAGE_TEXT = "Back to the start page";
    
    private static final String PROJECT_NOT_ACTIVE = "The project is not active";
    
    private VerticalPanel verticalPanel;
    private ProjectsTable projectsTable;
    private PushButton creationButton;
    private HTML systemTitle;
    private HTML backButton;
    
    private VerticalPanel emptyProjectsPanel;
    private HTML noProjectFound;
    private HTML createYourFirst;
    
    private List<ProjectDescriptor> projectDescriptors;
    private List<Row> tableRows;
    
    private static final String CREATION_BUTTON_STYLE = 
            RequiredResourcesBundle.INSTANCE.requiredCssResources().addButtonStyle();
    
    private static final String SYSTEM_LABEL_STYLE = 
            RequiredResourcesBundle.INSTANCE.requiredCssResources().systemLabelStyle();
    
    private static final String NO_PROJECTS_LABEL_STYLE = 
            RequiredResourcesBundle.INSTANCE.requiredCssResources().noProjectLabelStyle();
    
    private static final String CREATE_FIRST_PROJECT_LABEL_STYLE = 
            RequiredResourcesBundle.INSTANCE.requiredCssResources().createFirstProjectLabelStyle();
    
    private static final String NO_PROJECTS_PANEL_STYLE = 
            RequiredResourcesBundle.INSTANCE.requiredCssResources().emptyProjectsPanelStyle();
    
    private static final String BACK_BUTTON_STYLE = 
            RequiredResourcesBundle.INSTANCE.requiredCssResources().backButtonStyle();
    
    public static class Row {
        Long projectId;
        Integer rowId;
        String title;
        Boolean active;

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !obj.getClass().equals(Row.class)) {
                return false;
            }
            Row rowObj = (Row) obj;
            if (projectId == null || rowObj.projectId == null) {
                return false;
            }
            return projectId.equals(rowObj.projectId);
        }
    }

    @Override
    public String getTitle() {
        return ApplicationConfiguration.PROJECTS_PAGE;
    }

    @Override
    protected void initialize() {
        super.initialize();
        verticalPanel = new VerticalPanel();
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        initializeSystemTitle();
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_DEFAULT);
        initializeAuthorLabelAndCreationButton();
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        initializeCellTable();
        initWidget(verticalPanel);
    }
    
    private void initializeSystemTitle() {
        HorizontalPanel titleContainer = new HorizontalPanel();
        backButton = new HTML(new SafeHtmlBuilder().
                appendEscapedLines(BACK_TO_START_PAGE_TEXT).toSafeHtml());
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
    
    private void initializeAuthorLabelAndCreationButton() {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.add(new Label(TABLE_TITLE + currentRecord.getClientDescriptor().getClientEmail()));
        creationButton = new PushButton("");
        creationButton.setTitle(CREATION_BUTTON_TEXT);
        creationButton.setStyleName(CREATION_BUTTON_STYLE);
        creationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showCreationPopup();
            }
        });
        horizontalPanel.add(creationButton);
        verticalPanel.add(horizontalPanel);
    }
    
    protected void showCreationPopup() {
        final Modal dialogBox = new Modal();
        dialogBox.setTitle(CREATION_BUTTON_TEXT);
        dialogBox.setWidth("auto");
        VerticalPanel verticalPopupPanel = new VerticalPanel();
        verticalPopupPanel.add(new Label(CREATE_POPUP_LABEL_TEXT));
        final TextBox textBox = new TextBox();
        verticalPopupPanel.add(textBox);
        HorizontalPanel horizontalPopupPanel = new HorizontalPanel();
        final Button okButton = new Button();
        okButton.setText(OK_POPUP_BUTTON_TEXT);
        horizontalPopupPanel.add(okButton);
        final Button cancelButton = new Button();
        cancelButton.setText(CANCEL_POPUP_BUTTON_TEXT);
        horizontalPopupPanel.add(cancelButton);
        okButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                String title = textBox.getText();
                if (title != null && !title.isEmpty()) {
                    dialogBox.hide();
                    ProjectDescriptor projectDescriptor = new ProjectDescriptor();
                    projectDescriptor.setActive(Boolean.TRUE);
                    projectDescriptor.setTitle(title);
                    projectDescriptor.setClient(currentRecord.getClientDescriptor());
                    NewProjectAppearedEventHandlerImpl handlerImpl = new NewProjectAppearedEventHandlerImpl(ProjectsCompositeWidget.this);
                    handlerImpl.setHandlerRegistration(EventBusWrapper.getInstance()
                            .addHandler(NewProjectAppearedEvent.TYPE, handlerImpl));
                    EventBusWrapper.getInstance().fireEvent(
                            new CreateProjectEvent(projectDescriptor, currentRecord.getClientDescriptor()));
                    
                }
            }
        });
        cancelButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dialogBox.hide();
            }
        });
        verticalPopupPanel.add(horizontalPopupPanel);
        dialogBox.add(verticalPopupPanel);
        dialogBox.show();
    }
    
    public void addProject(Row row) {
        if (row != null) {
            row.rowId = tableRows == null ? 1 : tableRows.size() + 1;
            if (row.rowId == 1) {
                if (emptyProjectsPanel != null) {
                    emptyProjectsPanel.removeFromParent();
                }
                createTable();
            }
            projectsTable.addRow(row);
        }
    }
    
    private void initializeCellTable() {
        if (currentRecord == null) {
            return;
        }
        ClientDescriptor clientDescriptor = currentRecord.getClientDescriptor();
        if (clientDescriptor == null) {
            return;
        }
        projectDescriptors = clientDescriptor.getProjects();
        if (projectDescriptors != null && !projectDescriptors.isEmpty()) {
            createTable();
        }
        else {
            createEmptyProjectsPanel();
        }
    }
    
    private void createEmptyProjectsPanel() {
        emptyProjectsPanel = new VerticalPanel();
        emptyProjectsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        emptyProjectsPanel.setStyleName(NO_PROJECTS_PANEL_STYLE);
        noProjectFound = new HTML(new SafeHtmlBuilder().
                appendEscapedLines(NO_PROJECTS_TEXT).toSafeHtml());
        noProjectFound.setStyleName(NO_PROJECTS_LABEL_STYLE);
        createYourFirst = new HTML(new SafeHtmlBuilder().
                appendEscapedLines(TRY_TO_CREATE).toSafeHtml());
        createYourFirst.setStyleName(CREATE_FIRST_PROJECT_LABEL_STYLE);
        emptyProjectsPanel.add(noProjectFound);
        emptyProjectsPanel.add(createYourFirst);
        createYourFirst.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showCreationPopup();
            }
        });
        verticalPanel.add(emptyProjectsPanel);
    }
    
    public static class TitleRowStringChangeHandler implements ProjectsTable.RowStringChangeHandler {

        @Override
        public void commitChange(Row row, String newValue) {
            if (row == null || newValue == null || newValue.isEmpty()) {
                return;
            }
            Long projectId = row.projectId;
            ProjectDescriptor projectDescriptor = new ProjectDescriptor();
            projectDescriptor.setId(projectId);
            projectDescriptor.setTitle(newValue);
            projectDescriptor.setActive(row.active);
            row.title = newValue;
            EventBusWrapper.getInstance().fireEvent(new ProjectEditedEvent(projectDescriptor));
        }
        
    }
    
    public static class ActiveRowStringChangeHandler implements ProjectsTable.RowStringChangeHandler {

        @Override
        public void commitChange(Row row, String newValue) {
            if (row == null || newValue == null || newValue.isEmpty()) {
                return;
            }
            boolean isActiveNow = ProjectsTable.ACTIVE_PROJECT.equals(newValue);
            if (row.active ^ isActiveNow) {
                Long projectId = row.projectId;
                ProjectDescriptor projectDescriptor = new ProjectDescriptor();
                projectDescriptor.setId(projectId);
                projectDescriptor.setTitle(row.title);
                projectDescriptor.setActive(isActiveNow);
                row.active = isActiveNow;
                EventBusWrapper.getInstance().fireEvent(new ProjectEditedEvent(projectDescriptor));
            }
        }
        
    }
    
    public class CheckButtonClickedHandler implements ProjectsTable.RowButtonClickedHandler {

        @Override
        public void handle(Row row) {
            if (row == null || row.projectId == null || row.active == null) {
                return;
            }
            if (!row.active) {
                Alert alert = new Alert(PROJECT_NOT_ACTIVE, AlertType.WARNING, true);
                verticalPanel.insert(alert, 0);
                return;
            }
            ProjectDescriptor projectDescriptor = new ProjectDescriptor();
            projectDescriptor.setId(row.projectId);
            projectDescriptor.setTitle(row.title);
            projectDescriptor.setClient(currentRecord.getClientDescriptor());
            EventBusWrapper.getInstance().fireEvent(
                    new PrepareCheckProjectEvent(projectDescriptor, currentRecord.getClientDescriptor()));
        }
        
    }
    
    public static class ViewHistoryButtonClickedHandler implements ProjectsTable.RowButtonClickedHandler {

        @Override
        public void handle(Row row) {
            if (row == null || row.projectId == null) {
                return;
            }
            ProjectDescriptor projectDescriptor = new ProjectDescriptor();
            projectDescriptor.setId(row.projectId);
            projectDescriptor.setTitle(row.title);
            EventBusWrapper.getInstance().fireEvent(new ViewProjectHistoryEvent(projectDescriptor));
        }
        
    }
    
    private void createTable() {
        projectsTable = new ProjectsTable();
        projectsTable.createOrderColumn();
        projectsTable.createTitleColumn(new TitleRowStringChangeHandler());
        projectsTable.createStatusColumn(new ActiveRowStringChangeHandler());
        projectsTable.createRunCheckColumn(new CheckButtonClickedHandler());
        projectsTable.createViewHistoryColumn(new ViewHistoryButtonClickedHandler());
        projectsTable.setTitle(TABLE_TITLE + currentRecord.getClientDescriptor().getClientEmail());
        projectsTable.setData(buildFromProjectDescriptors());
        projectsTable.setSize("100%", "100%");
        verticalPanel.add(projectsTable);
    }
    
    private List<Row> buildFromProjectDescriptors() {
        tableRows = new ArrayList<>();
        int counter = 1;
        if (projectDescriptors != null) {
            for (ProjectDescriptor projectDescriptor : projectDescriptors) {
                Row row = new Row();
                row.rowId = counter++;
                row.projectId = projectDescriptor.getId();
                row.active = projectDescriptor.getActive();
                row.title = projectDescriptor.getTitle();
                tableRows.add(row);
            }
        }
        return tableRows;
    }
    
}
