package com.dfhqcode.model.dto;

import com.dfhqcode.model.entity.UserClick;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;


/**
 * @author zxd3099
 * @create 2022-06-27-11:16
 */
@Data
@ApiModel("输入点击实体")
public class ClickDTO {
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", example = "60a754ac634ba154596e8c6d", required = true)
    private String userId;

    /**
     * 新闻ID
     */
    @ApiModelProperty(value = "新闻ID", example = "606541be-fae5-4e8a-a7fb-c092316f7a49", required = true)
    private String newsId;

    /**
     * 页面停留时间
     */
    @ApiModelProperty(value = "页面停留时间", example = "123", required = true)
    @Min(0)
    private Integer timeOnPage;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "用户点击时间点", example = "2022-06-27 08:15:06", required = true)
    private String clickTime;

    public UserClick transform()
    {
        return new UserClick(userId, newsId, timeOnPage, clickTime);
    }
}
