package com.accp.tool.jenkins;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.accp.remote.api.serverOperation;
import com.accp.utils.LogUtil;
import com.offbytwo.jenkins.model.BuildResult;

/**
 *
 *
 *
 *
 * 
 *
 * 
 *
 * 
 */
public class BuildingInitialization {
	
	protected static int THREAD_COUNT = 0;
	protected static int THREAD_SUCCOUNT = 0;

	public static BuildResult buildingRun(String tastid) {
		try {
			String[] jobName = serverOperation.getBuildName(tastid);

			if (jobName != null) {
				ThreadPoolExecutor	threadExecute	= new ThreadPoolExecutor(jobName.length, 10, 3, TimeUnit.SECONDS,
						new ArrayBlockingQueue<>(1000),
			            new ThreadPoolExecutor.CallerRunsPolicy());
				
				LogUtil.APP.info("准备将配置的测试项目进行构建！请稍等。。。。");
				for (String s : jobName) {
					BuildingInitialization.THREAD_COUNT++;   //多线程计数++，用于检测线程是否全部执行完
					threadExecute.execute(new ThreadForBuildJob(s));
				}
				
				//多线程计数，用于检测线程是否全部执行完
				int k=0;
				while(BuildingInitialization.THREAD_COUNT!=0){
					k++;
					//最长等待构建时间45分钟
					if(k>2700){
						break;
					}
					Thread.sleep(1000);
				}
				threadExecute.shutdown();
				
				if(jobName.length!=THREAD_SUCCOUNT){
					LogUtil.APP.info("待构建项目{}个，构建成功的项目{}个，有构建任务异常或失败状态，详情请查看构建日志...",jobName.length,THREAD_SUCCOUNT);
					return BuildResult.FAILURE;
				}else{
					LogUtil.APP.info("总共构建成功的项目{}个，全部构建成功，详情请查看构建日志...",THREAD_SUCCOUNT);
				}
				
			} else {
				LogUtil.APP.info("当前任务没有找到需要构建的项目！");
			}
		} catch (Exception e) {
			LogUtil.APP.error("项目构建过程中出现异常", e);
			return BuildResult.UNSTABLE;
		}
		return BuildResult.SUCCESS;

	}

}
