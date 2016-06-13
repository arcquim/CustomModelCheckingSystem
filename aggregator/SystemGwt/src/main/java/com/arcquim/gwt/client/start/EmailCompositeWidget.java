package com.arcquim.gwt.client.start;

import com.arcquim.gwt.client.ApplicationConfiguration;
import com.arcquim.gwt.client.ConfigurableComposite;
import com.arcquim.gwt.client.event.SystemEnteredEvent;
import com.arcquim.gwt.client.resources.RequiredResourcesBundle;
import com.arcquim.gwt.client.util.EventBusWrapper;
import com.arcquim.gwt.client.util.Validator;
import com.arcquim.gwt.shared.ClientDescriptor;
import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 *
 * @author Alexander Erin <arcquim@gmail.com>
 */
public class EmailCompositeWidget extends ConfigurableComposite {
    
    private static final String EMAIL_LABEL_TEXT = "Please enter your e-mail";
    private static final String ENTER_SYSTEM_BUTTON_TEXT = "Enter the system";
    private static final String WELCOME_LABEL = "\u00A0"
            + "Model\nChecking System";
    
    private static final String WELCOME_LABEL_STYLE = 
            RequiredResourcesBundle.INSTANCE.requiredCssResources().welcomeLabelStyle();
    
    private static final String WRONG_EMAIL_MESSAGE = "The email you've entered is not correct";
    
    private Button enterSystemButton;
    private TextBox emailTextBox;
    private Label emailLabel;
    private HTML welcomeLabel;
    
    private final Validator validator = Validator.getInstance();
    
    private VerticalPanel verticalPanel;
    
    @Override
    protected void initialize() {
        super.initialize();
        verticalPanel = new VerticalPanel();
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        initializeWelcomeLabel();
        initializeEmailLabel();
        initializeEmailTextBox();
        initializeEnterSystemButton();
        initWidget(verticalPanel);
    }
    
    private void initializeEnterSystemButton() {
        enterSystemButton = new Button();
        enterSystemButton.setText(ENTER_SYSTEM_BUTTON_TEXT);
        enterSystemButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                String email = emailTextBox.getText();
                if (validator.isEmailValid(email)) {
                    ClientDescriptor clientDescriptor = new ClientDescriptor();
                    clientDescriptor.setClientEmail(email);
                    EventBusWrapper.getInstance().fireEvent(new SystemEnteredEvent(clientDescriptor));
                }
                else {
                    Alert alert = new Alert(WRONG_EMAIL_MESSAGE, AlertType.WARNING, true);
                    verticalPanel.insert(alert, 0);
                }
            }
        });
        verticalPanel.add(enterSystemButton);
    }
    
    private void initializeEmailTextBox() {
        emailTextBox = new TextBox();
        verticalPanel.add(emailTextBox);
    }
    
    private void initializeEmailLabel() {
        emailLabel = new Label(EMAIL_LABEL_TEXT);
        verticalPanel.add(emailLabel);
    }
    
    private void initializeWelcomeLabel() {
        welcomeLabel = new HTML(new SafeHtmlBuilder().
                appendEscapedLines(WELCOME_LABEL).toSafeHtml());
        welcomeLabel.setStyleName(WELCOME_LABEL_STYLE);
        verticalPanel.add(welcomeLabel);
    }

    @Override
    public String getTitle() {
        return ApplicationConfiguration.WELCOME_PAGE;
    }
    
}
