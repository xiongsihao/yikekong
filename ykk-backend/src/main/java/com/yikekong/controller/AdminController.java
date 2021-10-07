package com.yikekong.controller;

import com.yikekong.vo.AdminVO;
import com.yikekong.vo.LoginResultVO;
import com.yikekong.service.AdminService;
import com.yikekong.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController{
    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public LoginResultVO login(@RequestBody AdminVO admin){
        LoginResultVO result = new LoginResultVO();
        Integer adminId = adminService.login(admin.getLoginName(),admin.getPassword());
        if(adminId < 0){
            result.setLoginSuccess(false);
            return result;
        }
        result.setAdminId(adminId);
        String token = JwtUtil.createJWT(adminId);
        result.setToken(token);
        result.setLoginSuccess(true);

        return result;
    }
}
