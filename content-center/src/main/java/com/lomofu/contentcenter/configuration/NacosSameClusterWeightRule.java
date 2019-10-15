package com.lomofu.contentcenter.configuration;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
/** 基于统一集群下的负载均衡 加入控制版本 */
public class NacosSameClusterWeightRule extends AbstractLoadBalancerRule {
  @Resource private NacosDiscoveryProperties nacosDiscoveryProperties;

  @Override
  public void initWithNiwsConfig(IClientConfig iClientConfig) {}

  @Override
  public Server choose(Object key) {

    try {
      // 获取集群的名字
      String clusterName = nacosDiscoveryProperties.getClusterName();
      BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
      // 拿到实例的名字
      String name = baseLoadBalancer.getName();
      // 拿到服务发现的api
      NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();
      // 拿到实例
      List<Instance> instanceList = namingService.selectInstances(name, true);
      // 1.找出指定服务集群下的所有实例A
      List<Instance> sameClusterInstanceList =
          instanceList.stream()
              .filter(instance -> clusterName.equals(instance.getClusterName()))
              .collect(Collectors.toList());
      // 2.过滤相同集群下的所有实例B
      List<Instance> instancesToBeChosen = new ArrayList<>(10);
      if (CollectionUtils.isEmpty(sameClusterInstanceList)) {
        log.warn("发生跨集群调用");
      } else {
        instancesToBeChosen = sameClusterInstanceList;
      }
      // 3.如果B为空，就用A

      // 4.基于权重的负载均衡算法
      Instance randomWeight = ExtendBalancer.getHostByRandomWeight2(instancesToBeChosen);
      log.info("选择的实例是 端口= {} ,实例 = {}", randomWeight.getPort(), randomWeight);
      return new NacosServer(randomWeight);
    } catch (NacosException e) {
      log.error(e.getMessage());
      return null;
    }
  }
}

class ExtendBalancer extends Balancer {
  public static Instance getHostByRandomWeight2(List<Instance> hosts) {
    return getHostByRandomWeight(hosts);
  }
}
