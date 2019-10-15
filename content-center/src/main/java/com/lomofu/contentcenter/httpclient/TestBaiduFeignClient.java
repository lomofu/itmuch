package com.lomofu.contentcenter.httpclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/** * feign脱离ribbon使用 */
@FeignClient(name = "baidu", url = "www.baidu.com")
public interface TestBaiduFeignClient {
  @GetMapping("")
  String index();
}
