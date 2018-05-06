package com.spring.web.controller.admin;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.spring.common.base.ApiResponse;
import com.spring.common.base.ServiceResult;
import com.spring.entity.SupportAddress;
import com.spring.service.house.IAddressService;
import com.spring.service.house.IHouseService;
import com.spring.service.house.IQiNiuService;
import com.spring.web.dto.HouseDTO;
import com.spring.web.dto.QiNiuPutRet;
import com.spring.web.dto.SupportAddressDTO;
import com.spring.web.form.HouseForm;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
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

}
