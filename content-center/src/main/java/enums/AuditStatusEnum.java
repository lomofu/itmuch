package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuditStatusEnum {
  NOT_YET("待审核"),
  PASS("审核通过"),
  REJECT("未过审");

  private String msg;
}
