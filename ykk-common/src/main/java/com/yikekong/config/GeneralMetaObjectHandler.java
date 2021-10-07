//package com.yikekong.config;
//
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.reflection.MetaObject;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Slf4j
//@Component
//public class GeneralMetaObjectHandler implements MetaObjectHandler{
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        try {
//            setFieldValByName("createTime", LocalDateTime.now(), metaObject);
//            updateFill(metaObject);
//        } catch (Exception e) {
//            log.error("GeneralMetaObjectHandler error",e);
//        }
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        try {
//            setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
//        } catch (Exception e) {
//            log.error("GeneralMetaObjectHandler error",e);
//        }
//    }
//}
