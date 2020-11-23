package com.xinsteel.epaike.dao;

import com.xinsteel.epaike.pojo.NodeInfo;

public interface NodeInfoMapper {
    int deleteByPrimaryKey(String orderid);

    int insert(NodeInfo record);

    int insertSelective(NodeInfo record);

    NodeInfo selectByPrimaryKey(String orderid);

    int updateByPrimaryKeySelective(NodeInfo record);

    int updateByPrimaryKey(NodeInfo record);
}