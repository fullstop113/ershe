package com.ydles.seckill.config;

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

    //秒杀商品订单消息
    public static final String SECKILL_ORDER_QUEUE="seckill_order";

    @Bean
    public Queue queue(){
        return new Queue(SECKILL_ORDER_QUEUE,true);
    }

}
