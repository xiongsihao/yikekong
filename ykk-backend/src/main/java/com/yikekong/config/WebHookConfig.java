package com.yikekong.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : xsh
 * @create : 2021-12-19 - 22:15
 * @describe:
 */
@Configuration
@ConfigurationProperties("webhook")
@Data
public class WebHookConfig {

    private String online; //断连透传
    private String gps;//设备定位透传地址

}
