package com.yikekong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.yikekong.dto.DeviceDTO;
import com.yikekong.dto.DeviceInfoDTO;
import com.yikekong.dto.QuotaDTO;
import com.yikekong.dto.QuotaInfo;
import com.yikekong.entity.QuotaEntity;
import com.yikekong.influx.InfluxRepository;
import com.yikekong.mapper.QuotaMapper;
import com.yikekong.service.QuotaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuotaServiceImpl extends ServiceImpl<QuotaMapper, QuotaEntity> implements QuotaService {

    @Autowired
    private InfluxRepository influxRepository;

    @Override
    public IPage<QuotaEntity> queryPage(Long page, Long pageSize, String name) {
        Page<QuotaEntity> pageResult = new Page<>(page, pageSize);
        LambdaQueryWrapper<QuotaEntity> wrapper = new LambdaQueryWrapper<>();
        if (!Strings.isNullOrEmpty(name)) {
            wrapper.like(QuotaEntity::getName, name);
        }

        return this.page(pageResult, wrapper);
    }


    @Override
    public List<String> getAllSubject() {
        QueryWrapper<QuotaEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().select(QuotaEntity::getSubject);

        return this.list(wrapper).stream().map(q -> q.getSubject()).collect(Collectors.toList());
    }


    @Override
    public DeviceInfoDTO analysis(String topic, Map<String, Object> payloadMap) {

        //1.获取指标配置
        List<QuotaEntity> quotaList = baseMapper.selectBySubject(topic);//根据主题查询指标列表
        if (quotaList.size() == 0) return null;

        //2.封装设备信息
        String snKey = quotaList.get(0).getSnKey();
        if (Strings.isNullOrEmpty(snKey)) return null;

        String deviceId = (String) payloadMap.get(snKey);//设备编号
        if (Strings.isNullOrEmpty(deviceId)) return null;

        DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setDeviceId(deviceId);

        //3.封装指标列表  :  循环我们根据主题名称查询得指标列表，到报文中提取，如果能够提到，进行封装
        List<QuotaDTO> quotaDTOList = Lists.newArrayList();
        for (QuotaEntity quota : quotaList) {
            String quotaKey = quota.getValueKey();//指标key
            if (payloadMap.containsKey(quotaKey)) {
                QuotaDTO quotaDTO = new QuotaDTO();
                //复制指标配置信息
                BeanUtils.copyProperties(quota, quotaDTO);
                quotaDTO.setQuotaName(quota.getName());
                //指标值封装
                //指标分为两种  1.数值  2.非数值（string boolean）
                //1.数值   value 存储数值  stringValue :存储数值字符串
                //2.非数值  value 0   stringValue:内容
                //如果是非数值
                if ("String".equals(quotaDTO.getValueType()) || "Boolean".equals(quotaDTO.getValueType())) {
                    quotaDTO.setStringValue((String) payloadMap.get(quotaKey));
                    quotaDTO.setValue(0d);
                } else {//如果是数值
                    if (payloadMap.get(quotaKey) instanceof String) {
                        quotaDTO.setValue(Double.valueOf((String) payloadMap.get(quotaKey)));
                        quotaDTO.setStringValue((String) payloadMap.get(quotaKey));
                    } else {
                        quotaDTO.setValue(Double.valueOf(payloadMap.get(quotaKey) + ""));
                        quotaDTO.setStringValue(quotaDTO.getValue() + "");
                    }
                    quotaDTO.setDeviceId(deviceId);
                }
                quotaDTOList.add(quotaDTO);
            }
        }

        //4.封装设备+指标列表返回
        DeviceInfoDTO deviceInfoDTO = new DeviceInfoDTO();
        deviceInfoDTO.setDevice(deviceDTO);
        deviceInfoDTO.setQuotaList(quotaDTOList);

        return deviceInfoDTO;
    }

    /***
     * 保存指标数据到influxDb
     * @param quotaDTOList
     */
    @Override
    public void saveQuotaToInflux(List<QuotaDTO> quotaDTOList) {
        for(QuotaDTO quotaDTO:quotaDTOList){
            QuotaInfo quotaInfo=new QuotaInfo();
            BeanUtils.copyProperties(quotaDTO,quotaInfo);//拷贝属性
            quotaInfo.setQuotaId(quotaDTO.getId()+"");//指标id
            influxRepository.add(quotaInfo);
        }
    }

    /**
     * 根据设备id查询最新指标
     *
     * @param deviceId
     * @return
     */
    @Override
    public List<QuotaInfo> getLastQuotaList(String deviceId) {
        String ql="select last(value),* from quota where deviceId='"+ deviceId+"' group by quotaId";
        return influxRepository.query(ql,QuotaInfo.class);
    }

    @Override
    public IPage<QuotaEntity> queryNumberQuota(Long page, Long pageSize) {
        Page<QuotaEntity> pageResult = new Page<>(page,pageSize);
        LambdaQueryWrapper<QuotaEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(QuotaEntity::getValueType,"Long")
                .or()
                .eq(QuotaEntity::getValueType,"Integer")
                .or()
                .eq(QuotaEntity::getValueType,"Double");
        return this.page(pageResult,wrapper);
    }
}
