package com.accp.project.testmanagmt.projectCaseModule.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;

import com.accp.project.testmanagmt.projectCaseModule.domain.ProjectCaseModule;

/**
 * 测试用例模块管理 服务层
 * 
 * uckyframe
 * 
 */
public interface IProjectCaseModuleService 
{
	
	int bindProjectCaseModuleStu(Integer moduleId,String StudentNo);
	/**
     * 查询测试用例模块管理信息
     * 
     * @param moduleId 测试用例模块管理ID
     * @return 测试用例模块管理信息
     */
	ProjectCaseModule selectProjectCaseModuleById(Integer moduleId);
	
	/**
	 * 通过父级ID查询子列表
	 * @param parentId 父级ID
	 *
	 * 
	 */
	List<ProjectCaseModule> selectProjectCaseModuleByParentId(Integer parentId);
	
	/**
     * 查询测试用例模块管理列表
     * 
     * @param projectCaseModule 测试用例模块管理信息
     * @return 测试用例模块管理集合
     */
	List<ProjectCaseModule> selectProjectCaseModuleList(ProjectCaseModule projectCaseModule);
	
	/**
     * 查询测试用例模块管理列表
     * 新
     * 
     * @param projectCaseModule 测试用例模块管理信息
     * @return 测试用例模块管理集合
     */
	List<Object> selectProjectCaseModuleLists(ProjectCaseModule projectCaseModule);
	
	/**
     * 新增测试用例模块管理
     * 
     * @param projectCaseModule 测试用例模块管理信息
     * @return 结果
     */
	int insertProjectCaseModule(ProjectCaseModule projectCaseModule);
	
	/**
     * 修改测试用例模块管理
     * 
     * @param projectCaseModule 测试用例模块管理信息
     * @return 结果
     */
	int updateProjectCaseModule(ProjectCaseModule projectCaseModule);
		
	/**
     * 删除测试用例模块管理信息
     * 
     * @param moduleId 需要删除的数据ID
     * @return 结果
     */
	int deleteProjectCaseModuleById(Integer moduleId);
	
	/**
	 * 查询用例模块管理树
	 * @param projectCaseModule 模块对象
	 *
	 * 
	 */
    List<Map<String, Object>> selectProjectCaseModuleTree(ProjectCaseModule projectCaseModule);
    
    /**
     * 获取当前父节点下排序号
     * @param parentModuleId 模块ID
     *
     * 
     */
    int selectProjectCaseModuleMaxOrderNumByParentId(Integer parentModuleId);
    
    /**
     * 通过项目ID获取用例模块的项目根节点实体
     * @param projectId 项目ID
     *
     * 
     */
    ProjectCaseModule selectProjectCaseModuleParentZeroByProjectId(Integer projectId);

	/**
	 * 增加导入用例模块
	 * @param modulesList 用例模块列表
	 * ummer
	 * 
	 */
	String importProjectCaseModules(List<ProjectCaseModule> modulesList);
	
	/**
	 * 项目id和学生id找模块
	 * @param projectId
	 * @param studentNo
	 * @return
	 */
	List<ProjectCaseModule> findModByProjectIdAndStuNo(Integer projectId,String studentNo);
	
	Integer findModByProjectId(Integer projectId);
	
	List<ProjectCaseModule> selectModuleAndProject(String studentNo);
}
