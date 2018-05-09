package com.spring.repository;

import com.spring.entity.HouseSubscribe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 预约看房
 * Created by chenxizhong on 2018/5/6.
 */
@Repository
public interface HouseSubscribeRepository extends PagingAndSortingRepository<HouseSubscribe,Long> {
    HouseSubscribe findByHouseIdAndUserId(Long id, Long loginUserId);

    /**
     * 通过发布者id和状态分页查询预约看房信息
     * @param adminId
     * @param status
     * @param pageable
     * @return
     */
    Page<HouseSubscribe> findAllByAdminIdAndStatus(Long adminId, int status, Pageable pageable);

    /**
     * 查询房源发布者房源信息
     * @param houseId
     * @param adminId
     * @return
     */
    HouseSubscribe findByHouseIdAndAdminId(Long houseId, Long adminId);

    /**
     * 完成预约
     * @param id
     * @param status
     */
    @Modifying
    @Query("update HouseSubscribe as subscribe set subscribe.status = :status where subscribe.id = :id")
    void updateStatus(@Param(value = "id") Long id, @Param(value = "status") int status);
}
