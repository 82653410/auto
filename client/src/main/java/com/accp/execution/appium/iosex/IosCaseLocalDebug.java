package com.accp.execution.appium.iosex;

import com.accp.remote.api.GetServerApi;
import com.accp.remote.api.serverOperation;
import com.accp.remote.entity.ProjectCase;
import com.accp.remote.entity.ProjectCaseParams;
import com.accp.utils.LogUtil;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import com.accp.remote.entity.ProjectCaseSteps;

import java.util.List;

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
public class IosCaseLocalDebug {

	public static void oneCasedebug(IOSDriver<IOSElement> iosdriver, String testCaseExternalId) {
		// ����¼��־�����ݿ�
		serverOperation.exetype = 1;
		serverOperation caselog = new serverOperation();

		try {
			ProjectCase testcase = GetServerApi.cgetCaseBysign(testCaseExternalId);
			List<ProjectCaseParams> pcplist = GetServerApi
					.cgetParamsByProjectid(String.valueOf(testcase.getProjectId()));
			LogUtil.APP.info("��ʼִ����������{}��......",testCaseExternalId);
			List<ProjectCaseSteps> steps = GetServerApi.getStepsbycaseid(testcase.getCaseId());
			IosCaseExecution.caseExcution(testcase, steps, "888888", iosdriver, caselog, pcplist);

			LogUtil.APP.info("��ǰ��������{}��ִ�����......������һ��",testcase.getCaseSign());
		} catch (Exception e) {
			LogUtil.APP.error("�û�ִ�й������׳��쳣��", e);
		}
	}

	/**
	 * �����������������е���
	 * @param iosdriver IOS����
	 * @param projectname ��Ŀ����
	 * @param addtestcase �������
	 */
	public static void moreCaseDebug(IOSDriver<IOSElement> iosdriver, String projectname,
			List<String> addtestcase) {
		System.out.println("��ǰ���������ܹ���"+addtestcase.size());
		for(String testCaseExternalId:addtestcase) {
			try {
				LogUtil.APP
						.info("��ʼ���÷�������Ŀ����{}��������ţ�{}",projectname,testCaseExternalId);
				oneCasedebug(iosdriver, testCaseExternalId);
			} catch (Exception e) {
				LogUtil.APP.error("���������Թ������׳��쳣��", e);
			}
		}
		// �ر�APP�Լ�appium�Ự
		iosdriver.closeApp();
	}

}
