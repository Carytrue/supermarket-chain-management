package com.cqupt.th.supermarket.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author th
 * @date 2023/4/7 14:20
 */
@Data
public class OutstockVo {

    private Integer id;
    private Integer warehouseId;
    private String warehouseRegion;
    private Integer productId;
    private String productName;
    private Integer outstockCount;
    private Integer type;
    private Date gmtCreate;
    private Date gmtModified;
}
