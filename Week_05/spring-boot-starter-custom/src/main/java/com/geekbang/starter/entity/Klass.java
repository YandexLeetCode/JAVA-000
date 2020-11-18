package com.geekbang.starter.entity;

import com.geekbang.starter.properties.KlassProperties;
import lombok.Data;

import java.util.List;

@Data
public class Klass {
    private KlassProperties klassProperties;

    public Klass(KlassProperties p) {
        this.klassProperties = p;
    }

    public Integer getPort() {
        return klassProperties.getPort();
    }
    
    List<Student> students;
    
    public void dong(){
        System.out.println(this.getStudents());
    }
    
}
