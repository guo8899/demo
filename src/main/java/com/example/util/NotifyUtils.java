package com.example.util;

import com._21cn.open.common.util.StringUtil;
import com._21cn.open.common.util.WebUtil;
import com._21cn.wechat.entity.AccessToken;
import com._21cn.wechat.util.WechatCorpApi;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class NotifyUtils {
    //微信参数
    private static final String corpId = "wx115e4f4ad173e203"; //微信企业号appkey
    private static final String secret = "0p3wq9WQpYlcK71x2qEVJI2R6X_txX-sw28I-svx72w2jt-Lii53kIsrv1iBGaMe"; //微信企业号秘钥
    //邮件参数
    private static String mailAccount = WebUtil.getConfigValue("openstatistic.properties", "sendMailAccount");
    private static String mailPassword = WebUtil.getConfigValue("openstatistic.properties", "sendMailPassword");
    private static String mailHostName = WebUtil.getConfigValue("openstatistic.properties", "mailHostName");

    private static final Logger log = LoggerFactory.getLogger(NotifyUtils.class);
    /**
     *
     * @Title: sendMessage
     * @Description: 短信推送
     * @author：tangy@corp.21cn.com
     * @param @param mobileNum
     * @param @param message    设定文件
     * @return void    返回类型
     * @throws
     */
    public static void sendMessage(String mobileNum,String message){
        HttpClient httpClient = new HttpClient();
        PostMethod post = null;
        String url = "http://message.open.e.189.cn/api/sendSmsNew.do";
        post = new PostMethod(url);
        post.addParameter("mobile", mobileNum);
        post.addParameter("message", message);
        post.addParameter("accessCode", "zhptopen");
        post.addParameter("appKey", "open");
        post.addParameter("smsLevel", "2");//等级3给验证码使用，异网手机号不一定可以收到告警消息
        httpClient.getParams().setContentCharset("utf-8");
        httpClient.getParams().setConnectionManagerTimeout(600000);
        httpClient.getParams().setSoTimeout(600000);
        httpClient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(600000);
        int retryCount = 0;
        httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(retryCount, false));
        int statusCode;
        try {
            StringBuilder sb = new StringBuilder();
            statusCode = httpClient.executeMethod(post);
            if (statusCode == HttpStatus.SC_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        post.getResponseBodyAsStream()));
                String line = null;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                log.info("SendInfoUtils.sendMessage:发送成功"+sb.toString()+"手机号: "+mobileNum+"下发内容: "+message);

                br.close();
            }
        } catch (IOException e) {
            log.info("SendInfoUtils.sendMessage:发送失败"+"IOException: "+e.getMessage()+"手机号: "+mobileNum+"下发内容"+message);
            e.printStackTrace();
        }
    }

    /**
     *
     * @Title: sendWechatNotice
     * @Description:微信推送
     * @author：tangy@corp.21cn.com
     * @param @param toUser
     * @param @param content    设定文件
     * @return void    返回类型
     * @throws
     */
    public static void sendWechatNotice(String toUser,String content) {

        if(StringUtil.isEmpty(content) || StringUtil.isEmpty(toUser)){
            return;
        }
        AccessToken accessToken = null;
        try {
            accessToken = WechatCorpApi.getAccessToken(corpId, secret);
        } catch (Exception e) {
            return;
        }
        if(accessToken == null){
            return;
        }
        String appId = "1"; //微信企业号应用 ID
        String partyId = "1";//微信企业号部门 ID
        String tag = "1";//告警推送小组标签
        if("null".equals(appId)){
            appId = null;
        }
        if("null".equals(partyId)){
            partyId = null;
        }
        if("null".equals(tag)){
            tag = null;
        }
        if("null".equals(toUser)){
            toUser = null;
        }
        try {
            WechatCorpApi.sendTextMsg(accessToken, appId, toUser, partyId, tag, content);
        } catch (HttpException e) {
            return;
        }
    }

    /**
     *
     * @Title: sendWechatNotifyByTag
     * @Description: 根据标签组推送消息
     * @param @param tagId
     * @param @param toUser
     * @param @param msgContent    设定文件
     * @return void    返回类型
     * @throws
     */
    public static void sendWechatNotifyByTag(String tagId,String toUser,String msgContent){
        AccessToken token = getWechatToken();

        if(StringUtil.isEmpty(msgContent) || StringUtil.isEmpty(toUser)
                || token == null || StringUtil.isEmpty(tagId)){
            return;
        }
        String appId = "1"; //微信企业号应用 ID
        String partyId = "1";//微信企业号部门 ID
        try {
            WechatCorpApi.sendTextMsg(token, appId, toUser, partyId, tagId, msgContent);
        } catch (HttpException e) {
            return;
        }
    }

    private static AccessToken getWechatToken() {

        AccessToken accessToken = null;
        try {
            accessToken = WechatCorpApi.getAccessToken(corpId, secret);
        } catch (Exception e) {
            return null;
        }

        return accessToken;
    }

    /**
     *
     * @Title: sendEmail
     * @Description:多个邮件账号以 逗号（，分割 如 zhangs,lis,wangw）不需要邮箱后缀
     * @param @param emailAccount
     * @param @param sub
     * @param @param content    设定文件
     * @return void    返回类型
     * @throws
     */
    public static void sendEmail(String emailAccount, String sub,String content){
        HtmlEmail email = new HtmlEmail();
        email.setHostName(mailHostName);
        email.setAuthenticator(new DefaultAuthenticator(mailAccount,mailPassword));
        try {
            email.setCharset("GB2312");
            email.setFrom(mailAccount, "订单监控");
            String[] destMails = {};
            if(emailAccount != null){
                destMails = emailAccount.split(",");
            }

            for(String destMail: destMails){
                destMail = destMail+"@corp.21cn.com";
                email.addTo(destMail);
            }
            email.setSubject(sub);
            email.setHtmlMsg(content);
            email.send();
            log.info("发送邮件成功"+sub+"下发邮箱: "+emailAccount+"邮件内容"+content);
        } catch (EmailException e) {
            log.info("发送邮件失败, EmailException:"+e.getMessage()+sub+" "+"下发邮箱: "+emailAccount+"邮件内容"+content);
            e.printStackTrace();
        }
    }

    private static String toLog(String content) {
        return content.replaceAll("\\n", " ");
    }

    public static void main(String[] args) {
        //sendWechatNotifyByTag("2","@all","test");
        sendEmail("tangy", "测试一下", "just a test");
    }
}
