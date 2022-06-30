package com.dfhqcode.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import com.dfhqcode.model.dto.AdminDTO;
import com.dfhqcode.model.dto.JsonResult;
import com.dfhqcode.model.entity.Administrator;
import com.dfhqcode.service.AdministratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
}
