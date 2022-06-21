package com.ydles.user.dao;

import com.ydles.user.pojo.PointLog;
import com.ydles.user.pojo.Provinces;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
public interface PointLogMapper extends Mapper<PointLog> {

    @Select("select * from tb_point_log where order_id=#{orderId}")
    PointLog findPointLogByOrderId(@Param("orderId") String orderId);
}
