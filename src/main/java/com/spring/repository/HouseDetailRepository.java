package com.spring.repository;

import com.spring.entity.HouseDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 房屋具体信息
 * Created by chenxizhong on 2018/5/6.
 */
@Repository
public interface HouseDetailRepository extends CrudRepository<HouseDetail ,Long> {

    HouseDetail findByHouseId(Long id);

    /**
     * 查询房源信息
     * @param houseIds
     * @return
     */
    List<HouseDetail> findAllByHouseIdIn(List<Long> houseIds);
}
