package com.ydles.seckill.web.controller;

import com.ydles.entity.Result;
import com.ydles.entity.StatusCode;
import com.ydles.seckill.feign.SeckillOrderFeign;
import com.ydles.seckill.web.aspect.AccessLimit;
import com.ydles.seckill.web.util.CookieUtil;
import com.ydles.util.DateUtil;
import com.ydles.util.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@RestController
@RequestMapping("/wseckillorder")
public class SecKillOrderController {
    @Autowired
    SeckillOrderFeign seckillOrderFeign;
    @Autowired
    RedisTemplate redisTemplate;

    @GetMapping("/add")
    @AccessLimit //限流
    public Result add(@RequestParam("time")String time,@RequestParam("id")String id,@RequestParam("random")String random){
        //校验随机数是否正确
        String jti = readCookie();
        String redisRandom = (String) redisTemplate.boundValueOps("randomcode_" + jti).get();
        if(StringUtils.isEmpty(redisRandom)){
            return new Result(false,StatusCode.ERROR, "下单失败" );
        }
        if(!redisRandom.equals(random)){
            return new Result(false,StatusCode.ERROR, "下单失败" );
        }


        Result result = seckillOrderFeign.add(time, Long.parseLong(id));

        return result;
    }

    @GetMapping("/getToken")
    public String getToken(){
        //1 生成随机数
        String randomString = RandomUtil.getRandomString();
        //2 redis 放入随机数 jti-->random
        String jti = this.getToken();
        redisTemplate.boundValueOps("randomcode_"+jti).set(randomString,10, TimeUnit.SECONDS);

        return randomString;
    }

    //获取请求request当中的jti
    public String readCookie(){
        //获取request
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest();

        String jti = CookieUtil.readCookie(request, "uid").get("uid");
        return jti;
    }

}
