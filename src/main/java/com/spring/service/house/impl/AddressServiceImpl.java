package com.spring.service.house.impl;

import com.spring.common.base.ServiceMultiResult;
import com.spring.common.base.ServiceResult;
import com.spring.entity.Subway;
import com.spring.entity.SubwayStation;
import com.spring.entity.SupportAddress;
import com.spring.repository.SubwayRepository;
import com.spring.repository.SubwayStationRepository;
import com.spring.repository.SupportAddressRepository;
import com.spring.service.house.IAddressService;
import com.spring.web.dto.SubwayDTO;
import com.spring.web.dto.SubwayStationDTO;
import com.spring.web.dto.SupportAddressDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 房屋 service
 * Created by chenxizhong on 2018/5/6.
 */
@Service
public class AddressServiceImpl implements IAddressService{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SupportAddressRepository supportAddressRepository;

    @Autowired
    private SubwayRepository subwayRepository;

    @Autowired
    private SubwayStationRepository subwayStationRepository;

    @Override
    public ServiceMultiResult<SupportAddressDTO> findAllCities() {
        List<SupportAddress> addresses = supportAddressRepository.findAllByLevel(SupportAddress.Level.CITY.getValue());
        List<SupportAddressDTO> addressDTOS = new ArrayList<>();
        for (SupportAddress supportAddress : addresses) {
            SupportAddressDTO target = modelMapper.map(supportAddress, SupportAddressDTO.class);
            addressDTOS.add(target);
        }

        return new ServiceMultiResult<>(addressDTOS.size(), addressDTOS);
    }

    /**
     * 获取城市的下级列表
     * @param cityEnName
     * @return
     */
    @Override
    public ServiceMultiResult<SupportAddressDTO> findAllRegionsByCityName(String cityEnName) {
        if(cityEnName == null){
            return new ServiceMultiResult<>(0,null);
        }
        List<SupportAddress> addressList = supportAddressRepository.findAllByLevelAndBelongTo(SupportAddress.Level.REGION.getValue(),cityEnName);
        if(CollectionUtils.isEmpty(addressList)){
            return new ServiceMultiResult<>(0,null);
        }
        List<SupportAddressDTO> addressDTOS = new ArrayList<>();
        addressList.forEach(address ->{
            addressDTOS.add(modelMapper.map(address,SupportAddressDTO.class));
        });
        return new ServiceMultiResult<>(addressDTOS.size(),addressDTOS);
    }

    /**
     * 获取城市的所有地铁线路
     * @param cityName
     * @return
     */
    @Override
    public ServiceMultiResult<SubwayDTO> findAllSubwayByCity(String cityName) {
        if(StringUtils.isEmpty(cityName)){
            return new ServiceMultiResult<>(0,null);
        }
        List<Subway> subwayList = subwayRepository.findAllByCityEnName(cityName);
        if(CollectionUtils.isEmpty(subwayList)){
            return new ServiceMultiResult<>(0,null);
        }
        List<SubwayDTO> subwayDTOS = new ArrayList<>();
        subwayList.forEach(subway ->{subwayDTOS.add(modelMapper.map(subway,SubwayDTO.class));});
        return new ServiceMultiResult<>(subwayDTOS.size(),subwayDTOS);
    }

    /**
     * 获取地铁站点信息
     * @param subwayId
     * @return
     */
    @Override
    public ServiceMultiResult<SubwayStationDTO> findAllSubwayStationBySubwayId(Long subwayId) {
        if(StringUtils.isEmpty(String.valueOf(subwayId))){
            return new ServiceMultiResult<>(0,null);
        }
        List<SubwayStation> subwayStations = subwayStationRepository.findAllBySubwayId(subwayId);

        if(CollectionUtils.isEmpty(subwayStations)){
            return new ServiceMultiResult<>(0,null);
        }
        List<SubwayStationDTO> stationDTOS = new ArrayList<>();
        subwayStations.forEach(station ->{stationDTOS.add(modelMapper.map(station,SubwayStationDTO.class));});
        return new ServiceMultiResult<>(stationDTOS.size(),stationDTOS);
    }

    /**
     * 根据城市和区域英文简写 获取具体区域信息
     * @param cityEnName
     * @param regionEnName
     * @return
     */
    @Override
    public Map<SupportAddress.Level, SupportAddressDTO> findCityAndRegion(String cityEnName, String regionEnName) {
        Map<SupportAddress.Level, SupportAddressDTO> result = new HashMap<>();

        SupportAddress city = supportAddressRepository.findByEnNameAndLevel(cityEnName,SupportAddress.Level.CITY.getValue());

        SupportAddress region = supportAddressRepository.findByEnNameAndBelongTo(regionEnName,city.getEnName());

        result.put(SupportAddress.Level.CITY,modelMapper.map(city, SupportAddressDTO.class));
        result.put(SupportAddress.Level.REGION, modelMapper.map(region, SupportAddressDTO.class));

        return result;
    }

    /**
     * 获取地铁线信息
     * @param subwayId
     * @return
     */
    @Override
    public ServiceResult<SubwayDTO> findSubway(Long subwayId) {
        if (subwayId == null) {
            return ServiceResult.notFound();
        }
        Subway subway = subwayRepository.findOne(subwayId);
        if (subway == null) {
            return ServiceResult.notFound();
        }
        return ServiceResult.of(modelMapper.map(subway, SubwayDTO.class));
    }

    /**
     * 获取地铁站点信息
     * @param stationId
     * @return
     */
    @Override
    public ServiceResult<SubwayStationDTO> findSubwayStation(Long stationId) {
        if(stationId == null){
            return ServiceResult.notFound();
        }
        SubwayStation subwayStation = subwayStationRepository.findOne(stationId);
        if(subwayStation == null){
            return ServiceResult.notFound();
        }
        return ServiceResult.of(modelMapper.map(subwayStation,SubwayStationDTO.class));
    }
}
