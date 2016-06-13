package com.arcquim.gwt.client.project.check;

import com.arcquim.gwt.client.ApplicationConfiguration;
import com.arcquim.gwt.client.ConfigurableComposite;
import com.arcquim.gwt.client.event.CheckProjectEvent;
import com.arcquim.gwt.client.event.GoBackEvent;
import com.arcquim.gwt.client.event.ProjectCheckingEvent;
import com.arcquim.gwt.client.event.handler.ProjectCheckingEventHandler;
import com.arcquim.gwt.client.resources.RequiredResourcesBundle;
import com.arcquim.gwt.client.util.EventBusWrapper;
import com.arcquim.gwt.shared.ProjectDescriptor;
import com.arcquim.gwt.shared.Record;
import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.event.shared.HandlerRegistration;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class ProjectCheckCompositeWidget extends ConfigurableComposite {
    
    private static final String SYSTEM_TITLE = "Model Checking System";
    
    private static final String BACK_TO_PROJECTS_PAGE_TEXT = "Back to the projects page";
    
    private static final String PREDICATE_LABEL = "Add predicate";
    private static final String PREDICATE_BUTTON_TEXT = "Add";
    private static final String PREDICATE_TABLE_TITLE = "Existing predicates";
    
    private static final String PROPERTY_LABEL = "Property";
    private static final String MODEL_LABEL = "Model";
    
    private static final String SUBMIT_LABEL = "Run checking";
    
    private static final String WRONG_PREDICATE_MESSAGE = "A predicate cannot be empty";
    private static final String NO_PREDICATES_MESSAGE = "Please specify predicates";
    private static final String NO_MODEL_MESSAGE = "Please specify model";
    private static final String NO_PROPERTY_MESSAGE = "Please specify property";
    private static final String MODEL_IS_CHECKING = "The property is checking on the model. The result will be sent to ";
    private static final String USER_BUSY = "Please wait; another property is verifying by ";
    
    private static final int DEFAULT_LINES_NUMBER = 20;
    
    private VerticalPanel contentVerticalPanel;
    private HorizontalPanel horizontalPanel;
    
    private VerticalPanel atomicPredicatesPanel;
    private AtomicPredicatesTable predicatesTable;
    private TextBox predicateTextBox;
    private Label predicateLabel;
    private Button predicateAddButton;
    
    private HTML systemTitle;
    private HTML backButton;
    
    private Label propertyLabel;
    private TextArea propertyTextArea;
    
    private Label modelLabel;
    private TextArea modelTextArea;
    
    private Button submitButton;
    
    private final List<Row> atomicPredicates = new ArrayList<>();
    
    private static final String SYSTEM_LABEL_STYLE = 
            RequiredResourcesBundle.INSTANCE.requiredCssResources().systemLabelStyle();
    
    private static final String CHECK_PROJECT_TITLE_LABEL_STYLE = 
            RequiredResourcesBundle.INSTANCE.requiredCssResources().checkProjectTitleLabelStyle();
    
    private static final String BACK_BUTTON_STYLE = 
            RequiredResourcesBundle.INSTANCE.requiredCssResources().backButtonStyle();
    
    public static class Row {
        Integer rowId;
        String predicate;

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !obj.getClass().equals(Row.class)) {
                return false;
            }
            Row rowObj = (Row) obj;
            if (rowObj.rowId == null || rowId == null) {
                return false;
            }
            return rowObj.rowId.equals(rowId);
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        contentVerticalPanel = new VerticalPanel();
        contentVerticalPanel.setSize("100%", "100%");
        horizontalPanel = new HorizontalPanel();
        horizontalPanel.setSize("100%", "100%");
        horizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        contentVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        createCheckingEventHandler();
        initializeSystemTitle();
        initializeAtomicPredicatesPanel();
        initializePropertyPanel();
        initializeModelPanel();
        initializeSubmitButton();
        Label projectTitleLabel = new Label(currentRecord.getProjectDescriptor().getTitle());
        projectTitleLabel.setStyleName(CHECK_PROJECT_TITLE_LABEL_STYLE, true);
        contentVerticalPanel.add(projectTitleLabel);
        contentVerticalPanel.add(horizontalPanel);
        contentVerticalPanel.add(submitButton);
        initWidget(contentVerticalPanel);
    }
    
    private HandlerRegistration handlerRegistration;
    
    private void createCheckingEventHandler() {
        ProjectCheckingEventHandler handler = new ProjectCheckingEventHandlerImpl(this);
        handlerRegistration = EventBusWrapper.getInstance().addHandler(ProjectCheckingEvent.TYPE, handler);
    }

    @Override
    protected void onUnload() {
        super.onUnload();
        handlerRegistration.removeHandler();
    }
    
    public void showCheckResultMessage(Record record) {
        Alert alert = record == null ? 
                new Alert(USER_BUSY + currentRecord.getClientDescriptor().getClientEmail(), 
                        AlertType.WARNING, true)
                : new Alert(MODEL_IS_CHECKING + currentRecord.getClientDescriptor().getClientEmail(), 
                        AlertType.SUCCESS, true);
        contentVerticalPanel.insert(alert, 0);
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
        contentVerticalPanel.add(titleContainer);
    }

    private void initializeAtomicPredicatesPanel() {
        atomicPredicatesPanel = new VerticalPanel();
        predicateLabel = new Label(PREDICATE_LABEL);
        predicateTextBox = new TextBox();
        predicateAddButton = new Button();
        predicateAddButton.setText(PREDICATE_BUTTON_TEXT);
        predicateAddButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                String newPredicate = predicateTextBox.getText();
                if (newPredicate != null && !newPredicate.isEmpty()) {
                    Row row = new Row();
                    row.predicate = newPredicate;
                    row.rowId = atomicPredicates.size();
                    predicateTextBox.setText("");
                    predicatesTable.addRow(row);
                }
                else {
                    Alert alert = new Alert(WRONG_PREDICATE_MESSAGE, AlertType.INFO, true);
                    contentVerticalPanel.insert(alert, 0);
                }
            }
        });
        atomicPredicatesPanel.add(predicateLabel);
        atomicPredicatesPanel.add(predicateTextBox);
        atomicPredicatesPanel.add(predicateAddButton);
        initializePredicatesTable();
        atomicPredicatesPanel.add(predicatesTable);
        horizontalPanel.add(atomicPredicatesPanel);
    }
    
    private void initializePredicatesTable() {
        predicatesTable = new AtomicPredicatesTable();
        predicatesTable.createOrderColumn();
        predicatesTable.createPredicateColumn();
        predicatesTable.setTitle(PREDICATE_TABLE_TITLE);
        predicatesTable.setData(atomicPredicates);
    }

    private void initializePropertyPanel() {
        propertyTextArea = new TextArea();
        propertyTextArea.setVisibleLines(DEFAULT_LINES_NUMBER);
        propertyTextArea.setWidth("100%");
        VerticalPanel propertyVerticalPanel = new VerticalPanel();
        propertyVerticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        propertyVerticalPanel.setSize("90%", "100%");
        propertyLabel = new Label(PROPERTY_LABEL);
        propertyVerticalPanel.add(propertyLabel);
        propertyVerticalPanel.add(propertyTextArea);
        horizontalPanel.add(propertyVerticalPanel);
    }

    private void initializeModelPanel() {
        modelTextArea = new TextArea();
        modelTextArea.setVisibleLines(DEFAULT_LINES_NUMBER);
        modelTextArea.setWidth("100%");
        VerticalPanel modelVerticalPanel = new VerticalPanel();
        modelVerticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        modelVerticalPanel.setSize("90%", "100%");
        modelLabel = new Label(MODEL_LABEL);
        modelVerticalPanel.add(modelLabel);
        modelVerticalPanel.add(modelTextArea);
        horizontalPanel.add(modelVerticalPanel);
    }
    
    private void initializeSubmitButton() {
        submitButton = new Button();
        submitButton.setText(SUBMIT_LABEL);
        submitButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (atomicPredicates.isEmpty()) {
                    Alert alert = new Alert(NO_PREDICATES_MESSAGE, AlertType.WARNING, true);
                    contentVerticalPanel.insert(alert, 0);
                    return;
                }
                String property = propertyTextArea.getText();
                String model = modelTextArea.getText();
                if (property == null || property.isEmpty()) {
                    Alert alert = new Alert(NO_PROPERTY_MESSAGE, AlertType.WARNING, true);
                    contentVerticalPanel.insert(alert, 0);
                    return;
                }
                if (model == null || model.isEmpty()) {
                    Alert alert = new Alert(NO_MODEL_MESSAGE, AlertType.WARNING, true);
                    contentVerticalPanel.insert(alert, 0);
                    return;
                }
                ProjectDescriptor currentProjectDescriptor = currentRecord.getProjectDescriptor();
                ProjectDescriptor projectDescriptor = new ProjectDescriptor();
                projectDescriptor.setId(currentProjectDescriptor.getId());
                projectDescriptor.setProgram(model);
                projectDescriptor.setProperty(property);
                List<String> predicates = new ArrayList<>();
                for (Row row : atomicPredicates) {
                    predicates.add(row.predicate);
                }
                projectDescriptor.setAtomicPredicates(predicates);
                EventBusWrapper.getInstance().fireEvent(new CheckProjectEvent(projectDescriptor));
            }
        });
    }

    @Override
    public String getTitle() {
        return ApplicationConfiguration.CHECKING_PAGE;
    }
    
}
