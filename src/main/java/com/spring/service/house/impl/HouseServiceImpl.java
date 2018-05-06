package com.spring.service.house.impl;

import com.spring.common.base.ServiceResult;
import com.spring.service.house.IHouseService;
import com.spring.web.dto.HouseDTO;
import com.spring.web.form.HouseForm;
import org.springframework.stereotype.Service;

/**
 *
 * Created by chenxizhong on 2018/5/6.
 */
@Service
public class HouseServiceImpl implements IHouseService {


    @Override
    public ServiceResult<HouseDTO> save(HouseForm houseForm) {
        return null;
    }
}
