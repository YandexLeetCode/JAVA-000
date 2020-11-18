package com.geekbang.starter.entity;

import com.geekbang.starter.aop.ISchool;
import com.geekbang.starter.properties.SchoolProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

@Data
public class School implements ISchool {
   private SchoolProperties schoolProperties;

   public School(SchoolProperties p) {
       this.schoolProperties = p;
   }
   public String getValue() {
       return schoolProperties.getValue();
   }
    // Resource 
    @Autowired(required = true) //primary
            Klass class1;
    
    @Resource(name = "student100")
    Student student100;
    
    public void ding(){
    
        System.out.println("Class1 have " + this.class1.getStudents().size() + " students and one is " + this.student100);
        
    }
    
}
