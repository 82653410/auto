package com.accp.project.testmanagmt.projectCase.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accp.common.exception.BusinessException;
import com.accp.common.utils.StringUtils;
import com.accp.common.utils.security.ShiroUtils;
import com.accp.project.testmanagmt.projectCase.domain.ProjectCase;
import com.accp.project.testmanagmt.projectCase.domain.ProjectCaseSteps;
import com.accp.project.testmanagmt.projectCase.mapper.ProjectCaseMapper;
import com.accp.project.testmanagmt.projectCase.mapper.ProjectCaseStepsMapper;

/**
 * 测试用例步骤管理 服务层实现
 * 
 * uckyframe
 *
 */
@Service
public class ProjectCaseStepsServiceImpl implements IProjectCaseStepsService 
{
	@Autowired
	private ProjectCaseStepsMapper projectCaseStepsMapper;

	@Autowired
	private IProjectCaseService projectCaseService;
	

	@Autowired
	private ProjectCaseMapper projectCaseMapper;
	
	/**
     * 查询测试用例步骤管理信息
     * 
     * @param stepId 测试用例步骤管理ID
     * @return 测试用例步骤管理信息
     */
    @Override
	public ProjectCaseSteps selectProjectCaseStepsById(Integer stepId)
	{
	    return projectCaseStepsMapper.selectProjectCaseStepsById(stepId);
	}
	
	/**
	 * 通过用例ID以及步骤序号获取步骤实体
	 * @param projectCaseSteps 用例步骤对象
	 *
	 *
	 */
    @Override
	public ProjectCaseSteps selectProjectCaseStepsByCaseIdAndStepNum(ProjectCaseSteps projectCaseSteps)
	{
	    return projectCaseStepsMapper.selectProjectCaseStepsByCaseIdAndStepNum(projectCaseSteps);
	}
    
	/**
     * 查询测试用例步骤管理列表
     * 
     * @param projectCaseSteps 测试用例步骤管理信息
     * @return 测试用例步骤管理集合
     */
	@Override
	public List<ProjectCaseSteps> selectProjectCaseStepsList(ProjectCaseSteps projectCaseSteps)
	{
	    return projectCaseStepsMapper.selectProjectCaseStepsList(projectCaseSteps);
	}
	
    /**
     * 新增测试用例步骤管理
     * 
     * @param projectCaseSteps 测试用例步骤管理信息
     * @return 结果
     */
	@Override
	public int insertProjectCaseSteps(ProjectCaseSteps projectCaseSteps)
	{
		ProjectCase projectCase=projectCaseService.selectProjectCaseById(projectCaseSteps.getCaseId());
		projectCaseService.updateProjectCase(projectCase);
		
		projectCaseSteps.setCreateBy(ShiroUtils.getLoginName());
		projectCaseSteps.setCreateTime(new Date());
		projectCaseSteps.setUpdateBy(ShiroUtils.getLoginName());
		projectCaseSteps.setUpdateTime(new Date());
		
	    return projectCaseStepsMapper.insertProjectCaseSteps(projectCaseSteps);
	}
	
	/**
     * 修改测试用例步骤管理
     * 
     * @param projectCaseSteps 测试用例步骤管理信息
     * @return 结果
     */
	@Override
	public int updateProjectCaseSteps(ProjectCaseSteps projectCaseSteps)
	{
		projectCaseSteps.setUpdateBy(ShiroUtils.getLoginName());
		projectCaseSteps.setUpdateTime(new Date());
		
		ProjectCase projectCase=projectCaseService.selectProjectCaseById(projectCaseSteps.getCaseId());
		projectCaseService.updateProjectCase(projectCase);
		
	    return projectCaseStepsMapper.updateProjectCaseSteps(projectCaseSteps);
	}

	/**
     * 删除测试用例步骤管理对象
     * 
     * @param listSteps 步骤对象集合
     * @return 结果
     */
	@Override
	public int deleteProjectCaseStepsByIds(List<ProjectCaseSteps> listSteps)
	{
		int caseId=0;
		if(null!=listSteps&&listSteps.size()>0){
			if(null!=listSteps.get(0).getCaseId()){
				caseId=listSteps.get(0).getCaseId();
			}
		}
		return projectCaseStepsMapper.deleteProjectCaseStepsByCaseId(caseId);
	}
	
	@Override
	public String importProjectCaseSteps(List<ProjectCaseSteps> stepList) {
    	if (StringUtils.isNull(stepList) || stepList.size() == 0) {
			throw new BusinessException("导入用例不能为空！");
		}
    	int insertcount = 0;
		int updatecount = 0;
		int failcount = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();


		for (ProjectCaseSteps step : stepList) {
			try {
				String re = projectCaseMapper.selectCaseByProjectIdAndCaseName(step.getProjectId(), step.getCaseName());
				Integer ret=0;
				if(re!=null) {
					ret = Integer.parseInt(re);
				}
				//判断项目id和模块id是否存在
				if(ret<=0) {
					failcount++;
					failureMsg.append("<br/>" + "第").append(stepList.indexOf(step) + 2).append("行,项目ID对应用例名不正确！");
					
					
				}else {
					String st = projectCaseStepsMapper.selectStepIdByCaseIdAndSerialNumber(ret, step.getStepSerialNumber());
					Integer ret2=0;
					if(st!=null) {
						ret2 = Integer.parseInt(st);
					}
					if(ret2>0) {
						
						step.setUpdateBy(ShiroUtils.getLoginName());
						step.setUpdateTime(new Date());
						
						
					    projectCaseStepsMapper.updateProjectCaseSteps(step);
						
					    updatecount++;

					}else {
//						ProjectCase projectCase=projectCaseService.selectProjectCaseById(step.getCaseId());
//						projectCaseService.updateProjectCase(projectCase);
						step.setCaseId(ret2);
						step.setCreateBy(ShiroUtils.getLoginName());
						step.setCreateTime(new Date());
						step.setUpdateBy(ShiroUtils.getLoginName());
						step.setUpdateTime(new Date());
						
					    projectCaseStepsMapper.insertProjectCaseSteps(step);
						insertcount++;
					}
					
					
				}
				
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
				failcount++;
				String msg = "<br/>" + "第" +  (stepList.indexOf(step) + 2) + "行导入失败：";
				failureMsg.append(msg).append(e.getMessage());
			}
		}
		

		if ((insertcount + updatecount) == stepList.size()) {    //如果全部成功
			successMsg.append("<br/>").append(stepList.size()).append("行全部导入成功，");
			if (insertcount > 0 && updatecount > 0) {
				successMsg.append("插入数据").append(insertcount).append("行，更新数据").append(updatecount).append("行！");
			} else if (insertcount > 0) {
				successMsg.append("插入数据").append(insertcount).append("行！");
			} else {
				successMsg.append("更新数据").append(updatecount).append("行！");
			}
		} else if (failcount == stepList.size()) {  //如果全部失败
			failureMsg.insert(0, stepList.size() + "行全部导入失败，" + failureMsg);
			throw new BusinessException(failureMsg.toString());
		}
		else     //如果部分成功，部分失败
		{
			if (insertcount > 0 & updatecount > 0) {
				successMsg.append("成功导入").append(insertcount + updatecount).append("行，插入数据").append(insertcount).append("行，更新数据").append(updatecount).append("行！").append(failcount).append("行导入失败，").append(failureMsg);
			} else if (insertcount > 0) {
				successMsg.append("成功导入").append(insertcount + updatecount).append("行，插入数据").append(insertcount).append("行！").append(failcount).append("行导入失败，").append(failureMsg);
			} else {
				successMsg.append("成功导入").append(insertcount + updatecount).append("行，更新数据").append(updatecount).append("行！").append(failcount).append("行导入失败，").append(failureMsg);
			}
		}
		return successMsg.toString();
    }
	
}
