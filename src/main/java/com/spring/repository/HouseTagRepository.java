package com.spring.repository;

import com.spring.entity.HouseTag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 房屋标签
 * Created by chenxizhong on 2018/5/6.
 */
@Repository
public interface HouseTagRepository extends CrudRepository<HouseTag,Long>{
    List<HouseTag> findAllByHouseId(Long id);

    /**
     * 通过房源标签和id
     * @param tag
     * @param houseId
     * @return
     */
    HouseTag findByNameAndHouseId(String tag, Long houseId);

    List<HouseTag> findAllByHouseIdIn(List<Long> houseIds);
}
