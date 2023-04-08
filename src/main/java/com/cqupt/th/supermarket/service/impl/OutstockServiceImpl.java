package com.cqupt.th.supermarket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.th.supermarket.entity.*;
import com.cqupt.th.supermarket.mapper.InventoryMapper;
import com.cqupt.th.supermarket.mapper.ProductMapper;
import com.cqupt.th.supermarket.mapper.WarehouseMapper;
import com.cqupt.th.supermarket.query.OutstockQuery;
import com.cqupt.th.supermarket.service.OutstockService;
import com.cqupt.th.supermarket.mapper.OutstockMapper;
import com.cqupt.th.supermarket.service.RegionService;
import com.cqupt.th.supermarket.utils.CommonResult;
import com.cqupt.th.supermarket.vo.InstockVo;
import com.cqupt.th.supermarket.vo.OutstockVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 16075
 * @description 针对表【outstock】的数据库操作Service实现
 * @createDate 2023-04-07 12:48:21
 */
@Service("outstockService")
public class OutstockServiceImpl extends ServiceImpl<OutstockMapper, Outstock>
        implements OutstockService {
@Resource
private ProductMapper productMapper;
@Autowired
@Qualifier("regionService")
private RegionService regionService;
@Resource
private WarehouseMapper warehouseMapper;
@Resource
private InventoryMapper inventoryMapper;
    @Override
    public CommonResult getOutstockListPage(Integer currentPage, Integer pageSize, OutstockQuery outstockQuery) {
        if (currentPage <= 0 || pageSize <= 0) {
            return CommonResult.error().message("参数错误");
        }
        QueryWrapper<Outstock> outstockQueryWrapper = new QueryWrapper<>();
        outstockQueryWrapper.orderByDesc("gmt_modified");
        if (outstockQuery != null) {
            if (outstockQuery.getProductId() != null) {
                outstockQueryWrapper.eq("product_id", outstockQuery.getProductId());
            }
            if (outstockQuery.getWarehouseId() != null) {
                outstockQueryWrapper.eq("warehouse_id", outstockQuery.getWarehouseId());
            }
            if (outstockQuery.getOutstockCount() != null) {
                outstockQueryWrapper.eq("outstock_count", outstockQuery.getOutstockCount());
            }
            if (outstockQuery.getToWarehouseId() != null) {
                outstockQueryWrapper.eq("to_warehouse_id", outstockQuery.getToWarehouseId());
            }
            if(outstockQuery.getStartTime() != null){
                outstockQueryWrapper.ge("gmt_create", outstockQuery.getStartTime());
            }
            if(outstockQuery.getEndTime() != null){
                outstockQueryWrapper.le("gmt_create", outstockQuery.getEndTime());
            }
        }
        Page<Outstock> outstockPage = new Page<>(currentPage, pageSize);
       baseMapper.selectPage(outstockPage, outstockQueryWrapper);
        long total = outstockPage.getTotal();
        List<Outstock> records = outstockPage.getRecords();
        HashMap<Integer, String> productHashMap = new HashMap<>();
        productMapper.selectList(null).stream().forEach(product -> {
            productHashMap.put(product.getId(), product.getName());
        });
        HashMap<Integer, Region> regionHashMap = new HashMap<>();
        regionService.list(null).stream().forEach(r -> {
            regionHashMap.put(r.getId(), r);
        });
        List<OutstockVo> rows = records.stream().map(r -> {
            OutstockVo outstockVo = new OutstockVo();
            BeanUtils.copyProperties(r, outstockVo);
            outstockVo.setProductName(productHashMap.get(r.getProductId()));
            outstockVo.setWarehouseRegion(regionService.getRegionName(getRegionIdByWarehouseId(r.getWarehouseId()), regionHashMap));
            outstockVo.setToWarehouseRegion(regionService.getRegionName(getRegionIdByWarehouseId(r.getToWarehouseId()), regionHashMap));
            return outstockVo;
        }).collect(Collectors.toList());
        return CommonResult.ok().data("total", total).data("rows", rows);
    }

    @Override
    public CommonResult getwarehouseIdByRegionId(Integer regionId) {
        if (regionId == null) {
            return CommonResult.error().message("参数错误");
        }
        Warehouse warehouse = warehouseMapper.selectOne(new QueryWrapper<Warehouse>().eq("region_id", regionId));
        int id = -1;
        if (warehouse != null) {
            id = warehouse.getId();
        }
        return CommonResult.ok().data("item", id);
    }

    @Override
    public CommonResult deleteOutstockById(Integer id) {
        if (id == null) {
            return CommonResult.error().message("参数错误");
        }
        //TODO 库存的删除
        Outstock outstock = baseMapper.selectById(id);
        if (outstock == null) {
            return CommonResult.error().message("参数错误");
        }
        Inventory inventory = inventoryMapper.selectOne(new QueryWrapper<Inventory>().eq("warehouse_id", outstock.getWarehouseId()).eq("product_id", outstock.getProductId()));
        if (inventory == null) {
            return CommonResult.error().message("参数错误");
        }
        inventory.setOutstockCount(inventory.getOutstockCount() - outstock.getOutstockCount());
        inventory.setInventoryCount(inventory.getInventoryCount() + outstock.getOutstockCount());
        inventoryMapper.updateById(inventory);
        int i = baseMapper.deleteById(id);
        if (i == 0) {
            return CommonResult.error().message("删除失败");
        }
        return CommonResult.ok().message("删除成功");
    }

    @Override
    public CommonResult updateOutstockById(Integer id, Outstock outstock) {
        if (id == null || outstock == null) {
            return CommonResult.error().message("参数错误");
        }
        outstock.setId(id);
        Outstock outstock1 = baseMapper.selectById(id);
        if (outstock1 == null) {
            return CommonResult.error().message("参数错误");
        }
        Inventory inventory = inventoryMapper.selectOne(new QueryWrapper<Inventory>().eq("warehouse_id", outstock1.getWarehouseId()).eq("product_id", outstock1.getProductId()));
        if (inventory == null) {
            return CommonResult.error().message("参数错误");
        }
        if (outstock.getProductId() == inventory.getProductId() && outstock.getWarehouseId() == inventory.getWarehouseId()) {
            inventory.setOutstockCount(inventory.getOutstockCount() - outstock1.getOutstockCount() + outstock.getOutstockCount());
            inventory.setInventoryCount(inventory.getInventoryCount() + outstock1.getOutstockCount() + outstock.getOutstockCount());
            inventoryMapper.updateById(inventory);
        } else {
            inventory.setOutstockCount(inventory.getOutstockCount() - outstock1.getOutstockCount());
            inventory.setInventoryCount(inventory.getInventoryCount() + outstock1.getOutstockCount());
            inventoryMapper.updateById(inventory);
            Inventory inventory1 = new Inventory();
            BeanUtils.copyProperties(outstock, inventory1);
            inventory1.setInventoryCount(inventory1.getInstockCount());
            inventory1.setId(null);
            inventoryMapper.insert(inventory1);
        }
        //TODO 库存的更新
        int i = baseMapper.updateById(outstock);
        if (i == 0) {
            return CommonResult.error().message("更新失败");
        }
        return CommonResult.ok().message("更新成功");
    }

    @Override
    public CommonResult addOutstock(Outstock outstock) {
        if (outstock == null) {
            return CommonResult.error().message("参数错误");
        }
        //TODO 库存的添加
        Inventory inventory = inventoryMapper.selectOne(new QueryWrapper<Inventory>().eq("warehouse_id", outstock.getWarehouseId()).eq("product_id", outstock.getProductId()));
        if (inventory == null) {
            Inventory inventory1 = new Inventory();
            BeanUtils.copyProperties(outstock, inventory1);
            inventory1.setInventoryCount(inventory1.getInstockCount());
            inventoryMapper.insert(inventory1);
        } else {
            inventory.setOutstockCount(inventory.getOutstockCount() + outstock.getOutstockCount());
            inventory.setInventoryCount(inventory.getInventoryCount() - outstock.getOutstockCount());
            inventoryMapper.updateById(inventory);
        }
        int i = baseMapper.insert(outstock);
        if (i == 0) {
            return CommonResult.error().message("添加失败");
        }
        return CommonResult.ok().message("添加成功");
    }

    @Override
    public CommonResult getwarehouseRegionByWarehouseId(Integer warehouseId) {
        if (warehouseId == null) {
            return CommonResult.error().message("参数错误");
        }
        Warehouse warehouse = warehouseMapper.selectById(warehouseId);
        int regionId = 0;
        if (warehouse != null) {

            regionId = warehouse.getRegionId();
        }
        if (regionId == 0) {
            return CommonResult.ok().data("items", new Integer[0]);
        }
        Integer[] ids = regionService.getAllRegionIds(regionId);
        if (ids == null) {
            return CommonResult.error().message("参数错误");
        }
        return CommonResult.ok().data("items", ids);
    }

    private Integer getRegionIdByWarehouseId(Integer warehouseId) {
        Warehouse warehouse = warehouseMapper.selectById(warehouseId);
        int regionId = 0;
        if (warehouse != null) {
            regionId = warehouse.getRegionId();
        }
        return regionId;
    }
}




