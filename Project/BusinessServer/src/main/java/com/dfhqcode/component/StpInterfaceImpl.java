package com.dfhqcode.component;

import cn.dev33.satoken.stp.StpInterface;
import com.dfhqcode.service.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zxd3099
 * @create 2022-06-27-21:13
 * 自定义权限验证接口扩展
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    private AdministratorService administratorService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object o, String s) {
        return null;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     * 默认root账号所有者是唯一的超级管理员
     */
    @Override
    public List<String> getRoleList(Object o, String s) {
        Boolean result = administratorService.queryUser((String) o);
        List<String> list = new ArrayList<String>();
        if (result == true) {
            list.add("admin");
        }
        return list;
    }
}
