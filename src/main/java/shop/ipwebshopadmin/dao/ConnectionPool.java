package shop.ipwebshopadmin.dao;

import java.sql.*;
import java.util.*;

public class ConnectionPool {

    public static ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    private static ConnectionPool connectionPool;

    static {
        String jdbcURL = "jdbc:mysql://localhost:3306/web_shop?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String username = "root";
        String password = "root";
        String driver = "com.mysql.cj.jdbc.Driver";
        int preconnectCount = 2;
        int maxIdleConnections = 25;
        int maxConnections = 25;
        try {
            Class.forName(driver);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            connectionPool = new ConnectionPool(
                    jdbcURL, username, password,
                    preconnectCount, maxIdleConnections,
                    maxConnections);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected ConnectionPool(String aJdbcURL, String aUsername,
                             String aPassword, int aPreconnectCount,
                             int aMaxIdleConnections,
                             int aMaxConnections)
            throws SQLException, ClassNotFoundException {

        freeConnections = new Vector<>();
        usedConnections = new Vector<>();
        jdbcURL = aJdbcURL;
        username = aUsername;
        password = aPassword;
        maxIdleConnections = aMaxIdleConnections;
        maxConnections = aMaxConnections;
        Class.forName("com.mysql.cj.jdbc.Driver");
        for (int i = 0; i < aPreconnectCount; i++) {
            Connection conn = DriverManager.getConnection(
                    jdbcURL, username, password);
            conn.setAutoCommit(true);
            freeConnections.addElement(conn);
        }
        connectCount = aPreconnectCount;
    }

    public synchronized Connection checkOut()
            throws SQLException {

        Connection conn = null;
        if (freeConnections.size() > 0) {
            conn = freeConnections.elementAt(0);
            freeConnections.removeElementAt(0);
            usedConnections.addElement(conn);
        } else {
            if (connectCount < maxConnections) {
                conn = DriverManager.getConnection(
                        jdbcURL, username, password);
                usedConnections.addElement(conn);
                connectCount++;
            } else {
                try {
                    wait();
                    conn = freeConnections.elementAt(0);
                    freeConnections.removeElementAt(0);
                    usedConnections.addElement(conn);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return conn;
    }

    public synchronized void checkIn(Connection aConn) {
        if (aConn ==  null)
            return;
        if (usedConnections.removeElement(aConn)) {
            freeConnections.addElement(aConn);
            while (freeConnections.size() > maxIdleConnections) {
                int lastOne = freeConnections.size() - 1;
                Connection conn = freeConnections.elementAt(lastOne);
                try { conn.close(); } catch (SQLException ex) {ex.printStackTrace(); }
                freeConnections.removeElementAt(lastOne);
            }
            notify();
        }
    }

    private final String jdbcURL;
    private final String username;
    private final String password;
    private int connectCount;
    private final int maxIdleConnections;
    private final int maxConnections;
    private final Vector<Connection> usedConnections;
    private final Vector<Connection> freeConnections;

}

