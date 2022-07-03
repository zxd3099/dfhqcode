package com.dfhqcode.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author zxd3099
 * @create 2022-07-02-10:59
 */
@Data
@ApiModel("输入反馈实体")
public class FeedbackDTO {
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", example = "60a754ac634ba154596e8c6d", required = true)
    private String userId;

    /**
     * 反馈内容
     */
    @ApiModelProperty(value = "反馈内容", required = true)
    @Field("context")
    private String context;
}
