package com.ydles.consume.service.impl;

import com.ydles.consume.dao.SeckillGoodsMapper;
import com.ydles.consume.dao.SeckillOrderMapper;
import com.ydles.consume.service.SeckillOrderService;
import com.ydles.seckill.pojo.SeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {
    @Autowired
    SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    SeckillOrderMapper seckillOrderMapper;

    @Override
    @Transactional
    public int createOrder(SeckillOrder seckillOrder) {
        //1更改库存
        int result = seckillGoodsMapper.updateStockCount(seckillOrder.getSeckillId());
        if(result<=0){
            return result;
        }
        //2添加订单
        int insertResult = seckillOrderMapper.insertSelective(seckillOrder);
        if(insertResult<=0){
            return insertResult;
        }
        return 1;
    }
}
