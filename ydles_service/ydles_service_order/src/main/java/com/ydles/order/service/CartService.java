package com.ydles.order.service;

import java.util.Map;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 * 购物车服务层
 */
public interface CartService {

    //添加购物车
    public void addCart(String skuId,Integer num,String username);

    //查询购物车
    public Map list(String username);
}
