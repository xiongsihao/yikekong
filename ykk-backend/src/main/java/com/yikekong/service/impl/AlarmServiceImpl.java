package com.yikekong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.yikekong.dto.DeviceDTO;
import com.yikekong.dto.DeviceInfoDTO;
import com.yikekong.dto.QuotaDTO;
import com.yikekong.entity.AlarmEntity;
import com.yikekong.mapper.AlarmMapper;
import com.yikekong.service.AlarmService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmMapper, AlarmEntity> implements AlarmService {


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

        //1.根据指标id查询告警判断规则列表
        List<AlarmEntity> alarmEntityList = getByQuotaId(quotaDTO.getId());
        AlarmEntity alarm = null;
        for (AlarmEntity alarmEntity : alarmEntityList) {
            //判断：操作符和指标对比
            if ("String".equals(quotaDTO.getValueType()) || "Boolean".equals(quotaDTO.getValueType())) {
                if (alarmEntity.getOperator().equals("=") && quotaDTO.getStringValue().equals(alarmEntity.getThreshold())) {
                    alarm = alarmEntity;
                    break;
                }
            } else //数值
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

        // 封装指标的告警  封装设备的告警
        DeviceDTO deviceDTO = deviceInfoDTO.getDevice();

        deviceDTO.setLevel(0);//假设不告警
        deviceDTO.setAlarm(false);
        deviceDTO.setAlarmName("正常");
        deviceDTO.setStatus(true);
        deviceDTO.setOnline(true);

        for (QuotaDTO quotaDTO : deviceInfoDTO.getQuotaList()) {

            AlarmEntity alarmEntity = verifyQuota(quotaDTO);//根据指标得到告警信息
            if (alarmEntity != null) {  //如果指标存在告警

                quotaDTO.setAlarm("1");
                quotaDTO.setAlarmName(alarmEntity.getName());//告警名称
                quotaDTO.setLevel(alarmEntity.getLevel() + "");//告警级别
                quotaDTO.setAlarmWebHook(alarmEntity.getWebHook());//告警web钩子
                quotaDTO.setCycle(alarmEntity.getCycle());//沉默周期
                //存储设备告警信息
                if (alarmEntity.getLevel().intValue() > deviceDTO.getLevel().intValue()) {
                    deviceDTO.setLevel(alarmEntity.getLevel());
                    deviceDTO.setAlarm(true);
                    deviceDTO.setAlarmName(alarmEntity.getName());
                }

            } else {//如果指标不存储在告警
                quotaDTO.setAlarm("0");
                quotaDTO.setAlarmName("正常");
                quotaDTO.setLevel("0");
                quotaDTO.setAlarmWebHook("");
                quotaDTO.setCycle(0);
            }
        }
        return deviceInfoDTO;
    }
}
