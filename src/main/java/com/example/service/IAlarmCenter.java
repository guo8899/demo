package com.example.service;

import com.example.entity.Alarm;
import com.example.entity.AlarmTemplate;
import freemarker.template.Configuration;

import java.util.List;

/**
 * Created by fzy on 2017/9/15.
 */
public abstract class IAlarmCenter {
    protected String alarmId;
    protected AlarmTemplate alarmTemplate;
    protected List<Alarm> alarms;
    protected int lastIndex = 0;
    protected int counter = 0;
    protected Configuration cfg;

    public abstract void add(Alarm alarm);

    public abstract void merge();

    public abstract Alarm create();

    public abstract String generate(Alarm alarm);

    public abstract void init();

    public abstract int getCounter();

    public abstract void send();

    public abstract void send(Alarm alarm);

    public String getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId;
    }

    public AlarmTemplate getAlarmTemplate() {
        return alarmTemplate;
    }

    public void setAlarmTemplate(AlarmTemplate alarmTemplate) {
        this.alarmTemplate = alarmTemplate;
    }

    public List<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(List<Alarm> alarms) {
        this.alarms = alarms;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

}
