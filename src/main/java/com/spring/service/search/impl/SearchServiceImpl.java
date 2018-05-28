package com.spring.service.search.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.entity.House;
import com.spring.repository.HouseRepository;
import com.spring.service.search.HouseIndexKey;
import com.spring.service.search.HouseIndexTemplate;
import com.spring.service.search.ISearchService;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * 检索接口
 * Created by chenxizhong on 2018/5/28.
 */
@Service
public class SearchServiceImpl implements ISearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private static final String INDEX_NAME = "xunwu";

    private static final String INDEX_TYPE = "house";

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TransportClient esClient;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 索引目标房源
     * @param houseId
     */
    @Override
    public void index(Long houseId) {
        House house = houseRepository.findOne(houseId);
        if(house == null){
            logger.error("Index houseId => {} dose not exist!" ,houseId);
            return;
        }
        HouseIndexTemplate indexTemplate = new HouseIndexTemplate();
        modelMapper.map(house,indexTemplate);



    }

    /**
     * 移除房源索引
     * @param houseId
     */
    @Override
    public void remove(Long houseId) {

    }

    /**
     * 创建索引
     * @param indexTemplate
     * @return
     */
    private boolean create(HouseIndexTemplate indexTemplate){
        try {
            IndexResponse indexResponse = this.esClient.prepareIndex(INDEX_NAME, INDEX_TYPE)
                    .setSource(objectMapper.writeValueAsBytes(indexTemplate), XContentType.JSON).get();
            logger.info("Create Index With HouseId =>{}",indexTemplate.getHouseId());
            if(indexResponse.status() == RestStatus.CREATED){
                return true;
            }else {
                return false;
            }
        } catch (JsonProcessingException e) {
            logger.error("Error to index houseId =>",indexTemplate.getHouseId(),e);
            return false;
        }
    }

    /**
     * 更新索引
     * @param esId esId
     * @param indexTemplate
     * @return
     */
    private boolean update(String esId,HouseIndexTemplate indexTemplate){
        try {
            UpdateResponse updateResponse = this.esClient.prepareUpdate(INDEX_NAME, INDEX_TYPE, esId)
                    .setDoc(objectMapper.writeValueAsBytes(indexTemplate), XContentType.JSON).get();
            logger.info("Create Index With HouseId =>{}",indexTemplate.getHouseId());
            if(updateResponse.status() == RestStatus.OK){
                return true;
            }else {
                return false;
            }
        } catch (JsonProcessingException e) {
            logger.error("Error to index houseId =>",indexTemplate.getHouseId(),e);
            return false;
        }
    }

    /**
     * 删除索引并创建
     * @param totalHid 需要删除个数
     * @param indexTemplate
     * @return
     */
    private boolean deleteAndCreate(int totalHid,HouseIndexTemplate indexTemplate){
        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE.newRequestBuilder(esClient)
                .filter(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID,indexTemplate.getHouseId()))
                .source(INDEX_NAME);
        logger.info("Delete by Query for house:",builder);

        BulkByScrollResponse response = builder.get();
        long deleted = response.getDeleted();
        if(deleted != totalHid){
            logger.error("Need delete totalHid =>{},but deleted =>{} was deleted!",totalHid,deleted);
            return false;
        } else {
            return create(indexTemplate);
        }
    }

}
