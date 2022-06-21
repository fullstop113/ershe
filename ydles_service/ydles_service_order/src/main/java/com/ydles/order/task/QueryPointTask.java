package com.ydles.order.task;

import com.alibaba.fastjson.JSON;
import com.ydles.order.dao.TaskMapper;
import com.ydles.order.pojo.Task;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Component
public class QueryPointTask {

    @Autowired
    TaskMapper taskMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0/60 * * * * ?")
    public void queryPoint(){
        //3task扫表
        List<Task> taskList = taskMapper.findTaskLessThanCurrentTime(new Date());

        //4往mq里发消息
        for (Task task : taskList) {
            rabbitTemplate.convertAndSend(task.getMqExchange(),task.getMqRoutingkey(), JSON.toJSONString(task));
            System.out.println("往mq中发了消息了！");
        }


    }

}
