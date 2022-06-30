package com.dfhqcode.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.dfhqcode.model.entity.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author zxd3099
 * @create 2022-06-26-20:34
 */
@Service
public class NewsService {
    @Autowired
    private MongoTemplate template;

    private final String prefix = "News";

    /**
     * 实现分页查询和查询结果排序的工具函数
     *
     * @param pageable 分页
     * @param key      排序依据
     * @param order    1表示升序；0表示降序
     */
    private Query attachQuery(Query query, Pageable pageable, String key, int order)
    {
        // 排序
        if (order == 0) {
            query.with(Sort.by(Sort.Order.asc(key)));
        } else {
            query.with(Sort.by(Sort.Order.desc(key)));
        }
        // 分页查询
        query.with(pageable);
        return query;
    }

    /**
     * 根据标题查找资讯
     * @param date 日期
     * @param title 新闻标题
     */
    public List<News> findByTitle(String title, String date)
    {
        // 定位到数据库的Collection
        String collectionName = prefix + date;

        // 字符串模糊查询
        Query query = new Query();
        Criteria criteria = Criteria.where("title").regex(title);
        query.addCriteria(criteria);

        return template.find(query, News.class, collectionName);
    }

    /**
     * 根据作者查找资讯
     * @param date 日期
     * @param authorName 作者姓名
     */
    public List<News> findByAuthor(String authorName, String date, Pageable pageable, String key, int order)
    {
        // 定位到数据库的Collection
        String collectionName = prefix + date;

        // 准确查询
        Query query = new Query();
        Criteria criteria = Criteria.where("author").is(authorName);
        query.addCriteria(criteria);
        query = attachQuery(query, pageable, key, order);

        return template.find(query, News.class, collectionName);
    }

    /**
     * 按照新闻类别查找新闻
     * @param date
     * @param categoryId
     */
    public List<News> findByCategory(int categoryId, String date, Pageable pageable, String key, int order)
    {
        // 定位到数据库的Collection
        String collectionName = prefix + date;
        System.out.println(collectionName);

        // 准确查询
        Query query = new Query();
        Criteria criteria = Criteria.where("category_id").is(categoryId);
        query.addCriteria(criteria);
        query = attachQuery(query, pageable, key, order);

        return template.find(query, News.class, collectionName);
    }

    /**
     * 根据关键词模糊查询
     * @param date
     * @param keyword 关键词
     */
    public List<News> findByKeyWord(String keyword, String date, Pageable pageable, String key, int order)
    {
        // 定位到数据库的Collection
        String collectionName = prefix + date;

        // 模糊查询
        Query query = new Query();
        Pattern pattern = Pattern.compile("^.*" + keyword + ".*$", Pattern.CASE_INSENSITIVE);
        Criteria criteria = Criteria.where("content").regex(pattern);
        query.addCriteria(criteria);
        query = attachQuery(query, pageable, key, order);

        return template.find(query, News.class, collectionName);
    }

    /**
     * 根据新闻Id查找新闻内容
     */
    public News findNewsById(String newsId)
    {
        DateTime time = DateUtil.parse(DateUtil.today());
        for (int i = 0; i < 10; ++i) {
            String collectionName = prefix + DateUtil.format(DateUtil.offsetDay(time, -i), "yyyy_MM_dd");
            Query query = new Query(Criteria.where("_id").is(newsId));
            return template.findOne(query, News.class, collectionName);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.now());
    }
}