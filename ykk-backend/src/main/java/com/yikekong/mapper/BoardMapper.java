package com.yikekong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yikekong.entity.BoardEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper extends BaseMapper<BoardEntity>{
}
