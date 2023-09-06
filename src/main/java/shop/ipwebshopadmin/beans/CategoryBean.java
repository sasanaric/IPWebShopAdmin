package shop.ipwebshopadmin.beans;

import shop.ipwebshopadmin.dao.CategroyDAO;
import shop.ipwebshopadmin.dto.Attribute;
import shop.ipwebshopadmin.dto.Category;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryBean implements Serializable {
    private Category category;
    private Attribute attribute;
    public ArrayList<Category> getAll(){
        return CategroyDAO.getAll();
    }
    public ArrayList<Attribute> getAttributesByCategory(Integer categoryId){
        return CategroyDAO.getAttributesByCategory(categoryId);
    }
    public Category getCategoryById(Integer id){
        return CategroyDAO.getCategory(id);
    }
    public Attribute getAttribute(Integer id){
        return CategroyDAO.getAttribute(id);
    }
    public boolean addCategory(String name){
        return CategroyDAO.addCategory(name);
    }
    public boolean deleteCategory(Integer id){
        return CategroyDAO.deleteCategoryById(id);
    }
    public boolean updateCategory(Category category){
        return CategroyDAO.updateCategory(category);
    }
    public boolean addAttribute(String name, Integer categoryId){
        return CategroyDAO.addAttribute(name,categoryId);
    }
    public boolean updateAttribute(Attribute attribute){
        return CategroyDAO.updateAttribute(attribute);
    }
    public boolean deleteAttribute(Integer id){
        return CategroyDAO.deleteAttributeById(id);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }
}
