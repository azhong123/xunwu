package com.spring.service.search.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.entity.House;
import com.spring.entity.HouseDetail;
import com.spring.entity.HouseTag;
import com.spring.repository.HouseDetailRepository;
import com.spring.repository.HouseRepository;
import com.spring.repository.HouseTagRepository;
import com.spring.service.search.HouseIndexKey;
import com.spring.service.search.HouseIndexMessage;
import com.spring.service.search.HouseIndexTemplate;
import com.spring.service.search.ISearchService;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 检索接口
 * Created by chenxizhong on 2018/5/28.
 */
@Service
public class SearchServiceImpl implements ISearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private static final String INDEX_NAME = "xunwu";

    private static final String INDEX_TYPE = "house";

    private static final String INDEX_TOPIC = "house_build";

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private HouseDetailRepository houseDetailRepository;

    @Autowired
    private HouseTagRepository houseTagRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TransportClient esClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @KafkaListener(topics = INDEX_TOPIC)
    private void handleMessage(String content){
        try {
            HouseIndexMessage message = objectMapper.readValue(content, HouseIndexMessage.class);
            switch (message.getOperation()){
                case HouseIndexMessage.INDEX:
                    this.createOrUpdateIndex(message);
                    break;
                case HouseIndexMessage.REMOVE:
                    this.removeIndex(message);
                    break;
                default:
                    logger.info("【Kafka Get Message】Not support message content =>",content);
                    break;
            }
        } catch (IOException e) {
            logger.error("【Kafka Get Message】 Cannot parse json for content =>{} " , content , e);
        }
    }

    /**
     * 索引目标房源
     * @param houseId
     */
    @Override
    public void index(Long houseId) {
        // 正常用户创建 es 索引 retry  = 0 并给 kafka 发送消息
        this.index(houseId,0);
    }

    /**
     * 移除房源索引
     * @param houseId
     */
    @Override
    public void remove(Long houseId) {
        // 正常用户移除 es 索引 retry  = 0 并给 kafka 发送消息
        this.remove(houseId,0);
    }

    /**
     * 创建 ElasticSearch 索引 发送 kafka 消息
     * @param houseId
     * @param retry
     */
    private void index (Long houseId,int retry){
        if(retry > HouseIndexMessage.MAX_RETRY){
            logger.error("【Index ElasticSearch】Retry index times over 3 for houseId =>{},please check it !" ,houseId);
            return;
        }
        HouseIndexMessage message = new HouseIndexMessage(houseId,HouseIndexMessage.INDEX,retry);
        try {
            kafkaTemplate.send(INDEX_TOPIC,objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            logger.error("【Index ElasticSearch】json encode error for message =>{}",message,e);
        }
    }

    /**
     * 移除 ElasticSearch 索引 ，发送 kafka 消息
     * @param houseId
     * @param retry
     */
    private void remove(Long houseId,int retry){
        if(retry > HouseIndexMessage.MAX_RETRY){
            logger.error("【Remove ElasticSearch Index】 Retry index times over 3 for houseId =>{},please check it !" ,houseId);
            return;
        }
        HouseIndexMessage message = new HouseIndexMessage(houseId,HouseIndexMessage.REMOVE,retry);
        try {
            kafkaTemplate.send(INDEX_TOPIC,objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            logger.error("【Remove ElasticSearch Index】json encode error for message =>{}",message,e);
        }
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
            logger.info("Update Index With HouseId =>{}",indexTemplate.getHouseId());
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
    private boolean deleteAndCreate(long totalHid,HouseIndexTemplate indexTemplate){
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

    /**
     * 通过消费 kafka 消息，创建索引
     * @param message
     */
    private void createOrUpdateIndex(HouseIndexMessage message){
        Long houseId = message.getHouseId();
        House house = houseRepository.findOne(houseId);
        if(house == null){
            logger.error("【Create ElasticSearch Index】Index houseId => {} dose not exist!" ,houseId);
            this.index(houseId ,message.getRetry() + 1);
            return;
        }
        HouseIndexTemplate indexTemplate = new HouseIndexTemplate();
        modelMapper.map(house,indexTemplate);

        // 房屋详情信息
        HouseDetail houseDetail = houseDetailRepository.findByHouseId(houseId);
        if(houseDetail == null){
            //todo 异常情况
        }
        modelMapper.map(houseDetail,indexTemplate);

        // 房屋标签
        List<HouseTag> houseTags = houseTagRepository.findAllByHouseId(houseId);
        if(!CollectionUtils.isEmpty(houseTags)){
            List<String> tagStrings = new ArrayList<>();
            houseTags.stream().forEach(tag ->tagStrings.add(tag.getName()));
            indexTemplate.setTags(tagStrings);
        }

        // 查询 es 是否有数据
        SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(INDEX_NAME).setTypes(INDEX_TYPE)
                .setQuery(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseId));
        logger.info("【Create ElasticSearch index 】 requestBuilder =>{}",requestBuilder.toString());

        SearchResponse searchResponse = requestBuilder.get();
        // 查询es 数据数量
        long totalHits = searchResponse.getHits().getTotalHits();
        boolean success;
        if(totalHits == 0){
            success = create(indexTemplate);
        }else if(totalHits == 1){
            String esId = searchResponse.getHits().getAt(0).getId();
            success = update(esId,indexTemplate);
        } else {
            success = deleteAndCreate(totalHits,indexTemplate);
        }

        if(success){
            logger.info("【Create ElasticSearch index】 success with house" + houseId);
        }
    }

    /**
     * 消费 kafka 消息，移除索引
     * @param message
     */
    private void removeIndex(HouseIndexMessage message){
        Long houseId = message.getHouseId();
        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE.newRequestBuilder(esClient)
                .filter(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID,houseId))
                .source(INDEX_NAME);
        logger.info("【Remove ElasticSearch index】Delete by Query for house =>",builder);

        BulkByScrollResponse response = builder.get();
        long deleted = response.getDeleted();
        logger.info("【Remove ElasticSearch index】Delete total" + deleted);
        if(deleted <= 0){
            this.remove(houseId,message.getRetry() + 1);
        }
    }

}
