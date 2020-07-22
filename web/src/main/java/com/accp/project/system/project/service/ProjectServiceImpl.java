package com.accp.project.system.project.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accp.common.constant.ProjectConstants;
import com.accp.common.exception.BusinessException;
import com.accp.common.support.Convert;
import com.accp.common.utils.StringUtils;
import com.accp.common.utils.security.PermissionUtils;
import com.accp.common.utils.security.ShiroUtils;
import com.accp.project.system.clazz.mapper.ClazzMapper;
import com.accp.project.system.client.domain.ClientProject;
import com.accp.project.system.client.mapper.ClientProjectMapper;
import com.accp.project.system.project.domain.Project;
import com.accp.project.system.project.mapper.ProjectMapper;
import com.accp.project.system.projectclazz.domain.ProjectClazz;
import com.accp.project.system.projectclazz.mapper.ProjectClazzMapper;
import com.accp.project.system.role.domain.RoleProject;
import com.accp.project.system.role.mapper.RoleProjectMapper;
import com.accp.project.testexecution.taskCaseExecute.mapper.TaskCaseExecuteMapper;
import com.accp.project.testexecution.taskExecute.mapper.TaskExecuteMapper;
import com.accp.project.testexecution.taskScheduling.mapper.TaskSchedulingMapper;
import com.accp.project.testmanagmt.projectCase.mapper.ProjectCaseMapper;
import com.accp.project.testmanagmt.projectCaseModule.domain.ProjectCaseModule;
import com.accp.project.testmanagmt.projectCaseModule.mapper.ProjectCaseModuleMapper;

/**
 * 测试项目管理 服务层实现
 * 
 * uckyframe
 *
 */
@Service
public class ProjectServiceImpl implements IProjectService 
{
	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private ProjectCaseModuleMapper projectCaseModuleMapper;
	
	@Autowired
	private ProjectCaseMapper projectCaseMapper;
	
	@Autowired
	private ClientProjectMapper clientProjectMapper;
	
	@Autowired
	private RoleProjectMapper roleProjectMapper;
	
	@Autowired
	private TaskSchedulingMapper taskSchedulingMapper;
	
	@Autowired
	private TaskExecuteMapper taskExecuteMapper;

	@Autowired
	private TaskCaseExecuteMapper taskCaseExecuteMapper;

	@Autowired
	private ClazzMapper clazzMapper;
	
	@Autowired
	private ProjectClazzMapper projectClazzMapper;

	/**
     * 查询测试项目管理信息
     * 
     * @param projectId 测试项目管理ID
     * @return 测试项目管理信息
     */
    @Override
	public Project selectProjectById(int projectId)
	{
	    return projectMapper.selectProjectById(projectId);
	}
	
	/**
     * 查询测试项目管理列表
     * 
     * @param project 测试项目管理信息
     * @return 测试项目管理集合
     */
	@Override
	public List<Project> selectProjectList(Project project)
	{
	    return projectMapper.selectProjectList(project);
	}
	
    /**
     * 查询所有项目管理列表
     */
    @Override
    public List<Project> selectProjectAll(Integer projectId)
    {
    	List<Project> projectList= selectProjectList(new Project());
    	if(projectId!=0){
    		for(Project p:projectList){
    			if(p.getProjectId().equals(projectId)){
    				p.setFlag(true);
    			}   			
    		}   		
    	}
        return projectList;
    }
    
    /**
     * 新增测试项目管理
     * 
     * @param project 测试项目管理信息
     * @return 结果
     */
	@Override
	public int insertProject(Project project,String clazzId)
	{
		projectMapper.insertProject(project);
		ProjectCaseModule projectCaseModule=new ProjectCaseModule();
		projectCaseModule.setModuleName(project.getProjectName());
		projectCaseModule.setProjectId(project.getProjectId());
		projectCaseModule.setCreateBy(ShiroUtils.getLoginName());
		projectCaseModule.setCreateTime(new Date());
        projectCaseModule.setUpdateBy(ShiroUtils.getLoginName());
        projectCaseModule.setUpdateTime(new Date());
		projectCaseModule.setAncestors("0");
		projectCaseModule.setOrderNum(projectCaseModuleMapper.selectProjectCaseModuleMaxOrderNumByParentId(0)+1);
		projectCaseModule.setRemark("项目初始化模块");
	    projectCaseModuleMapper.insertProjectCaseModule(projectCaseModule);
	    projectClazzMapper.deleteByProjectId(project.getProjectId());
		List<Integer> list = Arrays.asList(clazzId.split(",")).stream().map(s -> Integer.parseInt(s.trim()))
				.collect(Collectors.toList());
		
		projectClazzMapper.inserts(project.getProjectId(),list);
	    return project.getProjectId();
	}
	
	/**
     * 修改测试项目管理
     * 
     * @param project 测试项目管理信息
     * @return 结果
     */
	@Override
	public int updateProject(Project project,String clazzId)
	{
		ProjectCaseModule projectCaseModule = projectCaseModuleMapper.selectProjectCaseModuleParentZeroByProjectId(project.getProjectId());
		projectCaseModule.setModuleName(project.getProjectName());
		projectCaseModuleMapper.updateProjectCaseModule(projectCaseModule);
		projectClazzMapper.deleteByProjectId(project.getProjectId());
		List<Integer> list = Arrays.asList(clazzId.split(",")).stream().map(s -> Integer.parseInt(s.trim()))
				.collect(Collectors.toList());
		
		projectClazzMapper.inserts(project.getProjectId(),list);
	    return projectMapper.updateProject(project);
	}

	/**
     * 删除测试项目管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteProjectByIds(String ids) throws BusinessException
	{
		String[] projectIds=Convert.toStrArray(ids);
		for(String projectIdStr:projectIds){
			int projectId=Integer.parseInt(projectIdStr);
			if(projectCaseModuleMapper.selectProjectCaseModuleCountByProjectId(projectId)>0){
				throw new BusinessException(String.format("【%1$s】已绑定子用例模块,不能删除", projectMapper.selectProjectById(projectId).getProjectName()));
			}
			if(clientProjectMapper.selectClientProjectCountByProjectId(projectId)>0){
				throw new BusinessException(String.format("【%1$s】已绑定客户端,不能删除", projectMapper.selectProjectById(projectId).getProjectName()));
			}
			if(projectCaseMapper.selectProjectCaseCountByProjectId(projectId)>0){
				throw new BusinessException(String.format("【%1$s】已绑定测试用例,不能删除", projectMapper.selectProjectById(projectId).getProjectName()));
			}
			if(taskSchedulingMapper.selectTaskSchedulingCountByProjectId(projectId)>0){
				throw new BusinessException(String.format("【%1$s】已绑定调度,不能删除", projectMapper.selectProjectById(projectId).getProjectName()));
			}
			if(taskExecuteMapper.selectTaskExecuteCountByProjectId(projectId)>0){
				throw new BusinessException(String.format("【%1$s】已生成执行任务,不能删除", projectMapper.selectProjectById(projectId).getProjectName()));
			}
			if(taskCaseExecuteMapper.selectTaskCaseExecuteCountByProjectId(projectId)>0){
				throw new BusinessException(String.format("【%1$s】已生成执行用例明细,不能删除", projectMapper.selectProjectById(projectId).getProjectName()));
			}
			if(!PermissionUtils.isProjectPermsPassByProjectId(projectId)){	
				throw new BusinessException(String.format("没有项目【%1$s】删除权限", projectMapper.selectProjectById(projectId).getProjectName()));
			}			
			List<Project> listProject = selectProjectAll(0);
			if(listProject.size()<=1){
				throw new BusinessException(String.format("【%1$s】是系统中唯一项目,不能删除", projectMapper.selectProjectById(projectId).getProjectName()));
			}
		}
		projectCaseModuleMapper.deleteProjectCaseModuleByProjectIds(projectIds);
		return projectMapper.deleteProjectByIds(projectIds);
	}
	
    /**
     * 根据客户端ID查询所有项目列表(打标记)
     * @param clientId 客户端ID
     * @return 返回项目集合
     *
     *
     */
    @Override
    public List<Project> selectProjectsByClientId(int clientId)
    {
        List<ClientProject> clientProjects = clientProjectMapper.selectClientProjectsById(clientId);
        List<Project> projects = selectProjectAll(0);
        for (Project project : projects)
        {
            for (ClientProject cp : clientProjects)
            {
                if (cp.getProjectId() == project.getProjectId().longValue())
                {
                	project.setFlag(true);
                    break;
                }
            }
        }
        return projects;
    }
    
    /**
     * 根据角色ID查询所有项目列表(打标记)
     * @param roleId 角色ID
     * @return 返回项目集合
     *
     *
     */
    @Override
    public List<Project> selectProjectsByRoleId(int roleId)
    {
        List<RoleProject> roleProjects = roleProjectMapper.selectRoleProjectsById(roleId);
        List<Project> projects = selectProjectAll(0);
        for (Project project : projects)
        {
            for (RoleProject rp : roleProjects)
            {
                if (rp.getProjectId() == project.getProjectId().longValue())
                {
                	project.setFlag(true);
                    break;
                }
            }
        }
        return projects;
    }
    
    /**
     * 校验项目名称唯一性
     */
    @Override
    public String checkProjectNameUnique(Project project)
    {
        long projectId = StringUtils.isNull(project.getProjectId()) ? -1L : project.getProjectId();
        Project info = projectMapper.checkProjectNameUnique(project.getProjectName());
        if (StringUtils.isNotNull(info) && info.getProjectId().longValue() != projectId)
        {
            return ProjectConstants.PROJECT_NAME_NOT_UNIQUE;
        }
        return ProjectConstants.PROJECT_NAME_UNIQUE;
    }
    
    /**
     * 校验项目标识唯一性
     */
    @Override
    public String checkProjectSignUnique(Project project)
    {
        long projectId = StringUtils.isNull(project.getProjectId()) ? -1L : project.getProjectId();
        Project info = projectMapper.checkProjectSignUnique(project.getProjectSign());
        if (StringUtils.isNotNull(info) && info.getProjectId().longValue() != projectId)
        {
            return ProjectConstants.PROJECT_SIGN_NOT_UNIQUE;
        }
        return ProjectConstants.PROJECT_SIGN_UNIQUE;
    }
    
    /**
     * 检查班级id是否存在
     */
    @Override
    public boolean checkClazzIdPresence(String clazzId) {
    	int num = appearNumber(clazzId, ",")+1;
    	List<Integer> list = Arrays.asList(clazzId.split(",")).stream().map(s -> Integer.parseInt(s.trim()))
				.collect(Collectors.toList());
    	int count = clazzMapper.checkClazzIdPresence(list);
    	if(num==count) {
    		return true;
    	}else {
    		return false;
    	}
    }
    
    /**
     * 获取指定字符串出现的次数
     * 
     * @param srcText 源字符串
     * @param findText 要查找的字符串
     * @return
     */
    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        Pattern p = Pattern.compile(findText);
        Matcher m = p.matcher(srcText);
        while (m.find()) {
            count++;
        }
        return count;
    }
}
