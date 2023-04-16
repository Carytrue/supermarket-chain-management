package com.cqupt.th.supermarket.query;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author th
 * @date 2023/4/5 17:06
 */
@Data
public class PurchaseQuery implements Serializable {
    private Integer productId;
    private Long purchaseNumber;
    private String productName;
    private Integer supplierId;
    private BigDecimal totalPrice;
    private Date startTime;
    private Date endTime;
    private Date shelfLifeStart;
    private Date shelfLifeEnd;
}
