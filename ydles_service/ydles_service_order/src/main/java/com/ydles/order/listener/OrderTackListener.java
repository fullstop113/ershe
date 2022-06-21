package com.ydles.order.listener;

import com.ydles.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Component
public class OrderTackListener {
    @Autowired
    OrderService orderService;

    @RabbitListener(queues = "order_tack")
    public void aotuTack(){
        System.out.println("接受到该自动收货的消息了");

        orderService.autoTack();
    }

}
