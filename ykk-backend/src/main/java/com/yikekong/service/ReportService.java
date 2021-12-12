package com.yikekong.service;

import com.yikekong.dto.HeapPoint;
import com.yikekong.dto.TrendPoint;
import com.yikekong.dto.TrendPoint2;
import com.yikekong.vo.BoardQuotaVO;
import com.yikekong.vo.Pager;
import com.yikekong.vo.PieVO;

import java.util.List;

/**
 * @author : xsh
 * @create : 2021-12-12 - 21:40
 * @describe: 报表服务
 */
public interface ReportService {


    /**
     * 设备状态分布
     * @return
     */
    List<PieVO> getStatusCollect();

    /**
     * 获取异常趋势指标
     * @param start 开始时间 yyyy-MM-dd HH:mm:ss
     * @param end 结束时间 yyyy-MM-dd HH:mm:ss
     * @param type 时间统计类型(1:60分钟之内,2:当天24小时,3:7天内)
     * @return
     */
    List<TrendPoint> getAlarmTrend(String start, String end, int type);

    /**
     * 获取一定时间范围之内的报警次数最多的设备指标
     * @return
     */
    List<HeapPoint> getTop10Alarm(String startTime, String endTime);

    /**
     * 通过指标获取关联设备
     * @param quotaId
     * @return
     */
    Pager<String> getDeviceByQuota(Long page, Long pageSize, String quotaId);

    /**
     * 获取指标趋势
     * @param start 开始时间 yyyy-MM-dd HH:mm:ss
     * @param end 结束时间 yyyy-MM-dd HH:mm:ss
     * @param quotaId 指标Id
     * @param type 时间统计类型(1:60分钟之内,2:当天24小时,3:7天内)
     * @param deviceId 设备编码
     * @return
     */
    List<TrendPoint2> getQuotaTrend(
            String start, String end, String quotaId,String deviceId,int type);

    /**
     * 指标趋势图
     * @param quotaId
     * @param deviceIds
     * @param startTime
     * @param endTime
     * @param type
     * @return
     */
    BoardQuotaVO getBoardData(String quotaId, List<String> deviceIds, String startTime, String endTime, Integer type  );
}
