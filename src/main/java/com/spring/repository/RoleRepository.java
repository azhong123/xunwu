package com.spring.repository;

import com.spring.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色 Repository
 * Created by chenxizhong on 2018/5/5.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role,Long> {

    /**
     * 通过用户id查找对应角色
     * @param userId
     * @return
     */
    List<Role> findRolesByUserId(Long userId);

}
