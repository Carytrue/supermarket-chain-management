package com.cqupt.th.supermarket.mapper;

import com.cqupt.th.supermarket.entity.Purchase;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 16075
* @description 针对表【purchase】的数据库操作Mapper
* @createDate 2023-04-05 16:27:30
* @Entity com.cqupt.th.supermarket.entity.Purchase
*/
public interface PurchaseMapper extends BaseMapper<Purchase> {

    void updatePurchaseByProductId(@Param("productId") Integer productId);

    void updatePurchaseByProductIds(@Param("productIds") Integer[] productIds);

    void updateTypeByPurchaseOrderId(@Param("purchaseId") Integer purchaseId);
}




