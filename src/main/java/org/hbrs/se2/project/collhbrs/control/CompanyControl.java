package org.hbrs.se2.project.collhbrs.control;

import org.hbrs.se2.project.collhbrs.dao.CompanyDAO;
import org.hbrs.se2.project.collhbrs.dtos.CompanyDTO;
import org.hbrs.se2.project.collhbrs.services.db.exceptions.DatabaseLayerException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Implementierung der Control-Klasse CompanyControl
 * Hier sollen alle Methoden implementiert werden, die für die Steuerung einer Company zuständig sind
 * Dazu gehören u.a. Methoden zum Anzeigen von Anzeigen, zum Erstellen und zum Löschen einer Company
 */

public class CompanyControl {
    private CompanyDAO companyDAO;

    public CompanyControl() {
        this.companyDAO = new CompanyDAO();
    }

    public CompanyDTO findCompany(int companyId) throws DatabaseLayerException, SQLException {
        // Anzeigen einer Company anhand der ID
        return companyDAO.getCompanyById(companyId);
    }

    public List<CompanyDTO> readAllCompanies() throws DatabaseLayerException{
        return companyDAO.getAllCompanies();

    }

    public boolean updateCompanyProfile(int companyId, String newCompanyName, LocalDate newFoundingDate, int newEmployees,
                                        String newLocations, String newDescription) throws DatabaseLayerException {
        return companyDAO.updateCompanyProfileInDB(companyId, newCompanyName, newFoundingDate, newEmployees,
                newLocations, newDescription);
    }
}
