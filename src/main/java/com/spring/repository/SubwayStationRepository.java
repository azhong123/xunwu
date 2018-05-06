package com.spring.repository;

import com.spring.entity.SubwayStation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 地铁站点信息
 * Created by chenxizhong on 2018/5/6.
 */
@Repository
public interface SubwayStationRepository extends CrudRepository<SubwayStation,Long>{

    List<SubwayStation> findAllBySubwayId(Long subwayId);

}
