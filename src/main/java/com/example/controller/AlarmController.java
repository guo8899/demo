package com.example.controller;

import com._21cn.framework.util.StringUtil;
import com._21cn.framework.web.HttpRequestInfo;
import com._21cn.open.common.util.AjaxResponseUtil;
import com.example.entity.Alarm;
import com.example.handle.AlarmCenter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by fzy on 2017/9/15.
 */
@Controller
@RequestMapping("/alarm")
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.example"})      //扫描包路径

public class AlarmController {
    AlarmCenter alarmCenter;
    @Autowired
    public void setAlarmCenter(AlarmCenter alarmCenter) {
        this.alarmCenter = alarmCenter;
    }

    @RequestMapping("/raise")
    public void raiseAlarm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //参数获取
        HttpRequestInfo reqInfo = new HttpRequestInfo(request);
        Map<String, Object> resultData = new HashMap<String, Object>();
        String alarmId = null;
        Double alarmValue = null;
        Double alarmThreshold = null;
        JSONArray data = null;
        try {
            alarmId = reqInfo.getParameter("alarmId");
            alarmValue = Double.parseDouble(reqInfo.getParameter("alarmValue"));
            alarmThreshold = Double.parseDouble(reqInfo.getParameter("alarmThreshold"));
            String params = reqInfo.getParameter("params");
            data = JSONArray.fromObject(params);
        }catch (Exception e) {
            e.printStackTrace();
            resultData.put("result", -10000);
            resultData.put("msg", "fail");
            AjaxResponseUtil.returnData(response, "json", resultData);
        }
        //统计，打日志
        alarmCenter.increaseAlarmCount(alarmId);
        Alarm alarm = new Alarm();
        alarm.setAlarmId(alarmId);
        alarm.setAlarmVlaue(alarmValue);
        alarm.setAlarmThreshold(alarmThreshold);
        alarmCenter.addAlarm(alarm);

        JSONObject jb = JSONObject.fromObject(alarm);
        //log.info(jb);

        //发送: 短信微信邮件
        //可能有过滤规则(不发送)
        resultData.put("result", 10000);
        resultData.put("msg", "success");
        AjaxResponseUtil.returnData(response, "json", resultData);
    }

    @RequestMapping("/query")
    public void queryAlarm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        HttpRequestInfo reqInfo = new HttpRequestInfo(request);
        Map<String, Object> resultData = new HashMap<String, Object>();
        List<Map<String, Object>> alarms = new ArrayList<Map<String, Object>>();
        String alarmId = null;
        alarmId = reqInfo.getParameter("alarmId");

        if (!StringUtil.isEmpty(alarmId)) {
            Alarm alarm = alarmCenter.getAlarmTemplate(alarmId);
            String alarmTitle = alarm.getAlarmTitle();
            Integer alarmCount = alarmCenter.getAlarmCount(alarmId);
            Map<String, Object> alarmInfo = new HashMap<String, Object>();
            alarmInfo.put("alarmId", alarmId);
            alarmInfo.put("alarmTitle", alarmTitle);
            alarmInfo.put("alarmCount", alarmCount);
            alarms.add(alarmInfo);
        }
        resultData.put("result", 10000);
        resultData.put("msg", "success");
        resultData.put("alarms", alarms);
        AjaxResponseUtil.returnData(response, "json", resultData);
    }

    @RequestMapping("/init")
    public void init(HttpServletRequest request, HttpServletResponse response) {
        alarmCenter.init();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AlarmController.class, args);
    }
}
