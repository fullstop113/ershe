package com.github.wxpay.sdk;

import java.io.InputStream;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 *  微信支付配置类，放着商户的相关信息
 */
public class MyConfig extends WXPayConfig{

    @Override
    String getAppID() {
        return "wxababcd122d1618eb";
    }

    @Override
    String getMchID() {
        return "1611671554";
    }

    @Override
    String getKey() {
        return "ydlclass66666688888YDLCLASS66688";
    }

    @Override
    InputStream getCertStream() {
        return null;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String s, long l, Exception e) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig wxPayConfig) {
                return new DomainInfo("api.mch.weixin.qq.com", true);
            }
        };
    }
}
