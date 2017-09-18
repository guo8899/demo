package com.example.entity;

import net.sf.json.JSONArray;

/**
 * Created by fzy on 2017/9/15.
 */
public class Alarm extends AlarmTemplate {
    /**
     * 告警编号
     */
    //private String alarmId;
    /**
     * 告警格式
     * 可配置
     */
    //private String alarmFormat;
    /**
     * 告警标题
     */
    //private String alarmTitle;
    /**
     * 告警数值
     */
    private Double alarmVlaue;
    /**
     * 告警阈值
     */
    private Double alarmThreshold;

    /**
     * 可选属性，可配置
     * data = JSONArray.fromObject(params);
     * [
            {"key":"provinceCode", "value":"CN45"},
            {"key" :"channelCode", "value":"E_189"},
            {"key" :"businessType", "value":1}
        ]
     */
    private JSONArray data;

    /*
    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }
     public String getAlarmTitle() {
        return alarmTitle;
    }

    public void setAlarmTitle(String alarmTitle) {
        this.alarmTitle = alarmTitle;
    }

    public String getAlarmFormat() {
        return alarmFormat;
    }

    public void setAlarmFormat(String alarmFormat) {
        this.alarmFormat = alarmFormat;
    }
    */
    public Double getAlarmVlaue() {
        return alarmVlaue;
    }

    public void setAlarmVlaue(Double alarmVlaue) {
        this.alarmVlaue = alarmVlaue;
    }

    public Double getAlarmThreshold() {
        return alarmThreshold;
    }

    public void setAlarmThreshold(Double alarmThreshold) {
        this.alarmThreshold = alarmThreshold;
    }

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }


    public void setDataByParams(String params) {
        this.setData(JSONArray.fromObject(params));
    }
}
