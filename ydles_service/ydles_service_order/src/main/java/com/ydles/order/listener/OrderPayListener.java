package com.ydles.order.listener;

import com.alibaba.fastjson.JSON;
import com.ydles.order.config.RabbitMQConfig;
import com.ydles.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Component
public class OrderPayListener {
    @Autowired
    OrderService orderService;
    @RabbitListener(queues = RabbitMQConfig.ORDER_PAY)
    public void receiveMsg(String msg){
        //1监听住消息
        System.out.println("接受到支付成功消息了："+msg);

        Map<String, String> map = JSON.parseObject(msg, Map.class);

        //2修改order表中的数据
        orderService.updatePayStatus(map.get("orderId"),map.get("transactionId"));
    }


}
