package com.ydles.user.feign;

import com.ydles.user.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@FeignClient("user")
public interface UserFeign {

    @GetMapping("/user/load/{username}")
    public User findUserInfo(@PathVariable("username") String username);
}
