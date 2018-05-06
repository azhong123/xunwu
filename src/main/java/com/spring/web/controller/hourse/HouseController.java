package com.spring.web.controller.hourse;

import com.spring.common.base.ApiResponse;
import com.spring.common.base.ServiceMultiResult;
import com.spring.service.house.IAddressService;
import com.spring.web.dto.SubwayDTO;
import com.spring.web.dto.SubwayStationDTO;
import com.spring.web.dto.SupportAddressDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 房屋controller
 * Created by chenxizhong on 2018/5/6.
 */
@Controller
public class HouseController {

    @Autowired
    private IAddressService addressService;

    /**
     * 获取所有的城市行政
     * @return
     */
    @ResponseBody
    @GetMapping("address/support/cities")
    public ApiResponse getSupportCities(){
        ServiceMultiResult<SupportAddressDTO> cities = addressService.findAllCities();
        if(cities.getResultSize() == 0){
            return ApiResponse.ofSuccess(ApiResponse.Status.NOT_FOUND);
        }

        return ApiResponse.ofSuccess(cities.getResult());
    }

    /**
     * 获取对应城市支持区域列表
     * @param cityEnName
     * @return
     */
    @ResponseBody
    @GetMapping("address/support/regions")
    public ApiResponse getSupportRegions(@RequestParam("city_name") String cityEnName){
        ServiceMultiResult<SupportAddressDTO> addressResult = addressService.findAllRegionsByCityName(cityEnName);
        if(addressResult.getResult() == null || addressResult.getTotal() < 1){
            return ApiResponse.ofSuccess(ApiResponse.Status.NOT_FOUND);
        }

        return ApiResponse.ofSuccess(addressResult.getResult());
    }

    /**
     * 获取城市地铁信息
     * @param cityEnName
     * @return
     */
    @ResponseBody
    @GetMapping("address/support/subway/line")
    public ApiResponse getSupportSubwayLine(@RequestParam(name = "city_name") String cityEnName){
        ServiceMultiResult<SubwayDTO> subways = addressService.findAllSubwayByCity(cityEnName);
        if(subways.getResultSize() <= 0){
            return ApiResponse.ofSuccess(ApiResponse.Status.NOT_FOUND);
        }
        return ApiResponse.ofSuccess(subways.getResult());
    }

    /**
     * 获取地铁对应站点信息
     * @param subwayId
     * @return
     */
    @ResponseBody
    @GetMapping("address/support/subway/station")
    public ApiResponse getSupportSubwayStation(@RequestParam(name = "subway_id") Long subwayId){
        ServiceMultiResult<SubwayStationDTO> stationDTOS = addressService.findAllSubwayStationBySubwayId(subwayId);
        if(stationDTOS.getResultSize() <= 0){
            return ApiResponse.ofSuccess(ApiResponse.Status.NOT_FOUND);
        }
        return ApiResponse.ofSuccess(stationDTOS.getResult());

    }



}
