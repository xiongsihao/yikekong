import com.yikekong.YkkApplication;
import com.yikekong.dto.QuotaInfo;
import com.yikekong.influx.InfluxRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author : xsh
 * @create : 2021-11-29 - 0:54
 * @describe:
 */
@SpringBootTest(classes = YkkApplication.class)
@RunWith(SpringRunner.class)
public class InfluxTest {

    @Autowired
    private InfluxRepository influxDBRepository;

    @Test
    public void testAdd(){
        QuotaInfo quotaInfo=new QuotaInfo();
        quotaInfo.setDeviceId("123456");
        quotaInfo.setQuotaId("1");
        quotaInfo.setQuotaName("温度");
        quotaInfo.setReferenceValue("0-10");
        quotaInfo.setUnit("摄氏度");
        quotaInfo.setAlarm("1");
        quotaInfo.setValue(11D);
        influxDBRepository.add(quotaInfo);
    }
}
