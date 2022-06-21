package com.ydles.oauth.controller;

import com.ydles.entity.Result;
import com.ydles.entity.StatusCode;
import com.ydles.oauth.service.AuthService;
import com.ydles.oauth.util.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Controller
@RequestMapping("/oauth")
public class AuthController {
    @Autowired
    AuthService authService;
    @Value("${auth.clientId}")
    String clientId;
    @Value("${auth.clientSecret}")
    String clientSecret;
    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;
    @Value("${auth.cookieDomain}")
    String cookieDomain;

    @RequestMapping("/login")
    @ResponseBody
    public Result login(String username, String password, HttpServletResponse response){

        AuthToken authToken = authService.login(username, password, clientId, clientSecret);

        //三 responses cookie 放jti
        Cookie cookie = new Cookie("uid",authToken.getJti());
        cookie.setMaxAge(cookieMaxAge);
        cookie.setPath("/");
        cookie.setDomain(cookieDomain);
        cookie.setHttpOnly(false);
        response.addCookie(cookie);

        return new Result(true, StatusCode.OK,"登陆成功", authToken.getJti());
    }

    //跳转到登陆页面
    @GetMapping("/toLogin")
    public String toLogin(@RequestParam(value = "FROM",required = false,defaultValue = "")String from, Model model){
        model.addAttribute("from", from);
        return "login";
    }
}
