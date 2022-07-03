package com.dfhqcode.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.dfhqcode.model.dto.FeedbackDTO;
import com.dfhqcode.model.dto.InputModel;
import com.dfhqcode.model.dto.JsonResult;
import com.dfhqcode.model.dto.UserDTO;
import com.dfhqcode.model.entity.Feedback;
import com.dfhqcode.model.entity.User;
import com.dfhqcode.service.FaceRecognitionService;
import com.dfhqcode.service.FeedbackService;
import com.dfhqcode.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author zxd3099
 * @create 2022-06-27-10:05
 */
@Api(tags = "用户注册与登录信息处理")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private FaceRecognitionService faceRecognitionService;

    @Autowired
    private UserService userService;

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/register")
    @ApiOperation(value = "普通用户注册")
    public JsonResult<Object> register(@RequestParam @Validated UserDTO userDTO)
    {
        if (StpUtil.isLogin()) {
            return JsonResult.invalidReq("用户已经注册");
        }

        JSONObject result = null;
        try {
            result = faceRecognitionService.register(userDTO.getImage(), userDTO.getUsername());
        } catch (IOException | IllegalAccessException e) {
            return JsonResult.error("系统错误");
        }
        if ("error".equals(result.getString("state")))
        {
            return JsonResult.error(result.getString("info"));
        }
        // 将用户信息插入到数据库中
        String userId = result.getJSONObject("info").getString("userId");
        int age = result.getJSONObject("info").getJSONObject("exDescriptionInfos").getInteger("age");
        String gender = result.getJSONObject("info").getJSONObject("exDescriptionInfos").getString("gender");
        String time = DateUtil.now();

        User user = new User(userId, userDTO.getUsername(), userDTO.getInterestNews(),
                age, userDTO.getProvince() ,gender, time, time);
        userService.insertUser(user);

        // 标记用户登录
        StpUtil.login(userId);
        return JsonResult.success(userId, "注册成功");
    }

    @PutMapping("/login")
    @ApiOperation(value = "普通用户登录")
    public JsonResult<Object> login(@RequestParam String imageBase64)
    {
        // 如果用户已经登录，拒绝请求
        if (StpUtil.isLogin()) {
            return JsonResult.invalidReq("用户已经登录");
        }

        JSONObject result = null;
        try {
            result = faceRecognitionService.login(imageBase64);
        } catch (IOException | IllegalAccessException e) {
            return JsonResult.error("系统错误");
        }
        if ("success".equals(result.getString("state"))) {
            // 标记用户登录
            String userId = result.getJSONObject("info").getString("userId");
            StpUtil.login(userId);

            // 修改用户登录时间
            userService.updateUser(userId, DateUtil.now());
            return JsonResult.success(userId, "登录成功");
        } else {
            return JsonResult.error(result.getString("info"));
        }
    }

    @PostMapping("/submitFeedback")
    @ApiOperation("提交反馈")
    public JsonResult<Object> submitFeedback(@RequestBody @Valid FeedbackDTO feedbackDTO)
    {
        Feedback feedback = new Feedback("", feedbackDTO.getUserId(), DateUtil.now(),
                "", feedbackDTO.getContext(), "", false);
        feedbackService.insertFeedback(feedback);
        return JsonResult.success("感谢您的反馈，我们会尽快处理");
    }

    @GetMapping("/getFeedbacks")
    @ApiOperation("查询用户反馈")
    public JsonResult<Object> getFeedbacks(@RequestParam @Valid String userId, @RequestBody @Valid InputModel inputModel)
    {
        Pageable pageable = PageRequest.of(inputModel.getPageNum(), inputModel.getPageSize());
        List<Feedback> feedbacks = feedbackService.searchFeedback(userId, pageable, inputModel.getKey(), inputModel.getOrder());
        return JsonResult.success(feedbacks, "查询成功");
    }
}