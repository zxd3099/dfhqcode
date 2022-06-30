package com.dfhqcode.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author zxd3099
 * @create 2022-06-26-21:51
 */
@Data
@ApiModel("输入分页和排序实体")
public class InputModel {
    /**
     * 页码
     */
    @ApiModelProperty(value = "页码", example = "0", required = true)
    @Min(0)
    private int pageNum;

    /**
     * 每页的数据条数
     */
    @ApiModelProperty(value = "页大小", example = "1", required = true)
    @Min(1)
    @Max(50)
    private int pageSize;

    /**
     * 排序的依据
     */
    @NotNull
    @ApiModelProperty("排序的依据")
    private String key;

    /**
     * 排序的顺序：1-升序，0-降序
     */
    @Min(0)
    @Max(1)
    @ApiModelProperty("排序的顺序")
    private int order;
}
