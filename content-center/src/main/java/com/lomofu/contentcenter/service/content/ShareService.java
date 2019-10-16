package com.lomofu.contentcenter.service.content;

import com.lomofu.contentcenter.dao.content.ShareMapper;
import com.lomofu.contentcenter.dto.content.ShareAuditDTO;
import com.lomofu.contentcenter.dto.content.ShareDTO;
import com.lomofu.contentcenter.dto.user.UserDTO;
import com.lomofu.contentcenter.entity.content.Share;
import com.lomofu.contentcenter.httpclient.UserCenterFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareService {
  private final ShareMapper shareMapper;
  private final RestTemplate restTemplate;
  private final String USER_CENTER = "user-center";
  private final String RIBBON_USER_URL = "http://user-center/users/{id}";
  private final String NOT_YET = "NOT_YET";

  /** ？这里使用@Resource会装配失败 只有@AutoWire可以 */
  @Autowired private UserCenterFeignClient userCenterFeignClient;

  @Resource private DiscoveryClient discoveryClient;

  public ShareDTO findById(Integer id) {
    Share share = shareMapper.selectByPrimaryKey(id);
    Integer userId = share.getUserId();

    /** Ribbon会自动负载均衡"user-center" 并将user-center 填充为对应的url地址 */
    //    @Deprecated
    //    ResponseEntity<UserDTO> responseEntity =
    //        restTemplate.getForEntity(RIBBON_USER_URL, UserDTO.class, userId);
    //    UserDTO userDTO = responseEntity.getBody();

    // 使用新的FeignClient使得restTemplate更好管理与维护并且易读
    UserDTO userDTO = userCenterFeignClient.findUserById(id);
    ShareDTO shareDTO = new ShareDTO();
    BeanUtils.copyProperties(userDTO, shareDTO);
    shareDTO.setWxNickname(userDTO.getWxNickname());
    return shareDTO;
  }

  public ShareDTO findById(Integer id, int i) {
    // 获取分享详情
    Share share = shareMapper.selectByPrimaryKey(id);
    // 发布人id
    Integer userId = share.getUserId();

    // 用户中心所有服务的服务实例列表
    List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances(USER_CENTER);
    // 所用用户中心实例的请求地址
    List<String> targetURLList =
        serviceInstanceList.stream()
            .map(serviceInstance -> serviceInstance.getUri().toString() + "/users/{id}")
            .collect(Collectors.toList());

    // 客户端随机算法负载下标
    Random random = new Random();
    int index = ThreadLocalRandom.current().nextInt(targetURLList.size());
    index = random.nextInt(targetURLList.size());

    log.info("请求目标地址：{}", targetURLList.get(index));
    ResponseEntity<UserDTO> responseEntity =
        restTemplate.getForEntity(targetURLList.get(index), UserDTO.class, userId);
    UserDTO userDTO = responseEntity.getBody();
    ShareDTO shareDTO = new ShareDTO();

    // 消息的装配
    BeanUtils.copyProperties(share, shareDTO);
    shareDTO.setWxNickname(userDTO.getWxNickname());
    return shareDTO;
  }

  public Share auditById(Integer id, ShareAuditDTO shareAuditDTO) {

    // 查询share是否存在
    Share share = shareMapper.selectByPrimaryKey(id);
    if (share == null) {
      throw new IllegalArgumentException("参数非法！该分享不存在！");
    }
    if (!Objects.equals(NOT_YET, share.getAuditStatus())) {
      throw new IllegalArgumentException("参数非法！该分享已经已经审核通过！");
    }
    // 审核资源
    share.setAuditStatus(shareAuditDTO.getAuditStatusEnum().toString());
    shareMapper.updateByPrimaryKey(share);

    // 异步：pass 为发布人添加积分
    // userCenterFeignClient.addBonus(id, 500);
  }
}
