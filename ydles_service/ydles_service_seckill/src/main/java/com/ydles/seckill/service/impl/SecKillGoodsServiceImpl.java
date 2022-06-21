package com.ydles.seckill.service.impl;

import com.ydles.seckill.pojo.SeckillGoods;
import com.ydles.seckill.service.SecKillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Service
public class SecKillGoodsServiceImpl implements SecKillGoodsService {
    @Autowired
    RedisTemplate redisTemplate;
    //redis 秒杀商品key开头
    public static final String SECKILL_GOODS_KEY = "seckill_goods_";
    //秒杀商品库存key头
    public static final String SECKILL_GOODS_STOCK_COUNT_KEY = "seckill_goods_stock_count_";
    /**
     *
     * @param time  2021122816
     * @return
     */
    @Override
    public List<SeckillGoods> list(String time) {
        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps(SECKILL_GOODS_KEY + time).values();

       //获取当前真实的库存量
        for (SeckillGoods seckillGoods : seckillGoodsList) {
            String stock = (String) redisTemplate.opsForValue().get(SECKILL_GOODS_STOCK_COUNT_KEY + seckillGoods.getId());
            seckillGoods.setStockCount(Integer.parseInt(stock));
        }


        return seckillGoodsList;
    }
}
