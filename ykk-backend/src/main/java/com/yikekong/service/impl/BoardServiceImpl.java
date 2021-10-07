package com.yikekong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yikekong.entity.BoardEntity;
import com.yikekong.mapper.BoardMapper;
import com.yikekong.service.BoardService;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceImpl extends ServiceImpl<BoardMapper, BoardEntity> implements BoardService{
    @Override
    public Boolean disable(Integer boardId) {
        BoardEntity boardEntity = this.getById(boardId);
        if(boardEntity == null) return false;
        if(boardEntity.getSystem() == true){
            boardEntity.setDisable(true);
            return this.updateById(boardEntity);
        }

        return this.removeById(boardId);
    }
}