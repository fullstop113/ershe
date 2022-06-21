package com.ydles.order.feign;

import com.ydles.entity.Result;
import com.ydles.order.pojo.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@FeignClient("order")
public interface OrderFeign {

    @PostMapping("/order")
    public Result add(@RequestBody Order order);

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping("/order/{id}")
    public Result<Order> findById(@PathVariable String id);
}
