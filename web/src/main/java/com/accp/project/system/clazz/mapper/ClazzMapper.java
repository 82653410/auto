package com.accp.project.system.clazz.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.accp.project.system.clazz.domain.Clazz;
import com.accp.project.system.student.domain.Student;

import org.springframework.stereotype.Component;

/**
 * 部门管理 数据层
 * 

 */
@Component
public interface ClazzMapper
{
    /**
     * 查询部门人数
     * 
     * @param clazz 部门信息
     * @return 结果
     */
    int selectClazzCount(Clazz clazz);

    /**
     * 查询部门是否存在用户
     * 
     * @param clazzId 部门ID
     * @return 结果
     */
    int checkClazzExistStudent(Long clazzId);

    /**
     * 查询部门管理数据
     * 
     * @param clazz 部门信息
     * @return 部门信息集合
     */
    List<Clazz> selectClazzList(Clazz clazz);
    
    /**
     * 带学生的查询
     * @param clazz
     * @return
     */
    List<Clazz> selectClazzStudentList(Clazz clazz);

    /**
     * 删除部门管理信息
     * 
     * @param clazzId 部门ID
     * @return 结果
     */
    int deleteClazzById(Long clazzId);

    /**
     * 新增部门信息
     * 
     * @param clazz 部门信息
     * @return 结果
     */
    int insertClazz(Clazz clazz);

    /**
     * 修改部门信息
     * 
     * @param clazz 部门信息
     * @return 结果
     */
    int updateClazz(Clazz clazz);

    /**
     * 修改子元素关系
     * 
     * @param clazzs 子元素
     * @return 结果
     */
    int updateClazzChildren(@Param("clazzs") List<Clazz> clazzs);

    /**
     * 根据部门ID查询信息
     * 
     * @param clazzId 部门ID
     * @return 部门信息
     */
    Clazz selectClazzById(Long clazzId);
    

    /**
     * 根据部门ID数组查询信息
     * 
     * @param clazzId 部门ID
     * @return 部门信息
     */
    List<Clazz> selectClazzByIds(Long[] clazzId);

    /**
     * 校验部门名称是否唯一
     * 
     * @param clazzName 部门名称
     * @param parentId 父部门ID
     * @return 结果
     */
    Clazz checkClazzNameUnique(@Param("clazzName") String clazzName, @Param("parentId") Long parentId);

    /**
     * 根据角色ID查询部门
     *
     * @param roleId 角色ID
     * @return 部门列表
     */
    List<String> selectRoleClazzTree(Long roleId);

    /**
     * 根据项目ID查询班级
     *
     * @param projectId 角色ID
     * @return 部门列表
     */
    List<String> selectProjectClazzTree(Integer projectId);
    
    /**
     * 根据模块ID查询学生
     *
     * @param moduleId 模块ID
     * @return 学生列表
     */
    List<String> selectModuleStudentTree(Integer moduleId);
    
    
    
    /**
     * 修改所在部门的父级部门状态
     * 
     * @param clazz 部门
     */
    void updateClazzStatus(Clazz clazz);
    
    /**
     * 检查班级是否存在多个,分隔
     * @param ids
     * @return
     */
    int checkClazzIdPresence(List<Integer> list);
    
    
    
    List<Student> selectStudentByClazzId(Integer clazzId);
}
