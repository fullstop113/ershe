package com.ydles.order.dao;

import com.ydles.order.pojo.Order;
import com.ydles.order.pojo.Task;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
public interface TaskMapper extends Mapper<Task> {

    //查询task 比当前时间小
    @Select("select * from tb_task where update_time<=#{currentTime}")
    @Results({@Result(column = "create_time",property = "createTime"),
            @Result(column = "update_time",property = "updateTime"),
            @Result(column = "delete_time",property = "deleteTime"),
            @Result(column = "task_type",property = "taskType"),
            @Result(column = "mq_exchange",property = "mqExchange"),
            @Result(column = "mq_routingkey",property = "mqRoutingkey"),
            @Result(column = "request_body",property = "requestBody"),
            @Result(column = "status",property = "status"),
            @Result(column = "errormsg",property = "errormsg")})
    public List<Task> findTaskLessThanCurrentTime(Date currentTime);

}
