package com.xinsteel.epaike.dao;

import com.xinsteel.epaike.pojo.ProductInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductInfoMapper {
    int insert(ProductInfo record);

    int insertSelective(ProductInfo record);

    List<ProductInfo> selectAllProductInfo ();
}