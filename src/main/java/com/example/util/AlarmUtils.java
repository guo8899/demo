package com.example.util;

import com.example.entity.Alarm;
import com.example.entity.AlarmTemplate;

/**
 * Created by fzy on 2017/9/15.
 */
public class AlarmUtils {
    public static AlarmTemplate createE189_PROFITRATEWARNING() {
        AlarmTemplate alarmTemplate = new AlarmTemplate();
        alarmTemplate.setAlarmId(ConstantUtils.E189_PROFITRATEWARNING_ID);
        alarmTemplate.setAlarmTitle(ConstantUtils.E189_PROFITRATEWARNING_TITLE);
        alarmTemplate.setAlarmFormat(ConstantUtils.E189_PROFITRATEWARNING_FORMAT);
        return  alarmTemplate;
    }
    public static AlarmTemplate createINTENET_WARING() {
        AlarmTemplate alarmTemplate = new AlarmTemplate();
        alarmTemplate.setAlarmId(ConstantUtils.INTENET_WARING_ID);
        alarmTemplate.setAlarmTitle(ConstantUtils.INTENET_WARING_TITLE);
        alarmTemplate.setAlarmFormat(ConstantUtils.INTENET_WARING_FORMAT);
        return  alarmTemplate;
    }
    public static Alarm fromTemplate(AlarmTemplate temp) {
        Alarm alarm = new Alarm();
        alarm.setAlarmId(temp.getAlarmId());
        alarm.setAlarmTitle(temp.getAlarmTitle());
        alarm.setAlarmFormat(temp.getAlarmFormat());
        return alarm;
    }

    public static String gererate(Alarm alarm) {
        String templateId = "template" + alarm.getAlarmId();
        String templateContent = alarm.getAlarmFormat();
        String message = TemplateUtils.generate(alarm, templateId, templateContent);
        return message;
    }
}
