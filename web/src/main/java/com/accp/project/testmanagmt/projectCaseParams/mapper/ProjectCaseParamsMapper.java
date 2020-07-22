package com.accp.project.testmanagmt.projectCaseParams.mapper;

import java.util.List;

import com.accp.project.testmanagmt.projectCaseParams.domain.ProjectCaseParams;
import org.springframework.stereotype.Component;

/**
 * 用例公共参数 数据层
 * 
 * uckyframe
 *
 */
@Component
public interface ProjectCaseParamsMapper 
{
	/**
     * 查询用例公共参数信息
     * 
     * @param paramsId 用例公共参数ID
     * @return 用例公共参数信息
     */
	ProjectCaseParams selectProjectCaseParamsById(Integer paramsId);
	
	/**
     * 查询用例公共参数列表
     * 
     * @param projectCaseParams 用例公共参数信息
     * @return 用例公共参数集合
     */
	List<ProjectCaseParams> selectProjectCaseParamsList(ProjectCaseParams projectCaseParams);
	
	/**
	 * 根据项目ID查询项目下以及所有项目公共的参数
	 * @param projectId 项目ID
	 *
	 *
	 */
	List<ProjectCaseParams> selectProjectCaseParamsListByProjectId(Integer projectId);
	
	/**
     * 新增用例公共参数
     * 
     * @param projectCaseParams 用例公共参数信息
     * @return 结果
     */
	int insertProjectCaseParams(ProjectCaseParams projectCaseParams);
	
	/**
     * 修改用例公共参数
     * 
     * @param projectCaseParams 用例公共参数信息
     * @return 结果
     */
	int updateProjectCaseParams(ProjectCaseParams projectCaseParams);
	
	/**
     * 删除用例公共参数
     * 
     * @param paramsId 用例公共参数ID
     * @return 结果
     */
	int deleteProjectCaseParamsById(Integer paramsId);
	
	/**
     * 批量删除用例公共参数
     * 
     * @param paramsIds 需要删除的数据ID
     * @return 结果
     */
	int deleteProjectCaseParamsByIds(String[] paramsIds);
	
	/**
	 * 检查项目中参数名称的唯一性
	 * @param projectCaseParams 公共参数对象
	 *
	 *
	 */
	ProjectCaseParams checkProjectCaseParamsNameUnique(ProjectCaseParams projectCaseParams);
}