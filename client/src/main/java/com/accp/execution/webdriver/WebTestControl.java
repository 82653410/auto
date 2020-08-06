package com.accp.execution.webdriver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.accp.execution.httpinterface.TestControl;
import com.accp.execution.webdriver.ex.WebCaseExecution;
import com.accp.remote.api.GetServerApi;
import com.accp.remote.api.serverOperation;
import com.accp.remote.entity.ProjectCase;
import com.accp.remote.entity.ProjectCaseParams;
import com.accp.remote.entity.TaskExecute;
import com.accp.tool.jenkins.BuildingInitialization;
import com.accp.tool.mail.HtmlMail;
import com.accp.tool.mail.MailSendInitialization;
import com.accp.tool.shell.RestartServerInitialization;
import com.accp.utils.LogUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.offbytwo.jenkins.model.BuildResult;

import com.accp.remote.entity.ProjectCaseSteps;
import com.accp.remote.entity.TaskScheduling;

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
public class WebTestControl {

	/**
	 * ����̨ģʽ���ȼƻ�ִ������
	 * @param planname �ƻ�����
	 */
	public static void manualExecutionPlan(String planname) {
		// ������־�����ݿ�
		serverOperation.exetype = 1;
		String taskid = "888888";
		WebDriver wd = null;
		try {
			wd = WebDriverInitialization.setWebDriverForLocal();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			LogUtil.APP.error("��ʼ��WebDriver�����쳣��",e);
		}
		serverOperation caselog = new serverOperation();
		List<ProjectCase> testCases = GetServerApi.getCasesbyplanname(planname);
		List<ProjectCaseParams> pcplist = new ArrayList<>();
		if (testCases.size() != 0) {
			pcplist = GetServerApi.cgetParamsByProjectid(String.valueOf(testCases.get(0).getProjectId()));
		}
		LogUtil.APP.info("��ǰ�ƻ��ж�ȡ����������{}����",testCases.size());
		int i = 0;
		for (ProjectCase testcase : testCases) {
			List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
			if (steps.size() == 0) {
				continue;
			}
			i++;
			LogUtil.APP.info("��ʼִ�е�{}������:��{}��......",i,testcase.getCaseSign());
			try {
				WebCaseExecution.caseExcution(testcase, steps, taskid, wd, caselog, pcplist);
			} catch (Exception e) {
				LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
			}
			LogUtil.APP.info("��ǰ����:��{}��ִ�����......������һ��",testcase.getCaseSign());
		}
		LogUtil.APP.info("��ǰ��Ŀ���Լƻ��е������Ѿ�ȫ��ִ�����...");
		// �ر������
		assert wd != null;
		wd.quit();
	}

	public static void taskExecutionPlan(TaskExecute task) {
		// ��¼��־�����ݿ�
		serverOperation.exetype = 0;
		String taskid = task.getTaskId().toString();
		TestControl.TASKID = taskid;
		String restartstatus = RestartServerInitialization.restartServerRun(taskid);
		BuildResult buildResult = BuildingInitialization.buildingRun(taskid);
		List<ProjectCaseParams> pcplist = GetServerApi.cgetParamsByProjectid(task.getProjectId().toString());
		TaskScheduling taskScheduling = GetServerApi.cGetTaskSchedulingByTaskId(task.getTaskId());
		String projectname = taskScheduling.getProject().getProjectName();
		task = GetServerApi.cgetTaskbyid(Integer.parseInt(taskid));
		String jobname = taskScheduling.getSchedulingName();
		int drivertype = serverOperation.querydrivertype(taskid);
		int[] tastcount;
		// �ж��Ƿ�Ҫ�Զ�����TOMCAT
		if (restartstatus.contains("Status:true")) {
			// �ж��Ƿ񹹽��Ƿ�ɹ�
			if (BuildResult.SUCCESS.equals(buildResult)) {
				WebDriver wd = null;
				try {
					wd = WebDriverInitialization.setWebDriverForTask(drivertype);
				} catch (WebDriverException e1) {
					LogUtil.APP.error("��ʼ��WebDriver���� WebDriverException��", e1);
				} catch (IOException e2) {
					LogUtil.APP.error("��ʼ��WebDriver���� IOException��", e2);
				}
				serverOperation caselog = new serverOperation();

				List<ProjectCase> cases = GetServerApi.getCasesbyplanId(taskScheduling.getPlanId());
				LogUtil.APP.info("��ǰ�������� {} �й��С�{}��������������...",task.getTaskName(),cases.size());
				serverOperation.updateTaskExecuteStatusIng(taskid, cases.size());
				int i = 0;
				for (ProjectCase testcase : cases) {
					i++;
					LogUtil.APP.info("��ʼִ�е�ǰ�������� {} �ĵڡ�{}������������:��{}��......",task.getTaskName(),i,testcase.getCaseSign());
					List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
					if (steps.size() == 0) {
						continue;
					}
					try {
						// ���뿪ʼִ�е�����
						caselog.insertTaskCaseExecute(taskid, taskScheduling.getProjectId(),testcase.getCaseId(),testcase.getCaseSign(), testcase.getCaseName(), 4);
						WebCaseExecution.caseExcution(testcase, steps, taskid, wd, caselog, pcplist);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
					}
					LogUtil.APP.info("��ǰ����:��{}��ִ�����......������һ��",testcase.getCaseSign());
				}
				tastcount = serverOperation.updateTaskExecuteData(taskid, cases.size(),2);

				String testtime = serverOperation.getTestTime(taskid);
				LogUtil.APP.info("��ǰ��Ŀ��{}�����Լƻ��е������Ѿ�ȫ��ִ�����...",projectname);
				MailSendInitialization.sendMailInitialization(HtmlMail.htmlSubjectFormat(jobname),
						HtmlMail.htmlContentFormat(tastcount, taskid, buildResult.toString(), restartstatus, testtime, jobname),
						taskid, taskScheduling, tastcount);
				// �ر������
				assert wd != null;
				wd.quit();
			} else {
				LogUtil.APP.warn("��Ŀ����ʧ�ܣ��Զ��������Զ��˳�����ǰ��JENKINS�м����Ŀ���������");
				MailSendInitialization.sendMailInitialization(jobname, "������Ŀ������ʧ�ܣ��Զ��������Զ��˳�����ǰȥJENKINS�鿴���������", taskid,
						taskScheduling, null);
			}
		} else {
			LogUtil.APP.warn("��ĿTOMCAT����ʧ�ܣ��Զ��������Զ��˳���������ĿTOMCAT���������");
			MailSendInitialization.sendMailInitialization(jobname, "��ĿTOMCAT����ʧ�ܣ��Զ��������Զ��˳���������ĿTOMCAT���������", taskid,
					taskScheduling, null);
		}
	}

}
