package com.yikekong.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.yikekong.exception.BussinessException;

import com.yikekong.vo.BoardStatus;
import com.yikekong.vo.BoardVO;
import com.yikekong.entity.BoardEntity;
import com.yikekong.service.BoardService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController{
    @Autowired
    private BoardService boardService;

    @PostMapping
    public boolean add(@RequestBody BoardVO vo){
        try {
            BoardEntity entity = new BoardEntity();
            BeanUtils.copyProperties(vo,entity);

            return boardService.save(entity);
        }catch (DuplicateKeyException e){
            throw new BussinessException("已存在该名称");
        }

    }

    @GetMapping()
    public List<BoardEntity> getAll(){
        QueryWrapper<BoardEntity> wrapper = new QueryWrapper<>();
        wrapper
                .lambda()
                .eq(BoardEntity::getSystem,false);
        return boardService.list(wrapper);
    }

    /**
     * 获取系统看板
     * @return
     */
    @GetMapping("/systemBoard")
    public List<BoardEntity> getSystemBoard(){
        QueryWrapper<BoardEntity> wrapper = new QueryWrapper<>();
        wrapper
                .lambda()
                .eq(BoardEntity::getDisable,false)
                .eq(BoardEntity::getSystem,true);

        return boardService.list(wrapper);
    }

    /**
     * 删除看板
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id){
        BoardEntity boardEntity = boardService.getById(id);
        if(boardEntity.getSystem() == true){
            BoardStatus boardStatus = new BoardStatus();
            boardStatus.setBoardId(id);
            boardStatus.setDisable(true);
            List<BoardStatus> boardStatuses = Lists.newArrayList();
            boardStatuses.add(boardStatus);
            return setStatus(boardStatuses);
        }
        return boardService.removeById(id);
    }

    @PutMapping
    public Boolean update(@RequestBody BoardEntity boardEntity){
        try {
            return boardService.updateById(boardEntity);
        }catch (DuplicateKeyException e){
            throw new BussinessException("已存在该名称");
        }

    }

    /**
     * 设置面板状态
     * @param
     * @return
     */
    @PutMapping("/status")
    public Boolean setStatus(@RequestBody List<BoardStatus> boardStatusList){
        boardStatusList.forEach(x->{
            BoardEntity boardEntity = boardService.getById(x.getBoardId());
            boardEntity.setDisable(x.getDisable());
            boardService.updateById(boardEntity);
        });

        return true;
    }

}
