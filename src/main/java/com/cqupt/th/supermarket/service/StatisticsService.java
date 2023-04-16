package com.cqupt.th.supermarket.service;

import com.cqupt.th.supermarket.entity.Product;
import com.cqupt.th.supermarket.utils.CommonResult;

/**
 * @author th
 * @date 2023/4/16 16:57
 */

public interface StatisticsService {
    CommonResult getPriceComparison(Product product);

    CommonResult getSupplierPriceChange(Product product);
}