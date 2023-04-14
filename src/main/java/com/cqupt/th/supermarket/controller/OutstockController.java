package com.cqupt.th.supermarket.controller;

import com.cqupt.th.supermarket.entity.Outstock;
import com.cqupt.th.supermarket.query.OutstockQuery;
import com.cqupt.th.supermarket.service.OutstockService;
import com.cqupt.th.supermarket.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author th
 * @date 2023/4/7 12:49
 */
@RestController
@PreAuthorize("hasAuthority('inventory:outstock:index')")
@RequestMapping("/outstock")
public class OutstockController {
    @Autowired
    @Qualifier("outstockService")
    private OutstockService outstockService;

    @PostMapping("{currentPage}/{pageSize}")
    public CommonResult getOutstockListPage(@PathVariable("currentPage") Integer currentPage, @PathVariable("pageSize") Integer pageSize, @RequestBody(required = false) OutstockQuery outstockQuery) {
        return outstockService.getOutstockListPage(currentPage, pageSize, outstockQuery);
    }

    @GetMapping("warehouseIdByRegionId/{regionId}")
    public CommonResult getWarehouseIdByRegionId(@PathVariable("regionId") Integer regionId) {
        return outstockService.getWarehouseIdByRegionId(regionId);
    }

    @DeleteMapping("{id}")
    public CommonResult deleteOutstockById(@PathVariable("id") Integer id) {
        return outstockService.deleteOutstockById(id);
    }

    @PutMapping("{id}")
    public CommonResult updateOutstockById(@PathVariable("id") Integer id, @RequestBody Outstock outstock) {
        return outstockService.updateOutstockById(id, outstock);
    }

    @PostMapping
    public CommonResult addOutstock(@RequestBody Outstock outstock) {
        return outstockService.addOutstock(outstock);
    }

    @GetMapping("outstockRegionIds/{warehouseId}")
    public CommonResult getOutstockRegionIds(@PathVariable("warehouseId") Integer warehouseId) {
        return outstockService.getOutstockRegionIds(warehouseId);
    }

}
