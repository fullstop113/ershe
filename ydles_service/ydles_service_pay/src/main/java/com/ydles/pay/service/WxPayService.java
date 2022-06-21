package com.ydles.pay.service;

import java.util.Map;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
public interface WxPayService {
    //微信下单要二维码
    public Map nativePay(String orderId,Integer money);

    //查询订单
    Map<String, String> orderquery(String orderId);

    //关闭订单
    Map<String, String> closeorder(String orderId);
}
