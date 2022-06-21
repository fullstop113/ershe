package com.ydles.seckill.dao;

import com.ydles.seckill.pojo.SeckillOrder;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;


public interface SeckillOrderMapper extends Mapper<SeckillOrder> {

    //根据用户名和秒杀商品的id查询秒杀订单
    @Select("select * from tb_seckill_order where seckill_id=#{id} and user_id=#{username}")
    SeckillOrder getOrderInfoByUserNameAndGoodsId(@Param("username") String username, @Param("id") Long id);

}
