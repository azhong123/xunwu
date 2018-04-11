package com.spring.repository;

import com.spring.ApplicationTests;
import com.spring.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by chenxizhong on 2018/4/11.
 */
public class UserRepositoryTest extends ApplicationTests{

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindOne(){
        User user = userRepository.findOne(1L);
        Assert.assertEquals("wali",user.getName());
    }
}