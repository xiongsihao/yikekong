package com.yikekong.common;

public class SystemDefinition{
    //redis中指标主题前缀
    public static final String QUOTA_KEY_PREFIX = "ykk.quota.";
    //redis中gps信息key前缀
    public static final String GPS_KEY_PREFIX = "ykk.gps.quota.";
    //redis中客户端状态key前缀
    public static final String CLIENT_INFO = "ykk.client.";

    //redis中客户端状态key前缀
    public static final String CYCLE_KEY = "ykk.cycle.";

    //redis中设备存储key前缀
    public static final  String DEVICE_KEY = "ykk.device";
    //ES中设备索引名称
    public static final String ES_INDEX_NAME = "Device";
    //redis中通过主题存储的设备Id字段前缀
    public static final String QUOTA_SUBJECT_DEVICE_KEY_PREFIX = "ykk.quota.deviceId.";

    /**
     * redis中设备指标存储key的前缀
     */
    public static final String DEVICE_QUOTA_KEY_PREFIX = "ykk.device.quotaList.";
}
