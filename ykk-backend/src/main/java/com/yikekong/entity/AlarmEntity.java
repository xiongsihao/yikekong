package com.yikekong.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName(value = "tb_alarm",resultMap = "alarmMap",autoResultMap = true)
@Data
public class AlarmEntity implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer quotaId;
    private String operator;
    private Integer threshold;
    private Integer level;
    private Integer cycle;
    @TableField(value = "webhook")
    private String webHook;
    @TableField(exist = false)
    private QuotaEntity quota;
}
