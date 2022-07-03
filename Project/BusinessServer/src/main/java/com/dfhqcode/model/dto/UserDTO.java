package com.dfhqcode.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author zxd3099
 * @create 2022-06-29-17:12
 */
@ApiModel("用户输入实体")
@Data
public class UserDTO {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", example = "zxd3099", required = true)
    @Size(max = 10, min = 4)
    private String username;

    /**
     * 用户Base64编码图片
     */
    @ApiModelProperty(value = "用户Base64编码图片", required = true)
    private String image;

    /**
     * 用户感兴趣的新闻类别
     */
    @ApiModelProperty(value = "用户感兴趣的新闻类别", example = "[0, 4, 6]", required = false)
    private List<Integer> interestNews;

    /**
     * 用户所在城市
     */
    @ApiModelProperty(value = "用户所在省份", example = "湖北省", required = true)
    private String province;
}
