package com.yikekong.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yikekong.entity.GPSEntity;
import com.yikekong.mapper.GpsMapper;
import com.yikekong.service.GpsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GpsServiceImpl extends ServiceImpl<GpsMapper, GPSEntity> implements GpsService{

    @Override
    public boolean update(GPSEntity gpsEntity) {
        gpsEntity.setId(1);
        return this.updateById(gpsEntity);
    }


    @Override
    public GPSEntity getGps() {
        return this.getById(1);
    }


}
