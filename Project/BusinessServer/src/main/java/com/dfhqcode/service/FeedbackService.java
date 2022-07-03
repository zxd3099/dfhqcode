package com.dfhqcode.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.dfhqcode.model.entity.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zxd3099
 * @create 2022-07-02-11:22
 */
@Service
public class FeedbackService {
    @Autowired
    private MongoTemplate template;

    /**
     * 反馈Collection名称
     */
    private final String FeedbackCollectionName = "Feedback";

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
     * 将用户反馈信息保存进数据库中
     * @param feedback  用户反馈信息
     */
    public void insertFeedback(Feedback feedback)
    {
        template.save(feedback, FeedbackCollectionName);
    }

    /**
     * 更新用户反馈信息
     * @param userId  提交反馈的用户ID
     * @param submitTime  用户提交反馈时间
     * @param administrator  处理反馈的管理员姓名
     * @param handleTime  管理员处理反馈时间
     * @param conclusion  管理员处理反馈结果
     */
    public void updateFeedback(String userId, String submitTime, String administrator,
                               String handleTime, String conclusion)
    {
        // 根据userId查询反馈
        Query query = new Query(Criteria.where("user_id").is(userId).and("submit_time").is(submitTime));
        // 更新内容
        Update update = new Update();
        update.set("administrator", administrator);
        update.set("handle_time", handleTime);
        update.set("conclusion", conclusion);
        update.set("is_handled", true);

        template.updateFirst(query, update, FeedbackCollectionName);
    }

    /**
     * 查询所有未被处理的反馈信息
     * @param handled  带查询的反馈是否被处理
     * @param pageable  分页
     * @param key  排序关键词
     * @param order  排列顺序
     * @return
     */
    public List<Feedback> searchFeedback(boolean handled, Pageable pageable, String key, int order)
    {
        Query query = new Query(Criteria.where("is_handled").is(handled));

        query = attachQuery(query, pageable, key, order);
        return template.find(query, Feedback.class, FeedbackCollectionName);
    }

    /**
     * 根据用户ID查询反馈信息
     * @param userId 用户ID
     * @param pageable  分页
     * @param key  排序关键词
     * @param order  排列顺序
     * @return
     */
    public List<Feedback> searchFeedback(String userId, Pageable pageable, String key, int order)
    {
        Query query = new Query(Criteria.where("user_id").is("userId"));

        query = attachQuery(query, pageable, key, order);
        return template.find(query, Feedback.class, FeedbackCollectionName);
    }

    /**
     * 查询某日用户反馈数、处理的反馈数和未被处理的反馈数
     * @param date  日期
     */
    public JSONObject getFeedbackData(String date)
    {
        // 格式化日期
        date = DateUtil.parse(date, "yyyy-MM-dd").toString("yyyy-MM-dd");

        // 查询某日用户总反馈数
        Query query1 = new Query(Criteria.where("submit_time").regex(date));
        int totalCount = template.find(query1, Feedback.class, FeedbackCollectionName).size();
        // 查询某日未被处理的反馈数
        Query query2 = new Query(Criteria.where("submit_time").regex(date).and("is_handled").is(false));
        int unhandledCount = template.find(query2, Feedback.class, FeedbackCollectionName).size();
        // 计算某日处理的反馈数
        int handledCount = totalCount - unhandledCount;

        JSONObject result = new JSONObject();
        result.put("totalCount", totalCount);
        result.put("handledCount", handledCount);
        result.put("unhandledCount", unhandledCount);
        return result;
    }
}
