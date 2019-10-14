package com.lomofu.contentcenter.configuration;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;

import javax.annotation.Resource;

@Slf4j
/** 支持nacos的权重规则 */
public class NacosWeihtedRule extends AbstractLoadBalancerRule {
  @Resource private NacosDiscoveryProperties nacosDiscoveryProperties;

  @Override
  // 读取配置文件,并初始化NacosWeihtedRule
  public void initWithNiwsConfig(IClientConfig iClientConfig) {}

  @Override
  public Server choose(Object key) {
    try {
      BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
      // 想要请求的微服务的名称
      String name = baseLoadBalancer.getName();

      // 实现负载均衡算法。。。

      // 1.拿到服务发现相关的api
      NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
      // 2.nacos client自动基于权重的负载均衡算法，给我们一个选择实例
      Instance instance = namingService.selectOneHealthyInstance(name);
      log.info("选择的实例是： 端口={}, 实例={}", instance.getPort(), instance.getServiceName());
      return new NacosServer(instance);
    } catch (NacosException e) {
      log.error(e.getMessage());
      return null;
    }
  }
}
