package com.geekbang.jdbc.PrepareStatementUtils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Optional;

/**
 * Created by yandex on 2020/11/19.
 */
@Data
@Service
public class PrepareStatementUtils {
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
     * 通过传入的不同SQL 语句 执行 add delete update
     * @param queryString
     * @throws SQLException
     */
    public void querySQL(String queryString) throws SQLException {
        Connection connection = getConnection();
        if (Optional.ofNullable(connection).isPresent()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.executeUpdate();
            connection.commit();
            connection.close();
        }
    }

    /**
     * 查询
      * @param queryString
     * @return
     */
    public void searchQuery(String queryString) throws SQLException {
        Connection connection = getConnection();
        if (Optional.ofNullable(connection).isPresent()) {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String uerName = resultSet.getString("userName");
                System.out.println("userName=" + username);
            }
            connection.commit();
            connection.close();
        }
    }

}
