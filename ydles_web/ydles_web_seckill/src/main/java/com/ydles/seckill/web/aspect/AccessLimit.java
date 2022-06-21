package com.ydles.seckill.web.aspect;

import java.lang.annotation.*;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Retention(RetentionPolicy.RUNTIME) //什么时候起作用   运行时
@Target(ElementType.METHOD)  //能用在哪里
@Documented   //生成javadoc
public @interface AccessLimit {
}
