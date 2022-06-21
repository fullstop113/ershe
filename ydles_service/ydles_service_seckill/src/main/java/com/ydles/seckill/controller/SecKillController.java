package com.ydles.seckill.controller;

import com.ydles.entity.Result;
import com.ydles.entity.StatusCode;
import com.ydles.seckill.pojo.SeckillGoods;
import com.ydles.seckill.service.SecKillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@RestController
@RequestMapping("/seckillgoods")
public class SecKillController {
    @Autowired
    SecKillGoodsService secKillGoodsService;

    @GetMapping("/list")
    public Result<List<SeckillGoods>> list(@RequestParam("time")String time){
        List<SeckillGoods> seckillGoodsList = secKillGoodsService.list(time);
        return new Result(true, StatusCode.OK,"查询时间段秒杀商品成功",seckillGoodsList);
    }

}
