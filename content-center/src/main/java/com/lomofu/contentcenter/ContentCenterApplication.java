package com.lomofu.contentcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

// 扫描mybatis那些的包里的接口
@MapperScan(basePackages = "com.lomofu")
@SpringBootApplication
public class ContentCenterApplication {
  public static void main(String[] args) {
    SpringApplication.run(ContentCenterApplication.class, args);
  }
}
