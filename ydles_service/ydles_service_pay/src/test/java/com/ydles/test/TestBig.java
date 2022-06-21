package com.ydles.test;

import java.math.BigDecimal;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
public class TestBig {
    public static void main(String[] args) {

        BigDecimal yuan=new BigDecimal(99.999999);
        BigDecimal beishu=new BigDecimal(100);
        BigDecimal fen = yuan.multiply(beishu);
        fen=fen.setScale(0,BigDecimal.ROUND_DOWN);
        System.out.println(String.valueOf(fen));
    }
}
