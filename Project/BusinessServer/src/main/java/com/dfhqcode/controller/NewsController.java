package com.dfhqcode.controller;

import cn.hutool.core.date.DateUtil;
import com.dfhqcode.model.dto.InputModel;
import com.dfhqcode.model.dto.JsonResult;
import com.dfhqcode.model.entity.News;
import com.dfhqcode.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author zxd3099
 * @create 2022-06-26-21:42
 */
@Api(tags = "新闻浏览")
@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping("/getByTitle")
    @ApiOperation(value = "根据新闻标题和发布时间查找新闻")
    public JsonResult<Object> findNewsByTitle(@RequestParam String title, @RequestParam String date)
    {
        JsonResult<Object> result = processDate(date);
        if(result.getCode() == JsonResult.CODE_ERROR) {
            return result;
        }
        String newDate = result.getMsg();
        List<News> byTitle = newsService.findByTitle(title, newDate);
        if(CollectionUtils.isEmpty(byTitle)) {
            return JsonResult.error("新闻不存在");
        }
        return JsonResult.success(byTitle);
    }

    @PostMapping(value = "/getByKeyWord")
    @ApiOperation(value = "根据新闻内容的关键词查找新闻")
    public JsonResult<Object> findNewsByKeyWord(@RequestParam String keyWord, @RequestParam String date, @RequestBody @Valid InputModel inputModel)
    {
        JsonResult<Object> result = processDate(date);
        if(result.getCode() == JsonResult.CODE_ERROR) {
            return result;
        }
        String newDate = result.getMsg();
        Pageable pageable = PageRequest.of(inputModel.getPageNum(), inputModel.getPageSize());
        List<News> newsList = newsService.findByKeyWord(keyWord, newDate, pageable, inputModel.getKey(), inputModel.getOrder());
        if(CollectionUtils.isEmpty(newsList)) {
            return JsonResult.error("新闻不存在");
        }
        return JsonResult.success(newsList);
    }

    @PostMapping(value = "/getByCategoryId")
    @ApiOperation(value = "根据新闻分类查找新闻")
    public JsonResult<Object> findNewsByCategory(@RequestParam int categoryId, @RequestParam String date, @RequestBody @Valid InputModel inputModel)
    {
        JsonResult<Object> result = processDate(date);
        if(result.getCode() == JsonResult.CODE_ERROR) {
            return result;
        }
        if (categoryId < 1 || categoryId > 10)
        {
            return JsonResult.error("新闻分类不存在");
        }
        String newDate = result.getMsg();
        Pageable pageable = PageRequest.of(inputModel.getPageNum(), inputModel.getPageSize());
        List<News> newsList = newsService.findByCategory(categoryId, newDate, pageable, inputModel.getKey(), inputModel.getOrder());
        if(CollectionUtils.isEmpty(newsList)) {
            return JsonResult.error("新闻不存在");
        }
        return JsonResult.success(newsList);
    }

    /**
     * 判断日期是否符合要求
     * @param date 传入的日期
     * @return
     */
    private JsonResult<Object> processDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd");
        Date date1 = null, date2 = null;
        try {
            date1 = format.parse(date);
            date2 = format.parse(format.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
            return JsonResult.error("日期格式错误");
        }
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 24 * 60 * 60));
        if(days > 10) {
            return JsonResult.error("新闻已过期");
        }
        if(days < 0) {
            return JsonResult.error("日期未到");
        }
        return JsonResult.success(DateUtil.format(date1, "yyyy_MM_dd"));
    }
}
