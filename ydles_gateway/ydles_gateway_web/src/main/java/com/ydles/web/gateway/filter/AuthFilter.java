package com.ydles.web.gateway.filter;

import com.ydles.web.gateway.utils.UrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Component
public class AuthFilter implements Ordered, GlobalFilter {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //登陆页面地址
    public static final String LOGIN_URL = "http://localhost:8001/api/oauth/toLogin";

    //业务逻辑
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //1.1登陆时：放行
        String path = request.getURI().getPath(); // /oauth/login
        System.out.println("path:" + path);
        if (!UrlUtil.hasAuthorize(path)) {
            //放行
            return chain.filter(exchange);
        }

        //1.2用户访问时：
        //- 1）判断当前请求是否为登录请求，是的话，则放行
        //- 2 )判断cookie中是否存在信息, 没有的话，拒绝访问
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        HttpCookie cookie = cookies.getFirst("uid");
        if (cookie == null) {
            //拒绝访问
            //response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //return response.setComplete();

            //重定向 登陆页面
            return toLoginPage(response,LOGIN_URL+"?FROM="+path);
        }

        //- 3）判断redis中令牌是否存在，没有的话，拒绝访问
        String jti = cookie.getValue();
        String jwt = stringRedisTemplate.opsForValue().get(jti);
        if (StringUtils.isEmpty(jwt)) {
            //拒绝访问
            //response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //return response.setComplete();

            //重定向 登陆页面
            return toLoginPage(response,LOGIN_URL+"?FROM="+path);
        }

        //4) 拼接 jwt到请求头中
        request.mutate().header("Authorization", "Bearer " + jwt);

        //放行
        return chain.filter(exchange);
    }

    //跳到登陆页面
    private Mono<Void> toLoginPage(ServerHttpResponse response, String loginUrl) {
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location", loginUrl);
        return response.setComplete();
    }

    //顺序
    @Override
    public int getOrder() {
        return 0;
    }
}
