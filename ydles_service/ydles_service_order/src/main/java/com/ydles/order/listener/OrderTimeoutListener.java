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
public class OrderTimeoutListener {
    @Autowired
    OrderService orderService;

    @RabbitListener(queues = "queue.ordertimeout")
    public void recieveMsg(String orderId){
        System.out.println("接受到关闭订单的消息了！");
        try {
            orderService.closeOrder(orderId);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
