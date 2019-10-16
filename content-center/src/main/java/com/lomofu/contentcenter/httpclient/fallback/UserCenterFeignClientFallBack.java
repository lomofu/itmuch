package com.lomofu.contentcenter.httpclient.fallback;

import com.lomofu.contentcenter.dto.user.UserDTO;
import com.lomofu.contentcenter.httpclient.UserCenterFeignClient;
import org.springframework.stereotype.Component;

@Component
public class UserCenterFeignClientFallBack implements UserCenterFeignClient {
  @Override
  public UserDTO findUserById(Integer id) {
    UserDTO userDTO = new UserDTO();
    userDTO.setWxNickname("mock name");
    return userDTO;
  }
}
