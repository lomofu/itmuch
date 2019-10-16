package com.lomofu.contentcenter;

import com.lomofu.contentcenter.dao.content.ShareMapper;
import com.lomofu.contentcenter.dto.user.UserDTO;
import com.lomofu.contentcenter.entity.content.Share;
import com.lomofu.contentcenter.httpclient.TestBaiduFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
public class TestController {

  @Resource private ShareMapper shareMapper;

  @Autowired private TestBaiduFeignClient testBaiduFeignClient;

  /** springCloud 提供的接口提供查询 */
  @Resource private DiscoveryClient discoveryClient;

  @Resource private RestTemplate restTemplate;

  @GetMapping("/test")
  public List<Share> testInsert() {
    // 插入
    Share share =
        Share.builder()
            .userId(1)
            .createTime(new Date())
            .updateTime(new Date())
            .cover("xxx")
            .author("Xxx")
            .buyCount(1)
            .title("xxxx")
            .isOriginal(true)
            .summary("xxxxx")
            .price(10)
            .downloadUrl("Xxxx")
            .auditStatus("xxxx")
            .buyCount(1)
            .reason("xxxxx")
            .showFlag(true)
            .build();
    shareMapper.insert(share);

    // 查询
    List<Share> shareList = shareMapper.selectAll();
    return shareList;
  }

  /**
   * 测试：服务发现，证明内容中心总能找到用户中心
   *
   * @return 用户中心实例的地址信息
   */
  @GetMapping("/test2")
  public List<ServiceInstance> getInstance() {

    return discoveryClient.getInstances("user-center");
  }

  @GetMapping("/baidu")
  public String baidIndex() {
    return testBaiduFeignClient.index();
  }

  @GetMapping("/test-rest/{id}")
  public UserDTO testRest(@PathVariable Integer id) {
    return restTemplate
        .getForEntity("http://user-center/users/{userId}", UserDTO.class, id)
        .getBody();
  }
}
