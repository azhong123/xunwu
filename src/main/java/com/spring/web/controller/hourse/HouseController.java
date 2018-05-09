package com.spring.web.controller.hourse;

import com.spring.common.base.ApiResponse;
import com.spring.common.base.ServiceMultiResult;
import com.spring.common.base.ServiceResult;
import com.spring.entity.SupportAddress;
import com.spring.service.house.IAddressService;
import com.spring.service.house.IHouseService;
import com.spring.service.user.IUserService;
import com.spring.web.dto.*;
import com.spring.web.form.RentSearch;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 房屋controller
 * Created by chenxizhong on 2018/5/6.
 */
@Controller
public class HouseController {

    @Autowired
    private IAddressService addressService;

    @Autowired
    private IHouseService houseService;

    @Autowired
    private IUserService userService;

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

    /**
     * 获取前端房源信息
     * @param rentSearch
     * @param model
     * @param session
     * @param redirectAttributes
     * @return
     */
    @GetMapping("rent/house")
    public String rentHousePage(@ModelAttribute RentSearch rentSearch, Model model, HttpSession session,RedirectAttributes redirectAttributes){
            if(rentSearch.getCityEnName() == null){
                String cityEnNameInSession = (String) session.getAttribute("cityEnName");
                if(cityEnNameInSession == null){
                    redirectAttributes.addAttribute("msg", "must_chose_city");
                }else {
                    rentSearch.setCityEnName(cityEnNameInSession);
                }
            }else {
                session.setAttribute("cityEnName", rentSearch.getCityEnName());
            }

        ServiceResult<SupportAddressDTO> city = addressService.findCity(rentSearch.getCityEnName());
            if(!city.isSuccess()){
                redirectAttributes.addAttribute("msg", "must_chose_city");
                return "redirect:/index";
            }

            model.addAttribute("currentCity",city.getResult());


        // 获取全部区域信息
        ServiceMultiResult<SupportAddressDTO> regionsByCityName = addressService.findAllRegionsByCityName(rentSearch.getCityEnName());
        if(regionsByCityName.getResult() == null || regionsByCityName.getTotal() < 1){
            redirectAttributes.addAttribute("msg", "must_chose_city");
            return "redirect:/index";
        }

        return null;


    }

    /**
     * 获房源详细信息
     * @return
     */
    @GetMapping("rent/house/show/{id}")
    public String show(@PathVariable(value = "id") Long houseId, Model model){
        if(houseId <= 0L){
            return "404";
        }

        ServiceResult<HouseDTO> serviceResult = houseService.findCompleteOne(houseId);
        if (!serviceResult.isSuccess()) {
            return "404";
        }

        // 获取房源的 城市和区域信息
        HouseDTO houseDTO = serviceResult.getResult();
        Map<SupportAddress.Level, SupportAddressDTO> addressMap = addressService.findCityAndRegion(houseDTO.getCityEnName(), houseDTO.getRegionEnName());

        SupportAddressDTO city = addressMap.get(SupportAddress.Level.CITY);
        SupportAddressDTO region = addressMap.get(SupportAddress.Level.REGION);

        model.addAttribute("city", city);
        model.addAttribute("region", region);

        ServiceResult<UserDTO> userDTOServiceResult = userService.findById(houseDTO.getAdminId());
        model.addAttribute("agent", userDTOServiceResult.getResult());
        model.addAttribute("house", houseDTO);
        return "house-detail";

    }

}
