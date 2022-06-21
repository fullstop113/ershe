package com.ydles.seckill.feign;

import com.ydles.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@FeignClient(name = "seckill")
public interface SeckillOrderFeign {

    //秒杀下单
    @RequestMapping("/seckillorder/add")
    public Result add(@RequestParam("time")String time, @RequestParam("id")Long id);
}
