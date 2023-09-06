package shop.ipwebshopadmin.dao;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import shop.ipwebshopadmin.dto.Avatar;
import shop.ipwebshopadmin.dto.Category;
import shop.ipwebshopadmin.dto.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserDAO {
    private static final ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final String SQL_SELECT_ALL_USERS = "SELECT * FROM user WHERE role = ?";
    private static final String SQL_GET_AVATARS = "SELECT * FROM avatar";
    private static final String SQL_SELECT_USER = "SELECT * FROM user u WHERE u.id=?";
    private static final String SQL_UPDATE_USER = "UPDATE user u SET first_name=?, last_name=?, username=?, password=?, location=? ,email=?,account_confirmed=?, avatar_id=? WHERE u.id=?";
    private static final String SQL_DEACTIVATE_USER = "UPDATE user u SET account_confirmed = false WHERE u.id=?";
    private static final String SQL_ADD_USER = "INSERT INTO user (first_name, last_name, username, password,location, email ,role,avatar_id) VALUES(?, ?, ?, ?, ?, ?, ?,?)";
    private static final String SQL_USERNAME_EXISTS = "SELECT COUNT(*) FROM user WHERE username = ?";

    public static ArrayList<User> getAll() {
        ArrayList<User> users = new ArrayList<>();
        Connection connection = null;
        ResultSet rs;
        Object[] values = {"USER"};
        try {
            connection = connectionPool.checkOut();
            PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_SELECT_ALL_USERS, false, values);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getInt(1),rs.getString(2),rs.getString(3),
                        rs.getString(4),rs.getString(5), rs.getString(6),rs.getString(7),
                        rs.getString(8),rs.getBoolean(9),rs.getString(10),rs.getInt(11)));
            }
            pstmt.close();
        } catch (SQLException exp) {
            exp.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }
        return users;
    }
    public static ArrayList<Avatar> getAvatars() {
        ArrayList<Avatar> avatars = new ArrayList<>();
        Connection connection = null;
        ResultSet rs;
        Object[] values = {};
        try {
            connection = connectionPool.checkOut();
            PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_GET_AVATARS, false, values);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                avatars.add(new Avatar(rs.getInt(1),rs.getString(2)));
            }
            pstmt.close();
        } catch (SQLException exp) {
            exp.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }
        return avatars;
    }

    public static boolean addUser(User user) {
        Connection connection = null;
        boolean result = false;
        if(usernameExists(user.getUsername())){
            return false;
        }
        try {
            connection = connectionPool.checkOut();
            PreparedStatement preparedStatement = DAOUtil.prepareStatement(connection, SQL_ADD_USER, false);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, passwordEncoder.encode(user.getPassword()));
            preparedStatement.setString(5, user.getLocation());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getRole());
            preparedStatement.setInt(8, user.getAvatarId());
            result = preparedStatement.executeUpdate() == 1;
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }
        return result;
    }

    public static User getById(int id) {
        Connection connection = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            connection = connectionPool.checkOut();
            PreparedStatement preparedStatement = DAOUtil.prepareStatement(connection, SQL_SELECT_USER, false);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getInt("id"), resultSet.getString("first_name"),
                        resultSet.getString("last_name"), resultSet.getString("username"),
                        resultSet.getString("password"), resultSet.getString("location"),
                        resultSet.getString("email"), resultSet.getString("role"),
                        resultSet.getBoolean("account_confirmed"), resultSet.getString("pin"), resultSet.getInt("avatar_id"));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }
        return user;
    }
    public static boolean usernameExists(String username) {
        Connection connection = null;
        ResultSet resultSet = null;
        boolean exists = false;

        try {
            connection = connectionPool.checkOut();
            PreparedStatement preparedStatement = DAOUtil.prepareStatement(connection, SQL_USERNAME_EXISTS, false);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                exists = (count > 0);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }
        return exists;
    }

    public static void deactivateUserAccount(Integer id) {
        Connection connection = null;

        try {
            connection = connectionPool.checkOut();
            PreparedStatement preparedStatement = DAOUtil.prepareStatement(connection, SQL_DEACTIVATE_USER, false);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }
    }

    public static boolean updateUser(User user) {
        Connection connection = null;
        boolean result = false;
        try {
            connection = connectionPool.checkOut();
            PreparedStatement preparedStatement = DAOUtil.prepareStatement(connection, SQL_UPDATE_USER, false);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, passwordEncoder.encode(user.getPassword()));
            preparedStatement.setString(5, user.getLocation());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setBoolean(7, user.getAccountConfirmed());
            preparedStatement.setInt(8, user.getAvatarId());
            preparedStatement.setInt(9, user.getId());
            result = preparedStatement.executeUpdate() == 1;
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }
        return result;
    }
}
