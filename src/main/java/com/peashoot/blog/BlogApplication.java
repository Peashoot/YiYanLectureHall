package com.peashoot.blog;

import com.peashoot.blog.util.property.PeaBlogProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 项目运行入口
 * @author peashoot
 */
@SpringBootApplication
@EnableAsync
@MapperScan("com.peashoot.blog.batis.mapper")
@EnableConfigurationProperties({ PeaBlogProperties.class })
public class BlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }
}
