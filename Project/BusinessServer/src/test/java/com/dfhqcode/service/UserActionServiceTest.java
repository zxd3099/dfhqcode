package com.dfhqcode.service;

import com.dfhqcode.base.BaseTest;
import com.dfhqcode.model.entity.UserClick;
import com.dfhqcode.model.entity.UserCollection;
import com.dfhqcode.model.entity.UserLike;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zxd3099
 * @create 2022-06-30-15:16
 */
class UserActionServiceTest extends BaseTest {
    @Autowired
    private UserActionService userActionService;

    @Test
    void insertUserClick() {
        UserClick userClick = new UserClick(
                "TBRhr0KfZObHcJZGFW6FxUtr",
                "751ca56d-140d-4307-b836-2c4b7be2cd21",
                37,
                "2022-06-21 02:04:04"
        );
        userActionService.InsertUserClick(userClick);
    }

    @Test
    void insertUserLike() {
        UserLike userLike = new UserLike(
                "TBRhr0KfZObHcJZGFW6FxUtr",
                "751ca56d-140d-4307-b836-2c4b7be2cd21",
                "2022-06-21 02:04:04"
        );
        userActionService.InsertUserLike(userLike);
    }

    @Test
    void insertUserCollection() {
        UserCollection userCollection = new UserCollection(
                "TBRhr0KfZObHcJZGFW6FxUtr",
                "606541be-fae5-4e8a-a7fb-c092316f7a49",
                "英国工业联合会警告：英国经济面临滞胀和衰退风险",
                "sina_news",
                10,
                "英国工业联合会（CBI）周一警告称，英国经济明年面临滞胀，并可能很容易陷入衰退。此前，该组织因通胀飙升下调了经济增长预期。CBI是过去一周第三个下调英国经济增长预期的主要机构，此前英国商会（BritishChambersofCommerce）下调了英国的经济增长预期，经合组织（OECD）也警告称，在除俄罗斯外的所有主要经济体中，英国的经济前景最疲软。英国工业联合会总干事托尼·丹克说：“让我明确一点，我们预计英国经济将陷入停滞。让我们陷入经济衰退并不需要太多。即使我们不陷入衰退，也会有很多人觉得这就是衰退。”。CBI预测，尽管英国政府采取了370亿英镑的生活成本支持措施，今年英国家庭的实际可支配收入仍将下降2.2%，这是自上世纪50年代有记录以来的最大降幅。CBI预测英国经济明年将增长1.0%，低于此前预测的3%。该协会预测2022年英国经济将增长3.7%，但这在很大程度上反映了2021年GDP低迷的基数效应，当时企业在一年中大部分时间都面临着新冠疫情导致的限制措施。责任编辑：于健SF069",
                "2022-06-13 08:15",
                "2022-06-30 15:57:24"
        );
        userActionService.InsertUserCollection(userCollection);
    }

    @Test
    void removeUserCollection() {
        userActionService.removeUserCollection("TBRhr0KfZObHcJZGFW6FxUtr", "606541be-fae5-4e8a-a7fb-c092316f7a49");
    }

    @Test
    void removeUserLike() {
        userActionService.RemoveUserLike("TBRhr0KfZObHcJZGFW6FxUtr", "751ca56d-140d-4307-b836-2c4b7be2cd21");
    }

    @Test
    void removeUser() {
        userActionService.RemoveUser("TBRhr0KfZObHcJZGFW6FxUtr");
    }

    @Test
    void listCollections() {
        List<UserCollection> list = new ArrayList<>();
        UserCollection userCollection = new UserCollection(
                "TBRhr0KfZObHcJZGFW6FxUtr",
                "606541be-fae5-4e8a-a7fb-c092316f7a49",
                "英国工业联合会警告：英国经济面临滞胀和衰退风险",
                "sina_news",
                10,
                "英国工业联合会（CBI）周一警告称，英国经济明年面临滞胀，并可能很容易陷入衰退。此前，该组织因通胀飙升下调了经济增长预期。CBI是过去一周第三个下调英国经济增长预期的主要机构，此前英国商会（BritishChambersofCommerce）下调了英国的经济增长预期，经合组织（OECD）也警告称，在除俄罗斯外的所有主要经济体中，英国的经济前景最疲软。英国工业联合会总干事托尼·丹克说：“让我明确一点，我们预计英国经济将陷入停滞。让我们陷入经济衰退并不需要太多。即使我们不陷入衰退，也会有很多人觉得这就是衰退。”。CBI预测，尽管英国政府采取了370亿英镑的生活成本支持措施，今年英国家庭的实际可支配收入仍将下降2.2%，这是自上世纪50年代有记录以来的最大降幅。CBI预测英国经济明年将增长1.0%，低于此前预测的3%。该协会预测2022年英国经济将增长3.7%，但这在很大程度上反映了2021年GDP低迷的基数效应，当时企业在一年中大部分时间都面临着新冠疫情导致的限制措施。责任编辑：于健SF069",
                "2022-06-13 08:15",
                "2022-06-30 15:57:24"
        );
        list.add(userCollection);

        List<UserCollection> result = userActionService.listCollections("TBRhr0KfZObHcJZGFW6FxUtr");
        Assert.assertEquals("结果错误", list, result);
    }
}