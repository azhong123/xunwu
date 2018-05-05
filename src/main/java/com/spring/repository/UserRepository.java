package com.spring.repository;

import com.spring.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户
 * Created by chenxizhong on 2018/4/11.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * 通过用户名查询查找用户
     */
    User findUserByName(String name);
}
