package com.geekbang.jdbc.JdbcUtils;

import org.springframework.beans.factory.annotation.Value;

import java.sql.*;

/**
 * Created by yandex on 2020/11/19.
 */
public class JdbcUtils {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.usrname}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    /**
     * 初始化 连接
     * @throws ClassNotFoundException
     */
    public void initDb() throws ClassNotFoundException, SQLException {
        Class.forName(driverClassName);
    }

    /**
     * 获取一个连接
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * 插入
     * @param sqlString
     * @throws SQLException
     */
    public void query(String sqlString) throws SQLException {
        Connection connection = getConnection();
        if (connection != null) {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sqlString);
            stmt.close();
            connection.close();
        }
    }

    public void search(String sqlString) throws SQLException {
        Connection connection = getConnection();
        if (connection != null) {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sqlString);
            while (resultSet.next()) {
                String uerName = resultSet.getString("userName");
                System.out.println("userName=" + username);
            }
            stmt.close();
            connection.close();
        }
    }

}
