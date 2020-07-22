package com.accp.project.testmanagmt.projectCaseModule.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import com.accp.project.system.student.domain.Student;
import com.accp.project.testmanagmt.projectCaseModule.domain.ProjectCaseModule;
import org.springframework.stereotype.Component;

/**
 * 测试用例模块管理 数据层
 * 
 * uckyframe
 *
 */
@Component
public interface ProjectCaseModuleMapper 
{
	/**
     * 根据ID查询测试用例模块管理信息
     * 
     * @param moduleId 测试用例模块管理ID
     * @return 测试用例模块管理信息
     */
	ProjectCaseModule selectProjectCaseModuleById(Integer moduleId);
	
	/**
     * 查询测试用例模块管理信息列表
     * 
     * @param parentId 测试用例模块管理父节点ID
     * @return 测试用例模块管理信息
     */
	List<ProjectCaseModule> selectProjectCaseModuleByParentId(Integer parentId);
	
	/**
     * 根据项目ID查询测试用例模块管理信息
     * 
     * @param projectId 项目ID
     * @return 测试用例模块管理信息
     */
	ProjectCaseModule selectProjectCaseModuleParentZeroByProjectId(Integer projectId);
	
	/**
     * 查询测试用例模块管理列表
     * 
     * @param projectCaseModule 测试用例模块管理对象
     * @return 测试用例模块管理集合
     */
	List<ProjectCaseModule> selectProjectCaseModuleList(ProjectCaseModule projectCaseModule);
	
	/**
     * 新增测试用例模块管理
     * 
     * @param projectCaseModule 测试用例模块管理对象
     * @return 结果
     */
	int insertProjectCaseModule(ProjectCaseModule projectCaseModule);
	
	/**
     * 修改测试用例模块管理
     * 
     * @param projectCaseModule 测试用例模块管理对象
     * @return 结果
     */
	int updateProjectCaseModule(ProjectCaseModule projectCaseModule);
	
	/**
     * 删除测试用例模块管理
     * 
     * @param moduleId 测试用例模块管理ID
     * @return 结果
     */
	int deleteProjectCaseModuleById(Integer moduleId);
	
	/**
     * 批量删除测试用例模块管理
     * 
     * @param moduleIds 需要删除的数据ID数组
     * @return 结果
     */
	int deleteProjectCaseModuleByIds(String[] moduleIds);
	
	/**
	 * 根据projectIds批量删除测试用例模块管理
	 * @param projectIds 项目ID数组
	 *
	 *
	 */
	int deleteProjectCaseModuleByProjectIds(String[] projectIds);
	
    /**
     * 修改子元素关系
     * @param projectCaseModules 项目模块对象集合
     *
     *
     */
    int updateModuleChildren(@Param("modules") List<ProjectCaseModule> projectCaseModules);
    
    /**
     * 查询当前父节点下排序的最大数
     * @param moduleId 模块ID
     *
     *
     */
    int selectProjectCaseModuleMaxOrderNumByParentId(Integer moduleId);
    
    /**
     * 查询项目下有没有子模块
     * @param projectId 项目ID
     *
     *
     */
    int selectProjectCaseModuleCountByProjectId(Integer projectId);

	/**
	 * 精准查询测试用例模块管理列表
	 *
	 * @param projectCaseModule 测试用例模块管理信息
	 * @return 测试用例模块管理集合
	 * ummer
	 *
	 */
	List<ProjectCaseModule> selectProjectCaseModuleListPrecise(ProjectCaseModule projectCaseModule);
    
	int bindProjectCaseModuleStu(@Param("moduleId") Integer moduleId,@Param("list") List<String> list);
	int deleteByModuleId(Integer moduleId);
	
	List<ProjectCaseModule> findModByProjectIdAndStuNo(@Param("projectId") Integer projectId,@Param("studentNo") String studentNo);
	
	Integer findModByProjectId(Integer projectId);
	
	/**
	 * 找所有绑定了模块的学生
	 * @return
	 */
	List<Student> getStudentHasMod();
	
	/**
	 * 根据学号找模块绑定的项目
	 * @param studentNo
	 * @return
	 */
	List<ProjectCaseModule> selectModuleAndProject(String studentNo);
	
	List<String> getStudentListByModId(Integer id);
}