package com.spring.repository;

import com.spring.entity.House;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 房屋
 * Created by chenxizhong on 2018/5/6.
 */
@Repository
public interface HouseRepository extends PagingAndSortingRepository<House, Long>, JpaSpecificationExecutor<House> {

    @Modifying
    @Query("update House as house set house.cover = :cover where house.id = :id")
    int updateCover(@Param(value = "id") Long id, @Param(value = "cover") String cover);

    @Modifying
    @Query("update House as house set house.status = :status where house.id = :id")
    int updateStatus(Long id, int status);

    @Modifying
    @Query("update House as house set house.watchTimes = house.watchTimes + 1 where house.id = :id")
    int updateWatchTimes(@Param(value = "id") Long houseId);
}
