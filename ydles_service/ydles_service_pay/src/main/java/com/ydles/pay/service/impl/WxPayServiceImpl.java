package com.ydles.pay.service.impl;

import com.github.wxpay.sdk.MyConfig;
import com.github.wxpay.sdk.WXPay;
import com.ydles.pay.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Service
public class WxPayServiceImpl implements WxPayService {
    @Autowired
    WXPay wxPay;
    @Value("${wxpay.notify_url}")
    String notifyUrl;

    @Override
    public Map nativePay(String orderId, Integer money) {
        try {
            //根据官方文档 请求接口
            Map<String, String> reqData=new HashMap<>();
            reqData.put("body","动力二奢下单支付");
            reqData.put("out_trade_no", orderId);

            //金钱计算 BigDecimal:金额和计算都用他的
            BigDecimal yuan=new BigDecimal("0.01");
            BigDecimal beishu=new BigDecimal(100);
            BigDecimal fen = yuan.multiply(beishu);
            fen=fen.setScale(0,BigDecimal.ROUND_UP);
            reqData.put("total_fee", String.valueOf(fen));

            reqData.put("spbill_create_ip", "192.168.1.1");
            reqData.put("notify_url", notifyUrl);
            reqData.put("trade_type", "NATIVE");

            Map<String, String> resultMap = wxPay.unifiedOrder(reqData);
            System.out.println(resultMap);
            return resultMap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public Map<String, String> orderquery(String orderId) {
        try {
            Map<String, String> reqData=new HashMap<>();
            reqData.put("out_trade_no",orderId);
            Map<String, String> map = wxPay.orderQuery(reqData);

            return map;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Map<String, String> closeorder(String orderId) {
        try {
            Map<String, String> reqData=new HashMap<>();
            reqData.put("out_trade_no",orderId);
            Map<String, String> map = wxPay.closeOrder(reqData);

            return map;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
