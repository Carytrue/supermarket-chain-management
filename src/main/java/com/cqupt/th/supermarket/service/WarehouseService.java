package com.cqupt.th.supermarket.service;

import com.cqupt.th.supermarket.entity.Warehouse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.th.supermarket.query.WarehouseQuery;
import com.cqupt.th.supermarket.utils.CommonResult;

/**
* @author 16075
* @description 针对表【warehouse】的数据库操作Service
* @createDate 2023-03-29 16:40:00
*/
public interface WarehouseService extends IService<Warehouse> {

    CommonResult getWarehouseByPage(Integer currentPage, Integer pageSize, WarehouseQuery warehouseQuery);

    CommonResult getWarehouseRegionIds(Integer regionId);

    CommonResult deleteWarehouseById(Integer id);

    CommonResult addWarehouse(Warehouse warehouse);

    CommonResult updateWarehouse(Integer id, Warehouse warehouse);

    CommonResult getWarehouseIdByRegionId(Integer regionId);

    CommonResult getRegionIdByWarehouseId(Integer warehouseId);
}
