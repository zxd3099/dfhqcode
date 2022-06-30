package com.dfhqcode.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author zxd3099
 * @create 2022-06-27-10:34
 */
@Data
@AllArgsConstructor
@ApiModel("用户收藏实体")
public class UserCollection {
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
     * 新闻标题
     */
    @Field("title")
    @ApiModelProperty("新闻标题")
    private String title;

    /**
     * 类别ID
     */
    @Field("category_id")
    @ApiModelProperty("类别ID")
    private Integer categoryId;

    /**
     * 内容
     */
    @Field("content")
    @ApiModelProperty("新闻内容")
    private String content;

    /**
     * 发布时间
     */
    @Field("time")
    @ApiModelProperty("发布时间")
    private String time;

    /**
     * 操作时间
     */
    @Field("time")
    @ApiModelProperty("用户收藏时间")
    private String collectionTime;

    public UserCollection(News news, String userId, String collectionTime)
    {
        this.userId = userId;
        this.collectionTime = collectionTime;
        this.newsId = news.getId();
        this.categoryId = news.getCategoryId();
        this.content = news.getContent();
    }
}
