package com.ydles.seckill.service;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
public interface SecKillOrderService {

    //秒杀下单
    boolean add(Long id, String time, String username);
}
