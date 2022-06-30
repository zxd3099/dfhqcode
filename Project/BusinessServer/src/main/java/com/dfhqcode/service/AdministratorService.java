package com.dfhqcode.service;

import com.dfhqcode.model.entity.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * @author zxd3099
 * @create 2022-06-27-13:00
 */
@Service
public class AdministratorService {
    @Autowired
    private MongoTemplate template;

    private String CollectionName = "Administrator";

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
}
