package com.ydles.seckill.web.controller;

import com.ydles.entity.Result;
import com.ydles.seckill.feign.SecKillFeign;
import com.ydles.seckill.pojo.SeckillGoods;
import com.ydles.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Controller
@RequestMapping("/wseckillgoods")
public class SecKillGoodsController {

    @GetMapping("/toIndex")
    public String toIndex() {
        return "seckill-index";
    }

    @GetMapping("/timeMenus")
    @ResponseBody
    public List<String> timeMenus() {
        List<Date> dateMenus = DateUtil.getDateMenus();

        List<String> result = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Date dateMenu : dateMenus) {
            String format = simpleDateFormat.format(dateMenu);
            result.add(format);
        }

        return result;
    }

    @Autowired
    SecKillFeign secKillFeign;

    @GetMapping("/list")
    @ResponseBody
    public Result<List<SeckillGoods>> list(@RequestParam("time") String time) {
        //time   2021-12-28%2022:00:00  ---->   2021122816
        System.out.println(time);
        String formatStr = DateUtil.formatStr(time);
        System.out.println(formatStr);
        return secKillFeign.list(formatStr);
    }

}
