package com.example.demo;

import com.example.entity.Alarm;
import com.example.entity.AlarmTemplate;
import com.example.util.AlarmUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		//SpringApplication.run(DemoApplication.class, args);
		//String test = "{\"result\": 10000,\"msg\": \"成功\",\"alarms\": [{\"alarmId\": \"001\",\"alarmTitle\": \"话费成功率告警\",\"alarmCount\": 60},{\"alarmId\": \"007\",\"alarmTitle\": \"流量退费率告警\", \"alarmCount\": 70}]}";
		/*
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
		*/

		/*
		AlarmTemplate temp = AlarmUtils.createE189_PROFITRATEWARNING();
		Alarm alarm = AlarmUtils.fromTemplate(temp);
		alarm.setAlarmThreshold(0.80);
		alarm.setAlarmVlaue(0.70);
		String message = AlarmUtils.gererate(alarm);
		System.out.println(message);
		*/

		AlarmTemplate temp = AlarmUtils.createINTENET_WARING();
		Alarm alarm = AlarmUtils.fromTemplate(temp);
		alarm.setAlarmThreshold(0.80);
		alarm.setAlarmVlaue(0.70);
		//Map<String, String> params = new HashMap<String, String>();
		//params.put("")
		JSONArray ja = new JSONArray();
		ja.add(testJson("", ""));
		String message = AlarmUtils.gererate(alarm);
		System.out.println(message);
	}

	static JSONObject testJson(String key, String value) {
		JSONObject jb = new JSONObject();
		jb.put("key", key);
		return jb;
	}
}
