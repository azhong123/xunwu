package com.spring.service.search.impl;

import com.spring.ApplicationTests;
import com.spring.service.search.ISearchService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by chenxizhong on 2018/5/28.
 */
public class SearchServiceImplTest extends ApplicationTests {

    @Autowired
    private ISearchService iSearchService;

    @Test
    public void index() throws Exception {
        Long houseId = 15L;
        iSearchService.index(houseId);

    }

    @Test
    public void remove() throws Exception {
        Long houseId = 15L;
        iSearchService.remove(houseId);

    }

}