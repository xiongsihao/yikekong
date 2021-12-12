package com.yikekong.service.impl;

import com.google.common.collect.Lists;
import com.yikekong.dto.HeapPoint;
import com.yikekong.dto.TrendPoint;
import com.yikekong.es.ESRepository;
import com.yikekong.influx.InfluxRepository;
import com.yikekong.service.ReportService;
import com.yikekong.vo.PieVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : xsh
 * @create : 2021-12-12 - 21:41
 * @describe:
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ESRepository esRepository;

    @Override
    public List<PieVO> getStatusCollect() {

        Long allDeviceCount = esRepository.getAllDeviceCount();//全部设备数量
        Long offlineCount = esRepository.getOfflineCount();//离线设备数量
        Long alarmCount = esRepository.getAlarmCount();//报警设备数量

        PieVO devicePie=new PieVO();
        devicePie.setName("正常运转");
        devicePie.setValue(allDeviceCount-offlineCount-alarmCount);

        PieVO offlinePie=new PieVO();
        offlinePie.setName("离线");
        offlinePie.setValue(offlineCount);

        PieVO alarmPie =new PieVO();
        alarmPie.setName("报警");
        alarmPie.setValue(alarmCount);

        List<PieVO> pieVOList= Lists.newArrayList();
        pieVOList.add(devicePie);
        pieVOList.add(offlinePie);
        pieVOList.add(alarmPie);

        return pieVOList;
    }

    @Autowired
    private InfluxRepository influxRepository;

    /**
     * 要执行的sql里的group by分组里使用了InfluxDB的time时间函数
     * 1m:按每分钟进行分组汇总   1h:按每小时进行分组汇总  1d:按每天进行分组汇总
     * @param start 开始时间 yyyy-MM-dd HH:mm:ss
     * @param end 结束时间 yyyy-MM-dd HH:mm:ss
     * @param type 时间统计类型(1:60分钟之内,2:当天24小时,3:7天内)
     * @return
     */
    @Override
    public List<TrendPoint> getAlarmTrend(String start, String end, int type) {

        StringBuilder ql=new StringBuilder("select count(value) as pointValue from quota where alarm='1' ");
        ql.append("and time>='"+ start +"' and time<='"+ end+"' ");
        if(type==1){
            ql.append("group by time(1m)");
        }
        if(type==2){
            ql.append("group by time(1h)");
        }
        if(type==3){
            ql.append("group by time(1d)");
        }
        List<TrendPoint> trendPointList = influxRepository.query(ql.toString(), TrendPoint.class);

        return trendPointList;
    }

    @Override
    public List<HeapPoint> getTop10Alarm(String startTime, String endTime) {
        StringBuilder sbSql =
                new StringBuilder("select top(heapValue,deviceId,quotaId,quotaName,10) as heapValue " +
                        " from(select count(value) as heapValue from quota where alarm='1' ");
        sbSql.append("and time>='");
        sbSql.append(startTime);
        sbSql.append("' and time<='");
        sbSql.append(endTime);
        sbSql.append("' group by deviceId,quotaId) order by desc");

        return influxRepository.query(sbSql.toString(),HeapPoint.class);
    }
}
