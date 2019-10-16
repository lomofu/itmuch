package com.lomofu.contentcenter.httpclient;

import com.lomofu.contentcenter.dto.user.UserDTO;
import com.lomofu.contentcenter.httpclient.fallback.UserCenterFeignClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// @FeignClient(name = "user-center", configuration = UserCenterFeignConfiguration.class)
@FeignClient(name = "user-center", fallback = UserCenterFeignClientFallBack.class)
/** 标注为Feign 声明式Http */
public interface UserCenterFeignClient {

  /**
   * 相当于http://user-center/users/{id}
   *
   * @param id
   * @return 会自动转化为指定的返回值类型
   */
  @GetMapping("/users/{id}")
  UserDTO findUserById(@PathVariable Integer id);
}
