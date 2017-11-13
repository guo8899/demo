package com.example.controller;

import com._21cn.framework.util.StringUtil;
import com._21cn.framework.web.HttpRequestInfo;
import com._21cn.open.common.util.AjaxResponseUtil;
import com.example.entity.Alarm;
import com.example.entity.AlarmTemplate;
import com.example.service.IAlarmCenter;
import com.example.service.factoryimpl.AlarmFactory;
import com.example.util.AlarmUtils;
import freemarker.template.Configuration;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
@ComponentScan(basePackages = {"com.example", "com.example.util"})      //扫描包路径
@SpringBootApplication

public class AlarmController {
    private static final Logger log = LoggerFactory.getLogger("notifyLogger");

    Configuration cfg;
    @Autowired
    public void setCfg(Configuration cfg) {
        this.cfg = cfg;
    }

    /*
    IAlarmCenter alarmCenter;
    @Autowired
    public void setAlarmCenter(IAlarmCenter alarmCenter) {
        this.alarmCenter = alarmCenter;
    }
    */

    @Autowired
    AlarmFactory alarmFactory;
    public void setAlarmFactory(AlarmFactory alarmFactory) {
        this.alarmFactory = alarmFactory;
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
        IAlarmCenter alarmCenter = alarmFactory.getAlarmCenter(alarmId);
        //统计，打日志
        Alarm alarm = alarmCenter.create();
        alarm.setAlarmId(alarmId);
        alarm.setAlarmVlaue(alarmValue);
        alarm.setAlarmThreshold(alarmThreshold);
        alarm.setData(data);
        alarmCenter.add(alarm);

        JSONObject jb = JSONObject.fromObject(alarm);
        log.info(jb.toString());

        //发送: 短信/微信/邮件
        //可能有发送策略
        String notice = alarmCenter.generate(alarm);
        String TAG = "2";                   //分组编号
        //NotifyUtils.sendWechatNotifyByTag(TAG, "@all", notice);
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
            IAlarmCenter alarmCenter = alarmFactory.getAlarmCenter(alarmId);
            Integer alarmCount = alarmCenter.getTotalCount();
            if (alarmCount == null) {
                alarmCount = 0;
            }
            Map<String, Object> alarmInfo = new HashMap<String, Object>();
            alarmInfo.put("alarmId", alarmId);
            AlarmTemplate atl = alarmCenter.getAlarmTemplate();
            if(atl != null) {
                alarmInfo.put("alarmTitle", atl.getAlarmTitle());
            }
            alarmInfo.put("alarmCount", alarmCount);
            alarms.add(alarmInfo);
        }
        else {
            String showAll = reqInfo.getParameter("showAll");
            List<IAlarmCenter> alarmCenters = alarmFactory.getAlarmCenters();
            for(IAlarmCenter alarmCenter : alarmCenters) {
                AlarmTemplate alarmTemplate = alarmCenter.getAlarmTemplate();
                String aId = alarmTemplate.getAlarmId();
                String aTitle = alarmTemplate.getAlarmTitle();
                Integer alarmCount = alarmCenter.getTotalCount();
                if (!StringUtil.isEmpty(showAll) && !showAll.equals("1") && alarmCount == 0) {
                    continue;
                }else {
                    Map<String, Object> alarmInfo = new HashMap<String, Object>();
                    alarmInfo.put("alarmId", aId);
                    alarmInfo.put("alarmTitle", aTitle);
                    alarmInfo.put("alarmCount", alarmCount);
                    alarms.add(alarmInfo);
                }
            }
        }
        resultData.put("result", 10000);
        resultData.put("msg", "success");
        resultData.put("alarms", alarms);
        AjaxResponseUtil.returnData(response, "json", resultData);
    }

    //每天0点执行
    @RequestMapping("/init")
    @ResponseBody
    public String init(HttpServletRequest request, HttpServletResponse response) {
        alarmFactory.init();
        List<IAlarmCenter> alarmCenters = alarmFactory.getAlarmCenters();
        for(IAlarmCenter alarmCenter : alarmCenters) {
            alarmCenter.init();
        }
        return "ok";
    }

    /*
    //App启动时执行
    @RequestMapping("/setAlarmTemplate")
    @ResponseBody
    public String setAlarmTemplate(HttpServletRequest request, HttpServletResponse response) {
        AlarmUtils.setAlarmTemplate(alarmFa, cfg);
        return "ok";
    }
    */

    /**
     * @Scheduled(fixedRate=120000)
     * 定时任务，2分钟触发一次，收集alarmId=xxx的"连续告警"并进行整合
     * 用变量保存维护上次的计数器count0，并和本次计数器count = alarmCenter.getAlarmCount(xxx)比较
     * 若count0 == count，把收集到的告警整合成一条告警, 发送
     * 若count0 <> count，则继续收集
     * 队列模式：收集完就立即发送
     * 数组模式：记录每次收集的下标
     */
    @RequestMapping("/merge")
    @ResponseBody
    public String mergeAlarm(HttpServletRequest request, HttpServletResponse response) {
        List<IAlarmCenter> alarmCenters = alarmFactory.getAlarmCenters();
        for(IAlarmCenter alarmCenter : alarmCenters) {
            alarmCenter.merge();
        }
        return "ok";
    }

    /**
     * 定时发送
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/send")
    @ResponseBody
    public String send(HttpServletRequest request, HttpServletResponse response) {
        HttpRequestInfo reqInfo = new HttpRequestInfo(request);
        String alarmId = null;
        alarmId = reqInfo.getParameter("alarmId");
        if (!StringUtil.isEmpty(alarmId)) {
            IAlarmCenter alarmCenter = alarmFactory.getAlarmCenter(alarmId);
            alarmCenter.send();
        }else{
            List<IAlarmCenter> alarmCenters = alarmFactory.getAlarmCenters();
            for(IAlarmCenter alarmCenter : alarmCenters) {
                alarmCenter.send();
            }
        }
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
