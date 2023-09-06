package shop.ipwebshopadmin.beans;

import org.mindrot.jbcrypt.BCrypt;
import shop.ipwebshopadmin.dao.AdminDAO;
import shop.ipwebshopadmin.dto.Admin;

import java.io.Serializable;

public class AdminBean implements Serializable {
    public boolean isLoggedIn = false;
    public boolean login(String username,String password){
        Admin admin = AdminDAO.login(username);
        if(admin != null  &&  checkPassword(password,admin.getPassword())){
            isLoggedIn = true;
            return true;
        } else {
            return false;
        }
    }
    public boolean isLoggedIn(){
        return isLoggedIn;
    }

    private boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

}
