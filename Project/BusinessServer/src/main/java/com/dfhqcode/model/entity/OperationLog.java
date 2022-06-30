package com.dfhqcode.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zxd3099
 * @create 2022-06-27-12:48
 */
@Data
@ApiModel("操作日志实体")
public class OperationLog {
    /**
     * 操作方法名
     */
    @ApiModelProperty("操作方法名")
    private String methods;

    /**
     * 操作类型
     */
    @ApiModelProperty("操作类型")
    private String operationType;

    /**
     * 返回结果
     */
    @ApiModelProperty("返回结果")
    private String returnValue;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
