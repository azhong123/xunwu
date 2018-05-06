package com.spring.repository;

import com.spring.entity.HouseSubscribe;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * 预约看房
 * Created by chenxizhong on 2018/5/6.
 */
@Repository
public interface HouseSubscribeRepository extends CrudRepository<HouseSubscribe,Long> {
}
