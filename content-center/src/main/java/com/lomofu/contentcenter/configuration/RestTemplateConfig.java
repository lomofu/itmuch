package com.lomofu.contentcenter.configuration;

import org.springframework.cloud.alibaba.sentinel.annotation.SentinelRestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateConfig {
  @Bean
  @LoadBalanced
  @SentinelRestTemplate
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
