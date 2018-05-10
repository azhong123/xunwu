package com.spring.service.house;

import com.spring.common.base.ServiceMultiResult;
import com.spring.common.base.ServiceResult;
import com.spring.web.dto.HouseDTO;
import com.spring.web.dto.HouseSubscribeDTO;
import com.spring.web.form.DatatableSearch;
import com.spring.web.form.HouseForm;
import com.spring.web.form.RentSearch;
import org.springframework.data.util.Pair;

/**
 * 房屋 service
 * Created by chenxizhong on 2018/5/6.
 */
public interface IHouseService {

    /**
     * 保存房源信息
     * @param houseForm
     * @return
     */
    ServiceResult<HouseDTO> save(HouseForm houseForm);


    /**
     * 查询完整房源信息
     * @param id
     * @return
     */
    ServiceResult<HouseDTO> findCompleteOne(Long id);

    /**
     * 更新房源数据
     * @param houseForm
     * @return
     */
    ServiceResult update(HouseForm houseForm);

    /**
     * 移除图片接口
     * @param id
     * @return
     */
    ServiceResult removePhoto(Long id);

    /**
     * 修改封面
     * @param coverId
     * @param targetId
     * @return
     */
    ServiceResult updateCover(Long coverId, Long targetId);

    /**
     * 根据查询 条件查出对于信息
     * @param searchBody
     * @return
     */
    ServiceMultiResult<HouseDTO> adminQuery(DatatableSearch searchBody);

    /**
     * 添加房源标签
     * @param houseId
     * @param tag
     * @return
     */
    ServiceResult addTag(Long houseId, String tag);

    /**
     * 移除房源标签
     * @param houseId
     * @param tag
     * @return
     */
    ServiceResult removeTag(Long houseId, String tag);

    /**
     * 更改房源 审核状态
     * @param id
     * @param status
     * @return
     */
    ServiceResult updateStatus(Long id, int status);

    /**
     * 管理员查询预约信息接口
     * @param start
     * @param size
     */
    ServiceMultiResult<Pair<HouseDTO,HouseSubscribeDTO>> findSubscribeList(int start, int size);

    /**
     * 完成预约
     */
    ServiceResult finishSubscribe(Long houseId);

    /**
     * 查询房源信息
     * @param rentSearch
     * @return
     */
    ServiceMultiResult<HouseDTO> query(RentSearch rentSearch);
}
