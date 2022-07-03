package com.dfhqcode.controller;

import com.dfhqcode.model.dto.ClickDTO;
import com.dfhqcode.model.dto.JsonResult;
import com.dfhqcode.model.dto.LikeCollectionDTO;
import com.dfhqcode.model.entity.News;
import com.dfhqcode.model.entity.UserCollection;
import com.dfhqcode.model.entity.UserLike;
import com.dfhqcode.service.NewsService;
import com.dfhqcode.service.UserActionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zxd3099
 * @create 2022-06-27-11:09
 */
@Api(tags = "用户交互信息处理")
@RestController
@RequestMapping("/userAction")
public class UserActionController {
    @Autowired
    private UserActionService userActionService;

    @Autowired
    private NewsService newsService;

    @PostMapping("/click")
    @ApiOperation(value = "用户点击操作")
    public JsonResult<Object> click(@RequestBody @Valid ClickDTO clickDTO)
    {
        userActionService.InsertUserClick(clickDTO.transform());
        return JsonResult.success();
    }

    @PostMapping("/like")
    @ApiOperation(value = "用户点赞操作")
    public JsonResult<Object> like(@RequestBody @Valid LikeCollectionDTO likeDTO)
    {
        if (likeDTO.getType() == 1) {
            return JsonResult.error("类型错误");
        }

        // 根据新闻Id查找指定的论文
        News news = newsService.findNewsById(likeDTO.getNewsId());
        if (news == null) {
            return JsonResult.error("新闻不存在");
        }

        UserLike userLike = new UserLike(likeDTO.getUserId(), likeDTO.getNewsId(), likeDTO.getOpearteTime());
        userActionService.InsertUserLike(userLike);
        return JsonResult.success("点赞成功");
    }

    @PostMapping("/cancelLike")
    @ApiOperation(value = "用户取消点赞操作")
    public JsonResult<Object> cancelLike(@RequestParam String userId, @RequestParam String newsId)
    {
        Boolean result = userActionService.RemoveUserLike(userId, newsId);
        if (result == true) {
            return JsonResult.success("取消点赞成功");
        }
        return JsonResult.invalidReq("已经取消点赞");
    }

    @PostMapping("/collect")
    @ApiOperation(value = "用户收藏操作")
    public JsonResult<Object> collect(@RequestBody @Valid LikeCollectionDTO collectionDTO)
    {
        if (collectionDTO.getType() == 0) {
            return JsonResult.error("类型错误");
        }

        // 根据新闻Id查找指定的论文
        News news = newsService.findNewsById(collectionDTO.getNewsId());
        if (news == null) {
            return JsonResult.error("新闻不存在");
        }

        UserCollection userCollection = new UserCollection(news, collectionDTO.getUserId(), collectionDTO.getOpearteTime());
        userActionService.InsertUserCollection(userCollection);
        return JsonResult.success("收藏成功");
    }

    @PostMapping("/cancelCollection")
    @ApiOperation(value = "用户取消点赞操作")
    public JsonResult<Object> cancelCollection(@RequestParam String userId, @RequestParam String newsId)
    {
        Boolean result = userActionService.removeUserCollection(userId, newsId);
        if (result == true) {
            return JsonResult.success("取消收藏成功");
        }
        return JsonResult.invalidReq("已经取消收藏");
    }

    @GetMapping("/listCollections")
    @ApiOperation(value = "用户列出收藏列表")
    public JsonResult<Object> listCollections(@RequestParam String userId)
    {
        List<UserCollection> result = userActionService.listCollections(userId);
        if (CollectionUtils.isEmpty(result)) {
            return JsonResult.invalidReq("收藏列表为空");
        }
        return JsonResult.success(result, "展示收藏列表成功");
    }
}
