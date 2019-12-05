package br.edu.ifsul.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDAO {
    protected Connection getConnection() throws SQLException{
        try {
            String url = "jdbc:mariadb://localhost:3306/vendas";
            String user = "root";
            String password = "matrix666";
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        BaseDAO baseDAO = new BaseDAO();
        try {
            System.out.println(baseDAO.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
