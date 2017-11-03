package com.example.service.impl;

import com.example.entity.Alarm;
import com.example.entity.AlarmTemplate;
import com.example.service.IAlarmCenter;
import com.example.util.ConstantUtils;
import com.example.util.NotifyUtils;
import com.example.util.TemplateUtils;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fzy on 2017/9/15.
 */
@Component("e189ProfitRateWaringCenter")
public class E189ProfitRateWaringCenter extends IAlarmCenter {
    //protected String alarmId;
    //protected AlarmTemplate alarmTemplate;
    //protected int lastIndex = 0;
    protected String alarmId = "001";

    protected List<Alarm> alarms = new ArrayList<Alarm>();
    protected AlarmTemplate alarmTemplate = new AlarmTemplate(
            ConstantUtils.E189_PROFITRATEWARNING_ID,
            ConstantUtils.E189_PROFITRATEWARNING_TITLE,
            ConstantUtils.E189_PROFITRATEWARNING_FORMAT
            );

    protected Configuration cfg = Configuration.getDefaultConfiguration();
    //init
    {
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate("template" + alarmId, alarmTemplate.getAlarmFormat());
        cfg.setTemplateLoader(stringLoader);
    }

    @Override
    public void add(Alarm alarm) {
        alarms.add(alarm);
        lastIndex++;
        counter++;
    }

    @Override
    public void merge() {
        for (int index = alarms.size() - 1; index >= lastIndex + 1; index--) {
            Alarm e1 = alarms.get(index - 1);
            Alarm e2 = alarms.get(index);
            merge(e1, e2);
            alarms.remove(e2);
        }
        lastIndex = alarms.size();
    }

    @Override
    public Alarm create() {
        Alarm alarm = new Alarm();
        alarm.setAlarmId(alarmId);
        alarm.setAlarmTitle(alarmTemplate.getAlarmTitle());
        //alarm.setAlarmFormat(alarmTemplate.getAlarmFormat());
        return alarm;
    }

    private void merge(Alarm e1, Alarm e2) {
        //e1.getData().get("")
    }

    @Override
    public String generate(Alarm alarm) {
        String templateId = "template" + alarmId;
        String message = TemplateUtils.generate(cfg, alarm, templateId);
        return message;
    }

    @Override
    public void init() {
        alarms = new ArrayList<Alarm>();
        lastIndex = 0;
        counter = 0;
    }

    @Override
    public int getCounter() {
        return counter;
    }


    @Override
    public void send() {
        for (Alarm alarm : alarms) {
            send(alarm);
        }
    }

    @Override
    public void send(Alarm alarm) {
        String notice = generate(alarm);
        String TAG = "2";                   //分组编号
        NotifyUtils.sendWechatNotifyByTag(TAG, "@all", notice);
    }
}
