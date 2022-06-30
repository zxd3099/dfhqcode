package com.dfhqcode.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author zxd3099
 * @create 2022-06-26-20:24
 */
@Data
@ApiModel("新闻实体")
public class News implements Serializable {
    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty("主键ID")
    @Id
    private String id;

    /**
     * 作者
     */
    @Field("author")
    @ApiModelProperty("主键ID")
    private String author;

    /**
     * 标题
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
}
