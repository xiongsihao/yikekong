package com.yikekong.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Pager<T> implements Serializable{
    /**
     * 总条数
     */
    private long counts;
    /**
     * 每页条数
     */
    private long pageSize;
    /**
     * 总页数
     */
    private long pages;
    /**
     * 当前页数
     */
    private long page;

    /**
     * 当前页数据
     */
    private List<T> items;

    public Pager(IPage page) {
        this.pageSize = page.getSize();
        this.counts = page.getTotal();
        this.page = page.getCurrent();
        this.pages = page.getPages();
        this.items = page.getRecords();
    }

    public Pager(Long counts,Long pageSize){
        this.counts = counts;
        this.pageSize = pageSize;
        if(pageSize <= 0){
            pages = 0;
        }else {
            pages = counts%pageSize ==0? (counts/pageSize) : (counts/pageSize) +1;
        }
    }
}
