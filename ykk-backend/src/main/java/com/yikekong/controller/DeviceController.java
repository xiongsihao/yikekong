package com.yikekong.controller;
import com.yikekong.dto.DeviceDTO;
import com.yikekong.service.DeviceService;
import com.yikekong.vo.DeviceVO;
import com.yikekong.vo.Pager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/device")
@Slf4j
public class DeviceController{

    @Autowired
    private DeviceService deviceService;

    /**
     * 设置状态的接口
     * @param deviceVO
     * @return
     */
    @PutMapping("/status")
    public boolean setStatus(@RequestBody DeviceVO deviceVO){
        return deviceService.setStatus(deviceVO.getSn(),deviceVO.getStatus());
    }

    /**
     * 设置标签的接口
     * @param deviceVO
     * @return
     */
    @PutMapping("/tags")
    public boolean setTags(@RequestBody DeviceVO deviceVO){
        return deviceService.updateTags(deviceVO.getSn(),deviceVO.getTags());
    }

    /**
     * 分页搜索设备
     * @param page
     * @param pageSize
     * @param sn
     * @param tag
     * @return
     */
    @GetMapping
    public Pager<DeviceDTO> findPage(@RequestParam(value = "page",required = false,defaultValue = "1") Long page,
                                     @RequestParam(value = "pageSize",required = false,defaultValue = "10") Long pageSize,
                                     @RequestParam(value = "sn",required = false) String sn,
                                     @RequestParam(value = "tag",required = false) String tag){
        return deviceService.queryPage(page,pageSize,sn,tag,null);
    }
}
