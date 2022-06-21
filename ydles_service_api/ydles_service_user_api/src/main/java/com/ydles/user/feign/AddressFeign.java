package com.ydles.user.feign;

import com.ydles.user.pojo.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@FeignClient("user")
public interface AddressFeign {

    @GetMapping("/address/list")
    public List<Address> list();
}
