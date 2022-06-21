package com.ydles.user.listener;

import com.alibaba.fastjson.JSON;
import com.ydles.order.pojo.Task;
import com.ydles.user.config.RabbitMQConfig;
import com.ydles.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Component
public class AddPointListener {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    UserService userService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.CG_BUYING_ADDPOINT)
    public void recieveMsg(String msg){
        //5 监听
        System.out.println("监听到需要增加积分的任务了"+msg);

        //转换消息
        Task task = JSON.parseObject(msg, Task.class);
        if(task==null|| StringUtils.isEmpty(task.getRequestBody())){
            return;
        }

        //6 redis有没有这个任务
        String value = stringRedisTemplate.boundValueOps(task.getId() + "").get();
        if(StringUtils.isNotEmpty(value)){
            return;
        }

        //7-12部
        int i = userService.updateUserPoint(task);
        if(i==0){
            return;
        }

        //13 返回通知 积分更新完毕
        rabbitTemplate.convertAndSend(RabbitMQConfig.EX_BUYING_ADDPOINTUSER,RabbitMQConfig.CG_BUYING_FINISHADDPOINT_KEY,JSON.toJSONString(task));
        System.out.println("返回添加积分成功消息！");
    }



}
