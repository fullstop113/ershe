package com.ydles.controller;

import com.ydles.entity.Result;
import com.ydles.entity.StatusCode;
import com.ydles.order.feign.CartFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 *  模板加数据 返回给用户页面
 */
@Controller
@RequestMapping("/wcart")
public class CartController {
    @Autowired
    CartFeign cartFeign;

    //查询购物车
    @GetMapping("/list")
    public String list(Model model){
        Map map = cartFeign.list();
        model.addAttribute("items",map);

        return "cart";
    }

    //添加购物车
    @GetMapping("/add")
    @ResponseBody
    public Result add(String skuId,Integer num){
        cartFeign.addCart(skuId,num);

        //重新查询
        Map map = cartFeign.list();
        return new Result(true, StatusCode.OK,"添加购物车成功了",map);
    }

}
