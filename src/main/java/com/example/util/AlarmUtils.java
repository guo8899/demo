package com.example.util;

import com.example.entity.Alarm;
import com.example.entity.AlarmTemplate;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    public static String gererate(Alarm alarm) {
        String title = alarm.getAlarmTitle();
        String format = alarm.getAlarmFormat();

        List<Object> args = new ArrayList<Object>();
        args.add(alarm.getAlarmVlaue());
        args.add(alarm.getAlarmThreshold());
        JSONArray data = alarm.getData();
        if (null != data && data.size() > 0) {
            Iterator<Object> it = data.iterator();
            while (it.hasNext()) {
                JSONObject jb = (JSONObject) it.next();
                //String key = (String) jb.get("key");
                String value = (String) jb.get("value");
                args.add(value);
            }
        }
        return gererate(title, format, args);
    }

    public static String gererate(String title, String format, Object... args) {
        String message = String.format(format, args);
        return title + "\n" + message;
    }
}
