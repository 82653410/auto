package com.accp.common.utils.client;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.accp.common.constant.ClientConstants;
import com.accp.common.utils.StringUtils;
import com.accp.project.testexecution.taskExecute.domain.TaskExecute;
import com.accp.project.testexecution.taskExecute.service.ITaskExecuteService;
import com.accp.project.testexecution.taskScheduling.domain.TaskScheduling;
import com.accp.project.testexecution.taskScheduling.service.ITaskSchedulingService;

/**
 * 远程唤起客户端工具类
 * 
 * 
 * 
 * 
 *
 * 
 */
public class ClientRun {
	
	@Resource
	private ITaskSchedulingService taskSchedulingService;
	
	@Resource
	private ITaskExecuteService taskExecuteService;
	
	public static ClientRun clientRun;

	@PostConstruct
	public void init() {
		clientRun = this;
	}

	public String toRunTask(String schedulingId,int autoType){
		String result="启动失败！";
		TaskScheduling taskScheduling = taskSchedulingService.selectTaskSchedulingById(Integer.valueOf(schedulingId));
		
		TaskExecute taskExecute = new TaskExecute();
		String taskName = "【"+taskScheduling.getSchedulingName()+ "】_"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date().getTime());
		taskExecute.setTaskName(taskName);
		taskExecute.setSchedulingId(taskScheduling.getSchedulingId());
		taskExecute.setProjectId(taskScheduling.getProjectId());
		taskExecute.setTaskStatus(0);
		
		taskExecuteService.insertTaskExecute(taskExecute);
		try{			
    		RunTaskEntity runTaskEntity = new RunTaskEntity();
    		runTaskEntity.setSchedulingName(taskScheduling.getSchedulingName());
    		runTaskEntity.setTaskId(taskExecute.getTaskId().toString());   		
    		if(StringUtils.isEmpty(taskScheduling.getClientDriverPath())){
    			runTaskEntity.setLoadPath("/TestDriven");
    		}else{
    			runTaskEntity.setLoadPath(taskScheduling.getClientDriverPath());
    		}

			result=HttpRequest.httpClientPost("http://"+taskScheduling.getClient().getClientIp()+":"+ClientConstants.CLIENT_MONITOR_PORT+"/runtask", taskScheduling.getClient(),JSONObject.toJSONString(runTaskEntity),3000);
    		System.out.println(result);
    		return result;
		}catch (Exception e) {
			//执行失败时,写入任务表记录为执行失败
			taskExecute.setTaskStatus(0);
			taskExecuteService.updateTaskExecute(taskExecute);
			return result;
		}
			
	}

}
