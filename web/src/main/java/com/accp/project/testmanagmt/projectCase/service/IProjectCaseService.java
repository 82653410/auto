package com.accp.project.testmanagmt.projectCase.service;

import java.util.List;

import com.accp.project.system.student.domain.Student;
import com.accp.project.testmanagmt.projectCase.domain.ProjectCase;
import com.accp.project.testmanagmt.projectCaseModule.domain.ProjectCaseModule;

/**
 * 项目测试用例管理 服务层
 * 
 * uckyframe
 * 
 */
public interface IProjectCaseService 
{
	/**
     * 查询项目测试用例管理信息
     * 
     * @param caseId 项目测试用例管理ID
     * @return 项目测试用例管理信息
     */
	ProjectCase selectProjectCaseById(Integer caseId);
	
	/**
	 * 根据用例编号查询实体
	 * @param caseSign 用例编号
	 *
	 * 
	 */
	ProjectCase selectProjectCaseByCaseSign(String caseSign);
	
	/**
     * 查询项目测试用例管理列表
     * 
     * @param projectCase 项目测试用例管理信息
     * @return 项目测试用例管理集合
     */
	List<ProjectCase> selectProjectCaseList(ProjectCase projectCase);
	
	/**
	 * 根据项目ID查询用例集合
	 * @param projectId 项目ID
	 *
	 * 
	 */
	List<ProjectCase> selectProjectCaseListByProjectId(Integer projectId);
	
	/**
	 * 根据计划ID查询用例列表
	 * @param projectCase 用例对象
	 *
	 * 
	 */
	List<ProjectCase> selectProjectCaseListForPlan(ProjectCase projectCase);
	
	/**
     * 新增项目测试用例管理
     * 
     * @param projectCase 项目测试用例管理信息
     * @return 结果
     */
	int insertProjectCase(ProjectCase projectCase);
	
	/**
     * 修改项目测试用例管理
     * 
     * @param projectCase 项目测试用例管理信息
     * @return 结果
     */
	int updateProjectCase(ProjectCase projectCase);
		
	/**
     * 删除项目测试用例管理信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteProjectCaseByIds(String ids);
	
    /**
     * 校验测试用例名称是否唯一
     * @param projectCase 用例对象
     *
     * 
     */
    String checkProjectCaseNameUnique(ProjectCase projectCase);
    
	/**
	 * 查询总用例数
	 *
	 * 
	 */
	int selectProjectCaseCount();
	
	/**
	 * 查询30天内更新的用例数
	 *
	 * 
	 */
	int selectProjectCaseCountForThirtyDays();
	
	List<Student> getStudentHasMod();
	
	String importProjectCase(List<ProjectCase> caseList);
}
