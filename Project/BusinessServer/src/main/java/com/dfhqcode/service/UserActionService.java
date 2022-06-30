package com.dfhqcode.service;

import com.dfhqcode.model.entity.UserClick;
import com.dfhqcode.model.entity.UserCollection;
import com.dfhqcode.model.entity.UserLike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * @author zxd3099
 * @create 2022-06-27-10:12
 */
@Service
public class UserActionService {
    @Autowired
    private MongoTemplate template;

    /**
     * Collection名称
     */
    private String CollectionName1 = "User_Click";
    private String CollectionName2 = "User_Collection";
    private String CollectionName3 = "User_Like";

    /**
     * 向用户点击集合中添加数据
     */
    public void InsertUserClick(UserClick userClick)
    {
        template.save(userClick, CollectionName1);
    }

    /**
     * 向用户点赞集合中添加数据
     */
    public void InsertUserLike(UserLike userLike)
    {
        template.save(userLike, CollectionName3);
    }

    /**
     * 向用户收藏集合中添加数据
     */
    public void InsertUserCollection(UserCollection userCollection)
    {
        template.save(userCollection, CollectionName2);
    }

    /**
     * 用户收藏集合删除数据
     * @param userId
     * @param newsId
     */
    public void removeUserCollection(String userId, String newsId)
    {
        Query query = new Query(Criteria.where("user_id").is(userId)
                .and("news_id").is(newsId));
        template.remove(query, CollectionName2);
    }

    /**
     * 用户点赞集合删除数据
     * @param userId
     * @param newsId
     */
    public void RemoveUserLike(String userId, String newsId)
    {
        Query query = new Query(Criteria.where("user_id").is(userId)
                                        .and("news_id").is(newsId));
        template.remove(query, CollectionName3);
    }

    /**
     * 删除指定用户的点赞和收藏记录
     * @param userId
     */
    public void RemoveUser(String userId)
    {
        Query query = new Query(Criteria.where("user_id").is(userId));
        template.remove(query, CollectionName2);
        template.remove(query, CollectionName3);
    }

}
