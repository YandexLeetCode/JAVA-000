package com.geekbang.starter.entity;

import com.geekbang.starter.properties.StudentProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Student implements Serializable {

    private StudentProperties studentProperties;
    private int id;
    private String name;
    public Student(StudentProperties p) {
        this.studentProperties = p;
    }

    public Student(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getValue() {
        return studentProperties.getValue();
    }

    public void init(){
        System.out.println("hello...........");
    }
    
    public Student create(){
        return new Student(101,"KK101");
    }
}
