package com.dfhqcode.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author zxd3099
 * @create 2022-06-27-13:10
 */
@Data
@AllArgsConstructor
@ApiModel("管理员实体")
public class Administrator implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * 管理员用户名
     */
    @ApiModelProperty("用户名")
    @Field("user_name")
    private String userName;

    /**
     * 管理员密码
     */
    @ApiModelProperty("管理员密码")
    @Field("password")
    private String password;

    /**
     * 管理员邮箱
     */
    @ApiModelProperty("管理员邮箱")
    @Field("email")
    private String email;

    /**
     * 管理员联系方式
     */
    @ApiModelProperty("管理员联系方式")
    @Field("phone")
    private String phone;

    /**
     * 上次登录时间
     */
    @ApiModelProperty("上次登录时间")
    @Field("last_login_time")
    private String lastLoginTime;

    /**
     * 账号注册时间
     */
    @ApiModelProperty("创建时间")
    @Field("register_time")
    private String createTime;
}
