package com.accp.tool.jenkins;

import com.offbytwo.jenkins.model.BuildResult;

/**
 * 
 * 
 * 
 * 
 * 
 *
 *
 */
public class ThreadForBuildJob extends Thread{
	
	private String jobName;
	
	public ThreadForBuildJob(String jobName){
		this.jobName = jobName;
	}
	
	@Override
	public void run(){
		JobBuildApi jobBuildApi=new JobBuildApi();
		BuildResult buildResult = jobBuildApi.buildAndGetResultForJobName(jobName);
		if(BuildResult.SUCCESS.equals(buildResult)){
			BuildingInitialization.THREAD_SUCCOUNT++;
		}
		BuildingInitialization.THREAD_COUNT--;        //多线程计数--，用于检测线程是否全部执行完
	}

}
