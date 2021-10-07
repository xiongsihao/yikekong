package com.yikekong.controller;


import com.yikekong.exception.BussinessException;
import com.yikekong.vo.AlarmVO;
import com.yikekong.vo.Pager;

import com.yikekong.entity.AlarmEntity;
import com.yikekong.service.AlarmService;
import com.yikekong.service.QuotaService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/alarm")
public class AlarmController{
    @Autowired
    private AlarmService alarmService;
    @Autowired
    private QuotaService quotaService;

    @PostMapping
    public boolean create(@RequestBody AlarmVO vo){
        try {
            AlarmEntity entity = new AlarmEntity();
            BeanUtils.copyProperties(vo,entity);

            return alarmService.save(entity);
        }catch (DuplicateKeyException e){
            throw new BussinessException("已存在该名称");
        }

    }

    @GetMapping
    public Pager<AlarmEntity> queryPage(@RequestParam(value = "page",required = false,defaultValue = "1") Long page,
                                        @RequestParam(value = "pageSize",required = false,defaultValue = "10") Long pageSize,
                                        @RequestParam(value = "name",required = false) String name,
                                        @RequestParam(value = "quotaId",required = false) Integer quotaId){
        return new Pager<>(alarmService.queryPage(page,pageSize,name,quotaId));
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id){
        return alarmService.removeById(id);
    }

    @PutMapping
    public Boolean update(@RequestBody AlarmVO vo){
        try {
            if(vo.getId() == null) return false;
            AlarmEntity entity = new AlarmEntity();
            BeanUtils.copyProperties(vo,entity);

            return alarmService.updateById(entity);
        }catch (DuplicateKeyException e){
            throw new BussinessException("已存在该名称");
        }
    }









}
