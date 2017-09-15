package com.example.handle;

import com.example.entity.Alarm;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

/**
 * Created by fzy on 2017/9/15.
 */
@ComponentScan("com.example.handle")
public interface IAlarmCenter {
    public Integer getAlarmCount(String alarmId);
    public void addAlarm(Alarm alarm);

    public void addAlarms(List<Alarm> alarms);
    public void increaseAlarmCount(String alarmId);

    public Alarm getAlarmTemplate(String alarmId);
    public void init() ;
}
