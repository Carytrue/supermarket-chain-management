package com.cqupt.th.supermarket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.th.supermarket.entity.Inventory;
import com.cqupt.th.supermarket.entity.Product;
import com.cqupt.th.supermarket.entity.Region;
import com.cqupt.th.supermarket.entity.Warehouse;
import com.cqupt.th.supermarket.mapper.ProductMapper;
import com.cqupt.th.supermarket.mapper.RegionMapper;
import com.cqupt.th.supermarket.mapper.WarehouseMapper;
import com.cqupt.th.supermarket.query.InventoryQuery;
import com.cqupt.th.supermarket.service.InventoryService;
import com.cqupt.th.supermarket.mapper.InventoryMapper;
import com.cqupt.th.supermarket.service.RegionService;
import com.cqupt.th.supermarket.utils.CommonResult;
import com.cqupt.th.supermarket.vo.InventoryVo;
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
 * @description 针对表【inventory】的数据库操作Service实现
 * @createDate 2023-04-06 19:09:26
 */
@Service("inventoryService")
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory>
        implements InventoryService {
    @Resource
    private ProductMapper productMapper;
    @Autowired
    @Qualifier("regionService")
    private RegionService regionService;
    @Resource
    private WarehouseMapper warehouseMapper;

    @Override
    public CommonResult getInventoryListPage(Integer currentPage, Integer pageSize, InventoryQuery inventoryQuery) {
        if (currentPage <= 0 || pageSize <= 0) {
            return CommonResult.error().message("参数错误");
        }
        QueryWrapper<Inventory> inventoryQueryWrapper = new QueryWrapper<>();
        inventoryQueryWrapper.orderByDesc("gmt_modified");
        if (inventoryQuery != null) {
            if (inventoryQuery.getWarehouseId() != null) {
                inventoryQueryWrapper.eq("warehouse_id", inventoryQuery.getWarehouseId());
            }
            if (inventoryQuery.getProductId() != null) {
                inventoryQueryWrapper.eq("product_id", inventoryQuery.getProductId());
            }
            if (inventoryQuery.getInventoryCount() != null) {
                inventoryQueryWrapper.eq("inventory_count", inventoryQuery.getInventoryCount());
            }
            if (inventoryQuery.getStartTime() != null) {
                inventoryQueryWrapper.ge("gmt_modified", inventoryQuery.getStartTime());
            }
            if (inventoryQuery.getEndTime() != null) {
                inventoryQueryWrapper.le("gmt_modified", inventoryQuery.getEndTime());
            }
        }
        Page<Inventory> inventoryPage = new Page<>(currentPage, pageSize);
        baseMapper.selectPage(inventoryPage, inventoryQueryWrapper);
        long total = inventoryPage.getTotal();
        List<Inventory> records = inventoryPage.getRecords();
        HashMap<Integer, String> productHashMap = new HashMap<>();
        List<Product> products = productMapper.selectList(null);
        products.stream().forEach(p -> {
            productHashMap.put(p.getId(), p.getName());
        });
        HashMap<Integer, Region> regionHashMap = new HashMap<>();
        List<Region> regions = regionService.list(null);
        regions.stream().forEach(r -> {
            regionHashMap.put(r.getId(), r);
        });
        HashMap<Integer, Integer> regionIdHashMap = new HashMap<>();
        warehouseMapper.selectList(null).stream().forEach(w -> {
            regionIdHashMap.put(w.getId(), w.getRegionId());
        });
        List<InventoryVo> rows = records.stream().map(r -> {
            InventoryVo inventoryVo = new InventoryVo();
            BeanUtils.copyProperties(r, inventoryVo);
            inventoryVo.setProductName(productHashMap.get(r.getProductId()));
            inventoryVo.setWarehouseRegion(regionService.getRegionName(regionIdHashMap.get(r.getWarehouseId()), regionHashMap));
            return inventoryVo;
        }).collect(Collectors.toList());
        return CommonResult.ok().data("total", total).data("rows", rows);
    }

    @Override
    public CommonResult getWarehouseIdByRegionId(Integer regionId) {
        if (regionId == null) {
            return CommonResult.error().message("参数错误");
        }
        Warehouse warehouse = warehouseMapper.selectOne(new QueryWrapper<Warehouse>().eq("region_id", regionId));
        if (warehouse == null) {
            return CommonResult.ok().data("item", null);
        }
        return CommonResult.ok().data("item", warehouse.getId());
    }
}




