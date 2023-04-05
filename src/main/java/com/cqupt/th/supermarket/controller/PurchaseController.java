package com.cqupt.th.supermarket.controller;

import com.cqupt.th.supermarket.entity.Purchase;
import com.cqupt.th.supermarket.query.PurchaseQuery;
import com.cqupt.th.supermarket.service.PurchaseService;
import com.cqupt.th.supermarket.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * @author th
 * @date 2023/4/5 16:28
 */
@RestController
@CrossOrigin
@RequestMapping("/purchase")
public class PurchaseController {
    @Autowired
    @Qualifier("purchaseService")
    private PurchaseService purchaseService;

    @PostMapping("{currentPage}/{pageSize}")
    public CommonResult getPurchaseListPage(@PathVariable("currentPage") Integer currentPage, @PathVariable("pageSize") Integer pageSize, @RequestBody(required = false) PurchaseQuery purchaseQuery) {
        return purchaseService.getPurchaseListPage(currentPage, pageSize, purchaseQuery);
    }
    @DeleteMapping("{id}")
    public CommonResult deletePurchase(@PathVariable("id") Integer id) {
        return purchaseService.deletePurchase(id);
    }
    @PostMapping
    public CommonResult addPurchase(@RequestBody Purchase purchase) {
        return purchaseService.addPurchase(purchase);
    }
    @PutMapping("{id}")
    public CommonResult updatePurchase(@PathVariable("id") Integer id, @RequestBody Purchase purchase) {
        return purchaseService.updatePurchase(id, purchase);
    }
}