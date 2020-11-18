package com.geekbang.jdbc.HikariUtils;

/**
 * Created by yandex on 2020/11/19.
 */
public interface StudentService {
    int save(Student student);
    int update(Student student);
    int delete(long id);
    Student findById(long id);
}
