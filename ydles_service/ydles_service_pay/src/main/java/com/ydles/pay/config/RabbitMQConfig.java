package com.ydles.pay.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Configuration
public class RabbitMQConfig {

    public static final String ORDER_PAY="order_pay";

    @Bean
    public Queue queue(){
        return  new Queue(ORDER_PAY);
    }
}
