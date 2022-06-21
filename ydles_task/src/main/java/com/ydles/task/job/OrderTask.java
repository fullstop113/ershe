package com.ydles.task.job;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Component
public class OrderTask {
    @Autowired
    RabbitTemplate rabbitTemplate;


    @Scheduled(cron = "0/5 * * * * *")
    public void aotuTake(){
        System.out.println("该自动收货了！"+new Date());
        rabbitTemplate.convertAndSend("","order_tack","-");
    }



}
