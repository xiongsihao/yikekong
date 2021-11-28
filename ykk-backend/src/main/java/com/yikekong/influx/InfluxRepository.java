package com.yikekong.influx;

import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * @author : xsh
 * @create : 2021-11-29 - 0:46
 * @describe:
 */
@Component
@Slf4j
public class InfluxRepository {

    @Autowired
    private InfluxDB influxDB;

    @Value("${spring.influx.db}")
    private String dbName;

    /**
     * 添加数据
     * @param object
     */
    public void add( Object object ){
        Point.Builder pointBuilder= Point.measurementByPOJO( object.getClass() );

        Point point = pointBuilder.addFieldsFromPOJO(object)
                //influxDB的默认时间是UTC,比北京时间相差8小时；所以需要在此处手动处理下时间
                .time(LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("+8")).toEpochMilli() , TimeUnit.MILLISECONDS)
                .build();
        influxDB.setDatabase(dbName);
        influxDB.write(point);
        influxDB.close();
    }
}
