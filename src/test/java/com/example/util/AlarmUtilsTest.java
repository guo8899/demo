package com.example.util;

import com.example.entity.Alarm;
import com.example.entity.AlarmTemplate;
import com.example.handle.AlarmCenter;
import freemarker.template.Configuration;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fzy on 2017/9/20.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackages = {"com.example"})
public class AlarmUtilsTest {
/*
    Configuration cfg;
    @Autowired
    public void setCfg(Configuration cfg) {
        this.cfg = cfg;
    }
    AlarmCenter alarmCenter;
    @Autowired
    public void setAlarmCenter(AlarmCenter alarmCenter) {
        this.alarmCenter = alarmCenter;
    }
*/
    @Test
    public void gererate002() throws Exception {
        AlarmTemplate temp = AlarmUtils.createE189_PROFITRATEWARNING();
        Alarm alarm = AlarmUtils.fromTemplate(temp);
        alarm.setAlarmThreshold(0.80);
        alarm.setAlarmVlaue(0.70);
        Map<String, Object> data = new HashMap<String, Object>();
        alarm.setData(data);

        AlarmCenter alarmCenter = new AlarmCenter();
        Configuration cfg = new Configuration();
        AlarmUtils.setAlarmTemplate(alarmCenter, cfg);
        String message = AlarmUtils.gererate(cfg, alarm);
        System.out.println(message);
    }

    @Test
    public void gererate001() throws Exception {
        AlarmTemplate temp = AlarmUtils.createINTENET_WARING();
        Alarm alarm = AlarmUtils.fromTemplate(temp);
        alarm.setAlarmThreshold(0.80);
        alarm.setAlarmVlaue(0.70);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("supplier", "翼支付");
        data.put("bizType", "全业务");
        data.put("operator", "电信");
        data.put("spec", "500M");
        alarm.setData(data);

        AlarmCenter alarmCenter = new AlarmCenter();
        Configuration cfg = new Configuration();
        AlarmUtils.setAlarmTemplate(alarmCenter, cfg);
        String message = AlarmUtils.gererate(cfg, alarm);
        System.out.println(message);
    }

    @Test
    public void jsonTest() throws Exception {
        //String test = "{\"result\": 10000,\"msg\": \"成功\",\"alarms\": [{\"alarmId\": \"001\",\"alarmTitle\": \"话费成功率告警\",\"alarmCount\": 60},{\"alarmId\": \"007\",\"alarmTitle\": \"流量退费率告警\", \"alarmCount\": 70}]}";
        String test = "{\n" +
                "    \"result\": 10000,\n" +
                "    \"msg\": \"成功\",\n" +
                "    \"alarms\": [\n" +
                "        {\n" +
                "            \"alarmId\": \"001\",\n" +
                "            \"alarmTitle\": \"话费成功率告警\",\n" +
                "            \"alarmCount\": 60\n" +
                "        },\n" +
                "        {\n" +
                "            \"alarmId\": \"007\",\n" +
                "            \"alarmTitle\": \"流量退费率告警\",\n" +
                "            \"alarmCount\": 70\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        JSONObject js = JSONObject.fromObject(test);
        System.out.println(test);
        System.out.println(js.toString());
    }
}