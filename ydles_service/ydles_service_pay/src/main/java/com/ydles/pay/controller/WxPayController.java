package com.ydles.pay.controller;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import com.ydles.entity.Result;
import com.ydles.entity.StatusCode;
import com.ydles.pay.config.RabbitMQConfig;
import com.ydles.pay.service.WxPayService;
import com.ydles.util.ConvertUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@RestController
@RequestMapping("/wxpay")
public class WxPayController {
    @Autowired
    WxPayService wxPayService;

    //微信下单
    @GetMapping("/nativePay")
    public Result<Map> nativePay(@RequestParam("orderId")String orderId,@RequestParam("money")Integer money){
        Map map = wxPayService.nativePay(orderId, money);
        return new Result(true, StatusCode.OK,"微信下单成功",map);
    }

    @Autowired
    RabbitTemplate rabbitTemplate;
    @RequestMapping("/notify")
    public void notifyLogic(HttpServletRequest request, HttpServletResponse response){
        System.out.println("访问到 notify接口了！");
        //给微信正确的回应
        try {
            String xml = ConvertUtils.convertToString(request.getInputStream());
            System.out.println(xml);

            //微信给我们提供的工具类  xml--->map
            Map<String, String> map = WXPayUtil.xmlToMap(xml);
            if("SUCCESS".equals(map.get("return_code"))&&"SUCCESS".equals(map.get("result_code"))){
                String orderId = map.get("out_trade_no");
                String transactionId = map.get("transaction_id");

                Map<String, String> messageMap=new HashMap();
                messageMap.put("orderId",orderId);
                messageMap.put("transactionId",transactionId);

                //往mq发消息 order_pay
                rabbitTemplate.convertAndSend("", RabbitMQConfig.ORDER_PAY, JSON.toJSONString(messageMap));

                //往双工 交换机 发消息
                rabbitTemplate.convertAndSend("paynotify","",orderId);

            }else {
                System.out.println("出错了return_msg"+map.get("return_msg"));
            }


            //响应
            response.setContentType("text/xml");
            String data="<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
            response.getWriter().write(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //关闭订单
    @PutMapping("/close/{orderId}")
    public Result<Map<String, String>> closeOrder(@PathVariable("orderId")String orderId){
        Map<String, String> map = wxPayService.closeorder(orderId);
        return new Result(true,StatusCode.OK,"关闭订单成功", map);
    }

    //查询订单
    @GetMapping("/query/{orderId}")
    public Result<Map<String, String>> queryOrder(@PathVariable("orderId")String orderId){
        Map<String, String> map = wxPayService.orderquery(orderId);
        return new Result(true,StatusCode.OK,"查询订单成功", map);
    }
}
