package com.accp.project.system.clazz.service;

import java.util.List;
import java.util.Map;

import com.accp.project.system.clazz.domain.Clazz;
import com.accp.project.system.project.domain.Project;
import com.accp.project.system.role.domain.Role;
import com.accp.project.system.student.domain.Student;
import com.accp.project.testmanagmt.projectCaseModule.domain.ProjectCaseModule;

/**
 * 班级管理 服务层
 * 
 
 */
public interface IClazzService
{
    /**
     * 查询班级管理数据
     * 
     * @param clazz 班级信息
     * @return 班级信息集合
     */
    List<Clazz> selectClazzList(Clazz clazz);
    
    List<Clazz> selectClazzStudentList(Clazz clazz);

    /**
     * 查询班级管理树
     * 
     * @param clazz 班级信息
     * @return 所有班级信息
     */
    List<Map<String, Object>> selectClazzTree(Clazz clazz);
    

    /**
     * 根据角色ID查询菜单
     *
     * @param role 角色对象
     * @return 菜单列表
     */
    List<Map<String, Object>> roleClazzTreeData(Role role);
    
    /**
     * 根据项目ID查询班级
     *
     * @param role 角色对象
     * @return 菜单列表
     */
    List<Map<String, Object>> projectClazzTreeData(Project pro);
   

    /**
     * 查询班级人数
     * 
     * @param parentId 父班级ID
     * @return 结果
     */
    int selectClazzCount(Long parentId);

    /**
     * 查询班级是否存在用户
     * 
     * @param clazzId 班级ID
     * @return 结果 true 存在 false 不存在
     */
    boolean checkClazzExistStudent(Long clazzId);

    /**
     * 删除班级管理信息
     * 
     * @param clazzId 班级ID
     * @return 结果
     */
    int deleteClazzById(Long clazzId);

    /**
     * 新增保存班级信息
     * 
     * @param clazz 班级信息
     * @return 结果
     */
    int insertClazz(Clazz clazz);

    /**
     * 修改保存班级信息
     * 
     * @param clazz 班级信息
     * @return 结果
     */
    int updateClazz(Clazz clazz);

    /**
     * 根据班级ID查询信息
     * 
     * @param clazzId 班级ID
     * @return 班级信息
     */
    Clazz selectClazzById(Long clazzId);
    

    /**
     * 根据班级ID数组查询信息
     * 
     * @param clazzId[] 班级ID数组
     * @return 班级信息
     */
    List<Clazz> selectClazzByIds(Long[] clazzId);

    /**
     * 校验班级名称是否唯一
     * 
     * @param clazz 班级信息
     * @return 结果
     */
    String checkClazzNameUnique(Clazz clazz);
    
    List<Map<String, Object>> moduleStudentTreeData(ProjectCaseModule mod);
    
    /**
     * 根据班级id查学生
     * @param clazzId
     * @return
     */
    List<Student> selectStudentByClazzId(Integer clazzId);
    
}
