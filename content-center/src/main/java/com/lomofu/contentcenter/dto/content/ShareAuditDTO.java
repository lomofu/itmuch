package com.lomofu.contentcenter.dto.content;

import enums.AuditStatusEnum;
import lombok.Data;

@Data
public class ShareAuditDTO {

  /** 审核状态 */
  private AuditStatusEnum auditStatusEnum;

  /** 原因 */
  private String reson;
}
