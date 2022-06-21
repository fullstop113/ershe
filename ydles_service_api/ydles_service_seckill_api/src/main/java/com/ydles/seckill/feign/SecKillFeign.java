package com.ydles.seckill.feign;

import com.ydles.entity.Result;
import com.ydles.seckill.pojo.SeckillGoods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@FeignClient(name = "seckill")
public interface SecKillFeign {

    @GetMapping("/seckillgoods/list")
    public Result<List<SeckillGoods>> list(@RequestParam("time")String time);


}
