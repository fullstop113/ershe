package com.ydles.pay;

import com.github.wxpay.sdk.MyConfig;
import com.github.wxpay.sdk.WXPay;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@SpringBootApplication
@EnableEurekaClient
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class,args);
    }

    @Bean
    public WXPay wxPay(){
        MyConfig myConfig=new MyConfig();
        try {
           return new WXPay(myConfig);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
