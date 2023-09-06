package shop.ipwebshopadmin.controllers;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import shop.ipwebshopadmin.beans.CategoryBean;
import shop.ipwebshopadmin.beans.UserBean;
import shop.ipwebshopadmin.dto.Attribute;
import shop.ipwebshopadmin.dto.Category;
import shop.ipwebshopadmin.dto.User;
import shop.ipwebshopadmin.beans.AdminBean;

import java.io.IOException;

@WebServlet(name = "controller", value = "/controller")
public class Controller extends HttpServlet {
    private static final String LOGIN = "/WEB-INF/pages/login.jsp";
    private static final String ERROR = "/WEB-INF/pages/error.jsp";
    private static final String USERS = "/WEB-INF/pages/users.jsp";
    private static final String ADD_USER = "/WEB-INF/pages/add-user.jsp";
    private static final String UPDATE_USER = "/WEB-INF/pages/update-user.jsp";
    private static final String CATEGORIES = "/WEB-INF/pages/categories.jsp";
    private static final String UPDATE_CATEGORY = "/WEB-INF/pages/update-category.jsp";
    private static final String UPDATE_ATTRIBUTE = "/WEB-INF/pages/update-attribute.jsp";
    private static final String ADD_CATEGORY = "/WEB-INF/pages/add-category.jsp";
    private static final String ADD_ATTRIBUTE = "/WEB-INF/pages/add-attribute.jsp";

    public Controller() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String address = LOGIN;
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        session.setAttribute("notification", "");
        if (action == null || action.equals("")) {
            address = LOGIN;
        } else if (action.equals("logout")) {
            session.invalidate();
        } else if (action.equals("login")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            AdminBean adminBean = new AdminBean();
            if (adminBean.login(username, password)) {
                session.setAttribute("adminBean", adminBean);
                UserBean userBean = new UserBean();
                CategoryBean categoryBean = new CategoryBean();
                session.setAttribute("userBean", userBean);
                session.setAttribute("categoryBean", categoryBean);
                address = USERS;
            } else {
                session.setAttribute("notification", "Podaci nisu odgovarajući.");
            }
        } else {
            AdminBean adminBean = (AdminBean) session.getAttribute("adminBean");
            if (adminBean == null || !adminBean.isLoggedIn()) {
                address = LOGIN;
            } else {
                UserBean userBean = (UserBean) session.getAttribute("userBean");
                CategoryBean categoryBean = (CategoryBean) session.getAttribute("categoryBean");
                if (action.equals("users")) {

                    address = USERS;

                } else if (action.equals("categories")) {

                    address = CATEGORIES;

                } else  if (action.equals("add-user")){

                    address = ADD_USER;
                    if (request.getParameter("submit") != null) {
                        User user = new User(0, request.getParameter("firstName"), request.getParameter("lastName"),
                                request.getParameter("username"), request.getParameter("password"),
                                request.getParameter("location"), request.getParameter("email"),"USER",false,null,Integer.parseInt(request.getParameter("avatarId")));
                        if (userBean.addUser(user)) {
                            address = USERS;
                        }
                    }

                } else if (action.equals("add-category")){

                    address = ADD_CATEGORY;
                    if (request.getParameter("submit") != null) {
                        if (categoryBean.addCategory(request.getParameter("name"))) {
                            address = CATEGORIES;
                        }else {
                            session.setAttribute("notification", "Kategorija već postoji!");
                        }
                    }

                }else if (action.equals("add-attribute")){

                    address = ADD_ATTRIBUTE;
                    int categoryId = Integer.parseInt(request.getParameter("id"));
                    Category attributesCategory = categoryBean.getCategoryById(categoryId);
                    categoryBean.setCategory(attributesCategory);
                    if (request.getParameter("submit") != null) {
                        if(categoryBean.addAttribute(request.getParameter("name"),Integer.parseInt(request.getParameter("id")))){
                            address = CATEGORIES;
                        }else {
                            session.setAttribute("notification", "Atribut već postoji za tu kategoriju!");
                        }
                    }

                } else if (action.equals("update-user")){

                    address = UPDATE_USER;
                    int userId = Integer.parseInt(request.getParameter("id"));
                    User updateUser = userBean.getById(userId);
                    userBean.setUser(updateUser);
                    String username = updateUser.getUsername();
                    if (request.getParameter("submit") != null) {
                        String password = request.getParameter("password");
                        if (password == null || password.isEmpty()) {
                            password = updateUser.getPassword();
                        }

                        User user = new User(userId, request.getParameter("firstName"), request.getParameter("lastName"),
                                username, password,
                                request.getParameter("location"), request.getParameter("email"),
                                updateUser.getRole(), updateUser.getAccountConfirmed(), updateUser.getPin(),Integer.parseInt(request.getParameter("avatarId")));
                        if (userBean.updateUser(user)) {
                            address = USERS;
                        }
                    }

                } else if (action.equals("update-category")){

                    address = UPDATE_CATEGORY;
                    int updateCategoryId = Integer.parseInt(request.getParameter("id"));
                    Category updateCategory = categoryBean.getCategoryById(updateCategoryId);
                    categoryBean.setCategory(updateCategory);
                    if (request.getParameter("submit") != null) {
                        Category category = new Category(updateCategoryId, request.getParameter("name"));
                        if (categoryBean.updateCategory(category)) {
                            address = CATEGORIES;
                        }
                    }

                }
                else if (action.equals("update-attribute")){

                    address = UPDATE_ATTRIBUTE;
                    int updateAttributeId = Integer.parseInt(request.getParameter("id"));
                    Attribute updateAttribute = categoryBean.getAttribute(updateAttributeId);
                    categoryBean.setAttribute(updateAttribute);

                    if (request.getParameter("submit") != null) {
                        Attribute attribute = new Attribute(updateAttributeId, request.getParameter("name"),updateAttribute.getCategoryId());
                        if (categoryBean.updateAttribute(attribute)) {
                            address = CATEGORIES;
                        }
                    }

                } else if (action.equals("deactivate-user")){

                    int id = Integer.parseInt(request.getParameter("id"));
                    userBean.deactivateUser(id);
                    address = USERS;

                } else if (action.equals("delete-category")){

                    int categoryId = Integer.parseInt(request.getParameter("id"));
                    categoryBean.deleteCategory(categoryId);
                    address = CATEGORIES;

                } else if (action.equals("delete-attribute")){

                    int attributeId = Integer.parseInt(request.getParameter("id"));
                    categoryBean.deleteAttribute(attributeId);
                    address = CATEGORIES;

                } else {

                    address = ERROR;

                }
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(address);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
