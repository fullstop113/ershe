package com.ydles.controller;

import com.ydles.entity.Result;
import com.ydles.order.feign.CartFeign;
import com.ydles.order.feign.OrderFeign;
import com.ydles.order.pojo.Order;
import com.ydles.order.pojo.OrderItem;
import com.ydles.user.feign.AddressFeign;
import com.ydles.user.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 * 缺啥补啥
 */
@Controller
@RequestMapping("/worder")
public class OrderController {
    @Autowired
    AddressFeign addressFeign;
    @Autowired
    CartFeign cartFeign;

    @GetMapping("/ready/order")
    public String readyOrder(Model model){
        //获取数据了
        //1 收件人
        List<Address> addressList = addressFeign.list();
        model.addAttribute("address",addressList);
        //默认地址
        for (Address address : addressList) {
            if(address.getIsDefault().equals("1")){
                model.addAttribute("deAddr",address);
                break;
            }
        }

        //2 购物车信息
        Map map = cartFeign.list();
        //总购物项
        List<OrderItem> orderItemList = (List<OrderItem>) map.get("orderItemList");
        model.addAttribute("carts",orderItemList);
        //总钱数
        Integer totalMoney = (Integer) map.get("totalMoney");
        model.addAttribute("totalMoney",totalMoney);
        //总件数
        Integer totalNum = (Integer) map.get("totalNum");
        model.addAttribute("totalNum",totalNum);

        return "order";
    }

    @Autowired
    OrderFeign orderFeign;

    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestBody Order order){
        Result result = orderFeign.add(order);

        return result;
    }

    @GetMapping("/toPayPage")
    public String toPayPage(String orderId,Model model){
        Order order = orderFeign.findById(orderId).getData();
        model.addAttribute("orderId",orderId);
        model.addAttribute("payMoney",order.getPayMoney());

        return "pay";
    }



}
