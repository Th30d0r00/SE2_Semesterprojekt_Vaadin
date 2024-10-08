package org.hbrs.se2.project.collhbrs.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.hbrs.se2.project.collhbrs.control.CompanyControl;
import org.hbrs.se2.project.collhbrs.dtos.CompanyDTO;
import org.hbrs.se2.project.collhbrs.services.db.exceptions.DatabaseLayerException;
import org.hbrs.se2.project.collhbrs.util.Globals;

import java.util.List;

/*
 * Ansicht zur Darstellung aller Unternehmen, die für eine Initiativbewerbung in Frage kommen.
 */

@Route(value = Globals.Pages.SHOW_COMPANIES, layout = AppView.class)
@PageTitle(value = "Unternehmen finden")
public class ShowCompaniesView extends Div {

    private List<CompanyDTO> companiesList;
    private CompanyControl companyControl;

    public ShowCompaniesView() {
        try {
            companyControl = new CompanyControl();

            addClassName("showCompanies");

            // Auslesen aller abgespeicherten Unternehmen aus der DB (über das Control)
            companiesList = companyControl.readAllCompanies();

            // Titel über der Tabelle
            add(this.createTitle());

            // Hinzufügen der Tabelle (bei Vaadin: ein Grid)
            add(this.createGridTable());
        } catch (DatabaseLayerException e) {
            // Ausnahmebehandlung (Benachrichtigung anzeigen, Fehler protokollieren, etc.)
            Notification.show("Fehler beim Abrufen der Unternehmen: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private Component createTitle() {
        return new H3("Finde Unternehmen für eine Initiativbewerbung");
    }

    private Component createGridTable() {
        Grid<CompanyDTO> grid = new Grid<>();

        // Befüllen der Tabelle mit den zuvor ausgelesenen Unternehmen
        ListDataProvider<CompanyDTO> dataProvider = new ListDataProvider<>(companiesList);
        grid.setDataProvider(dataProvider);

        Grid.Column<CompanyDTO> nameColumn = grid.addColumn(CompanyDTO::getCompanyName).setHeader("Unternehmensname");
        Grid.Column<CompanyDTO> locationsColumn = grid.addColumn(CompanyDTO::getLocations).setHeader("Standorte");
        Grid.Column<CompanyDTO> foundingDateColumn = grid.addColumn(CompanyDTO::getFoundingDate).setHeader("Gründungsdatum");
        Grid.Column<CompanyDTO> employeesColumn = grid.addColumn(CompanyDTO::getEmployees).setHeader("Anzahl der Mitarbeiter");

        // Klick-Listener für Zeilen
        grid.addItemClickListener(event -> {
            CompanyDTO selectedCompany = event.getItem();
            System.out.println(selectedCompany.getId());
            // Weiterleitung zur Detailansicht des Unternehmens
            UI.getCurrent().navigate(CompanyDetailView.class, selectedCompany.getId());
        });

        HeaderRow filterRow = grid.appendHeaderRow();

        // Filter für Unternehmensname
        TextField nameField = new TextField();
        nameField.addValueChangeListener(event -> dataProvider.addFilter(
                company -> StringUtils.containsIgnoreCase(company.getCompanyName(), nameField.getValue())));
        nameField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(nameColumn).setComponent(nameField);
        nameField.setSizeFull();
        nameField.setPlaceholder("Filter");

        // Filter für Standort
        TextField standortField = new TextField();
        standortField.addValueChangeListener(event -> dataProvider.addFilter(
                company -> StringUtils.containsIgnoreCase(company.getLocations(), standortField.getValue())));
        standortField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(locationsColumn).setComponent(standortField);
        standortField.setSizeFull();
        standortField.setPlaceholder("Filter");

        // Filter nach Employees funktioniert aktuell nicht wegen Typumwandlung
        /*
        // Filter for employees
        TextField employeesField = new TextField();
        employeesField.addValueChangeListener(event -> dataProvider.addFilter(
                company -> StringUtils.containsIgnoreCase(Character.toString((char)company.getEmployees()), employeesField.getValue())));
        employeesField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(employeesColumn).setComponent(employeesField);
        employeesField.setSizeFull();
        employeesField.setPlaceholder("Filter");
        */


        return grid;
    }
}
