package com.yikekong.controller;

import com.google.common.collect.Lists;
import com.yikekong.dto.TrendPoint;
import com.yikekong.es.ESRepository;
import com.yikekong.service.ReportService;
import com.yikekong.vo.LineVO;
import com.yikekong.vo.MonitorVO;
import com.yikekong.vo.PieVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : xsh
 * @create : 2021-12-12 - 21:44
 * @describe:
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private ESRepository esRepository;

    /**
     * 设备状态分布
     * @return
     */
    @GetMapping("/statusCollect")
    public List<PieVO> getStatusCollect(){
        return  reportService.getStatusCollect();
    }


    /**
     * 获取实时监控数据
     * @return
     */
    @GetMapping("/monitor")
    public MonitorVO getMonitorData(){
        MonitorVO monitor = new MonitorVO();
        monitor.setDeviceCount(esRepository.getAllDeviceCount());
        monitor.setAlarmCount(esRepository.getAlarmCount());
        return monitor;
    }

    /**
     * 获取告警趋势
     * @return
     */
    @GetMapping("/trend/{startTime}/{endTime}/{type}")
    public LineVO getQuotaTrendCollect(@PathVariable String startTime, @PathVariable String endTime, @PathVariable Integer type){
        List<TrendPoint> trendPointList = reportService.getAlarmTrend(startTime, endTime, type);
        LineVO lineVO=new LineVO();
        lineVO.setXdata(Lists.newArrayList());
        lineVO.setSeries(Lists.newArrayList());
        trendPointList.forEach( t->{
            lineVO.getXdata().add( t.getTime() );
            lineVO.getSeries().add( t.getPointValue().longValue());
        });
        return lineVO;
    }
}
