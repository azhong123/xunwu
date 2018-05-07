package com.spring.repository;

import com.spring.entity.HousePicture;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * Created by chenxizhong on 2018/5/6.
 */
@Repository
public interface HousePictureRepository extends CrudRepository<HousePicture,Long>{

    List<HousePicture> findAllByHouseId(Long houseId);

}
