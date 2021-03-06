package com.yikekong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.yikekong.dto.*;
import com.yikekong.entity.AlarmEntity;
import com.yikekong.influx.InfluxRepository;
import com.yikekong.mapper.AlarmMapper;
import com.yikekong.service.AlarmService;
import com.yikekong.vo.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, AlarmEntity> implements AlarmService {


    @Autowired
    private InfluxRepository influxRepository;

    @Override
    public IPage<AlarmEntity> queryPage(Long page, Long pageSize, String alarmName, Integer quotaId) {
        LambdaQueryWrapper<AlarmEntity> wrapper = new LambdaQueryWrapper<>();
        if (!Strings.isNullOrEmpty(alarmName)) {
            wrapper.like(AlarmEntity::getName, alarmName);
        }
        if (quotaId != null) {
            wrapper.eq(AlarmEntity::getQuotaId, quotaId);
        }
        wrapper.orderByDesc(AlarmEntity::getId);

        //wrapper.orderByDesc(AlarmEntity::getCreateTime);

        Page<AlarmEntity> pageResult = new Page<>(page, pageSize);

        return this.page(pageResult, wrapper);
    }

    @Override
    public List<AlarmEntity> getByQuotaId(Integer quotaId) {
        QueryWrapper<AlarmEntity> wrapper = new QueryWrapper<>();
        wrapper
                .lambda()
                .eq(AlarmEntity::getQuotaId, quotaId)
                .orderByDesc(AlarmEntity::getLevel);

        return this.list(wrapper);
    }

    @Override
    public AlarmEntity verifyQuota(QuotaDTO quotaDTO) {

        //1.????????????id??????????????????????????????
        List<AlarmEntity> alarmEntityList = getByQuotaId(quotaDTO.getId());
        AlarmEntity alarm = null;
        for (AlarmEntity alarmEntity : alarmEntityList) {
            //?????????????????????????????????
            if ("String".equals(quotaDTO.getValueType()) || "Boolean".equals(quotaDTO.getValueType())) {
                if (alarmEntity.getOperator().equals("=") && quotaDTO.getStringValue().equals(alarmEntity.getThreshold())) {
                    alarm = alarmEntity;
                    break;
                }
            } else //??????
            {
                if (alarmEntity.getOperator().equals(">") && quotaDTO.getValue() > alarmEntity.getThreshold()) {
                    alarm = alarmEntity;
                    break;
                }
                if (alarmEntity.getOperator().equals("<") && quotaDTO.getValue() < alarmEntity.getThreshold()) {
                    alarm = alarmEntity;
                    break;
                }
                if (alarmEntity.getOperator().equals("=") && quotaDTO.getValue().equals(alarmEntity.getThreshold())) {
                    alarm = alarmEntity;
                    break;
                }
            }
        }
        return alarm;
    }

    @Override
    public DeviceInfoDTO verifyDeviceInfo(DeviceInfoDTO deviceInfoDTO) {

        // ?????????????????????  ?????????????????????
        DeviceDTO deviceDTO = deviceInfoDTO.getDevice();

        deviceDTO.setLevel(0);//???????????????
        deviceDTO.setAlarm(false);
        deviceDTO.setAlarmName("??????");
        deviceDTO.setStatus(true);
        deviceDTO.setOnline(true);

        for (QuotaDTO quotaDTO : deviceInfoDTO.getQuotaList()) {

            AlarmEntity alarmEntity = verifyQuota(quotaDTO);//??????????????????????????????
            if (alarmEntity != null) {  //????????????????????????

                quotaDTO.setAlarm("1");
                quotaDTO.setAlarmName(alarmEntity.getName());//????????????
                quotaDTO.setLevel(alarmEntity.getLevel() + "");//????????????
                quotaDTO.setAlarmWebHook(alarmEntity.getWebHook());//??????web??????
                quotaDTO.setCycle(alarmEntity.getCycle());//????????????
                //????????????????????????
                if (alarmEntity.getLevel().intValue() > deviceDTO.getLevel().intValue()) {
                    deviceDTO.setLevel(alarmEntity.getLevel());
                    deviceDTO.setAlarm(true);
                    deviceDTO.setAlarmName(alarmEntity.getName());
                }

            } else {//??????????????????????????????
                quotaDTO.setAlarm("0");
                quotaDTO.setAlarmName("??????");
                quotaDTO.setLevel("0");
                quotaDTO.setAlarmWebHook("");
                quotaDTO.setCycle(0);
            }
        }
        return deviceInfoDTO;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @Override
    public Pager<QuotaAllInfo> queryAlarmLog(Long page, Long pageSize, String start,
                                             String end, String alarmName, String deviceId) {
        //1.where??????????????????????????????
        StringBuilder whereSql = new StringBuilder("where alarm='1' ");
        if (!Strings.isNullOrEmpty(start)) {
            whereSql.append("and time>='" + start + "' ");
        }
        if (!Strings.isNullOrEmpty(end)) {
            whereSql.append("and time<='" + end + "' ");
        }
        if (!Strings.isNullOrEmpty(alarmName)) {
            whereSql.append("and alarmName=~/" + alarmName + "/ ");
        }
        if (!Strings.isNullOrEmpty(deviceId)) {
            whereSql.append("and deviceId=~/^" + deviceId + "/ ");
        }

        //2.??????????????????
        StringBuilder listQl = new StringBuilder("select * from quota  ");
        listQl.append(whereSql.toString());
        listQl.append("order by desc limit " + pageSize + " offset " + (page - 1) * pageSize);

        //3.?????????????????????
        StringBuilder countQl = new StringBuilder("select count(value) from quota ");
        countQl.append(whereSql.toString());

        //4.????????????????????????
        List<QuotaAllInfo> quotaList = influxRepository.query(listQl.toString(), QuotaAllInfo.class);

        //5.??????????????????
        List<QuotaCount> quotaCount = influxRepository.query(countQl.toString(), QuotaCount.class);

        //6.??????????????????
        if (CollectionUtils.isEmpty(quotaCount)) {
            Pager<QuotaAllInfo> pager = new Pager<>(0l, 0L);
            pager.setPage(0);
            pager.setItems(Lists.newArrayList());
            return pager;
        }
        // ????????????????????????
        for(QuotaAllInfo quotaAllInfo:quotaList){
            //2020-09-19T09:58:34.926Z   DateTimeFormatter.ISO_OFFSET_DATE_TIME
            //????????? 2020-09-19 09:58:34  ??????
            LocalDateTime dateTime = LocalDateTime.parse(quotaAllInfo.getTime(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            String time = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
            quotaAllInfo.setTime(time);
        }
        Long totalCount = quotaCount.get(0).getCount();//?????????
        Pager<QuotaAllInfo> pager = new Pager<>(totalCount, pageSize);
        pager.setPage(page);
        pager.setItems(quotaList);
        return pager;
    }
}
