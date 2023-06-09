package com.cqupt.th.supermarket.controller;

import com.cqupt.th.supermarket.entity.Member;
import com.cqupt.th.supermarket.query.MemberQuery;
import com.cqupt.th.supermarket.service.MemberService;
import com.cqupt.th.supermarket.utils.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author th
 * @date 2023/3/30 15:37
 */
@RestController
@PreAuthorize("hasAuthority('member:member:index')")
@RequestMapping("/member")
public class MemberController {
    @Autowired
    @Qualifier("memberService")
    private MemberService memberService;

    @PostMapping("{currentPage}/{size}")
    public CommonResult getMemberListByPage(@PathVariable("currentPage") Integer currentPage, @PathVariable("size") Integer size, @RequestBody(required = false) MemberQuery memberQuery) {
        return memberService.getMemberListByPage(currentPage, size, memberQuery);
    }

    @DeleteMapping("{id}")
    public CommonResult deleteMemberById(@PathVariable("id") Integer id) {
        return memberService.deleteMemberById(id);
    }

    @DeleteMapping("batch/{ids}")
    public CommonResult deleteMemberByIds(@PathVariable("ids") Integer[] ids) {
        return memberService.deleteMemberByIds(ids);
    }

    @PostMapping
    public CommonResult addMember(@RequestBody Member member) {
        return memberService.addMember(member);
    }

    @PutMapping("{id}")
    public CommonResult updateMemberById(@PathVariable("id") Integer id, @RequestBody Member member) {
        return memberService.updateMemberById(id, member);
    }
    @GetMapping("all")
    public CommonResult getMemberList() {
        return memberService.getMemberList();
    }
    @PostMapping("isExistMemberCardNumber")
    public CommonResult isExistMemberCardNumber(@RequestBody Member member) {
        return memberService.isExistMemberCardNumber(member);
    }
    @GetMapping("name/{id}")
    public CommonResult getMemberNameById(@PathVariable("id") Integer id) {
        return memberService.getMemberNameById(id);
    }
}
