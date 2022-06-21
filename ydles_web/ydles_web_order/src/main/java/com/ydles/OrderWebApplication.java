package com.ydles;

import com.ydles.interceptor.FeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.ydles.order.feign","com.ydles.user.feign","com.ydles.pay.feign"})
public class OrderWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderWebApplication.class, args);
    }


    @Bean
    public FeignInterceptor feignInterceptor(){
        return new FeignInterceptor();
    }
}
