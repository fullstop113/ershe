package com.ydles.order.controller;

import com.ydles.entity.Result;
import com.ydles.entity.StatusCode;
import com.ydles.order.config.TokenDecode;
import com.ydles.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;
    @Autowired
    TokenDecode tokenDecode;

    @GetMapping("/addCart")
    public Result addCart(@RequestParam("skuId")String skuId,@RequestParam("num")Integer num){
        //写死用户名
        //String username="itlils";

        //请求头Authorization->bearer jwt-->jwt-->第二部分--》base64解密--》map-->get(username)
        //动态
        String username = tokenDecode.getUserInfo().get("username");
        cartService.addCart(skuId,num,username);
        return new Result(true, StatusCode.OK,"添加购物车成功");
    }

    //查询购物车
    @GetMapping("/list")
    public Map list(){
        //String username="itlils";

        //动态
        String username = tokenDecode.getUserInfo().get("username");
        return cartService.list(username);
    }

}
