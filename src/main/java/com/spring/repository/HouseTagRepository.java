package com.spring.repository;

import com.spring.entity.HouseTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * 房屋标签
 * Created by chenxizhong on 2018/5/6.
 */
@Repository
public interface HouseTagRepository extends CrudRepository<HouseTag,Long>{
}
