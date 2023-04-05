package com.cqupt.th.supermarket.query;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author th
 * @date 2023/4/4 22:54
 */
@Data
public class SupplierQuery implements Serializable {

    private String name;
    private String contactPerson;
    private String tel;
    private Integer regionParentId;
    private Date startTime;
    private Date endTime;
}

