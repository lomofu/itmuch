package com.lomofu.contentcenter.controller;

import com.lomofu.contentcenter.dto.content.ShareAuditDTO;
import com.lomofu.contentcenter.entity.content.Share;
import com.lomofu.contentcenter.service.content.ShareService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/shares")
public class ShareAdminController {

  @Resource private ShareService shareService;

  @PutMapping("audit/{id}")
  public Share auditById(@PathVariable Integer id, @RequestBody ShareAuditDTO shareAuditDTO) {
    // TODO 认证，授权

    return shareService.auditById(id, shareAuditDTO);
  }
}
