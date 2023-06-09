package com.cqupt.th.supermarket.service;

import com.cqupt.th.supermarket.entity.MemberPoint;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.th.supermarket.query.MemberPointQuery;
import com.cqupt.th.supermarket.utils.CommonResult;

/**
* @author 16075
* @description 针对表【member_point】的数据库操作Service
* @createDate 2023-03-30 15:36:17
*/
public interface MemberPointService extends IService<MemberPoint> {

    CommonResult getMemberPointListPageByMemberId(Integer currentPage, Integer size, Integer memberId, MemberPointQuery memberPointQuery);

    CommonResult getMemberPointListPage(Integer currentPage, Integer size, MemberPointQuery memberPointQuery);

    CommonResult batchDelete(Integer[] ids);

    CommonResult deleteById(Integer id);

    CommonResult addMemberPoint(MemberPoint memberPoint);

    CommonResult updateMemberPointById(Integer id, MemberPoint memberPoint);

}
