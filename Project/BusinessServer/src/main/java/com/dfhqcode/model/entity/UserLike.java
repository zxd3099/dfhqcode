package com.dfhqcode.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author zxd3099
 * @create 2022-06-27-10:35
 */
@Data
@AllArgsConstructor
@ApiModel("用户点赞实体")
public class UserLike implements Serializable {
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
     * 操作时间
     */
    @Field("time")
    @ApiModelProperty("用户点赞时间")
    private String likeTime;
}
