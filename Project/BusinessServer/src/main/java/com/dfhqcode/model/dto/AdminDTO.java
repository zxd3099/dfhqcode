package com.dfhqcode.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author zxd3099
 * @create 2022-06-27-13:53
 */
@ApiModel("管理员输入实体")
@Data
public class AdminDTO {
    /**
     * 管理员用户名
     */
    @ApiModelProperty(value = "用户名", example = "zxd3099", required = true)
    @Size(max = 10, min = 4)
    private String userName;

    /**
     * 管理员密码
     * 密码长度:8-15,必须同时含有大小写字母、数字和特殊字符
     */
    @ApiModelProperty(value = "管理员密码", example = "Zxd3099$$&p", required = true)
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^\\w\\s]).{8,15}")
    private String password;

    /**
     * 管理员邮箱
     */
    @ApiModelProperty(value = "管理员邮箱", example = "zxd3099@163.com", required = true)
    @Email
    private String email;

    /**
     * 管理员联系方式
     */
    @ApiModelProperty(value = "管理员联系方式", example = "17715023619", required = true)
    @Pattern(regexp = "(\\+\\d+)?1[34578]\\d{9}$")
    private String phone;
}
