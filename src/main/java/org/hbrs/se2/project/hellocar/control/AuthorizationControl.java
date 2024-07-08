package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.util.AccountType;

// @Component
public class AuthorizationControl {

    /**
     * Methode zur Bestimmung, ob ein User ein bestimmtes Feature (hier: eine View) sehen darf
     * @param user
     * @param accountType
     * @return boolean
     */
    public boolean isUserInAccountType(UserDTO user, AccountType accountType) {
        if (user == null || accountType == null) {
            return false;
        }
        return user.getAccountType().equals(accountType);
    }
}
