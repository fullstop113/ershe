package com.ydles.consume.service;

import com.ydles.seckill.pojo.SeckillOrder;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
public interface  SeckillOrderService {

    //下单到数据库
    int createOrder(SeckillOrder seckillOrder);
}
