package com.dfhqcode.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author zxd3099
 * @create 2022-07-02-10:57
 */
@Data
@AllArgsConstructor
@ApiModel("用户反馈实体")
public class Feedback {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * 处理反馈管理员名
     */
    @ApiModelProperty("处理反馈管理员名")
    @Field("administrator")
    @Indexed
    private String administrator;

    /**
     * 用户Id
     */
    @ApiModelProperty("提出反馈用户ID")
    @Field("user_id")
    @Indexed
    private String userId;

    /**
     * 提出反馈时间
     */
    @ApiModelProperty("提出反馈时间")
    @Field("submit_time")
    private String submitTime;

    /**
     * 处理反馈的时间
     */
    @ApiModelProperty("处理反馈的时间")
    @Field("handle_time")
    private String handleTime;

    /**
     * 反馈内容
     */
    @ApiModelProperty("反馈内容")
    @Field("context")
    private String context;

    /**
     * 管理员处理反馈信息的结论
     */
    @ApiModelProperty("管理员处理反馈信息的结论")
    @Field("conclusion")
    private String conclusion;

    /**
     * 表示反馈是否被处理
     */
    @ApiModelProperty("表示反馈是否被处理")
    @Field("is_handled")
    private boolean isHandled;
}