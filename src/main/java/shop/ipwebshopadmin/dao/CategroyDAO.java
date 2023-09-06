package shop.ipwebshopadmin.dao;

import shop.ipwebshopadmin.dto.Attribute;
import shop.ipwebshopadmin.dto.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CategroyDAO {
    private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
    private static final String SQL_SELECT_ALL = "SELECT * FROM category WHERE deleted = false";
    private static final String SQL_GET_CATEGORY = "SELECT * FROM category WHERE id = ?";
    private static final String SQL_GET_ATTRIBUTE = "SELECT * FROM attribute WHERE id = ?";
    private static final String SQL_GET_ATTRIBUTE_BY_CATEGORY = "SELECT * FROM attribute WHERE category_id = ? AND deleted = false";
    private static final String SQL_ADD_CATEGORY = "INSERT INTO category (name) VALUES (?)";
    private static final String SQL_ADD_ATTRIBUTE = "INSERT INTO attribute (name, category_id) VALUES (?,?)";
    private static final String SQL_DELETE_CATEGORY = "UPDATE category c SET deleted = true WHERE c.id=?";
    private static final String SQL_DELETE_ATTRIBUTE_BY_CATEGORY = "UPDATE attribute a SET deleted = true WHERE a.category_id=?";
    private static final String SQL_DELETE_PRODUCT = "UPDATE product p SET deleted = true WHERE p.category_id=?";
    private static final String SQL_CATEGORY_EXISTS = "SELECT COUNT(*) FROM category WHERE name = ? AND deleted = false";
    private static final String SQL_ATTRIBUTE_EXISTS = "SELECT COUNT(*) FROM attribute WHERE name = ? AND category_id = ? AND deleted = false";
    private static final String SQL_DELETE_ATTRIBUTE = "UPDATE attribute a SET deleted = true WHERE a.id=?";
    private static final String SQL_DELETE_PRODUCT_ATTRIBUTE_BY_ATTRIBUTE_ID = "UPDATE product_attribute pa SET deleted = true WHERE pa.attribute_id = ?";
    private static final String SQL_DELETE_PRODUCT_ATTRIBUTE_BY_CATEGORY_ID = "DELETE FROM product_attribute WHERE attribute_id IN (SELECT id FROM attribute WHERE category_id = ?)";
    private static final String SQL_UPDATE_CATEGORY = "UPDATE category SET name = ? WHERE id = ?";
    private static final String SQL_UPDATE_ATTRIBUTE = "UPDATE attribute SET name = ? WHERE id = ?";

    public static ArrayList<Category> getAll() {
        ArrayList<Category> categories = new ArrayList<>();
        Connection connection = null;
        ResultSet rs;
        Object[] values = {};
        try {
            connection = connectionPool.checkOut();
            PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_SELECT_ALL, false, values);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                categories.add(new Category(rs.getInt(1),rs.getString(2)));
            }
            pstmt.close();
        } catch (SQLException exp) {
            exp.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }
        return categories;
    }
    public static ArrayList<Attribute> getAttributesByCategory(Integer categoryId) {
        ArrayList<Attribute> attributes = new ArrayList<>();
        Connection connection = null;
        ResultSet rs;
        try {
            connection = connectionPool.checkOut();
            PreparedStatement pstmt = DAOUtil.prepareStatement(connection, SQL_GET_ATTRIBUTE_BY_CATEGORY, false);
            pstmt.setInt(1,categoryId);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                attributes.add(new Attribute(rs.getInt(1),rs.getString(2),rs.getInt(3)));
            }
            pstmt.close();
        } catch (SQLException exp) {
            exp.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }
        return attributes;
    }
    public static boolean categoryExists(String name) {
        Connection connection = null;
        ResultSet resultSet = null;
        boolean exists = false;

        try {
            connection = connectionPool.checkOut();
            PreparedStatement preparedStatement = DAOUtil.prepareStatement(connection, SQL_CATEGORY_EXISTS, false);
            preparedStatement.setString(1, name);
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
    public static Category getCategory(Integer id) {
        Connection connection = null;
        ResultSet resultSet = null;
        Category result = null;
        try {
            connection = connectionPool.checkOut();
            PreparedStatement preparedStatement = DAOUtil.prepareStatement(connection, SQL_GET_CATEGORY, false);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = new Category(resultSet.getInt(1), resultSet.getString(2));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }
        return result;
    }
    public static Attribute getAttribute(Integer id) {
        Connection connection = null;
        ResultSet resultSet = null;
        Attribute result = null;
        try {
            connection = connectionPool.checkOut();
            PreparedStatement preparedStatement = DAOUtil.prepareStatement(connection, SQL_GET_ATTRIBUTE, false);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                result = new Attribute(resultSet.getInt(1), resultSet.getString(2),resultSet.getInt(3));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }
        return result;
    }
    public static boolean attributeExists(String name, Integer categoryId) {
        Connection connection = null;
        ResultSet resultSet = null;
        boolean exists = false;

        try {
            connection = connectionPool.checkOut();
            PreparedStatement preparedStatement = DAOUtil.prepareStatement(connection, SQL_ATTRIBUTE_EXISTS, false);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, categoryId);
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
    public static boolean addCategory(String name){
        Connection connection = null;
        boolean result = false;
        if(categoryExists(name)){
            return false;
        }
        try {
            connection = connectionPool.checkOut();
            PreparedStatement preparedStatement = DAOUtil.prepareStatement(connection, SQL_ADD_CATEGORY, false);
            preparedStatement.setString(1, name);
            result = preparedStatement.executeUpdate() == 1;
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }
        return result;
    }
    public static boolean addAttribute(String name, Integer categoryId){
        Connection connection = null;
        boolean result = false;
        if(attributeExists(name,categoryId)){
            return false;
        }
        try {
            connection = connectionPool.checkOut();
            PreparedStatement preparedStatement = DAOUtil.prepareStatement(connection, SQL_ADD_ATTRIBUTE, false);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2,categoryId);
            result = preparedStatement.executeUpdate() == 1;
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }
        return result;
    }
    public static boolean deleteCategoryById(int categoryId) {
        Connection connection = null;
        boolean result = false;

        try {
            connection = connectionPool.checkOut();

            PreparedStatement deleteProductAttributeStmt = DAOUtil.prepareStatement(connection, SQL_DELETE_PRODUCT_ATTRIBUTE_BY_CATEGORY_ID, false);
            deleteProductAttributeStmt.setInt(1, categoryId);
            deleteProductAttributeStmt.executeUpdate();

            PreparedStatement deleteProductsStmt = DAOUtil.prepareStatement(connection, SQL_DELETE_PRODUCT, false);
            deleteProductsStmt.setInt(1, categoryId);
            deleteProductsStmt.executeUpdate();

            PreparedStatement deleteAttributesStmt = DAOUtil.prepareStatement(connection, SQL_DELETE_ATTRIBUTE_BY_CATEGORY, false);
            deleteAttributesStmt.setInt(1, categoryId);
            deleteAttributesStmt.executeUpdate();

            PreparedStatement deleteCategoryStmt = DAOUtil.prepareStatement(connection, SQL_DELETE_CATEGORY, false);
            deleteCategoryStmt.setInt(1, categoryId);
            result = deleteCategoryStmt.executeUpdate() == 1;

            deleteProductAttributeStmt.close();
            deleteProductsStmt.close();
            deleteAttributesStmt.close();
            deleteCategoryStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }

        return result;
    }
    public static boolean updateCategory(Category category) {
        Connection connection = null;
        boolean result = false;

        try {
            connection = connectionPool.checkOut();
            PreparedStatement updateCategoryStmt = DAOUtil.prepareStatement(connection, SQL_UPDATE_CATEGORY, false);
            updateCategoryStmt.setString(1, category.getName());
            updateCategoryStmt.setInt(2, category.getId());
            result = updateCategoryStmt.executeUpdate() == 1;
            updateCategoryStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }

        return result;
    }
    public static boolean updateAttribute(Attribute attribute) {
        Connection connection = null;
        boolean result = false;

        try {
            connection = connectionPool.checkOut();
            PreparedStatement updateCategoryStmt = DAOUtil.prepareStatement(connection, SQL_UPDATE_ATTRIBUTE, false);
            updateCategoryStmt.setString(1, attribute.getName());
            updateCategoryStmt.setInt(2, attribute.getId());
            result = updateCategoryStmt.executeUpdate() == 1;
            updateCategoryStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }

        return result;
    }
    public static boolean deleteAttributeById(int attributeId) {
        Connection connection = null;
        boolean result = false;

        try {
            connection = connectionPool.checkOut();
            PreparedStatement deleteProductAttributeStmt = DAOUtil.prepareStatement(connection, SQL_DELETE_PRODUCT_ATTRIBUTE_BY_ATTRIBUTE_ID, false);
            deleteProductAttributeStmt.setInt(1, attributeId);
            deleteProductAttributeStmt.executeUpdate();

            PreparedStatement deleteAttributesStmt = DAOUtil.prepareStatement(connection, SQL_DELETE_ATTRIBUTE, false);
            deleteAttributesStmt.setInt(1, attributeId);
            result = deleteAttributesStmt.executeUpdate() == 1;

            deleteProductAttributeStmt.close();
            deleteAttributesStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.checkIn(connection);
        }

        return result;
    }



}
