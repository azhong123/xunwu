package com.spring.service.house;

import com.spring.common.base.ServiceMultiResult;
import com.spring.entity.SupportAddress;
import com.spring.web.dto.SubwayDTO;
import com.spring.web.dto.SubwayStationDTO;
import com.spring.web.dto.SupportAddressDTO;

import java.util.Map;


/**
 * 城市行政
 * Created by chenxizhong on 2018/5/6.
 */
public interface IAddressService {

    ServiceMultiResult<SupportAddressDTO> findAllCities();

    /**
     * 获取城市下级区域
     * @param cityEnName
     * @return
     */
    ServiceMultiResult<SupportAddressDTO> findAllRegionsByCityName(String cityEnName);


    /**
     * 获取城市的所有地铁线路
     * @param cityName
     * @return
     */
    ServiceMultiResult<SubwayDTO> findAllSubwayByCity(String cityName);

    /**
     * 获取地铁站点信息
     * @param subwayId
     * @return
     */
    ServiceMultiResult<SubwayStationDTO> findAllSubwayStationBySubwayId(Long subwayId);


    /**
     * 根据城市和区域英文简写 获取具体区域信息
     * @param cityEnName
     * @param regionEnName
     * @return
     */
    Map<SupportAddress.Level,SupportAddressDTO> findCityAndRegion(String cityEnName,String regionEnName);

}
