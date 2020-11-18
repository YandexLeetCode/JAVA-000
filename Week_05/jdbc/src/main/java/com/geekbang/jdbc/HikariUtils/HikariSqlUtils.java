package com.geekbang.jdbc.HikariUtils;

import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

/**
 * Created by yandex on 2020/11/19.
 */
public class HikariSqlUtils {
    @Autowired
    private DataSource dataSource;
}
