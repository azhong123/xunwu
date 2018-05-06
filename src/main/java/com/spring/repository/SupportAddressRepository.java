package com.spring.repository;

import com.spring.entity.SupportAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 地址行政
 * Created by chenxizhong on 2018/5/6.
 */
@Repository
public interface SupportAddressRepository extends CrudRepository<SupportAddress,Long> {

    /**
     * 获取所有的城市的行政
     * @param key
     * @return
     */
    List<SupportAddress> findAllByLevel(String key);

    /**
     * 获取省下级城市
     * @param level
     * @param belongTo
     * @return
     */
    List<SupportAddress> findAllByLevelAndBelongTo(String level,String belongTo);

    /**
     * 根据城市类型 和 城市英文简写 获取对应信息
     * @param enName
     * @param level
     * @return
     */
    SupportAddress findByEnNameAndLevel(String enName, String level);

    /**
     * 根据城市类型 和 区域英文简写 获取对应信息
     * @param enName
     * @param belongTo
     * @return
     */
    SupportAddress findByEnNameAndBelongTo(String enName, String belongTo);


}
