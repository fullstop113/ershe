package com.ydles.seckill.service;

import com.ydles.seckill.pojo.SeckillGoods;

import java.util.List;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
public interface SecKillGoodsService {

    //查询时间段内的秒杀商品
    public List<SeckillGoods> list(String time);
}
