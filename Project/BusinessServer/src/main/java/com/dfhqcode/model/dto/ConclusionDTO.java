package com.dfhqcode.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zxd3099
 * @create 2022-07-02-11:31
 */
@Data
@ApiModel("管理员处理反馈结果实体")
public class ConclusionDTO {
    /**
     * 提交反馈的用户ID
     */
    @ApiModelProperty(value = "用户ID", example = "60a754ac634ba154596e8c6d", required = true)
    private String userId;

    /**
     * 用户提交反馈时间
     */
    @ApiModelProperty(value = "用户提交反馈时间", example = "2022-07-02 14:18:27", required = true)
    private String submitTime;

    /**
     * 处理反馈的管理员姓名
     */
    @ApiModelProperty(value = "处理反馈的管理员姓名", example = "zxd3099", required = true)
    private String administrator;

    /**
     * 管理员处理反馈的结果
     */
    @ApiModelProperty(value = "管理员处理反馈的结果", example = "...", required = true)
    private String conclusion;
}
