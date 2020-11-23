package com.xinsteel.epaike.dao;

import com.xinsteel.epaike.pojo.ProductInfo;

public interface ProductInfoMapper {
    int insert(ProductInfo record);

    int insertSelective(ProductInfo record);
}