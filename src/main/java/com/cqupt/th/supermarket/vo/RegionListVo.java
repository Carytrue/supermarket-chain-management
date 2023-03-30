package com.cqupt.th.supermarket.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author th
 * @date 2023/3/27 22:00
 */
@Data
public class RegionListVo implements Serializable {
    private Integer id;
    private String name;
    private List<RegionListVo> children;
    private Integer level;
    private Integer parentId;
}
