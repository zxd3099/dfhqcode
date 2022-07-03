package com.dfhqcode.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dfhqcode.model.entity.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author zxd3099
 * @create 2022-06-27-13:00
 */
@Service
public class AdministratorService {
    @Autowired
    private MongoTemplate template;

    /**
     * 新闻Collection前缀
     */
    private final String prefix = "News";

    /**
     * 用户集合名称
     */
    private final String UserCollectionName = "User";

    /**
     * 管理员集合名称
     */
    private final String CollectionName = "Administrator";

    /**
     * 用户点击集合名称
     */
    private final String UserClickCollectionName = "User_Click";

    /**
     * 用户收藏集合名称
     */
    private final String UserCollectCollectionName = "User_Collection";

    /**
     * 用户点赞集合名称
     */
    private final String UserLikeCollectionName = "User_Like";

    /**
     * 将查询结果转换为List<String>类型
     * @return [{"_id": ,"": };{"_id": ,"": };{"_id": ,"": };...]
     */
    private List<String> transform(Aggregation aggregation, String collectionName)
    {
        AggregationResults<BasicDBObject> results = template.aggregate(aggregation, collectionName, BasicDBObject.class);

        List<String> result = new ArrayList<>();
        for (Iterator<BasicDBObject> iterator = results.iterator(); iterator.hasNext(); ) {
            DBObject obj = iterator.next();
            result.add(JSON.toJSONString(obj));
        }
        return result;
    }

    /**
     * 向Administrator集合中添加数据
     */
    public void insertAdmin(Administrator administrator)
    {
        template.save(administrator, CollectionName);
    }

    /**
     * 根据用户名和密码查找用户
     * @param username
     * @param password
     */
    public Boolean queryUser(String username, String password)
    {
        Query query = new Query(Criteria.where("user_name").is(username).and("password").is(password));
        Administrator result = template.findOne(query, Administrator.class, CollectionName);
        if (result == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据用户名查找用户
     * @param username
     */
    public Boolean queryUser(String username)
    {
        Query query = new Query(Criteria.where("user_name").is(username));
        Administrator result = template.findOne(query, Administrator.class, CollectionName);
        if (result == null) {
            return false;
        }
        return true;
    }

    /**
     * 更新用户登录时间
     * @param loginTime
     * @param username
     */
    public void updateUserInfo(String username, String loginTime)
    {
        Query query = new Query(Criteria.where("user_name").is(username));
        Update update = Update.update("last_login_time", loginTime);
        template.updateFirst(query, update, CollectionName);
    }

    /**
     * 管理员查看指定日期的新增新闻数及其分类
     * @param date 日期
     */
    public List<String> searchNewsAdd(String date)
    {
        // 处理日期，格式化为yyyy-MM-dd
        date = DateUtil.parse(date, "yyyy-MM-dd").toString("yyyy-MM-dd");

        AggregationOperation operation1 = Aggregation.group("category_id").count().as("totalNum");
        SortOperation operation2 = Aggregation.sort(Sort.by(Sort.Direction.DESC, "totalNum"));
        Aggregation aggregation = Aggregation.newAggregation(operation1, operation2);

        String collectionName = prefix + date;
        return transform(aggregation, collectionName);
    }


    /**
     * 查看用户地域分布
     * @return 用户地域分布 [{"_id": ,"cityNum": };...;{ ...}]
     */
    public List<String> viewUserDistribution()
    {
        AggregationOperation operation1 = Aggregation.group("city").count().as("cityNum");
        SortOperation operation2 = Aggregation.sort(Sort.by(Sort.Direction.DESC, "cityNum"));
        Aggregation aggregation = Aggregation.newAggregation(operation1, operation2);

        return transform(aggregation, UserCollectionName);
    }


    /**
     * 查看某天新增用户数量
     * @return  某天新增用户数量
     */
    public int searchUsersAdd(String date)
    {
        date = DateUtil.parse(date, "yyyy-MM-dd").toString("yyyy-MM-dd");
        Query query = new Query(Criteria.where("register_time").regex(date));
        List<User> users = template.find(query, User.class, UserCollectionName);

        return users.size();
    }

    /**
     * 查看用户的性别分布
     * @return 用户的性别分布 [{"_id": 'M', "genderNum": };{"_id": "F", "genderNum": }]
     */
    public List<String> getGenderDistribution()
    {
        AggregationOperation operation1 = Aggregation.group("gender").count().as("genderNum");
        SortOperation operation2 = Aggregation.sort(Sort.by(Sort.Direction.DESC, "genderNum"));
        Aggregation aggregation = Aggregation.newAggregation(operation1, operation2);

        return transform(aggregation, UserCollectionName);
    }

    /**
     * 查看用户年龄分布
     * @return 用户的年龄分布 [{"_id": age1, "ageNum": };{"_id": age2, "ageNum": };...]
     */
    public List<String> getAgeDistribution()
    {
        AggregationOperation operation1 = Aggregation.group("age").count().as("ageNum");
        SortOperation operation2 = Aggregation.sort(Sort.by(Sort.Direction.ASC, "_id"));
        Aggregation aggregation = Aggregation.newAggregation(operation1, operation2);

        return transform(aggregation, UserCollectionName);
    }

    /**
     * 查看某日新闻的总浏览量、点赞量和收藏量
     * @param date
     * @return
     */
    public JSONObject getNewsData(String date)
    {
        // 格式化日期
        date = DateUtil.parse(date, "yyyy-MM-dd").toString("yyyy-MM-dd");
        // 查询条件
        Query query = new Query(Criteria.where("time").regex(date));

        // 返回结果
        JSONObject result = new JSONObject();
        result.put("clickNum", template.find(query, UserClick.class, UserClickCollectionName).size());
        result.put("likeNum", template.find(query, UserLike.class, UserLikeCollectionName).size());
        result.put("collectNum", template.find(query, UserCollection.class, UserCollectCollectionName).size());
        return result;
    }

    /**
     * 查看某日老用户的数目
     * @param date  日期
     * @return  老用户的数目
     */
    public Integer getOldUserNum(String date)
    {
        // 格式化日期
        date = DateUtil.parse(date, "yyyy-MM-dd").toString("yyyy-MM-dd");
        // 老用户表示注册时间早于登录时间
        Query query = new Query(Criteria.where("last_login_time").regex(date).and("register_time").gt(date));
        return template.find(query, User.class, UserCollectionName).size();
    }
}
