package com.yikekong.controller;

import com.yikekong.dto.DeviceDTO;
import com.yikekong.service.DeviceService;
import com.yikekong.vo.DeviceQuotaVO;
import com.yikekong.vo.DeviceVO;
import com.yikekong.vo.Pager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/device")
@Slf4j
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    /**
     * 设置状态的接口
     *
     * @param deviceVO
     * @return
     */
    @PutMapping("/status")
    public boolean setStatus(@RequestBody DeviceVO deviceVO) {
        return deviceService.setStatus(deviceVO.getSn(), deviceVO.getStatus());
    }

    /**
     * 设置标签的接口
     *
     * @param deviceVO
     * @return
     */
    @PutMapping("/tags")
    public boolean setTags(@RequestBody DeviceVO deviceVO) {
        return deviceService.updateTags(deviceVO.getSn(), deviceVO.getTags());
    }

    /**
     * 分页搜索设备
     *
     * @param page
     * @param pageSize
     * @param sn
     * @param tag
     * @return
     */
    @GetMapping
    public Pager<DeviceDTO> findPage(@RequestParam(value = "page", required = false, defaultValue = "1") Long page,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Long pageSize,
                                     @RequestParam(value = "sn", required = false) String sn,
                                     @RequestParam(value = "tag", required = false) String tag) {
        return deviceService.queryPage(page, pageSize, sn, tag, null);
    }

    /**
     * 接收设备断链信息
     */
    @PostMapping("/clientAction")
    public void clientAction(@RequestBody Map<String, String> param) {
        log.info(param.toString());
        String deviceId = param.get("clientid");  //提取设备id
        if( param.get("action").equals("client_connected") ){ //如果是联网
            deviceService.updateOnline(deviceId,true);
        }
        if( param.get("action").equals("client_disconnected") ){ //如果是断网
            deviceService.updateOnline(deviceId,false);
        }
    }

    /**
     * 设备详情
     * @return
     */
    @GetMapping("/deviceQuota")
    public  Pager<DeviceQuotaVO> queryQuotaData(@RequestParam(value="page",required = false,defaultValue = "1") Long page,
                                                @RequestParam(value = "pageSize",required = false,defaultValue = "10") Long pageSize,
                                                @RequestParam(value = "deviceId",required = false) String deviceId,
                                                @RequestParam(value = "tag",required = false)  String tag,
                                                @RequestParam(value = "state",required = false)  Integer state){
        return deviceService.queryDeviceQuota(page,pageSize,deviceId,tag,state);
    }
}
