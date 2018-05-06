package com.spring.repository;

import com.spring.entity.House;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * 房屋
 * Created by chenxizhong on 2018/5/6.
 */
@Repository
public interface HouseRepository extends CrudRepository<House,Long> {
}
