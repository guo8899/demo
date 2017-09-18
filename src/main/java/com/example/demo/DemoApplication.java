package com.example.demo;

import net.sf.json.JSONObject;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		//SpringApplication.run(DemoApplication.class, args);
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
