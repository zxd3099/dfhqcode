package com.dfhqcode.service;

import com.dfhqcode.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * @author zxd3099
 * @create 2022-06-29-19:40
 */
@Service
public class UserService {
    @Autowired
    private MongoTemplate template;

    /**
     * 集合名称
     */
    private final String CollectionName = "User";

    /**
     * 向集合中添加数据
     * @param user 待插入用户数据
     */
    public void insertUser(User user)
    {
        template.save(user, CollectionName);
    }

    /**
     * 将用户信息从集合中删除
     * @param userId 待删除用户ID
     */
    public void deleteUser(String userId)
    {
        Query query = new Query(Criteria.where("_id").is(userId));
        template.remove(query, CollectionName);
    }

    /**
     * 更新用户最后一次登录时间
     * @param userId 待删除用户ID
     * @param loginTime  更新后的时间
     */
    public void updateUser(String userId, String loginTime)
    {
        Query query = new Query(Criteria.where("_id").is(userId));
        Update update = Update.update("last_login_time", loginTime);
        template.updateFirst(query, update, CollectionName);
    }
}
