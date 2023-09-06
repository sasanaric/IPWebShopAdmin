package shop.ipwebshopadmin.beans;

import shop.ipwebshopadmin.dao.UserDAO;
import shop.ipwebshopadmin.dto.Avatar;
import shop.ipwebshopadmin.dto.User;

import java.io.Serializable;
import java.util.ArrayList;

public class UserBean implements Serializable {
    private User user;
    public ArrayList<User> getAll(){
        return UserDAO.getAll();
    }
    public ArrayList<Avatar> getAvatars(){
        return UserDAO.getAvatars();
    }
    public boolean addUser(User user){
        return UserDAO.addUser(user);
    }
    public void setUser(User user){
        this.user = user;
    }
    public User getUser(){
        return user;
    }
    public User getById(Integer id){
        return UserDAO.getById(id);
    }
    public boolean updateUser(User updateUser){
        return UserDAO.updateUser(updateUser);
    }
    public void deactivateUser(Integer id){
        UserDAO.deactivateUserAccount(id);
    }
    public boolean usernameExists(String username){
        return UserDAO.usernameExists(username);
    }
}
