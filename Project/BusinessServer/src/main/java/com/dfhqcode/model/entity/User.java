package com.dfhqcode.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

/**
 * @author zxd3099
 * @create 2022-06-29-19:32
 */
@Data
@AllArgsConstructor
@ApiModel("用户实体")
public class User implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Id
    @ApiModelProperty("主键ID")
    private String userId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    @Field("user_name")
    private String userName;

    /**
     * 用户感兴趣的新闻类别
     */
    @ApiModelProperty("用户感兴趣的新闻类别")
    @Field("interest_news")
    private List<Integer> interestNews;

    /**
     * 用户年龄
     */
    @ApiModelProperty("用户年龄")
    @Field("age")
    private Integer age;

    /**
     * 用户所属省份
     */
    @ApiModelProperty("省份")
    @Field("province")
    private String province;

    /**
     * 用户性别
     */
    @ApiModelProperty("用户性别")
    @Field("gender")
    private String gender;

    /**
     * 最近一次登录时间
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
