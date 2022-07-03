package com.dfhqcode.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.dfhqcode.model.dto.*;
import com.dfhqcode.model.entity.Administrator;
import com.dfhqcode.model.entity.Feedback;
import com.dfhqcode.service.AdministratorService;
import com.dfhqcode.service.FeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zxd3099
 * @create 2022-06-27-13:43
 */
@Api(tags = "管理员功能接口")
@RestController
@RequestMapping("/administrator")
@Validated
public class AdministratorController {
    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private FeedbackService feedbackService;

    /**
     * 管理员注册
     */
    @PostMapping("/register")
    @ApiOperation(value = "管理员注册")
    public JsonResult<Object> register(@Valid @RequestBody AdminDTO adminDTO)
    {
        if (StpUtil.isLogin()) {
            return JsonResult.invalidReq("用户已登录");
        }
        // 查找用户名是否存在
        Boolean result = administratorService.queryUser(adminDTO.getUserName());
        if (result == true) {
            return JsonResult.error("用户名已经存在");
        }
        // sha1摘要加密
        String sha1 = SecureUtil.sha1(adminDTO.getPassword());
        String createTime = DateUtil.now();
        Administrator admin = new Administrator(adminDTO.getUserName(), sha1, adminDTO.getEmail(),
                                                adminDTO.getPhone(), createTime, createTime);
        administratorService.insertAdmin(admin);
        StpUtil.login(adminDTO.getUserName());
        return JsonResult.success("注册成功");
    }

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    @ApiOperation(value = "管理员登录")
    public JsonResult<Object> login(@RequestParam String username, @RequestParam String password)
    {
        // sha1摘要加密
        String sha1 = SecureUtil.sha1(password);
        Boolean result = administratorService.queryUser(username, sha1);
        if (result == false) {
            return JsonResult.notJur("用户未注册");
        }
        if (StpUtil.isLogin()) {
            return JsonResult.invalidReq("用户已登录");
        }
        administratorService.updateUserInfo(username, DateUtil.now());
        StpUtil.login(username);
        return JsonResult.success(StpUtil.getTokenInfo(), "登录成功");
    }

    /**
     * 管理员查看某天新增新闻数及其分类
     */
    @GetMapping("/searchNewsAdd")
    @ApiOperation(value = "查看某天新增新闻数及其分类")
    public JsonResult<Object> searchNewsAdd(@RequestParam String date)
    {
        List<String> result = administratorService.searchNewsAdd(date);
        return JsonResult.success(result, "查询成功");
    }

    /**
     * 管理员查看用户地域分布
     */
    @GetMapping("/viewUserDistribution")
    @ApiOperation(value = "查看用户地域分布")
    public JsonResult<Object> viewUserDistribution()
    {
        List<String> result = administratorService.viewUserDistribution();
        return JsonResult.success(result, "查询成功");
    }

    /**
     * 查看某天新增用户数量
     */
    @GetMapping("/searchUsersAdd")
    @ApiOperation(value = "查看某天新增用户数量")
    public JsonResult<Object> searchUsersAdd(@RequestParam String date)
    {
        int result = administratorService.searchUsersAdd(date);
        return JsonResult.success(result, "查询成功");
    }

    /**
     * 查看用户的性别分布
     */
    @GetMapping("/getGenderDistribution")
    @ApiOperation(value = "查看用户的性别分布")
    public JsonResult<Object> getGenderDistribution()
    {
        List<String> result = administratorService.getGenderDistribution();
        return JsonResult.success(result, "查询成功");
    }

    /**
     * 看用户年龄分布
     */
    @GetMapping("/getAgeDistribution")
    @ApiOperation(value = "getAgeDistribution")
    public JsonResult<Object> getAgeDistribution()
    {
        List<String> ageDistribution = administratorService.getAgeDistribution();
        return JsonResult.success(ageDistribution, "查询成功");
    }

    /**
     * 查看某日新闻总浏览量、点赞量和收藏量
     */
    @GetMapping("/getNewsData")
    @ApiOperation(value = "查看某日新闻总浏览量、点赞量和收藏量")
    public JsonResult<Object> getNewsData(@RequestParam String date)
    {
        JSONObject result = administratorService.getNewsData(date);
        return JsonResult.success(result, "查询成功");
    }

    /**
     * 查看某日老用户数量
     */
    @GetMapping("/getOldUserNum")
    @ApiOperation(value = "查看某日老用户数量")
    public JsonResult<Object> getOldUserNum(@RequestParam String date)
    {
        int result = administratorService.getOldUserNum(date);
        return JsonResult.success(result, "查询成功");
    }

    /**
     * 管理员处理反馈并提交
     */
    @PostMapping("/submitConclusion")
    @ApiOperation(value = "管理员处理反馈并提交")
    public JsonResult<Object> submitConclusion(@RequestBody @Valid ConclusionDTO conclusionDTO)
    {
        feedbackService.updateFeedback(conclusionDTO.getUserId(), conclusionDTO.getSubmitTime(),
                conclusionDTO.getAdministrator(), DateUtil.now(), conclusionDTO.getConclusion());
        return JsonResult.success("反馈已经提交");
    }

    /**
     * 管理员查询所有未被处理处理的反馈
     */
    @PostMapping("/searchUnhandledFeedbacks")
    @ApiOperation(value = "管理员查询所有未被处理处理的反馈")
    public JsonResult<Object> searchUnhandledFeedbacks(@RequestBody @Valid InputModel inputModel)
    {
        Pageable pageable = PageRequest.of(inputModel.getPageNum(), inputModel.getPageSize());
        List<Feedback> feedbacks = feedbackService.searchFeedback(false, pageable, inputModel.getKey(), inputModel.getOrder());

        return JsonResult.success(feedbacks, "查询成功");
    }

    /**
     * 管理员查询所有已经被处理处理的反馈
     */
    @PostMapping("/searchHandledFeedbacks")
    @ApiOperation(value = "管理员查询所有已经被处理处理的反馈")
    public JsonResult<Object> searchHandledFeedbacks(@RequestBody @Valid InputModel inputModel)
    {
        Pageable pageable = PageRequest.of(inputModel.getPageNum(), inputModel.getPageSize());
        List<Feedback> feedbacks = feedbackService.searchFeedback(true, pageable, inputModel.getKey(), inputModel.getOrder());

        return JsonResult.success(feedbacks, "查询成功");
    }

    /**
     * 查询某日用户反馈数、处理的反馈数和未被处理的反馈数
     */
    @PostMapping("/getFeedbackData")
    @ApiOperation(value = "查询某日用户反馈数、处理的反馈数和未被处理的反馈数")
    public JsonResult<Object> getFeedbackData(@RequestParam String date)
    {
        JSONObject feedbackData = feedbackService.getFeedbackData(date);
        return JsonResult.success(feedbackData, "查询成功");
    }
}