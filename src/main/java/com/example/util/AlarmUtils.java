package com.example.util;

import com.example.entity.Alarm;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by fzy on 2017/9/15.
 */
public class AlarmUtils {
    public static Alarm createFirst() {
        return new Alarm();
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
