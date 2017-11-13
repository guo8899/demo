package com.example.service.factoryimpl;

import com.example.service.IAlarmCenter;
import com.example.service.IAlarmFactory;
import com.example.service.impl.E189ProfitRateWaringCenter;
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

    private Map<String, IAlarmCenter> alarmCenterMap = new HashMap<String, IAlarmCenter>();

    @Override
    public IAlarmCenter getAlarmCenter(String alramId) {
        return alarmCenterMap.get(alramId);
    }

    @Override
    public List<IAlarmCenter> getAlarmCenters() {
        return new ArrayList(alarmCenterMap.values());
    }

    @Override
    public void init() {
        alarmCenterMap.put(e189ProfitRateWaringCenter.getAlarmId(), e189ProfitRateWaringCenter);
    }


}
