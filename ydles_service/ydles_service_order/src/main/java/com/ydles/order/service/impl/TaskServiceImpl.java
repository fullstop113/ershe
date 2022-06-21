package com.ydles.order.service.impl;

import com.ydles.order.dao.TaskHisMapper;
import com.ydles.order.dao.TaskMapper;
import com.ydles.order.pojo.Task;
import com.ydles.order.pojo.TaskHis;
import com.ydles.order.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Created by IT李老师
 * 公主号 “IT李哥交朋友”
 * 个人微 itlils
 */
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    TaskHisMapper taskHisMapper;


    @Override
    @Transactional
    public void delTask(Task task) {
        //14 历史积分表添加数据
        TaskHis taskHis=new TaskHis();
        BeanUtils.copyProperties(task,taskHis);
        taskHis.setId(null);
        taskHis.setDeleteTime(new Date()); //删除时间
        taskHisMapper.insertSelective(taskHis);

        //任务表中 删除数据
        taskMapper.deleteByPrimaryKey(task.getId());

        System.out.println("添加历史任务，删除任务");

    }
}
