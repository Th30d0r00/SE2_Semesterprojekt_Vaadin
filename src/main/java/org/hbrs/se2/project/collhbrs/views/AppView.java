package org.hbrs.se2.project.collhbrs.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.*;
import org.hbrs.se2.project.collhbrs.control.AuthorizationControl;
import org.hbrs.se2.project.collhbrs.dtos.UserDTO;
import org.hbrs.se2.project.collhbrs.util.AccountType;
import org.hbrs.se2.project.collhbrs.util.Globals;
import org.hbrs.se2.project.collhbrs.util.Utils;
import java.util.Optional;

/*
 * Haupt-View der Anwendung. Diese View wird nach dem Login angezeigt und enthält die Navigationsleiste.
 */

@CssImport("./styles/views/main/main-view.css")
@JsModule("./styles/shared-styles.js")
@Uses(TextArea.class)
@PageTitle("HelloCar")
@Route(Globals.Pages.MAIN_VIEW)
public class AppView extends AppLayout implements BeforeEnterObserver {

    private Tabs menu;
    private H1 viewTitle;
    private H1 helloUser;
    private AuthorizationControl authorizationControl;

    public AppView() {
        if (getCurrentUser() == null) {
            System.out.println("LOG: In Constructor of App View - No User given!");
        } else {
            setUpUI();
        }
    }

    public void setUpUI() {
        // Anzeige des Toggles über den Drawer
        setPrimarySection(Section.DRAWER);

        // Erstellung der horizontalen Statusleiste (Header)
        addToNavbar(true, createHeaderContent());

        // Erstellung der vertikalen Navigationsleiste (Drawer)
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private boolean checkIfUserIsLoggedIn() {
        // Falls der Benutzer nicht eingeloggt ist, dann wird er auf die Startseite gelenkt
        UserDTO userDTO = this.getCurrentUser();
        if (userDTO == null) {
            UI.getCurrent().navigate(Globals.Pages.LOGIN_VIEW);
            return false;
        }
        return true;
    }

    private Component createHeaderContent() {
        // Ein paar Grund-Einstellungen. Alles wird in ein horizontales Layout gesteckt.
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);

        // Hinzufügen des Toggles ('Big Mac') zum Ein- und Ausschalten des Drawers
        layout.add(new DrawerToggle());
        viewTitle = new H1();
        viewTitle.setWidthFull();
        layout.add(viewTitle);

        // Interner Layout
        HorizontalLayout topRightPanel = new HorizontalLayout();
        topRightPanel.setWidthFull();
        topRightPanel.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        topRightPanel.setAlignItems(FlexComponent.Alignment.CENTER);

        // Der Name des Users wird später reingesetzt, falls die Navigation stattfindet
        helloUser = new H1();
        topRightPanel.add(helloUser);

        // Logout-Button am rechts-oberen Rand.
        MenuBar bar = new MenuBar();
        MenuItem item = bar.addItem("Logout", e -> logoutUser());

        //Button: Profil bearbeiten
        MenuItem item_editProfil = bar.addItem("Profil bearbeiten", e -> editProfile());
        topRightPanel.add(bar);

        layout.add(topRightPanel);
        return layout;
    }

    private void editProfile() {
        UI.getCurrent().navigate(EditProfileView.class);
    }

    private void logoutUser() {
        UI ui = this.getUI().get();
        ui.getSession().close();
        ui.getPage().setLocation("/");
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);

        HorizontalLayout logoLayout = new HorizontalLayout();

        // Hinzufügen des Logos
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/freshconnect.png", "freshconnect logo"));
        //logoLayout.add(new H1("FreshConnect"));

        // Hinzufügen des Menus inklusive der Tabs
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        // Anlegen der Grundstruktur
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");

        // Anlegen der einzelnen Menuitems
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component[] createMenuItems() {
        // Abholung der Referenz auf den Authorisierungs-Service
        authorizationControl = new AuthorizationControl();

        // Jeder User sollte Autos sehen können, von daher wird dieser schon mal erzeugt und
        // und dem Tabs-Array hinzugefügt. In der Methode createTab wird ein (Key, Value)-Pair übergeben:
        // Key: der sichtbare String des Menu-Items
        // Value: Die UI-Component, die nach dem Klick auf das Menuitem angezeigt wird.
        // Tab[] tabs = new Tab[]{createTab("Show ", ShowCarsView.class)};
        Tab[] tabs = new Tab[]{};

        // Falls er Admin-Rechte hat, sollte der User auch Autos hinzufügen können
        // (Alternative: Verwendung der Methode 'isUserisAllowedToAccessThisFeature')
        if (this.authorizationControl.isUserInAccountType(this.getCurrentUser(), AccountType.UNTERNEHMEN)) {
            System.out.println("User is Unternehmen!");
            tabs = Utils.append(tabs, createTab("Eingegangene Bewerbungen", ShowApplicationsView.class));
            tabs = Utils.append(tabs, createTab("Meine Stellenanzeigen", ShowMyJobPostingsView.class));
            tabs = Utils.append(tabs, createTab("Stellenanzeige erstellen", EnterJobPostingView.class));
        } else if (this.authorizationControl.isUserInAccountType(this.getCurrentUser(), AccountType.STUDENT)) {
            System.out.println("User is Student!");
             tabs = Utils.append(tabs, createTab("Verfügbare Stellenanzeigen", ShowJobPostingsView.class));
             tabs = Utils.append(tabs, createTab("Unternehmen finden", ShowCompaniesView.class));
             tabs = Utils.append(tabs, createTab("Meine Bewerbungen", ShowMyApplications.class));
             //tabs = Utils.append(tabs, createTab("Enter Application", EnterApplicationView.class)); //Bewerbung nicht als Menütab
        }
        return tabs;
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        // Falls der Benutzer nicht eingeloggt ist, dann wird er auf die Startseite gelenkt
        if (!checkIfUserIsLoggedIn()) return;

        // Der aktuell-selektierte Tab wird gehighlighted.
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);

        // Setzen des aktuellen Names des Tabs
        viewTitle.setText(getCurrentPageTitle());

        // Setzen des Vornamens von dem aktuell eingeloggten Benutzer
        // Stringoperation: Nach @ String abschneiden, .,-_ ersetzen durch Leerzeichen
        helloUser.setText("Hello my dear old friend!! Wie geht's dir, " + this.getCurrentEmailOfUser().substring(0, this.getCurrentEmailOfUser().indexOf('@')));

    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    private String getCurrentEmailOfUser() {
        return getCurrentUser().getEmail();
    }

    private UserDTO getCurrentUser() {
        return (UserDTO) UI.getCurrent().getSession().getAttribute(Globals.CURRENT_USER);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (getCurrentUser() == null) {
            beforeEnterEvent.rerouteTo(Globals.Pages.LOGIN_VIEW);
        }
    }
}
