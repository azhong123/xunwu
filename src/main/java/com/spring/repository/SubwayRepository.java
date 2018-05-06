package com.spring.repository;

import com.spring.entity.Subway;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 城市地铁线路
 * Created by chenxizhong on 2018/5/6.
 */
@Repository
public interface SubwayRepository extends CrudRepository<Subway,Long> {

    List<Subway> findAllByCityEnName(String cityName);

}
