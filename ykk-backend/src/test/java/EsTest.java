import com.yikekong.YkkApplication;
import com.yikekong.dto.DeviceDTO;
import com.yikekong.es.ESRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : xsh
 * @create : 2021-10-07 - 22:41
 * @describe:
 */
@SpringBootTest(classes = YkkApplication.class)
@RunWith(SpringRunner.class)
public class EsTest {

    @Autowired
    private ESRepository esRepository;

    @Test
    public void testAdd(){
        DeviceDTO deviceDTO=new DeviceDTO();
        deviceDTO.setDeviceId("123456");
        deviceDTO.setStatus(true);
        deviceDTO.setAlarm(false);
        deviceDTO.setLevel(0);
        deviceDTO.setAlarmName("");
        deviceDTO.setOnline(true);
        deviceDTO.setTag("");
        esRepository.addDevices(deviceDTO);
    }
}
