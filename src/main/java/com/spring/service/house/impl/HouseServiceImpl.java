package com.spring.service.house.impl;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.spring.common.base.*;
import com.spring.entity.*;
import com.spring.repository.*;
import com.spring.service.house.IHouseService;
import com.spring.service.house.IQiNiuService;
import com.spring.service.search.ISearchService;
import com.spring.web.dto.HouseDTO;
import com.spring.web.dto.HouseDetailDTO;
import com.spring.web.dto.HousePictureDTO;
import com.spring.web.dto.HouseSubscribeDTO;
import com.spring.web.form.DatatableSearch;
import com.spring.web.form.HouseForm;
import com.spring.web.form.PhotoForm;
import com.spring.web.form.RentSearch;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.expression.Maps;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 *
 * Created by chenxizhong on 2018/5/6.
 */
@Service
public class HouseServiceImpl implements IHouseService {

    @Autowired
    private SubwayRepository subwayRepository;

    @Autowired
    private SubwayStationRepository subwayStationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private HouseDetailRepository houseDetailRepository;

    @Autowired
    private HousePictureRepository housePictureRepository;

    @Autowired
    private HouseTagRepository houseTagRepository;

    @Autowired
    private HouseSubscribeRepository subscribeRepository;

    @Autowired
    private ISearchService searchService;

    @Autowired
    private IQiNiuService qiNiuService;

    @Value("${qiniu.cdn.prefix}")
    private String cdnPrefix;

    /**
     * 添加房源
     * @param houseForm
     * @return
     */
    @Override
    @Transactional
    public ServiceResult<HouseDTO> save(HouseForm houseForm) {
        HouseDetail detail = new HouseDetail();
        ServiceResult<HouseDTO> wrapperDetailInfoResult = wrapperDetailInfo(detail, houseForm);
        if(wrapperDetailInfoResult != null){
            return wrapperDetailInfoResult;
        }
        // 保存房源信息
        House house = new House();
        modelMapper.map(houseForm,house);
        Date now = new Date();
        house.setCreateTime(now);
        house.setLastUpdateTime(now);
        house.setAdminId(LoginUserUtil.getLoginUserId());

        house = houseRepository.save(house);

        // 保存房源详细信息
        detail.setHouseId(house.getId());
        houseDetailRepository.save(detail);

        // 保存房源图片
        List<HousePicture> housePictures = generatePictures(houseForm, house.getId());
        Iterable<HousePicture> pictures = housePictureRepository.save(housePictures);

        HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
        HouseDetailDTO houseDetailDTO = modelMapper.map(detail, HouseDetailDTO.class);

        houseDTO.setHouseDetail(houseDetailDTO);

        List<HousePictureDTO> pictureDTOS = new ArrayList<>();
        housePictures.forEach(picture ->{pictureDTOS.add(modelMapper.map(picture,HousePictureDTO.class));});
        houseDTO.setPictures(pictureDTOS);
        houseDTO.setCover(this.cdnPrefix + houseDTO.getCover());

        // 添加房源标签
        List<String> tags = houseForm.getTags();
        if(tags != null && CollectionUtils.isEmpty(tags)){
            List<HouseTag> houseTags = new ArrayList<>();
            for (String tag : tags) {
                HouseTag houseTag = new HouseTag();
                houseTag.setHouseId(house.getId());
                houseTag.setName(tag);
                houseTags.add(houseTag);
            }
            houseTagRepository.save(houseTags);
            houseDTO.setTags(tags);
        }
        return new ServiceResult<HouseDTO>(true,"保存房源成功！",houseDTO);
    }

    /**
     * 查询完整房源信息
     * @param id
     * @return
     */
    @Override
    public ServiceResult<HouseDTO> findCompleteOne(Long id) {
        House house = houseRepository.findOne(id);
        if(house == null){
            return ServiceResult.notFound();
        }
        HouseDetail houseDetail = houseDetailRepository.findByHouseId(id);

        List<HousePicture> pictures = housePictureRepository.findAllByHouseId(id);

        // 数据整合
        HouseDTO result = modelMapper.map(house,HouseDTO.class);

        result.setHouseDetail(modelMapper.map(houseDetail,HouseDetailDTO.class));
        // 添加房源图片信息
        List<HousePictureDTO> pictureDTOS = new ArrayList<>();
        pictures.forEach(picture ->{pictureDTOS.add(modelMapper.map(picture,HousePictureDTO.class));});
        result.setPictures(pictureDTOS);

        List<HouseTag> tags = houseTagRepository.findAllByHouseId(id);
        List<String> tagList = new ArrayList<>();
        for (HouseTag tag : tags) {
            tagList.add(tag.getName());
        }
        result.setTags(tagList);

        if (LoginUserUtil.getLoginUserId() > 0) { // 已登录用户
            HouseSubscribe subscribe = subscribeRepository.findByHouseIdAndUserId(house.getId(), LoginUserUtil.getLoginUserId());
            if (subscribe != null) {
                result.setSubscribeStatus(subscribe.getStatus());
            }
        }
        return ServiceResult.of(result);
    }

    /**
     * 更新房源数据
     * @param houseForm
     * @return
     */
    @Override
    @Transactional
    public ServiceResult update(HouseForm houseForm) {
        House house = this.houseRepository.findOne(houseForm.getId());
        if (house == null) {
            return ServiceResult.notFound();
        }

        HouseDetail detail = this.houseDetailRepository.findByHouseId(house.getId());
        if (detail == null) {
            return ServiceResult.notFound();
        }

        ServiceResult wrapperResult = wrapperDetailInfo(detail, houseForm);
        if (wrapperResult != null) {
            return wrapperResult;
        }

        houseDetailRepository.save(detail);

        List<HousePicture> pictures = generatePictures(houseForm, houseForm.getId());
        housePictureRepository.save(pictures);

        if (houseForm.getCover() == null) {
            houseForm.setCover(house.getCover());
        }

        modelMapper.map(houseForm, house);
        house.setLastUpdateTime(new Date());
        houseRepository.save(house);

        // 状态为 上架 更新索引
        if(house.getStatus() == HouseStatus.PASSES.getValue()){
            searchService.index(house.getId());
        }

        return ServiceResult.success();
    }

    /**
     * 移除图片接口
     * @param id
     * @return
     */
    @Override
    @Transactional
    public ServiceResult removePhoto(Long id) {
        HousePicture housePicture = housePictureRepository.findOne(id);
        if(housePicture == null){
            return ServiceResult.notFound();
        }
        try {
            // 删除 七牛云图片
            Response response = this.qiNiuService.delete(housePicture.getPath());
            if(response.isOK()){
                // 删除本地图片
                housePictureRepository.delete(id);
                return ServiceResult.success();
            }else {
                return new ServiceResult(false, response.error);
            }
        } catch (QiniuException e) {
            e.printStackTrace();
            return new ServiceResult(false, e.getMessage());
        }
    }

    /**
     * 修改封面
     * @param coverId
     * @param targetId
     * @return
     */
    @Override
    @Transactional
    public ServiceResult updateCover(Long coverId, Long targetId) {
        HousePicture cover = housePictureRepository.findOne(coverId);
        if (cover == null) {
            return ServiceResult.notFound();
        }

        houseRepository.updateCover(targetId, cover.getPath());
        return ServiceResult.success();
    }

    /**
     * 根据查询 条件查出对于信息
     * @param searchBody
     * @return
     */
    @Override
    public ServiceMultiResult<HouseDTO> adminQuery(DatatableSearch searchBody) {
        List<HouseDTO> results = new ArrayList<>();
        Sort sort = new Sort(Sort.Direction.fromString(searchBody.getDirection()), searchBody.getOrderBy());
        // 添加分页参数
        int page = searchBody.getStart() / searchBody.getLength();
        Pageable pageable = new PageRequest(page,searchBody.getLength(),sort);

        Specification<House> specification = (root, query, cb) -> {
            Predicate predicate = cb.equal(root.get("adminId"), LoginUserUtil.getLoginUserId());
            predicate = cb.and(predicate, cb.notEqual(root.get("status"), HouseStatus.DELETED.getValue()));

            if(searchBody.getCity() != null){
                predicate = cb.and(predicate, cb.equal(root.get("cityEnName"), searchBody.getCity()));
            }
            if(searchBody.getStatus() != null){
                predicate = cb.and(predicate, cb.equal(root.get("status"), searchBody.getStatus()));
            }
            if(searchBody.getCreateTimeMin() != null){
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("createTime"), searchBody.getCreateTimeMin()));
            }
            if(searchBody.getCreateTimeMax() != null){
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("createTime"),searchBody.getCreateTimeMax()));
            }
            if(searchBody.getTitle() != null){
                predicate = cb.and(predicate, cb.like(root.get(""), "%" + searchBody.getTitle() + "%"));
            }

            return predicate;
        };

        Page<House> houses = houseRepository.findAll(specification, pageable);
        houses.forEach(house -> {
            HouseDTO houseDTO = modelMapper.map(house,HouseDTO.class);
            houseDTO.setCover(this.cdnPrefix + house.getCover());
            results.add(houseDTO);
        });

        return new ServiceMultiResult<>(houses.getTotalElements(),results);
    }

    /**
     * 添加房源标签
     * @param houseId
     * @param tag
     * @return
     */
    @Override
    @Transactional
    public ServiceResult addTag(Long houseId, String tag) {
        House house = houseRepository.findOne(houseId);
        if (house == null) {
            return ServiceResult.notFound();
        }

        HouseTag houseTag = houseTagRepository.findByNameAndHouseId(tag, houseId);
        if (houseTag != null) {
            return new ServiceResult(false, "标签已存在");
        }

        houseTagRepository.save(new HouseTag(houseId, tag));
        return ServiceResult.success();
    }

    /**
     * 移除房源标签
     * @param houseId
     * @param tag
     * @return
     */
    @Override
    @Transactional
    public ServiceResult removeTag(Long houseId, String tag) {
        House house = houseRepository.findOne(houseId);
        if (house == null) {
            return ServiceResult.notFound();
        }

        HouseTag houseTag = houseTagRepository.findByNameAndHouseId(tag, houseId);
        if (houseTag == null) {
            return new ServiceResult(false, "标签不存在");
        }

        houseTagRepository.delete(houseTag.getId());
        return ServiceResult.success();
    }

    /**
     * 更改房源 审核状态
     * @param id
     * @param status
     * @return
     */
    @Override
    @Transactional
    public ServiceResult updateStatus(Long id, int status) {
        House house = houseRepository.findOne(id);
        if(house == null){
            return ServiceResult.notFound();
        }
        if(house.getStatus() == status){
            return new ServiceResult(false, "房源状态没有发生变化");
        }
        if (house.getStatus() == HouseStatus.RENTED.getValue()) {
            return new ServiceResult(false,"房屋已出租，不可更改！");
        }
        if(house.getStatus() == HouseStatus.DELETED.getValue()){
            return new ServiceResult(false,"已删除的房屋，不可更改！");
        }
        houseRepository.updateStatus(id,status);

        // 状态为 上架 时生成索引，其余删除索引
        if(status == HouseStatus.PASSES.getValue()){
            searchService.index(id);
        } else {
            searchService.remove(id);
        }

        return ServiceResult.success();
    }
    /**
     * 管理员查询预约信息接口
     * @param start
     * @param size
     */
    @Override
    public ServiceMultiResult<Pair<HouseDTO, HouseSubscribeDTO>> findSubscribeList(int start, int size) {
        Long userId = LoginUserUtil.getLoginUserId();
        Pageable pageable = new PageRequest(start / size, size, new Sort(Sort.Direction.DESC, "orderTime"));

        Page<HouseSubscribe> page = subscribeRepository.findAllByAdminIdAndStatus(userId, HouseSubscribeStatus.IN_ORDER_TIME.getValue(), pageable);

        return wrapper(page);
    }
    /**
     * 完成预约
     */
    @Override
    @Transactional
    public ServiceResult finishSubscribe(Long houseId) {
        Long userId = LoginUserUtil.getLoginUserId();
        HouseSubscribe houseSubscribe = subscribeRepository.findByHouseIdAndAdminId(houseId, userId);
        if(houseSubscribe == null){
            return new ServiceResult(false, "无预约记录");
        }
        subscribeRepository.updateStatus(houseSubscribe.getId(), HouseSubscribeStatus.FINISH.getValue());
        houseRepository.updateWatchTimes(houseId);
        return ServiceResult.success();
    }


    /**
     * 查询房源信息
     * @param rentSearch
     * @return
     */
    @Override
    public ServiceMultiResult<HouseDTO> query(RentSearch rentSearch) {
        // 实现动态排序
        Sort sort = HouseSort.generateSort(rentSearch.getOrderBy(),rentSearch.getOrderDirection());
        int page = rentSearch.getStart() / rentSearch.getSize();

        Pageable pageable = new PageRequest(page,rentSearch.getSize(),sort);

        Specification<House> specification = (root,criteriaQuery,criteriaBuilder)->{

            Predicate predicate = criteriaBuilder.equal(root.get("status"),HouseStatus.PASSES.getValue());

            predicate = criteriaBuilder.and(predicate,criteriaBuilder.equal(root.get("cityEnName"),rentSearch.getCityEnName()));

            // 处理 距离地铁最近的排序
            if(HouseSort.DISTANCE_TO_SUBWAY_KEY.equals(rentSearch.getOrderBy())){
                predicate = criteriaBuilder.and(predicate,criteriaBuilder.gt(root.get(HouseSort.DISTANCE_TO_SUBWAY_KEY), -1));
            }
            return predicate;
        };

        Page<House> houses = houseRepository.findAll(specification, pageable);

        List<HouseDTO> houseDTOS = new ArrayList<>();
        List<Long> houseIds = new ArrayList<>();
        Map<Long, HouseDTO> idToHouseMap = new HashMap<>();

        houses.forEach(house -> {
            HouseDTO houseDTO = modelMapper.map(house,HouseDTO.class);
            houseDTO.setCover(cdnPrefix + house.getCover());
            houseDTOS.add(houseDTO);
            houseIds.add(house.getId());
            idToHouseMap.put(house.getId(),houseDTO);
        });

        // 渲染详细信息 及 标签
        wrapperHouseList(houseIds,idToHouseMap);

        return new ServiceMultiResult<>(houses.getTotalElements(),houseDTOS);
    }

    /**
     * 渲染详细信息 及 标签
     * @param houseIds
     * @param idToHouseMap
     */
    private void wrapperHouseList(List<Long> houseIds,Map<Long, HouseDTO> idToHouseMap){
        List<HouseDetail> houseDetails = houseDetailRepository.findAllByHouseIdIn(houseIds);
        houseDetails.forEach(houseDetail -> {
            HouseDTO houseDTO = idToHouseMap.get(houseDetail.getHouseId());
            HouseDetailDTO houseDetailDTO = modelMapper.map(houseDetail,HouseDetailDTO.class);
            houseDTO.setHouseDetail(houseDetailDTO);
        });

        List<HouseTag> houseTags = houseTagRepository.findAllByHouseIdIn(houseIds);
        houseTags.forEach(houseTag -> {
            HouseDTO houseDTO = idToHouseMap.get(houseTag.getHouseId());
            houseDTO.getTags().add(houseTag.getName());
        });
    }

    /**
     * 图片对象列表信息填充
     * @param form
     * @param houseId
     * @return
     */
    private List<HousePicture> generatePictures(HouseForm form, Long houseId) {
        List<HousePicture> pictures = new ArrayList<>();
        if (form.getPhotos() == null || form.getPhotos().isEmpty()) {
            return pictures;
        }

        for (PhotoForm photoForm : form.getPhotos()) {
            HousePicture picture = new HousePicture();
            picture.setHouseId(houseId);
            picture.setCdnPrefix(cdnPrefix);
            picture.setPath(photoForm.getPath());
            picture.setWidth(photoForm.getWidth());
            picture.setHeight(photoForm.getHeight());
            pictures.add(picture);
        }
        return pictures;
    }

    /**
     * 添加房源具体信息
     * @param detail
     * @param houseForm
     */
    private ServiceResult<HouseDTO> wrapperDetailInfo(HouseDetail detail, HouseForm houseForm) {
        Subway subway = subwayRepository.findOne(houseForm.getSubwayLineId());
        if(subway == null){
            return new ServiceResult<>(false,"Not Valid Subway Line!");
        }

        SubwayStation subwayStation = subwayStationRepository.findOne(houseForm.getSubwayStationId());
        if(subwayStation == null || subway.getId() != subwayStation.getSubwayId()){
            return new ServiceResult<>(false,"Not Valid Subway Station !");
        }

        detail.setSubwayLineId(subway.getId());
        detail.setSubwayLineName(subway.getName());

        detail.setSubwayStationId(subwayStation.getId());
        detail.setSubwayStationName(subwayStation.getName());

        //detail.setId(houseForm.getId());
        detail.setDescription(houseForm.getDescription());
        detail.setDetailAddress(houseForm.getDetailAddress());
        detail.setLayoutDesc(houseForm.getLayoutDesc());
        detail.setRentWay(houseForm.getRentWay());
        detail.setRoundService(houseForm.getRoundService());
        detail.setTraffic(houseForm.getTraffic());
        return null;
    }

    /**
     * 将预约看房信息合成
     * @param page
     * @return
     */
    private ServiceMultiResult<Pair<HouseDTO, HouseSubscribeDTO>> wrapper(Page<HouseSubscribe> page) {
        List<Pair<HouseDTO, HouseSubscribeDTO>> result = new ArrayList<>();

        if (page.getSize() < 1) {
            return new ServiceMultiResult<>(page.getTotalElements(), result);
        }

        List<HouseSubscribeDTO> subscribeDTOS = new ArrayList<>();
        List<Long> houseIds = new ArrayList<>();
        page.forEach(houseSubscribe -> {
            subscribeDTOS.add(modelMapper.map(houseSubscribe, HouseSubscribeDTO.class));
            houseIds.add(houseSubscribe.getHouseId());
        });

        Map<Long, HouseDTO> idToHouseMap = new HashMap<>();
        Iterable<House> houses = houseRepository.findAll(houseIds);
        houses.forEach(house -> {
            idToHouseMap.put(house.getId(), modelMapper.map(house, HouseDTO.class));
        });

        for (HouseSubscribeDTO subscribeDTO : subscribeDTOS) {
            Pair<HouseDTO, HouseSubscribeDTO> pair = Pair.of(idToHouseMap.get(subscribeDTO.getHouseId()), subscribeDTO);
            result.add(pair);
        }

        return new ServiceMultiResult<>(page.getTotalElements(), result);
    }
}
