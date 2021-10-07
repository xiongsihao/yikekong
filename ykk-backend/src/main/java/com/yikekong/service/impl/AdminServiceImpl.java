package com.yikekong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.yikekong.entity.AdminEntity;
import com.yikekong.mapper.AdminMapper;
import com.yikekong.service.AdminService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper,AdminEntity> implements AdminService{
    @Override
    public Integer login(String loginName, String password) {
        if(Strings.isNullOrEmpty(loginName) || Strings.isNullOrEmpty(password)){
            return -1;
        }
        QueryWrapper<AdminEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .lambda()
                .eq(AdminEntity::getLoginName,loginName);
        AdminEntity adminEntity = this.getOne(queryWrapper);
        if(adminEntity == null)
            return -1;

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(passwordEncoder.matches(password,adminEntity.getPassword())){
            return adminEntity.getId();
        }

        return -1;
    }
}
