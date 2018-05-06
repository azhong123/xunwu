package com.spring.repository;

import com.spring.entity.HouseDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * 房屋具体信息
 * Created by chenxizhong on 2018/5/6.
 */
@Repository
public interface HouseDetailRepository extends CrudRepository<HouseDetail ,Long> {
}
