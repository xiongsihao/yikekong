package com.yikekong.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName(value = "tb_gps")
@Data
public class GPSEntity implements Serializable{
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Integer id;
    private String subject;
    private String snKey;
    /**
     * 是否是单字段
     */
    private Boolean singleField;
    private String valueKey;
    private String separation;
    /**
     * 纬度字段
     */
    private String latitude;
    /**
     * 经度字段
     */
    private String longitude;
}
