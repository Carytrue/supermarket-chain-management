package com.cqupt.th.supermarket.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 位置
 *
 * @author TianHong
 * @TableName position
 * @date 2023/04/05
 */
@TableName(value ="position")
@Data
public class Position implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    /**
     * 
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}