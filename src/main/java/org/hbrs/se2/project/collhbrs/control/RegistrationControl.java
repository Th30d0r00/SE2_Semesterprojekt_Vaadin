package org.hbrs.se2.project.collhbrs.control;

import org.hbrs.se2.project.collhbrs.util.RegistrationResult;
import org.hbrs.se2.project.collhbrs.dao.UserDAO;
import org.hbrs.se2.project.collhbrs.dtos.UserDTO;
import org.hbrs.se2.project.collhbrs.services.db.exceptions.DatabaseLayerException;

public class RegistrationControl
{
    public RegistrationResult registerUser(UserDTO userDTO){

        RegistrationResult result = new RegistrationResult();
        UserDAO userDAO = new UserDAO();

        //check if User with this Email already exists
        try{
            UserDTO existingUser = userDAO.findUserByEmail(userDTO.getEmail()); //handle DatabaseLayerException
            if(existingUser == null)
            {
                if(userDAO.addUser(userDTO)){
                    result.setSuccess(true);
                    result.setMessage("User successfully registered.");
                }
                else{
                    result.setSuccess(false);
                    result.setMessage("Couldn't register user.");
                }
            }
            else
            {
                result.setSuccess(false);
                result.setMessage("User with email " + userDTO.getEmail() + " already exists");
            }
        }
        catch(DatabaseLayerException e){
            e.printStackTrace();
        }
        return result;
    }

}
