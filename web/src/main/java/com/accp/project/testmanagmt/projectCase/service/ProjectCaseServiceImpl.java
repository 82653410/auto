package com.accp.project.testmanagmt.projectCase.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accp.common.constant.ProjectCaseConstants;
import com.accp.common.exception.BusinessException;
import com.accp.common.support.Convert;
import com.accp.common.utils.StringUtils;
import com.accp.common.utils.security.PermissionUtils;
import com.accp.common.utils.security.ShiroUtils;
import com.accp.project.system.project.mapper.ProjectMapper;
import com.accp.project.system.student.domain.Student;
import com.accp.project.testmanagmt.projectCase.domain.ProjectCase;
import com.accp.project.testmanagmt.projectCase.mapper.ProjectCaseMapper;
import com.accp.project.testmanagmt.projectCase.mapper.ProjectCaseStepsMapper;
import com.accp.project.testmanagmt.projectCaseModule.domain.ProjectCaseModule;
import com.accp.project.testmanagmt.projectCaseModule.mapper.ProjectCaseModuleMapper;
import com.accp.project.testmanagmt.projectPlan.domain.ProjectPlanCase;
import com.accp.project.testmanagmt.projectPlan.mapper.ProjectPlanCaseMapper;

/**
 * 项目测试用例管理 服务层实现
 * 
 * uckyframe
 *
 */
@Service
public class ProjectCaseServiceImpl implements IProjectCaseService 
{
	@Autowired
	private ProjectCaseMapper projectCaseMapper;

	@Autowired
	private ProjectCaseStepsMapper projectCaseStepsMapper;
	@Autowired
	private ProjectCaseModuleMapper projectCaseModuleMapper;
	
	@Autowired
	private ProjectMapper projectMapper;
	
	@Autowired
	private ProjectPlanCaseMapper projectPlanCaseMapper;

	/**
     * 查询项目测试用例管理信息
     * 
     * @param caseId 项目测试用例管理ID
     * @return 项目测试用例管理信息
     */
    @Override
	public ProjectCase selectProjectCaseById(Integer caseId)
	{
	    return projectCaseMapper.selectProjectCaseById(caseId);
	}
    
	/**
	 * 根据用例编号查询实体
	 * @param caseSign 用例编号
	 *
	 *
	 */
    @Override
	public ProjectCase selectProjectCaseByCaseSign(String caseSign)
	{
	    return projectCaseMapper.selectProjectCaseByCaseSign(caseSign);
	}
	
	/**
     * 查询项目测试用例管理列表
     * 
     * @param projectCase 项目测试用例管理信息
     * @return 项目测试用例管理集合
     */
	@Override
	public List<ProjectCase> selectProjectCaseList(ProjectCase projectCase)
	{		
	    return projectCaseMapper.selectProjectCaseList(projectCase);
	}
	
	/**
	 * 根据项目ID查询用例集合
	 * @param projectId 项目ID
	 *
	 *
	 */
	@Override
	public List<ProjectCase> selectProjectCaseListByProjectId(Integer projectId)
	{
		ProjectCase projectCase = new ProjectCase();
		projectCase.setProjectId(projectId);
	    return projectCaseMapper.selectProjectCaseList(projectCase);
	}
	
	/**
	 * 根据计划ID查询用例列表
	 * @param projectCase 用例对象
	 *
	 *
	 */
	@Override
	public List<ProjectCase> selectProjectCaseListForPlan(ProjectCase projectCase)
	{	
		List<ProjectCase> projectCaseList = new ArrayList<>();
		if(StringUtils.isNotEmpty(projectCase.getPlanId())){
			List<ProjectPlanCase> projectPlanCaseList;
			ProjectPlanCase projectPlanCase = new ProjectPlanCase();
			projectPlanCase.setPlanId(projectCase.getPlanId());
			//查询计划内跟所有用例分两个语句
			if(projectCase.isFlag()){
				projectCaseList = projectCaseMapper.selectProjectCaseListForPlan(projectCase);
			}else{
				projectCaseList = projectCaseMapper.selectProjectCaseList(projectCase);
			}
			projectPlanCaseList = projectPlanCaseMapper.selectProjectPlanCaseList(projectPlanCase);

			//用例集合加入优先级以及标识
			for(ProjectCase pc:projectCaseList){
				pc.setPlanId(projectCase.getPlanId());
				for (ProjectPlanCase ppc:projectPlanCaseList) {
					if (pc.getCaseId().equals(ppc.getCaseId())) {
						pc.setFlag(true);
						pc.setPriority(ppc.getPriority());					
						pc.setPlanCaseId(ppc.getPlanCaseId());
						break;
					}
				}
			}
		}
		
	    return projectCaseList;
	}
	
    /**
     * 新增项目测试用例管理
     * 
     * @param projectCase 项目测试用例管理信息
     * @return 结果
     */
	@Override
	public int insertProjectCase(ProjectCase projectCase)
	{
		projectCase.setCreateBy(ShiroUtils.getLoginName());
		projectCase.setCreateTime(new Date());
		projectCase.setUpdateBy(ShiroUtils.getLoginName());
		projectCase.setUpdateTime(new Date());
		
		int caseSerialNumber=projectCaseMapper.selectMaxCaseSerialNumberByProjectId(projectCase.getProjectId())+1;
		projectCase.setCaseSerialNumber(caseSerialNumber);
		
		String caseSign=projectMapper.selectProjectById(projectCase.getProjectId()).getProjectSign()+"-"+caseSerialNumber;
		projectCase.setCaseSign(caseSign);
		
	    return projectCaseMapper.insertProjectCase(projectCase);
	}
	
	/**
     * 修改项目测试用例管理
     * 
     * @param projectCase 项目测试用例管理信息
     * @return 结果
     */
	@Override
	public int updateProjectCase(ProjectCase projectCase)
	{
		projectCase.setUpdateBy(ShiroUtils.getLoginName());
		projectCase.setUpdateTime(new Date());
	    return projectCaseMapper.updateProjectCase(projectCase);
	}

	/**
     * 删除项目测试用例管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteProjectCaseByIds(String ids)
	{
		String[] caseIds=Convert.toStrArray(ids);
		int result=0;
		for(String caseIdstr:caseIds){
			Integer caseId=Integer.valueOf(caseIdstr);
			ProjectCase projectCase = projectCaseMapper.selectProjectCaseById(caseId);
			
			if(projectPlanCaseMapper.selectProjectPlanCaseCountByCaseId(caseId)>0){
				throw new BusinessException(String.format("【%1$s】已绑定测试计划,不能删除", projectCase.getCaseSign()));
			}
			
			if(!PermissionUtils.isProjectPermsPassByProjectId(projectCase.getProjectId())){	
				  throw new BusinessException(String.format("用例【%1$s】没有项目删除权限", projectCase.getCaseSign()));
			}
			
			projectCaseStepsMapper.deleteProjectCaseStepsByCaseId(caseId);
			projectCaseMapper.deleteProjectCaseById(caseId);
			result++;
		}
		
		return result;
	}
	
    /**
     * 校验测试用例名称是否唯一
     */
    @Override
    public String checkProjectCaseNameUnique(ProjectCase projectCase)
    {
        long caseId = StringUtils.isNull(projectCase.getCaseId()) ? -1L : projectCase.getCaseId();
        List<ProjectCase> info = projectCaseMapper.checkProjectCaseNameUnique(projectCase);
        if (info.size()>0 && (info.get(0).getCaseId().longValue() != caseId || projectCase.getPriority()==99999999))
        {
            return ProjectCaseConstants.PROJECTCASE_NAME_NOT_UNIQUE;
        }
        return ProjectCaseConstants.PROJECTCASE_NAME_UNIQUE;
    }
    
	/**
	 * 查询总用例数
	 *
	 *
	 */
    @Override
	public int selectProjectCaseCount()
	{
	    return projectCaseMapper.selectProjectCaseCount();
	}
    
	/**
	 * 查询30天内更新的用例数
	 *
	 *
	 */
    @Override
	public int selectProjectCaseCountForThirtyDays()
	{
    	Date date;
    	Calendar calendar = new GregorianCalendar();
    	/*30天前*/
    	calendar.add(Calendar.DATE, -30);
    	date = calendar.getTime();
    	    
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    return projectCaseMapper.selectProjectCaseCountForThirtyDays(sdf.format(date));
	}
    
    /**
     * 找所有带模块的学生
     */
    @Override
    public List<Student> getStudentHasMod(){
    	return projectCaseModuleMapper.getStudentHasMod();
    }
    
    
    /**
     * 导入写完
     */
    @Override
	public String importProjectCase(List<ProjectCase> caseList) {
    	if (StringUtils.isNull(caseList) || caseList.size() == 0) {
			throw new BusinessException("导入用例不能为空！");
		}
    	int insertcount = 0;
		int updatecount = 0;
		int failcount = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();


		for (ProjectCase projectCase : caseList) {
			try {
				int ret = projectCaseMapper.selectCaseByProjectIdAndModuleId(projectCase.getProjectId(),projectCase.getModuleId());
				//判断项目id和模块id是否存在
				if(ret<=0) {
					failcount++;
					failureMsg.append("<br/>" + "第").append(caseList.indexOf(projectCase) + 2).append("行,项目下模块id不正确！");
					
					
				}else {
					int ret2 = projectCaseMapper.selectCaseByProjectIdAndCaseName(projectCase.getProjectId(),projectCase.getCaseName());
					if(ret2>0) {

						projectCase.setUpdateBy(ShiroUtils.getLoginName());
						projectCase.setUpdateTime(new Date());
					    projectCaseMapper.updateProjectCase(projectCase);
					    updatecount++;

					}else {
						projectCase.setCreateBy(ShiroUtils.getLoginName());
						projectCase.setCreateTime(new Date());
						projectCase.setUpdateBy(ShiroUtils.getLoginName());
						projectCase.setUpdateTime(new Date());
						
						int caseSerialNumber=projectCaseMapper.selectMaxCaseSerialNumberByProjectId(projectCase.getProjectId())+1;
						projectCase.setCaseSerialNumber(caseSerialNumber);
						
						String caseSign=projectMapper.selectProjectById(projectCase.getProjectId()).getProjectSign()+"-"+caseSerialNumber;
						projectCase.setCaseSign(caseSign);
						
					    projectCaseMapper.insertProjectCase(projectCase);
						insertcount++;
					}
					
					
				}
				
				
				
				
			} catch (Exception e) {
				// TODO: handle exception
				failcount++;
				String msg = "<br/>" + "第" +  (caseList.indexOf(projectCase) + 2) + "行导入失败：";
				failureMsg.append(msg).append(e.getMessage());
			}
		}
		

		if ((insertcount + updatecount) == caseList.size()) {    //如果全部成功
			successMsg.append("<br/>").append(caseList.size()).append("行全部导入成功，");
			if (insertcount > 0 && updatecount > 0) {
				successMsg.append("插入数据").append(insertcount).append("行，更新数据").append(updatecount).append("行！");
			} else if (insertcount > 0) {
				successMsg.append("插入数据").append(insertcount).append("行！");
			} else {
				successMsg.append("更新数据").append(updatecount).append("行！");
			}
		} else if (failcount == caseList.size()) {  //如果全部失败
			failureMsg.insert(0, caseList.size() + "行全部导入失败，" + failureMsg);
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
