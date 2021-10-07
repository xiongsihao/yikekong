package com.yikekong.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.yikekong.dto.DeviceDTO;
import com.yikekong.util.JsonUtil;
import com.yikekong.vo.Pager;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author : xsh
 * @create : 2021-10-07 - 22:36
 * @describe:
 */
@Component
@Slf4j
public class ESRepository {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 添加设备
     * @param deviceDTO
     */
    public  void addDevices(DeviceDTO deviceDTO){
        if(deviceDTO==null ) return;
        if(deviceDTO.getDeviceId()==null) return;
        IndexRequest request=new IndexRequest("devices");
        try {
            String json = JsonUtil.serialize(deviceDTO);
            Map map = JsonUtil.getByJson(json, Map.class);
            request.source(map);
            request.id(deviceDTO.getDeviceId());
            restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("设备添加发生异常");
        }
    }

    /**
     * 根据设备id 查询设备
     * @param deviceId  设备id
     * @return
     */
    public DeviceDTO searchDeviceById(String deviceId){
        SearchRequest searchRequest=new SearchRequest("devices");
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("_id",deviceId));
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();
            long hitsCount = hits.getTotalHits().value;
            if(hitsCount<=0) return null;
            DeviceDTO deviceDTO=null;
            for(SearchHit hit:hits){
                String hitResult = hit.getSourceAsString();
                deviceDTO=JsonUtil.getByJson(hitResult,DeviceDTO.class  );
                deviceDTO.setDeviceId(deviceId);
                break;
            }
            return deviceDTO;

        } catch (IOException e) {
            e.printStackTrace();
            log.error("查询设备异常");
            return null;
        }
    }

    /**
     * 更新设备状态
     * @param deviceId
     * @param status
     * @return
     */
    public boolean updateStatus(String deviceId,Boolean status){
        UpdateRequest updateRequest=new UpdateRequest("devices",deviceId)
                .doc( "status",status );
        try {
            restHighLevelClient.update( updateRequest,RequestOptions.DEFAULT );
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("更新设备状态出错");
            return false;
        }
    }

    /**
     * 更新设备标签
     * @param deviceId
     * @param tags
     * @return
     */
    public boolean updateDeviceTag(String deviceId,String tags){
        UpdateRequest updateRequest=new UpdateRequest("devices",deviceId)
                .doc( "tag",tags );
        try {
            restHighLevelClient.update( updateRequest,RequestOptions.DEFAULT );
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("更新设备标签出错");
            return false;
        }
    }

    /**
     * 更新设备告警信息
     * @param deviceDTO
     * @return
     */
    public boolean updateDevicesAlarm(DeviceDTO deviceDTO){
        UpdateRequest updateRequest=new UpdateRequest("devices",deviceDTO.getDeviceId())
                .doc(   "alarm",deviceDTO.getAlarm(),//是否告警
                        "level",deviceDTO.getLevel(),//告警级别
                        "alarmName",deviceDTO.getAlarmName() );//告警名称
        try {
            restHighLevelClient.update( updateRequest,RequestOptions.DEFAULT );
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("更新设备告警信息出错");
            return false;
        }
    }

    /**
     * 更新在线状态
     * @param deviceId
     * @param online
     * @return
     */
    public boolean updateOnline(String deviceId,Boolean online){
        UpdateRequest updateRequest=new UpdateRequest("devices",deviceId)
                .doc( "online",online );
        try {
            restHighLevelClient.update( updateRequest,RequestOptions.DEFAULT );
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("更新在线状态出错");
            return false;
        }
    }

    /**
     * 分页查询设备
     * @param page 页码
     * @param pageSize 页大小
     * @param deviceId 设备编号
     * @param tags 标签
     * @param state 状态
     * @return
     */
    public Pager<DeviceDTO> searchDevice(Long page,Long pageSize,String deviceId,String tags,Integer state){

        SearchRequest searchRequest=new SearchRequest("devices");
        SearchSourceBuilder sourceBuilder=new SearchSourceBuilder();
        //条件查询
        BoolQueryBuilder boolQueryBuilder=QueryBuilders.boolQuery();
        //设备编号
        if(!Strings.isNullOrEmpty(deviceId)) {
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("deviceId", deviceId + "*"));
        }
        //标签
        if(!Strings.isNullOrEmpty(tags) ){
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("tag","*"+tags+"*"));
        }
        //状态（在线状态和告警状态）  0：在线  1：离线  2：一般告警  3：严重告警
        if(state!=null){
            if(state.intValue()==0){
                boolQueryBuilder.must( QueryBuilders.termQuery("online",true));
            }
            if(state.intValue()==1){
                boolQueryBuilder.must( QueryBuilders.termQuery("online",false));
            }
            if(state.intValue()==2){
                boolQueryBuilder.must( QueryBuilders.termQuery("level",1));
            }
            if(state.intValue()==3){
                boolQueryBuilder.must( QueryBuilders.termQuery("level",2));
            }
        }
        sourceBuilder.query(boolQueryBuilder);
        //分页
        sourceBuilder.from( (page.intValue()-1)*pageSize.intValue()  );
        sourceBuilder.size( pageSize.intValue() );
        sourceBuilder.trackTotalHits(true);

        //排序
        sourceBuilder.sort("level", SortOrder.DESC);//告警级别高的排前面
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            SearchHits hits = searchResponse.getHits();
            SearchHit[] searchHits = hits.getHits();
            List<DeviceDTO> devices= Lists.newArrayList();
            for(SearchHit hit: searchHits){
                String hitResult = hit.getSourceAsString();
                DeviceDTO deviceDTO = JsonUtil.getByJson(hitResult, DeviceDTO.class);
                devices.add(deviceDTO);
            }
            Pager<DeviceDTO> pager=new Pager<>(   searchResponse.getHits().getTotalHits().value,pageSize );
            pager.setItems(devices);
            return  pager;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("查询设备失败");
            return null;
        }
    }
}
