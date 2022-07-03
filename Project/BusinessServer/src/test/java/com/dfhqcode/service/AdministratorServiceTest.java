package com.dfhqcode.service;

import com.dfhqcode.base.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zxd3099
 * @create 2022-06-30-15:15
 */
class AdministratorServiceTest extends BaseTest {
    @Autowired
    private AdministratorService administratorService;

    @Test
    void insertAdmin() {
    }

    @Test
    void queryUser() {
    }

    @Test
    void testQueryUser() {
    }

    @Test
    void updateUserInfo() {
    }

    @Test
    void getAgeDistribution() {
        List<String> result = administratorService.getAgeDistribution();
        System.out.println(result);
    }
}