package com.lomofu.usercenter;

import com.lomofu.usercenter.dao.user.UserMapper;
import com.lomofu.usercenter.entity.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class TestController {

  @Resource private UserMapper userMapper;

  @GetMapping("/test")
  public User testInsert() {
    User user =
        User.builder()
            .avatarUrl("xxx")
            .bonus(100)
            .createTime(new Date())
            .updateTime(new Date())
            .build();
    userMapper.insertSelective(user);

    String birth = "1997-02-17";
    DateFormat dateFormat = new SimpleDateFormat("yyyy-DD-mm");
    try {
      Date date = dateFormat.parse(birth);
      System.out.println(date);
    } catch (ParseException e) {
      e.getMessage();
    } finally {
      return user;
    }
  }
}
