package com.geekbang.starter;

import com.geekbang.starter.entity.Klass;
import com.geekbang.starter.entity.School;
import com.geekbang.starter.entity.Student;
import com.geekbang.starter.properties.KlassProperties;
import com.geekbang.starter.properties.SchoolProperties;
import com.geekbang.starter.properties.StudentProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yandex on 2020/11/19.
 */
@Configuration
@EnableConfigurationProperties({StudentProperties.class,SchoolProperties.class, KlassProperties.class})
public class CustomAutoConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "spring.custom.student", value = "enable",havingValue = "true")
    public Student student(StudentProperties studentProperties)
    {
        return new Student(studentProperties);
    }
    @Bean
    @ConditionalOnProperty(prefix = "spring.custom.school", value = "enable",havingValue = "true")
    public School school(SchoolProperties schoolProperties)
    {
        return new School(schoolProperties);
    }
    @Bean
    @ConditionalOnProperty(prefix = "spring.custom.klass", value = "enable",havingValue = "true")
    public Klass klass(KlassProperties klassProperties)
    {
        return new Klass(klassProperties);
    }


}
