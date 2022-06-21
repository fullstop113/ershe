package com.ydles.user.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Configuration
public class RabbitMQConfig {
    //添加积分任务交换机
    public static final String EX_BUYING_ADDPOINTUSER = "ex_buying_addpointuser";

    //添加积分消息队列
    public static final String CG_BUYING_ADDPOINT = "cg_buying_addpoint";

    //完成添加积分消息队列
    public static final String CG_BUYING_FINISHADDPOINT = "cg_buying_finishaddpoint";

    //添加积分路由key
    public static final String CG_BUYING_ADDPOINT_KEY = "addpoint";

    //完成添加积分路由key
    public static final String CG_BUYING_FINISHADDPOINT_KEY = "finishaddpoint";

    //说明交换机
    @Bean(EX_BUYING_ADDPOINTUSER)
    public Exchange EX_BUYING_ADDPOINTUSER(){
        return ExchangeBuilder.directExchange(EX_BUYING_ADDPOINTUSER).durable(true).build();
    }

    //声明队列
    @Bean(CG_BUYING_ADDPOINT)
    public Queue CG_BUYING_ADDPOINT(){
        return new Queue(CG_BUYING_ADDPOINT,true);
    }

    @Bean(CG_BUYING_FINISHADDPOINT)
    public Queue CG_BUYING_FINISHADDPOINT(){
        return new Queue(CG_BUYING_FINISHADDPOINT,true);
    }

    //声明绑定关系
    @Bean
    public Binding CG_BUYING_ADDPOINT_EX_BUYING_ADDPOINTUSER(
            @Qualifier(CG_BUYING_ADDPOINT)Queue queue,
            @Qualifier(EX_BUYING_ADDPOINTUSER) Exchange exchange ){
        return BindingBuilder.bind(queue).to(exchange).with(CG_BUYING_ADDPOINT_KEY).noargs();
    }

    @Bean
    public Binding CG_BUYING_FINISHADDPOINT_EX_BUYING_ADDPOINTUSER(
            @Qualifier(CG_BUYING_FINISHADDPOINT)Queue queue,
            @Qualifier(EX_BUYING_ADDPOINTUSER) Exchange exchange ){
        return BindingBuilder.bind(queue).to(exchange).with(CG_BUYING_FINISHADDPOINT_KEY).noargs();
    }



}
