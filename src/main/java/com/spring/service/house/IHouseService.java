package com.spring.service.house;

import com.spring.common.base.ServiceResult;
import com.spring.web.dto.HouseDTO;
import com.spring.web.form.HouseForm;

/**
 * 房屋 service
 * Created by chenxizhong on 2018/5/6.
 */
public interface IHouseService {

    ServiceResult<HouseDTO> save(HouseForm houseForm);

}
