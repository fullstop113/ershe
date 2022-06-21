package com.ydles.consume.listener;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.ydles.consume.config.RabbitMQConfig;
import com.ydles.consume.service.SeckillOrderService;
import com.ydles.seckill.pojo.SeckillOrder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Component
public class SecKillOrderListener {
    @Autowired
    SeckillOrderService seckillOrderService;

    @RabbitListener(queues = RabbitMQConfig.SECKILL_ORDER_QUEUE)
    public void receiveMsg(Message message, Channel channel){
        //慢一点抓取数据
        try {
            channel.basicQos(100);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String msgStr = new String(message.getBody());
        System.out.println("接受到了秒杀订单"+msgStr);
        //1监听 order
        SeckillOrder seckillOrder = JSON.parseObject(message.getBody(), SeckillOrder.class);
        //2先做业务逻辑
        int result = seckillOrderService.createOrder(seckillOrder);

        if(result>0){
            //2.1没问题    告诉mq收到消息了，可删
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            } catch (IOException e) {
                e.printStackTrace();
                //log.error
            }
        }else {
            //2.2有问题    告诉mq没收到消息，重回队列
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
            } catch (IOException e) {
                e.printStackTrace();
                //log.error
            }
        }






    }


}
