package com.example.handle;

import com.example.entity.Alarm;
import com.example.entity.AlarmTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fzy on 2017/9/15.
 */
@Component
//@ComponentScan("com.example.handle")
public class AlarmCenter {
    /**
     *  alarmId, alarmTitle, alarmFormat
     */
    private Map<String, AlarmTemplate> alarmTemplateMap = new HashMap<String, AlarmTemplate>();

    private Map<String, Integer> alarmCounter = new HashMap<String, Integer>();
    private Map<String, ArrayList<Alarm>> alarmMap = new HashMap<String, ArrayList<Alarm>>();

    public Map<String, AlarmTemplate> getAlarmTemplateMap() {
        return alarmTemplateMap;
    }

    public void setAlarmTemplateMap(Map<String, AlarmTemplate> alarmTemplateMap) {
        this.alarmTemplateMap = alarmTemplateMap;
    }

    public Map<String, Integer> getAlarmCounter() {
        return alarmCounter;
    }

    public void setAlarmCounter(Map<String, Integer> alarmCounter) {
        this.alarmCounter = alarmCounter;
    }

    public Map<String, ArrayList<Alarm>> getAlarmMap() {
        return alarmMap;
    }

    public void setAlarmMap(Map<String, ArrayList<Alarm>> alarmMap) {
        this.alarmMap = alarmMap;
    }


    public Integer getAlarmCount(String alarmId) {
        return alarmCounter.get(alarmId);
    }

    public void addAlarm(Alarm alarm) {
        ArrayList<Alarm> alarmList = alarmMap.get(alarm.getAlarmId());
        if (alarmList == null) {
            alarmList = new ArrayList<Alarm>();
        }
        alarmList.add(alarm);
        alarmMap.put(alarm.getAlarmId(), alarmList);
    }

    public void addAlarms(List<Alarm> alarms) {
        for (Alarm alarm : alarms) {
            addAlarm(alarm);
        }
    }
    public void increaseAlarmCount(String alarmId) {
        Integer count = getAlarmCount(alarmId);
        if (count == null) {
            count = 0;
        }
        alarmCounter.put(alarmId, count + 1);
    }

    public AlarmTemplate getAlarmTemplate(String alarmId) {
        return alarmTemplateMap.get(alarmId);
    }

    public void init() {
        alarmCounter = new HashMap<String, Integer>();
        alarmMap = new HashMap<String, ArrayList<Alarm>>();
    }

    public void setAlarmTemplate() {
        //set alarmTemplateMap 设置告警模板，从数据库或者配置或代码写死获取
        alarmTemplateMap = new HashMap<String, AlarmTemplate>();
    }
}
