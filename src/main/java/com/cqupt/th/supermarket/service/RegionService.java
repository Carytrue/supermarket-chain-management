package com.cqupt.th.supermarket.service;

import com.cqupt.th.supermarket.entity.Region;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.th.supermarket.utils.CommonResult;
import com.cqupt.th.supermarket.vo.RegionVo;

import java.util.HashMap;
import java.util.List;

/**
 * @author 16075
 * @description 针对表【region】的数据库操作Service
 * @createDate 2023-03-27 18:12:27
 */
public interface RegionService extends IService<Region> {

    CommonResult deleteRegionById(Integer id);

    CommonResult addRegion(Region region);

    CommonResult updateRegionById(Integer id, Region region);

    CommonResult getRegionAll();

    List<RegionVo> getRegionAll(List<Integer> regionIds);

    Integer[] getRegionIdsById(Integer id);

    String getRegionName(Integer id, HashMap<Integer, Region> map);

    CommonResult isExistRegionName(Region region);
}
