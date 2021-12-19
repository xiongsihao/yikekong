package com.yikekong.util;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * @author : xsh
 * @create : 2021-12-19 - 22:02
 * @describe:
 */
@Slf4j
public class HttpUtil {

    public static void httpPost(String url, Object msg) {
        if (Strings.isNullOrEmpty(url)) {
            return;
        }
        new Thread(() -> {
            RestTemplate restTemplate = new RestTemplateBuilder()
                    .defaultHeader(HttpHeaders.CONNECTION, MediaType.APPLICATION_JSON_VALUE).build();

            try {
                restTemplate.postForObject(url, msg, String.class);
            } catch (Exception e) {
                log.error("httpPost error", e);
            }

        }).start();


    }


}
