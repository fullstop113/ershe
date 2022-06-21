package com.ydles.order.listener;

import com.alibaba.fastjson.JSON;
import com.ydles.order.config.RabbitMQConfig;
import com.ydles.order.pojo.Task;
import com.ydles.order.service.TaskService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Component
public class DelTaskListener {
    @Autowired
    TaskService taskService;

    @RabbitListener(queues = RabbitMQConfig.CG_BUYING_FINISHADDPOINT)
    public void revieveMsg(String msg){
        System.out.println("接受到完成添加积分的消息了"+msg);

        //14
        Task task = JSON.parseObject(msg, Task.class);
        taskService.delTask(task);
    }


}
