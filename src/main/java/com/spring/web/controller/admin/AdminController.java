package com.spring.web.controller.admin;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.spring.common.base.*;
import com.spring.entity.SupportAddress;
import com.spring.service.house.IAddressService;
import com.spring.service.house.IHouseService;
import com.spring.service.house.IQiNiuService;
import com.spring.web.dto.*;
import com.spring.web.form.DatatableSearch;
import com.spring.web.form.HouseForm;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 后台
 * Created by chenxizhong on 2018/5/5.
 */
@Controller
public class AdminController {

    @Autowired
    private IQiNiuService qiNiuService;

    @Autowired
    private IAddressService addressService;

    @Autowired
    private IHouseService houseService;

    @Autowired
    private Gson gson;

    /**
     * 后台主页
     * @return
     */
    @GetMapping("/admin/center")
    public String adminCenterPage(){
        return "admin/center";
    }

    /**
     * 后台欢迎页
     * @return
     */
    @GetMapping("/admin/welcome")
    public String welcomePage(){
        return "admin/welcome";
    }

    /**
     * 管理员登陆
     * @return
     */
    @GetMapping("/admin/login")
    public String adminLoginPage(){
        return "admin/login";
    }

    /**
     * 新增房源功能页
     * @return
     */
    @GetMapping("admin/add/house")
    public String addHousePage() {
        return "admin/house-add";
    }

    /**
     * 房源列表页
     * @return
     */
    @GetMapping("admin/house/list")
    public String houseListPage() {
        return "admin/house-list";
    }

    /**
     * 房源列表
     * @param searchBody
     * @return
     */
    @ResponseBody
    @PostMapping("admin/houses")
    public ApiDataTableResponse houses(@ModelAttribute DatatableSearch searchBody) {
        ServiceMultiResult<HouseDTO> result = houseService.adminQuery(searchBody);

        ApiDataTableResponse response = new ApiDataTableResponse(ApiResponse.Status.SUCCESS);
        response.setData(result.getResult());
        response.setRecordsFiltered(result.getTotal());
        response.setRecordsTotal(result.getTotal());

        response.setDraw(searchBody.getDraw());
        return response;
    }

    /**
     * 上传图片
     * @param file
     * @return
     */
    @ResponseBody
    @PostMapping(value = "admin/upload/photo" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse uploadPhoto(@RequestParam("file")MultipartFile file){
        if(file.isEmpty()){
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_VALID_PARAMS);
        }
        // 获取文件名
        String filename = file.getOriginalFilename();

        try {
            InputStream inputStream = file.getInputStream();
            Response response = qiNiuService.uploadFile(inputStream);
            if(response.isOK()){
                QiNiuPutRet ret = gson.fromJson(response.bodyString(), QiNiuPutRet.class);
                return ApiResponse.ofSuccess(ret);
            }else {
                return ApiResponse.ofMessage(response.statusCode,response.getInfo());
            }

        } catch (QiniuException e){
            Response response = e.response;
            try {
                return ApiResponse.ofMessage(response.statusCode,response.bodyString());
            } catch (QiniuException e1) {
                e1.printStackTrace();
                return ApiResponse.ofStatus(ApiResponse.Status.INTERNAL_SERVER_ERROR);
            }
        }catch (IOException e) {
            return ApiResponse.ofStatus(ApiResponse.Status.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 新增房源
     * @param houseForm
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @PostMapping("admin/add/house")
    public ApiResponse addHouse(@Valid @ModelAttribute("form-house-add") HouseForm houseForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(),bindingResult.getAllErrors().get(0).getDefaultMessage(),null);
        }

        if(houseForm.getPhotos() == null || houseForm.getCover() == null){
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), "必须上传图片");
        }

        // 根据上传的城市和区域简写 查询并验证是否正确
        Map<SupportAddress.Level, SupportAddressDTO> addressDTOMap = addressService.findCityAndRegion(houseForm.getCityEnName(), houseForm.getRegionEnName());

        if(addressDTOMap.keySet().size() != 2){
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_VALID_PARAMS);
        }

        ServiceResult<HouseDTO> result = houseService.save(houseForm);
        if(result.isSuccess()){
            return ApiResponse.ofSuccess(result.getResult());
        }

        return ApiResponse.ofSuccess(ApiResponse.Status.NOT_VALID_PARAMS);
    }

    /**
     * 获取房源信息
     * @param id
     * @param model
     * @return
     */
    @GetMapping("admin/house/edit")
    public String getHouseEditPage(@RequestParam(value = "id") Long id, Model model){
        if(id == null && id < 1L){
            return "404";
        }
        ServiceResult<HouseDTO> serviceResult = houseService.findCompleteOne(id);
        if (!serviceResult.isSuccess()) {
            return "404";
        }

        HouseDTO result = serviceResult.getResult();
        model.addAttribute("house", result);

        Map<SupportAddress.Level, SupportAddressDTO> addressMap = addressService.findCityAndRegion(result.getCityEnName(), result.getRegionEnName());
        model.addAttribute("city", addressMap.get(SupportAddress.Level.CITY));
        model.addAttribute("region", addressMap.get(SupportAddress.Level.REGION));

        HouseDetailDTO detailDTO = result.getHouseDetail();
        ServiceResult<SubwayDTO> subwayServiceResult = addressService.findSubway(detailDTO.getSubwayLineId());
        if (subwayServiceResult.isSuccess()) {
            model.addAttribute("subway", subwayServiceResult.getResult());
        }

        ServiceResult<SubwayStationDTO> subwayStationServiceResult = addressService.findSubwayStation(detailDTO.getSubwayStationId());
        if (subwayStationServiceResult.isSuccess()) {
            model.addAttribute("station", subwayStationServiceResult.getResult());
        }

        return "admin/house-edit";
    }

    /**
     * 编辑接口
     *
     * @param houseForm
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @PostMapping("admin/house/edit")
    public ApiResponse saveHouse(@Valid @ModelAttribute("form-house-edit") HouseForm houseForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ApiResponse(HttpStatus.BAD_REQUEST.value(), bindingResult.getAllErrors().get(0).getDefaultMessage(), null);
        }

        Map<SupportAddress.Level, SupportAddressDTO> addressMap = addressService.findCityAndRegion(houseForm.getCityEnName(), houseForm.getRegionEnName());

        if (addressMap.keySet().size() != 2) {
            return ApiResponse.ofSuccess(ApiResponse.Status.NOT_VALID_PARAMS);
        }

        ServiceResult result = houseService.update(houseForm);
        if (result.isSuccess()) {
            return ApiResponse.ofSuccess(null);
        }

        ApiResponse response = ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        response.setMessage(result.getMessage());
        return response;
    }

    /**
     * 移除图片接口
     * @param id
     * @return
     */
    @DeleteMapping("admin/house/photo")
    @ResponseBody
    public ApiResponse removeHousePhoto(@RequestParam(value = "id") Long id) {
        ServiceResult result = this.houseService.removePhoto(id);

        if (result.isSuccess()) {
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);
        } else {
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
        }
    }

    /**
     * 修改封面接口
     * @param coverId
     * @param targetId
     * @return
     */
    @PostMapping("admin/house/cover")
    @ResponseBody
    public ApiResponse updateCover(@RequestParam(value = "cover_id") Long coverId,
                                   @RequestParam(value = "target_id") Long targetId) {
        ServiceResult result = this.houseService.updateCover(coverId, targetId);

        if (result.isSuccess()) {
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);
        } else {
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
        }
    }

    /**
     * 增加标签接口
     * @param houseId
     * @param tag
     * @return
     */
    @PostMapping("admin/house/tag")
    @ResponseBody
    public ApiResponse addHouseTag(@RequestParam(value = "house_id") Long houseId,
                                   @RequestParam(value = "tag") String tag) {
        if (houseId < 1 || Strings.isNullOrEmpty(tag)) {
            return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }

        ServiceResult result = this.houseService.addTag(houseId, tag);
        if (result.isSuccess()) {
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);
        } else {
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
        }
    }

    /**
     * 移除标签接口
     * @param houseId
     * @param tag
     * @return
     */
    @DeleteMapping("admin/house/tag")
    @ResponseBody
    public ApiResponse removeHouseTag(@RequestParam(value = "house_id") Long houseId,
                                      @RequestParam(value = "tag") String tag) {
        if (houseId < 1 || Strings.isNullOrEmpty(tag)) {
            return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }

        ServiceResult result = this.houseService.removeTag(houseId, tag);
        if (result.isSuccess()) {
            return ApiResponse.ofStatus(ApiResponse.Status.SUCCESS);
        } else {
            return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
        }
    }

    /**
     * 审核接口
     * @param id
     * @param operation
     * @return
     */
    @PutMapping("admin/house/operate/{id}/{operation}")
    @ResponseBody
    public ApiResponse operateHouse(@PathVariable(value = "id") Long id,
                                    @PathVariable(value = "operation") int operation) {
        if (id <= 0) {
            return ApiResponse.ofStatus(ApiResponse.Status.NOT_VALID_PARAMS);
        }
        ServiceResult result;

        switch (operation) {
            case HouseOperation.PASS:
                result = this.houseService.updateStatus(id, HouseStatus.PASSES.getValue());
                break;
            case HouseOperation.PULL_OUT:
                result = this.houseService.updateStatus(id, HouseStatus.NOT_AUDITED.getValue());
                break;
            case HouseOperation.DELETE:
                result = this.houseService.updateStatus(id, HouseStatus.DELETED.getValue());
                break;
            case HouseOperation.RENT:
                result = this.houseService.updateStatus(id, HouseStatus.RENTED.getValue());
                break;
            default:
                return ApiResponse.ofStatus(ApiResponse.Status.BAD_REQUEST);
        }

        if (result.isSuccess()) {
            return ApiResponse.ofSuccess(null);
        }
        return ApiResponse.ofMessage(HttpStatus.BAD_REQUEST.value(), result.getMessage());
    }
}
