package com.yikekong.vo;

import lombok.Data;
import java.io.Serializable;

@Data
public class BoardStatus implements Serializable {
    private Integer boardId;
    private Boolean disable;
}
