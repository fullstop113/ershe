package com.ydles.order.feign;

import com.ydles.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@FeignClient("order")
public interface CartFeign {

    @GetMapping("/cart/addCart")
    public Result addCart(@RequestParam("skuId")String skuId, @RequestParam("num")Integer num);

    @GetMapping("/cart/list")
    public Map list();

}
