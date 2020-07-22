package com.accp.project.testmanagmt.projectCase.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accp.common.support.Convert;
import com.accp.project.testmanagmt.projectCase.domain.ProjectCaseDebug;
import com.accp.project.testmanagmt.projectCase.mapper.ProjectCaseDebugMapper;

/**
 * 用例调试日志记录 服务层实现
 * 
 * uckyframe
 *
 */
@Service
public class ProjectCaseDebugServiceImpl implements IProjectCaseDebugService 
{
	@Autowired
	private ProjectCaseDebugMapper projectCaseDebugMapper;

	/**
     * 查询用例调试日志记录信息
     * 
     * @param debugId 用例调试日志记录ID
     * @return 用例调试日志记录信息
     */
    @Override
	public ProjectCaseDebug selectProjectCaseDebugById(Integer debugId)
	{
	    return projectCaseDebugMapper.selectProjectCaseDebugById(debugId);
	}
	
	/**
     * 查询用例调试日志记录列表
     * 
     * @param projectCaseDebug 用例调试日志记录信息
     * @return 用例调试日志记录集合
     */
	@Override
	public List<ProjectCaseDebug> selectProjectCaseDebugList(ProjectCaseDebug projectCaseDebug)
	{
	    return projectCaseDebugMapper.selectProjectCaseDebugList(projectCaseDebug);
	}
	
    /**
     * 新增用例调试日志记录
     * 
     * @param projectCaseDebug 用例调试日志记录信息
     * @return 结果
     */
	@Override
	public int insertProjectCaseDebug(ProjectCaseDebug projectCaseDebug)
	{
	    return projectCaseDebugMapper.insertProjectCaseDebug(projectCaseDebug);
	}
	
	/**
     * 修改用例调试日志记录
     * 
     * @param projectCaseDebug 用例调试日志记录信息
     * @return 结果
     */
	@Override
	public int updateProjectCaseDebug(ProjectCaseDebug projectCaseDebug)
	{
	    return projectCaseDebugMapper.updateProjectCaseDebug(projectCaseDebug);
	}

	/**
     * 删除用例调试日志记录对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteProjectCaseDebugByIds(String ids)
	{
		return projectCaseDebugMapper.deleteProjectCaseDebugByIds(Convert.toStrArray(ids));
	}
	
	/**
	 * 根据id,useid,caseid删除调试日志
	 * @param projectCaseDebug 用例调试对象
	 *
	 *
	 */
	public int deleteProjectCaseDebugById(ProjectCaseDebug projectCaseDebug){
		return projectCaseDebugMapper.deleteProjectCaseDebugById(projectCaseDebug);
	}
}
