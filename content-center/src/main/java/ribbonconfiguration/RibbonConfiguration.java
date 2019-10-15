package ribbonconfiguration;

import com.lomofu.contentcenter.configuration.NacosWeihtedRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** RibbonConfiguration规则 */
@Configuration
public class RibbonConfiguration {
  @Bean
  public IRule ribbonRule() {
    return new NacosWeihtedRule();
  }
}
