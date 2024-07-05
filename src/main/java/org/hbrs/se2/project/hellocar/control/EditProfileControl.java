package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.dao.CompanyDAO;
import org.hbrs.se2.project.hellocar.dao.StudentDAO;
import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.CompanyDTO;
import org.hbrs.se2.project.hellocar.dtos.StudentDTO;
import org.hbrs.se2.project.hellocar.services.db.exceptions.DatabaseLayerException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;

@Service
public class EditProfileControl {
    private final UserDAO userDAO;
    private final StudentDAO studentDAO;
    private final CompanyDAO companyDAO;

    public EditProfileControl() {
        userDAO = new UserDAO();
        studentDAO = new StudentDAO();
        companyDAO = new CompanyDAO();
    }

    public boolean updateStudentProfile(int studentId, String newFirstName, String newLastname,
                                     LocalDate newBirthday, int newFachsemester) throws DatabaseLayerException {
        return studentDAO.updateStudentProfileInDB(studentId, newFirstName, newLastname,
                newBirthday, newFachsemester);
    }

    public boolean updateCompanyProfile(int companyId, String newCompanyName, LocalDate newFoundingDate, int newEmployees,
                                     String newLocations, String newDescription) throws DatabaseLayerException {
        return companyDAO.updateCompanyProfileInDB(companyId, newCompanyName, newFoundingDate, newEmployees,
                newLocations, newDescription);
    }

    public boolean deleteUserProfile(int userId) {
        return userDAO.deleteUserProfile(userId);
    }

    public StudentDTO getCurrentStudent(int id) throws SQLException, DatabaseLayerException {
        return studentDAO.getStudentById(id);
    }

    public CompanyDTO getCurrentCompany(int id) throws SQLException, DatabaseLayerException {
        return companyDAO.getCompanyById(id);
    }
}
