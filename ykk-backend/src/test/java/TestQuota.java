import com.fasterxml.jackson.core.JsonProcessingException;
import com.yikekong.YkkApplication;
import com.yikekong.dto.DeviceInfoDTO;
import com.yikekong.service.AlarmService;
import com.yikekong.service.QuotaService;
import com.yikekong.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : xsh
 * @create : 2021-10-08 - 22:36
 * @describe:
 */
@SpringBootTest(classes = YkkApplication.class)
@RunWith(SpringRunner.class)
public class TestQuota {

    @Autowired
    private QuotaService quotaService;
    @Autowired
    private AlarmService alarmService;

    @Test
    public void testAnalysis1(){

        Map map=new HashMap<>();
        map.put("sn","123456");
        map.put("temp",1.2);// 也测试一下 map.put("temp","1.2");
        DeviceInfoDTO deviceInfoDTO = quotaService.analysis("temperature", map);
        String json = null;
        try {
            json = JsonUtil.serialize(deviceInfoDTO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(json);
    }


    /**
     * 测试报文解析(告警)
     */
    @Test
    public void testAnalysis2(){

        Map map=new HashMap<>();
        map.put("sn","123456");
        map.put("temp",12);
        DeviceInfoDTO deviceInfoDTO = quotaService.analysis("temperature", map);
        //告警信息封装
        DeviceInfoDTO deviceInfoDTO1 = alarmService.verifyDeviceInfo(deviceInfoDTO);
        String json = null;
        try {
            json = JsonUtil.serialize(deviceInfoDTO1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(json);
    }
}
