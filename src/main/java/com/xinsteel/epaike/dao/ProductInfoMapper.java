package com.xinsteel.epaike.dao;

import com.xinsteel.epaike.pojo.OrderInfo;
import com.xinsteel.epaike.pojo.ProductInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductInfoMapper {
    int insert(ProductInfo record);

    int insertSelective(ProductInfo record);

    List<ProductInfo> selectAllProductInfo ();

    int insertProductByOrderId(ProductInfo productInfo);

    int insertOrderInfoByOrderId(OrderInfo orderInfo);

    List selectApiMaterNoListByOrderId(String orderId);

    ProductInfo selectProductInfoByApiMaterNo(String apiMaterNo);
}