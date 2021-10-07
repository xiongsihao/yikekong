package com.yikekong.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@TableName(value = "tb_board")
@Data
public class BoardEntity implements Serializable{
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer adminId;
    private String name;
    private Integer quota;
    private String device;
    /**
     * 是否是系统看板
     */
    @TableField(value = "`system`")
    private Boolean system;
    private Boolean disable;
}
