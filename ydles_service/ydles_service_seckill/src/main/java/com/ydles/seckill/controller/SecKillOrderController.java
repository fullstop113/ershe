package com.ydles.seckill.controller;

import com.ydles.entity.Result;
import com.ydles.entity.StatusCode;
import com.ydles.seckill.config.TokenDecode;
import com.ydles.seckill.service.SecKillOrderService;
import com.ydles.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@RestController
@CrossOrigin
@RequestMapping("/seckillorder")
public class SecKillOrderController {
    @Autowired
    TokenDecode tokenDecode;
    @Autowired
    SecKillOrderService secKillOrderService;

    //秒杀下单
    @RequestMapping("/add")
    public Result add(@RequestParam("time")String time, @RequestParam("id")Long id){
        //获取当前登录人
        String username = tokenDecode.getUserInfo().get("username");
        //String formatStr = DateUtil.formatStr(time);
        boolean result = secKillOrderService.add(id, time, username);

        //根据下单成功或失败，返回Rseult
        if(result){
            return new Result(true, StatusCode.OK,"下单成功");
        }else {
            return new Result(false, StatusCode.ERROR,"下单失败");
        }
    }


}
