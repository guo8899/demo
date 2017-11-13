package com.example.service.factoryimpl;

import com.example.service.IAlarmCenter;
import com.example.service.IAlarmFactory;
import com.example.service.impl.DefaultAlarmCenter;
import com.example.service.impl.E189ProfitRateWaringCenter;
import com.example.service.impl.IntenetWarningCenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fzy on 2017/11/1.
 */
@Component
public class AlarmFactory implements IAlarmFactory {
    E189ProfitRateWaringCenter e189ProfitRateWaringCenter;
    @Autowired
    public void setE189ProfitRateWaringCenter(E189ProfitRateWaringCenter e189ProfitRateWaringCenter) {
        this.e189ProfitRateWaringCenter = e189ProfitRateWaringCenter;
    }

    IntenetWarningCenter intenetWarningCenter;
    @Autowired
    public void setIntenetWarningCenter(IntenetWarningCenter intenetWarningCenter) {
        this.intenetWarningCenter = intenetWarningCenter;
    }

    DefaultAlarmCenter defaultAlarmCenter;
    @Autowired
    public void setDefaultAlarmCenter(DefaultAlarmCenter defaultAlarmCenter) {
        this.defaultAlarmCenter = defaultAlarmCenter;
    }

    private Map<String, IAlarmCenter> alarmCenterMap = new HashMap<String, IAlarmCenter>();

    @Override
    public IAlarmCenter getAlarmCenter(String alramId) {
        IAlarmCenter alarmCenter = alarmCenterMap.get(alramId);
        if (alarmCenter != null) {
            return alarmCenter;
        }else{
            return defaultAlarmCenter;
        }
    }

    @Override
    public List<IAlarmCenter> getAlarmCenters() {
        return new ArrayList(alarmCenterMap.values());
    }

    @Override
    public void init() {
        alarmCenterMap.put(e189ProfitRateWaringCenter.getAlarmId(), e189ProfitRateWaringCenter);
        alarmCenterMap.put(intenetWarningCenter.getAlarmId(), intenetWarningCenter);
        alarmCenterMap.put(defaultAlarmCenter.getAlarmId(), defaultAlarmCenter);
    }

}
