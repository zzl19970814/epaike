package com.xinsteel.epaike.dao;

import com.xinsteel.epaike.pojo.OrderInfo;

public interface OrderInfoMapper {
    int deleteByPrimaryKey(String orderid);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(String orderid);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);
}