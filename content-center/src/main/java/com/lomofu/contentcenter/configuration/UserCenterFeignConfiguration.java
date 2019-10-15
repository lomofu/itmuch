package com.lomofu.contentcenter.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;

// @Configuration 父子上下文重叠 细粒度失效
public class UserCenterFeignConfiguration {

  @Bean
  Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }
}
