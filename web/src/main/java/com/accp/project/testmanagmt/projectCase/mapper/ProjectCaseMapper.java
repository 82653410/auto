package com.accp.project.testmanagmt.projectCase.mapper;

import java.util.List;

import com.accp.project.testmanagmt.projectCase.domain.ProjectCase;
import com.accp.project.testmanagmt.projectCaseModule.domain.ProjectCaseModule;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * 项目测试用例管理 数据层
 * 
 * uckyframe
 *
 */
@Component
public interface ProjectCaseMapper 
{
	/**
     * 查询项目测试用例管理信息
     * 
     * @param caseId 项目测试用例管理ID
     * @return 项目测试用例管理信息
     */
	ProjectCase selectProjectCaseById(Integer caseId);
	
	/**
     * 通过用例编号查询项目测试用例管理信息
     * 
     * @param caseSign 项目测试用例编号
     * @return 项目测试用例管理信息
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
	 * 查询计划中的用例列表
	 * @param projectCase 测试用例对象
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
     * 删除项目测试用例管理
     * 
     * @param caseId 项目测试用例管理ID
     * @return 结果
     */
	int deleteProjectCaseById(Integer caseId);
	
	/**
     * 批量删除项目测试用例管理
     * 
     * @param caseIds 需要删除的数据ID
     * @return 结果
     */
	int deleteProjectCaseByIds(String[] caseIds);
	
	/**
	 * 检查测试用例名称唯一性
	 * @param projectCase 测试用例对象
	 *
	 *
	 */
	List<ProjectCase> checkProjectCaseNameUnique(ProjectCase projectCase);
	
	/**
	 * 查询测试用例项目序号
	 * @param projectId 项目ID
	 *
	 *
	 */
	int selectMaxCaseSerialNumberByProjectId(Integer projectId);
	
    /**
     * 查询项目下有没有测试用例
     * @param projectId 项目ID
     *
     *
     */
    int selectProjectCaseCountByProjectId(Integer projectId);
    
    /**
     * 查询用例模块下有没有测试用例
     * @param moduleId 项目模块ID
     *
     *
     */
    int selectProjectCaseCountByModuleId(Integer moduleId);
    
    /**
     * 查询总用例数
     *
     *
     */
    int selectProjectCaseCount();
    
    /**
     * 查询指定天数内更新的用例
     * @param updateTime 根据更新时间
     *
     *
     */
    int selectProjectCaseCountForThirtyDays(String updateTime);
    
    int selectCaseByProjectIdAndModuleId(@Param("projectId") Integer projectId,@Param("moduleId") Integer moduleId);
    
    int selectCaseByProjectIdAndCaseName(@Param("projectId") Integer projectId,@Param("caseName") String caseName);
}