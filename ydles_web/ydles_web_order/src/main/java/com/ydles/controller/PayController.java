package com.ydles.controller;

import com.ydles.entity.Result;
import com.ydles.order.feign.OrderFeign;
import com.ydles.order.pojo.Order;
import com.ydles.pay.feign.PayFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Controller
@RequestMapping("/wxpay")
public class PayController {
    @Autowired
    OrderFeign orderFeign;
    @Autowired
    PayFeign payFeign;

    //跳转到微信支付页面 wxpay.html
    @GetMapping
    public String wxpay(@RequestParam("orderId")String orderId, Model model){
        //1查订单的金额
        Order order = orderFeign.findById(orderId).getData();
        if(order==null){
            return "fail";
        }
        if(!order.getPayStatus().equals("0")){
            return "fail";
        }
        //2申请支付二维码
        Result<Map> mapResult = payFeign.nativePay(orderId, order.getPayMoney());
        if(mapResult.getData()==null){
            return "fail";
        }
        Map map = mapResult.getData();
        //3封装结果到model
        map.put("orderId",orderId);
        map.put("payMoney", order.getPayMoney());
        model.addAllAttributes(map);

        return "wxpay";
    }

    @GetMapping("/toPaySeccess")
    public String toPaySeccess(String payMoney,Model model){
        model.addAttribute("payMoney",payMoney);

        return "paysuccess";
    }

}
