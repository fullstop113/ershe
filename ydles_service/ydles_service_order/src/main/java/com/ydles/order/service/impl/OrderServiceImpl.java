package com.ydles.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.ydles.goods.feign.SkuFeign;
import com.ydles.order.config.RabbitMQConfig;
import com.ydles.order.dao.*;
import com.ydles.order.pojo.*;
import com.ydles.order.service.CartService;
import com.ydles.order.service.OrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ydles.pay.feign.PayFeign;
import com.ydles.util.IdWorker;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<Order> findAll() {
        return orderMapper.selectAll();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Order findById(String id){
        return  orderMapper.selectByPrimaryKey(id);
    }

    @Autowired
    CartService cartService;
    @Autowired
    IdWorker idWorker;
    @Autowired
    OrderItemMapper orderItemMapper;
    //购物车redis key 头
    private static final String CART = "cart_";
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    SkuFeign skuFeign;
    @Autowired
    TaskMapper taskMapper;

    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     * 下单
     * @param order
     * 缺啥补啥
     */
    @Override
    @Transactional //spring 给我们带的事务控制，控制本地事务
    //@GlobalTransactional(name = "order_add")
    public String add(Order order){
        //1 获取 购物车信息
        Map cartMap = cartService.list(order.getUsername());
        //map.put("orderItemList",orderItemList);
        Integer totalNum = (Integer) cartMap.get("totalNum");
        Integer totalMoney = (Integer) cartMap.get("totalMoney");

        //2 order表里存数据
        String orderId = idWorker.nextId()+"";
        order.setId(orderId);
        order.setTotalNum(totalNum);
        order.setTotalMoney(totalMoney);
        //作业：优惠金额 怎么算      本店满50-5 跨店 300-50
        //作业：邮费金额 怎么算
        order.setPayMoney(totalMoney);
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        //作业：买家留言
        order.setBuyerRate("0");
        order.setSourceType("1");
        order.setOrderStatus("0");
        order.setPayStatus("0");
        order.setConsignStatus("0");
        order.setIsDelete("0");

        orderMapper.insertSelective(order);

        //3 orderItem表里存数据
        List<OrderItem> orderItemList = (List<OrderItem>) cartMap.get("orderItemList");
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderId(orderId);
            orderItem.setPostFee(0);
            orderItem.setIsReturn("0");
            orderItemMapper.insertSelective(orderItem);
        }

        //远程调用减库存
        skuFeign.decrCount(order.getUsername());

        //遇到错了
        //int a=1/0;

        //添加积分逻辑
        System.out.println("往任务表里填数据");


        //4 删除购物车信息
        stringRedisTemplate.delete(CART+order.getUsername());

        Task task=new Task();
        task.setCreateTime(new Date());
        task.setUpdateTime(new Date());
        task.setMqExchange(RabbitMQConfig.EX_BUYING_ADDPOINTUSER);
        task.setMqRoutingkey(RabbitMQConfig.CG_BUYING_ADDPOINT_KEY);

        //RequestBody 消息数据 需要order_id user_id point
        Map map=new HashMap();
        map.put("order_id", orderId);
        map.put("user_id", order.getUsername());
        map.put("point", totalMoney); //消费多少 积分关系
        task.setRequestBody(JSON.toJSONString(map));

        taskMapper.insertSelective(task);

        //往延时队列发消息
        rabbitTemplate.convertAndSend("","queue.ordercreate",orderId);


        return orderId;
    }


    /**
     * 修改
     * @param order
     */
    @Override
    public void update(Order order){
        orderMapper.updateByPrimaryKey(order);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id){
        orderMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<Order> findList(Map<String, Object> searchMap){
        Example example = createExample(searchMap);
        return orderMapper.selectByExample(example);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Order> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return (Page<Order>)orderMapper.selectAll();
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<Order> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        return (Page<Order>)orderMapper.selectByExample(example);
    }

    @Autowired
    OrderLogMapper orderLogMapper;

    @Override
    @Transactional
    public void updatePayStatus(String orderId, String transactionId) {
        //1查一下这笔订单状态
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(order!=null&&"0".equals(order.getPayStatus())){
            //订单存在并且未支付
            order.setPayStatus("1");
            order.setOrderStatus("1"); //订单状态 0下单 1支付 2发货 3收货 4退货
            order.setTransactionId(transactionId);
            order.setPayTime(new Date());
            order.setUpdateTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);

            //往订单日志表里 添加数据
            OrderLog orderLog = new OrderLog();
            orderLog.setId(idWorker.nextId()+"");
            orderLog.setOperater("system");
            orderLog.setOperateTime(new Date());
            orderLog.setOrderId(orderId);
            orderLog.setOrderStatus("1");
            orderLog.setPayStatus("1");
            orderLog.setConsignStatus("0");
            orderLog.setRemarks("支付，微信流水号是:"+transactionId);
            orderLogMapper.insertSelective(orderLog);

        }


    }

    @Autowired
    PayFeign payFeign;
    //关闭订单
    @Override
    @Transactional
    public void closeOrder(String orderId) {
        System.out.println("关闭订单开启了:"+orderId);

        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(order==null){
            throw new RuntimeException("这笔订单不存在！");
        }
        if(!order.getOrderStatus().equals("0")){
            System.out.println("这笔订单不用关闭");
            return;
        }
        System.out.println("关闭订单逻辑通过校验："+orderId);


        //1支付服务 微信查询订单
        Map<String, String> wxQueryMap = payFeign.queryOrder(orderId).getData();

        if(wxQueryMap.get("trade_state")!=null){
            //2.1支付了 order表修改
            if(wxQueryMap.get("trade_state").equals("SUCCESS")){
                updatePayStatus(orderId,wxQueryMap.get("transaction_id"));
                System.out.println("已支付"+orderId);
            }

            //2.2未支付 关闭订单微信 内部回滚库存 订单状态关闭
            if(wxQueryMap.get("trade_state").equals("NOTPAY")){
                //1关闭订单微信
                payFeign.closeOrder(orderId);

                //2订单状态关闭
                System.out.println("本项目关闭订单了");
                order.setOrderStatus("9");//订单状态 0下单 1支付 2发货 3收货 4退货 9关闭
                order.setCloseTime(new Date());
                orderMapper.updateByPrimaryKeySelective(order);

                //orderLog表 新增数据
                OrderLog orderLog = new OrderLog();
                orderLog.setId(idWorker.nextId()+"");
                orderLog.setOperater("system");
                orderLog.setOperateTime(new Date());
                orderLog.setOrderId(orderId);
                orderLog.setOrderStatus("9");
                orderLog.setPayStatus("0");
                orderLog.setConsignStatus("0");
                orderLog.setRemarks("超时未支付！");
                orderLogMapper.insertSelective(orderLog);


                //3内部回滚库存

                //查出来这笔订单的所有购物项
                OrderItem orderItem=new OrderItem();
                orderItem.setOrderId(orderId);
                List<OrderItem> orderItemList = orderItemMapper.select(orderItem);

                for (OrderItem orderItem1 : orderItemList) {
                    skuFeign.resumeStock(orderItem1.getSkuId(),orderItem1.getNum());
                }

            }

            //还有各种支付状态 都需要考虑。REFUND CLOSED USERPAYING
        }



    }

    @Override
    @Transactional
    public void batchSend(List<Order> orderList) {
        //循环1 物流公司和物流单号 不能为空
        for (Order order : orderList) {
            if(order.getId()==null){
                throw new RuntimeException("订单号为空！");
            }
            if(order.getShippingName()==null||order.getShippingCode()==null){
                throw new RuntimeException("物流公司或单号为空！");
            }
        }

        //循环2 查询订单状态 校验
        for (Order order : orderList) {
            Order queryOrder = orderMapper.selectByPrimaryKey(order.getId());
            if(!queryOrder.getOrderStatus().equals("1")||!queryOrder.getConsignStatus().equals("0")){
                throw new RuntimeException("订单状态不对，不能发货！");
            }
        }

        //循环3 发货
        for (Order order : orderList) {
            order.setOrderStatus("2");
            order.setConsignStatus("1");
            order.setUpdateTime(new Date());
            order.setConsignTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);

            //orderLog表 新增数据
            OrderLog orderLog = new OrderLog();
            orderLog.setId(idWorker.nextId()+"");
            orderLog.setOperater("店小二");
            orderLog.setOperateTime(new Date());
            orderLog.setOrderId(order.getId());
            orderLog.setOrderStatus("2");
            orderLog.setPayStatus("1");
            orderLog.setConsignStatus("1");
            orderLog.setRemarks("批量发货");
            orderLogMapper.insertSelective(orderLog);
        }

    }

    @Override
    @Transactional
    public void take(String orderId, String operator) {
        //1判断订单状态
        if(StringUtils.isEmpty(orderId)){
            throw new RuntimeException("订单号为空");
        }
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(order==null){
            throw new RuntimeException("订单为空");
        }
        if(!order.getConsignStatus().equals("1")){
            throw new RuntimeException("订单未发货！");
        }

        //2order属性改了
        order.setConsignStatus("2");
        order.setOrderStatus("3");
        order.setUpdateTime(new Date());
        order.setEndTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);

        //3orderLog 新增数据
        OrderLog orderLog = new OrderLog();
        orderLog.setId(idWorker.nextId()+"");
        orderLog.setOperater(operator);
        orderLog.setOperateTime(new Date());
        orderLog.setOrderId(order.getId());
        orderLog.setOrderStatus("3");
        orderLog.setPayStatus("1");
        orderLog.setConsignStatus("2");
        orderLog.setRemarks("收货了！");
        orderLogMapper.insertSelective(orderLog);

    }

    @Autowired
    OrderConfigMapper orderConfigMapper;

    @Override
    public void autoTack() {
        //1 从配置表中获取15天值
        OrderConfig orderConfig = orderConfigMapper.selectByPrimaryKey("1");
        Integer takeTimeout = orderConfig.getTakeTimeout();//15

        //2 推算拿几号之前发货的订单
        LocalDate now=LocalDate.now();//当前
        LocalDate date = now.plusDays(-takeTimeout);
        System.out.println(date);

        //3 查询发货超过15天的订单
        Example example=new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderStatus","2");
        criteria.andLessThan("consignTime", date);
        List<Order> orderList = orderMapper.selectByExample(example);

        //4 循环 把这些订单 收货
        for (Order order : orderList) {
            take(order.getId(),"system");
        }
    }

    /**
     * 构建查询对象
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 订单id
            if(searchMap.get("id")!=null && !"".equals(searchMap.get("id"))){
                criteria.andEqualTo("id",searchMap.get("id"));
           	}
            // 支付类型，1、在线支付、0 货到付款
            if(searchMap.get("payType")!=null && !"".equals(searchMap.get("payType"))){
                criteria.andEqualTo("payType",searchMap.get("payType"));
           	}
            // 物流名称
            if(searchMap.get("shippingName")!=null && !"".equals(searchMap.get("shippingName"))){
                criteria.andLike("shippingName","%"+searchMap.get("shippingName")+"%");
           	}
            // 物流单号
            if(searchMap.get("shippingCode")!=null && !"".equals(searchMap.get("shippingCode"))){
                criteria.andLike("shippingCode","%"+searchMap.get("shippingCode")+"%");
           	}
            // 用户名称
            if(searchMap.get("username")!=null && !"".equals(searchMap.get("username"))){
                criteria.andLike("username","%"+searchMap.get("username")+"%");
           	}
            // 买家留言
            if(searchMap.get("buyerMessage")!=null && !"".equals(searchMap.get("buyerMessage"))){
                criteria.andLike("buyerMessage","%"+searchMap.get("buyerMessage")+"%");
           	}
            // 是否评价
            if(searchMap.get("buyerRate")!=null && !"".equals(searchMap.get("buyerRate"))){
                criteria.andLike("buyerRate","%"+searchMap.get("buyerRate")+"%");
           	}
            // 收货人
            if(searchMap.get("receiverContact")!=null && !"".equals(searchMap.get("receiverContact"))){
                criteria.andLike("receiverContact","%"+searchMap.get("receiverContact")+"%");
           	}
            // 收货人手机
            if(searchMap.get("receiverMobile")!=null && !"".equals(searchMap.get("receiverMobile"))){
                criteria.andLike("receiverMobile","%"+searchMap.get("receiverMobile")+"%");
           	}
            // 收货人地址
            if(searchMap.get("receiverAddress")!=null && !"".equals(searchMap.get("receiverAddress"))){
                criteria.andLike("receiverAddress","%"+searchMap.get("receiverAddress")+"%");
           	}
            // 订单来源：1:web，2：app，3：微信公众号，4：微信小程序  5 H5手机页面
            if(searchMap.get("sourceType")!=null && !"".equals(searchMap.get("sourceType"))){
                criteria.andEqualTo("sourceType",searchMap.get("sourceType"));
           	}
            // 交易流水号
            if(searchMap.get("transactionId")!=null && !"".equals(searchMap.get("transactionId"))){
                criteria.andLike("transactionId","%"+searchMap.get("transactionId")+"%");
           	}
            // 订单状态
            if(searchMap.get("orderStatus")!=null && !"".equals(searchMap.get("orderStatus"))){
                criteria.andEqualTo("orderStatus",searchMap.get("orderStatus"));
           	}
            // 支付状态
            if(searchMap.get("payStatus")!=null && !"".equals(searchMap.get("payStatus"))){
                criteria.andEqualTo("payStatus",searchMap.get("payStatus"));
           	}
            // 发货状态
            if(searchMap.get("consignStatus")!=null && !"".equals(searchMap.get("consignStatus"))){
                criteria.andEqualTo("consignStatus",searchMap.get("consignStatus"));
           	}
            // 是否删除
            if(searchMap.get("isDelete")!=null && !"".equals(searchMap.get("isDelete"))){
                criteria.andEqualTo("isDelete",searchMap.get("isDelete"));
           	}

            // 数量合计
            if(searchMap.get("totalNum")!=null ){
                criteria.andEqualTo("totalNum",searchMap.get("totalNum"));
            }
            // 金额合计
            if(searchMap.get("totalMoney")!=null ){
                criteria.andEqualTo("totalMoney",searchMap.get("totalMoney"));
            }
            // 优惠金额
            if(searchMap.get("preMoney")!=null ){
                criteria.andEqualTo("preMoney",searchMap.get("preMoney"));
            }
            // 邮费
            if(searchMap.get("postFee")!=null ){
                criteria.andEqualTo("postFee",searchMap.get("postFee"));
            }
            // 实付金额
            if(searchMap.get("payMoney")!=null ){
                criteria.andEqualTo("payMoney",searchMap.get("payMoney"));
            }

        }
        return example;
    }

}
