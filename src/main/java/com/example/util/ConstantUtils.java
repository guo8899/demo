package com.example.util;

/**
 * Created by fzy on 2017/9/15.
 */
public class ConstantUtils {
    public static final String E189_PROFITRATEWARNING_ID = "001";
    public static final String E189_PROFITRATEWARNING_TITLE = "自营渠道利润率告警";
    public static final String E189_PROFITRATEWARNING_FORMAT =  ""
            + "---${object.alarmTitle}---\n"
            + "告  警  线：${object.alarmThreshold} \n"
            + "利  润   率：${object.alarmVlaue} \n";

    public static final String INTENET_WARING_ID = "002";
    public static final String INTENET_WARING_TITLE = "互联网渠道利润率告警";
    public static final String INTENET_WARING_FORMAT = ""
            + "---${object.alarmTitle}---\n"
            + "告  警  线：${object.alarmThreshold} \n"
            + "利  润   率：${object.alarmVlaue} \n"
            + "供   货  商：${object.data.supplier} \n"
            + "业         务：${object.data.bizType} \n"
            + "运   营  商:${object.data.operator} \n"
            + "规         格:${object.data.spec} \n";
}
