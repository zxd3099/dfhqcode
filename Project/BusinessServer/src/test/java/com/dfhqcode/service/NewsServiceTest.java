package com.dfhqcode.service;

import com.alibaba.fastjson.JSON;
import com.dfhqcode.base.BaseTest;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zxd3099
 * @create 2022-06-30-15:16
 */
class NewsServiceTest extends BaseTest {
    @Autowired
    private NewsService newsService;

    @Test
    void findByTitle() {
    }

    @Test
    void findByAuthor() {
    }

    @Test
    void findByCategory() {
    }

    @Test
    void findByKeyWord() {
    }

    @Test
    void findNewsById() {
    }
}