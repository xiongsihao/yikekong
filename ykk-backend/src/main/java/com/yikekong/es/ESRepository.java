package com.yikekong.es;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yikekong.dto.DeviceDTO;
import com.yikekong.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
}
