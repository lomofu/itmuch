package com.lomofu.usercenter.entity.user;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "user")
@Builder
public class User {
  /** Id */
  @Id
  @GeneratedValue(generator = "JDBC")
  private Integer id;

  /** 微信id */
  @Column(name = "wx_id")
  private String wxId;

  /** 微信昵称 */
  @Column(name = "wx_nickname")
  private String wxNickname;

  /** 角色 */
  private String roles;

  /** 头像地址 */
  @Column(name = "avatar_url")
  private String avatarUrl;

  /** 创建时间 */
  @Column(name = "create_time")
  private Date createTime;

  /** 修改时间 */
  @Column(name = "update_time")
  private Date updateTime;

  /** 积分 */
  private Integer bonus;
}
