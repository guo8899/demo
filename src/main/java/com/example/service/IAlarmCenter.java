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
    protected int totalCount = 0;
    protected int sendCount = 0;
    protected Configuration cfg;

    public abstract void add(Alarm alarm);

    public abstract void merge();

    public abstract Alarm create();

    public abstract String generate(Alarm alarm);

    public abstract void init();

    public abstract int getTotalCount();

    public abstract int getSendCount();

    public abstract void send();

    public abstract void send(Alarm alarm);

    public abstract String getAlarmId();

    public abstract void setAlarmId(String alarmId);

    public abstract AlarmTemplate getAlarmTemplate();

    public abstract void setAlarmTemplate(AlarmTemplate alarmTemplate);

    public abstract List<Alarm> getAlarms();


}
