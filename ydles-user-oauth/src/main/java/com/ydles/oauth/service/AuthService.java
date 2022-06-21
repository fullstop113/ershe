package com.ydles.oauth.service;

import com.ydles.oauth.util.AuthToken;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
public interface AuthService {

    //申请令牌
    public AuthToken login(String username,String password,String clientId,String clientSecret);
}
