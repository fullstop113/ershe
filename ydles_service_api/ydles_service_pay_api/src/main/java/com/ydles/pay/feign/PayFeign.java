package com.ydles.pay.feign;

import com.ydles.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@FeignClient("pay")
public interface PayFeign {

    //微信下单
    @GetMapping("/wxpay/nativePay")
    public Result<Map> nativePay(@RequestParam("orderId")String orderId, @RequestParam("money")Integer money);

    //关闭订单
    @PutMapping("/wxpay/close/{orderId}")
    public Result<Map<String, String>> closeOrder(@PathVariable("orderId")String orderId);

    //查询订单
    @GetMapping("/wxpay/query/{orderId}")
    public Result<Map<String, String>> queryOrder(@PathVariable("orderId")String orderId);
}
