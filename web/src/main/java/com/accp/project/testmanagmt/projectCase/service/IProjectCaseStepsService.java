package com.accp.project.testmanagmt.projectCase.service;

import java.util.List;

import com.accp.project.testmanagmt.projectCase.domain.ProjectCaseSteps;

/**
 * 测试用例步骤管理 服务层
 * 
 * uckyframe
 *
 */
public interface IProjectCaseStepsService 
{
	/**
     * 查询测试用例步骤管理信息
     * 
     * @param stepId 测试用例步骤管理ID
     * @return 测试用例步骤管理信息
     */
	ProjectCaseSteps selectProjectCaseStepsById(Integer stepId);
	
	/**
	 * 通过用例ID以及步骤序号获取步骤实体
	 * @param projectCaseSteps 用例步骤对象
	 *
	 *
	 */
	ProjectCaseSteps selectProjectCaseStepsByCaseIdAndStepNum(ProjectCaseSteps projectCaseSteps);
	
	/**
     * 查询测试用例步骤管理列表
     * 
     * @param projectCaseSteps 测试用例步骤管理信息
     * @return 测试用例步骤管理集合
     */
	List<ProjectCaseSteps> selectProjectCaseStepsList(ProjectCaseSteps projectCaseSteps);
	
	/**
     * 新增测试用例步骤管理
     * 
     * @param projectCaseSteps 测试用例步骤管理信息
     * @return 结果
     */
	int insertProjectCaseSteps(ProjectCaseSteps projectCaseSteps);
	
	/**
     * 修改测试用例步骤管理
     * 
     * @param projectCaseSteps 测试用例步骤管理信息
     * @return 结果
     */
	int updateProjectCaseSteps(ProjectCaseSteps projectCaseSteps);
		
	/**
	 * 删除单条用例中的所有步骤
	 * @param listSteps 步骤集合
	 *
	 *
	 */
	int deleteProjectCaseStepsByIds(List<ProjectCaseSteps> listSteps);
	
	String importProjectCaseSteps(List<ProjectCaseSteps> stepList);
	
}
