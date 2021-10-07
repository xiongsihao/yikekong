package com.yikekong.controller;

import com.yikekong.entity.GPSEntity;
import com.yikekong.service.GpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/gps")
public class GpsController{

    @Autowired
    private GpsService gpsService;

    /**
     * 修改gps指标
     * @param gpsEntity
     * @return
     */
    @PutMapping
    public boolean update(@RequestBody GPSEntity gpsEntity){
        return gpsService.update(gpsEntity);
    }

    /**
     * 获取gps指标
     * @return
     */
    @GetMapping
    public GPSEntity get(){
        return gpsService.getGps();
    }




}
