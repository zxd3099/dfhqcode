package com.dfhqcode.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * @author zxd3099
 * @create 2022-06-27-11:24
 */
@Data
@ApiModel("点赞和收藏实体")
public class LikeCollectionDTO {
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
     * 操作类型
     * 0-点赞；1-收藏
     */
    @ApiModelProperty(value = "操作类型", example = "0", required = true)
    @Min(0)
    @Max(1)
    private int type;

    /**
     * 操作时间
     */
    @ApiModelProperty(value = "用户操作时间点", example = "2022-06-27 08:15", required = true)
    private String opearteTime;
}
