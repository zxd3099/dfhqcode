package com.dfhqcode.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.dfhqcode.model.dto.JsonResult;
import com.dfhqcode.model.dto.UserDTO;
import com.dfhqcode.model.entity.User;
import com.dfhqcode.service.FaceRecognitionService;
import com.dfhqcode.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
        String userId = result.getString("userId");
        String time = DateUtil.now();
        User user = new User(userId, userDTO.getUsername(), userDTO.getInterestNews(), time, time);
        userService.insertUser(user);

        // 标记用户登录
        StpUtil.login(userId);
        return JsonResult.success("注册成功");
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
            String userId = result.getString("userId");
            StpUtil.login(userId);

            // 修改用户登录时间
            userService.updateUser(userId, DateUtil.now());
            return JsonResult.success("登录成功");
        } else {
            return JsonResult.error(result.getString("info"));
        }
    }
}
