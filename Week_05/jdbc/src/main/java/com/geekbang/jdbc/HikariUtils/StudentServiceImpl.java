package com.geekbang.jdbc.HikariUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by yandex on 2020/11/19.
 */
public class StudentServiceImpl implements StudentService{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public int save(Student student) {
        return jdbcTemplate.update("INSERT INTO students(name,sex,age) values(?,?,?)",student.getName(),student.getSex(),student.getAge());
    }

    @Override
    public int update(Student student) {
        return jdbcTemplate.update("UPDATE students SET name=?,sex=? where id=?",student.getName(),student.getSex(),student.getId());
    }

    @Override
    public int delete(long id) {
        return jdbcTemplate.update("DELETE FROM students where id=?",id);
    }

    @Override
    public Student findById(long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM students where id=?",new Object[] {id}, new BeanPropertyRowMapper<Student>(Student.class));
    }
}
