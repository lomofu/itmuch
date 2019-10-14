package com.lomofu.usercenter.service.user;

import com.lomofu.usercenter.dao.user.UserMapper;
import com.lomofu.usercenter.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
  public final UserMapper userMapper;

  public User findById(Integer id) {
    return userMapper.selectByPrimaryKey(id);
  }
}
