package com.example.entity;

/**
 * Created by fzy on 2017/9/18.
 */
public class AlarmTemplate {
    private static final long serialVersionUID = -4877893573223903453L;
    /**
     * 告警编号
     */
    protected String alarmId;
    /**
     * 告警格式
     * 可配置
     */
    protected String alarmFormat;
    /**
     * 告警标题
     */
    protected String alarmTitle;

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

    public AlarmTemplate(String alarmId, String alarmTitle, String alarmFormat) {
        this.alarmId = alarmId;
        this.alarmFormat = alarmFormat;
        this.alarmTitle = alarmTitle;
    }
    public AlarmTemplate() {}
}
