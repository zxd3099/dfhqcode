package com.dfhqcode.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author zxd3099
 * @create 2022-06-27-10:34
 */
@Data
@AllArgsConstructor
@ApiModel("用户点击实体")
public class UserClick implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Field("user_id")
    @ApiModelProperty("用户ID")
    private String userId;

    /**
     * 新闻ID
     */
    @Field("news_id")
    @ApiModelProperty("新闻ID")
    private String newsId;

    /**
     * 页内是否点击
     */
    @Field("operate")
    @ApiModelProperty("页内是否操作")
    private Boolean isOperate;

    /**
     * 页面停留时间
     */
    @Field("time_on_page")
    @ApiModelProperty("页面停留时间")
    private Integer timeOnPage;

    /**
     * 操作时间
     */
    @Field("time")
    @ApiModelProperty("用户点击时间")
    private String clickTime;
}
