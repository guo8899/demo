package com.example.controller;

import com._21cn.framework.util.StringUtil;
import com._21cn.framework.web.HttpRequestInfo;
import com._21cn.open.common.util.AjaxResponseUtil;
import com.example.entity.Alarm;
import com.example.entity.AlarmTemplate;
import com.example.handle.AlarmCenter;
import com.example.util.AlarmUtils;
import freemarker.template.Configuration;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


/**
 * Created by fzy on 2017/9/15.
 */
@Controller
@RequestMapping("/alarm")
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.example"})      //扫描包路径

public class AlarmController {
    private static final Logger log = LoggerFactory.getLogger("notifyLogger");

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

    @RequestMapping("/raise")
    @ResponseBody
    public void raiseAlarm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        //参数获取
        HttpRequestInfo reqInfo = new HttpRequestInfo(request);
        Map<String, Object> resultData = new HashMap<String, Object>();
        String alarmId = null;
        Double alarmValue = null;
        Double alarmThreshold = null;
        Map<String, Object> data = new HashMap<String, Object>();

        try {
            alarmId = reqInfo.getParameter("alarmId");
            alarmValue = Double.parseDouble(reqInfo.getParameter("alarmValue"));
            alarmThreshold = Double.parseDouble(reqInfo.getParameter("alarmThreshold"));
            String params = reqInfo.getParameter("data");
            if(!StringUtil.isEmpty(params)) {
                data = AlarmUtils.getMap(JSONArray.fromObject(params));
            }
        }catch (Exception e) {
            e.printStackTrace();
            resultData.put("result", -10000);
            resultData.put("msg", "fail");
            AjaxResponseUtil.returnData(response, "json", resultData);
            return;
        }
        //统计，打日志
        alarmCenter.increaseAlarmCount(alarmId);
        Alarm alarm = new Alarm();
        String alarmTitle = alarmCenter.getAlarmTitle(alarmId);
        String alarmFormat = alarmCenter.getAlarmFormat(alarmId);
        alarm.setAlarmTitle(alarmTitle);
        //alarm.setAlarmFormat(alarmFormat);   //格式不写入日志中
        alarm.setAlarmId(alarmId);
        alarm.setAlarmVlaue(alarmValue);
        alarm.setAlarmThreshold(alarmThreshold);
        alarm.setData(data);
        alarmCenter.addAlarm(alarm);

        JSONObject jb = JSONObject.fromObject(alarm);
        log.info(jb.toString());

        //发送: 短信/微信/邮件
        //可能有发送策略
        alarm.setAlarmFormat(alarmFormat);   //发送需要指定格式
        String notice = AlarmUtils.gererate(cfg, alarm);
        String TAG = "2";                   //分组编号
//        NotifyUtils.sendWechatNotifyByTag(TAG, "@all", notice);
        resultData.put("notice", notice);
        resultData.put("result", 10000);
        resultData.put("msg", "success");
        AjaxResponseUtil.returnData(response, "json", resultData);
    }

    @RequestMapping("/query")
    @ResponseBody
    public void queryAlarm(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        HttpRequestInfo reqInfo = new HttpRequestInfo(request);
        Map<String, Object> resultData = new HashMap<String, Object>();
        List<Map<String, Object>> alarms = new ArrayList<Map<String, Object>>();
        String alarmId = null;
        alarmId = reqInfo.getParameter("alarmId");

        if (!StringUtil.isEmpty(alarmId)) {
            AlarmTemplate alarmTemplate = alarmCenter.getAlarmTemplate(alarmId);
            String alarmTitle = alarmTemplate.getAlarmTitle();
            Integer alarmCount = alarmCenter.getAlarmCount(alarmId);
            if (alarmCount == null) {
                alarmCount = 0;
            }
            Map<String, Object> alarmInfo = new HashMap<String, Object>();
            alarmInfo.put("alarmId", alarmId);
            alarmInfo.put("alarmTitle", alarmTitle);
            alarmInfo.put("alarmCount", alarmCount);
            alarms.add(alarmInfo);
        }
        else {
            String showAll = reqInfo.getParameter("showAll");
            Set<String> aIds = null;
            if (StringUtil.isEmpty(showAll) || showAll.equals("1")) {
                aIds = alarmCenter.getAlarmTemplateMap().keySet();
            }
            else {
                aIds = alarmCenter.getAlarmMap().keySet();
            }
            for (String aId : aIds) {
                String alarmTitle = alarmCenter.getAlarmTitle(aId);
                Integer alarmCount = alarmCenter.getAlarmCount(aId);
                if (alarmCount == null) {
                    alarmCount = 0;
                }
                Map<String, Object> alarmInfo = new HashMap<String, Object>();
                alarmInfo.put("alarmId", aId);
                alarmInfo.put("alarmTitle", alarmTitle);
                alarmInfo.put("alarmCount", alarmCount);
                alarms.add(alarmInfo);
            }
        }
        resultData.put("result", 10000);
        resultData.put("msg", "success");
        resultData.put("alarms", alarms);
        AjaxResponseUtil.returnData(response, "json", resultData);
    }

    @RequestMapping("/init")
    @ResponseBody
    public String init(HttpServletRequest request, HttpServletResponse response) {
        alarmCenter.init();
        return "ok";
    }

    @RequestMapping("/setAlarmTemplate")
    @ResponseBody
    public String setAlarmTemplate(HttpServletRequest request, HttpServletResponse response) {
        AlarmUtils.setAlarmTemplate(alarmCenter, cfg);
        return "ok";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test(HttpServletRequest request, HttpServletResponse response) {
        return "ok";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AlarmController.class, args);
    }
}
