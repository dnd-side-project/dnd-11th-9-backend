package com._119.wepro.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com._119.wepro.auth.client")
public class FeignClientConfig {

}