package com.example.service;

import java.util.List;

/**
 * Created by fzy on 2017/11/1.
 */
public interface IAlarmFactory {
    public IAlarmCenter getAlarmCenter(String alramId);
    public List<IAlarmCenter> getAlarmCenters();
    public void init();
}
