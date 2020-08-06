package com.accp.tool.mail;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.accp.utils.LogUtil;
import com.accp.utils.config.SysConfig;
import com.accp.utils.httputils.HttpClientTools;
import com.accp.remote.entity.ProjectProtocolTemplate;

/**
 * 
 * 
 * 
 *
 * 
 *
 *
 *
 */
public class HtmlMail {
    static FreemarkerEmailTemplate fet = new FreemarkerEmailTemplate();

    public static String htmlContentFormat(int[] taskcount, String taskid, String buildstatus, String restartstatus, String time, String jobname) {
        Map<Object, Object> parameters = new HashMap<>(0);
        parameters.put("buildstatus", buildstatus);
        parameters.put("restartstatus", restartstatus);
        parameters.put("casecount", taskcount[0]);
        parameters.put("casesuc", taskcount[1]);
        parameters.put("casefail", taskcount[2]);
        parameters.put("caselock", taskcount[3]);
        parameters.put("caseunex", taskcount[4]);
        parameters.put("time", time);
        parameters.put("taskid", taskid);
        parameters.put("jobname", jobname);
        try {
            Map<String, String> headmsg = new HashMap<>(0);
            Properties properties = SysConfig.getConfiguration();
            if ("true".equals(properties.getProperty("task.push.switch").toLowerCase())) {
                LogUtil.APP.info("开始向第三方平台推送任务执行情况...");
                Map<String, Object> pushparameters = new HashMap<>(0);
                pushparameters.put("buildstatus", buildstatus);
                pushparameters.put("restartstatus", restartstatus);
                pushparameters.put("casecount", taskcount[0]);
                pushparameters.put("casesuc", taskcount[1]);
                pushparameters.put("casefail", taskcount[2]);
                pushparameters.put("caselock", taskcount[3]);
                pushparameters.put("caseunex", taskcount[4]);
                pushparameters.put("time", time);
                pushparameters.put("taskid", taskid);
                pushparameters.put("jobname", jobname);

                String pushurl = properties.getProperty("task.push.url");
                ProjectProtocolTemplate ppt=new ProjectProtocolTemplate();
                ppt.setEncoding("utf-8");
                ppt.setTimeout(60);
                HttpClientTools hct = new HttpClientTools();
                hct.httpClientPostJson(pushurl, pushparameters, headmsg,ppt);
            }
        } catch (Exception e) {
            LogUtil.APP.error("向第三方平台推送任务执行情况出现异常，请检查！", e);
            return fet.getText("task-body", parameters);
        }
        return fet.getText("task-body", parameters);
    }

    public static String htmlSubjectFormat(String jobname) {
        Map<Object, Object> parameters = new HashMap<>(0);
        parameters.put("jobname", jobname);
        return fet.getText("task-title", parameters);
    }

}
