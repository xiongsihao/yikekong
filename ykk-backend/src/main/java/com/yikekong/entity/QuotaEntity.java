package com.yikekong.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "tb_quota")
@Data
public class QuotaEntity{
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /**
     * 指标名称
     */
    private String name;
    /**
     * 单位
     */
    private String unit;
    /**
     * 报文主题
     */
    private String subject;
    /**
     * 指标值字段
     */
    private String valueKey;
    /**
     * 指标值数据类型
     */
    private String valueType;
    /**
     * 设备识别码字段(设备Id)
     */
    private String snKey;
    /**
     * web钩子地址
     */
    private String webhook;
    /**
     * 参考值
     */
    private String referenceValue;
}
