package org.hbrs.se2.project.collhbrs.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLink;
import org.hbrs.se2.project.collhbrs.control.AuthorizationControl;
import org.hbrs.se2.project.collhbrs.control.LoginControl;
import org.hbrs.se2.project.collhbrs.dtos.UserDTO;
import org.hbrs.se2.project.collhbrs.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.collhbrs.util.AccountType;
import org.hbrs.se2.project.collhbrs.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * View zur Darstellung der Startseite. Diese zeigt dem Benutzer ein Login-Formular an.
 */

@Route(value = "" )
@RouteAlias(value = "login")
public class MainView extends VerticalLayout {

    @Autowired
    private LoginControl loginControl;

    public MainView() {
        setSizeFull();

        // Hinzufügen des Logos
        Image logo = new Image("images/freshconnect.png", "freshconnect logo");
        logo.setWidth("200px");
        add(logo);

        LoginForm component = new LoginForm();

        add(createTitle());

        component.addLoginListener(e -> {
            boolean isAuthenticated = false;
            try {
                isAuthenticated = loginControl.authenticateWithHash( e.getUsername(), e.getPassword());
            } catch (DatabaseLayerException databaseException) {
                Dialog dialog = new Dialog();
                dialog.add( new Text( databaseException.getMessage()) );
                dialog.setWidth("400px");
                dialog.setHeight("150px");
                dialog.open();
            }
            if (isAuthenticated) {
                grabAndSetUserIntoSession();
                navigateToMainPage();
            } else {
                component.setError(true);
            }
        });

        add(component);
        add(new RouterLink("Noch keinen Account?", RegistrationView.class));
        this.setAlignItems( Alignment.CENTER );
    }

    private void grabAndSetUserIntoSession() {
        UserDTO userDTO = loginControl.getCurrentUser();
        UI.getCurrent().getSession().setAttribute( Globals.CURRENT_USER, userDTO );
    }

    private void navigateToMainPage() {
        AuthorizationControl authorizationControl = new AuthorizationControl();
        UserDTO userDTO = loginControl.getCurrentUser();

        if (authorizationControl.isUserInAccountType(userDTO, AccountType.UNTERNEHMEN)) {
            UI.getCurrent().navigate(ShowApplicationsView.class);
        } else {
            UI.getCurrent().navigate(ShowJobPostingsView.class);
        }
    }

    private Component createTitle() {
        return new H3("Willkommen bei Coll@hbrs by FreshConnect! Bitte loggen Sie sich ein.");
    }
}
