package com.accp.project.system.clazz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.accp.common.constant.UserConstants;
import com.accp.common.exception.BusinessException;
import com.accp.common.utils.StringUtils;
import com.accp.common.utils.security.ShiroUtils;
import com.accp.framework.aspectj.lang.annotation.DataScope;
import com.accp.project.system.clazz.domain.Clazz;
import com.accp.project.system.clazz.mapper.ClazzMapper;
import com.accp.project.system.project.domain.Project;
import com.accp.project.system.role.domain.Role;
import com.accp.project.system.student.domain.Student;
import com.accp.project.testmanagmt.projectCaseModule.domain.ProjectCaseModule;

/**
 * 班级管理 服务实现
 * 
 
 */
@Service
public class ClazzServiceImpl implements IClazzService
{
    @Autowired
    private ClazzMapper clazzMapper;

    /**
     * 查询班级管理数据
     * 
     * @param clazz 班级信息
     * @return 班级信息集合
     */
    @Override
    @DataScope(tableAlias = "d")
    public List<Clazz> selectClazzList(Clazz clazz)
    {
        return clazzMapper.selectClazzList(clazz);
    }
    
    /**
     * 查询班级管理数据带学生
     * 
     * @param clazz 班级信息
     * @return 班级信息集合
     */
    @Override
    @DataScope(tableAlias = "d")
    public List<Clazz> selectClazzStudentList(Clazz clazz)
    {
        return clazzMapper.selectClazzStudentList(clazz);
    }

    /**
     * 查询班级管理树
     * 
     * @param clazz 班级信息
     * @return 所有班级信息
     */
    @Override
    @DataScope(tableAlias = "d")
    public List<Map<String, Object>> selectClazzTree(Clazz clazz)
    {
        List<Map<String, Object>> trees;
        List<Clazz> clazzList = clazzMapper.selectClazzList(clazz);
        //trees = getTrees(clazzList, true, null);

        trees = getTrees(clazzList, false, null);
        return trees;
    }

    /**
     * 根据角色ID查询班级（数据权限）
     *
     * @param role 角色对象
     * @return 班级列表（数据权限）
     */
    @Override
    public List<Map<String, Object>> roleClazzTreeData(Role role)
    {
        Long roleId = role.getRoleId();
        List<Map<String, Object>> trees;
        List<Clazz> clazzList = selectClazzList(new Clazz());
        if (StringUtils.isNotNull(roleId))
        {
            List<String> roleClazzList = clazzMapper.selectRoleClazzTree(roleId);
            trees = getTrees(clazzList, true, roleClazzList);
        }
        else
        {
            trees = getTrees(clazzList, false, null);
        }
        return trees;
    }
    
    /**
     * 根据项目ID查询班级（数据权限）
     *
     * @param role 角色对象
     * @return 班级列表（数据权限）
     */
    @Override
    public List<Map<String, Object>> projectClazzTreeData(Project pro)
    {
        Integer proId = pro.getProjectId();
        List<Map<String, Object>> trees;
        List<Clazz> clazzList = selectClazzList(new Clazz());
        if (StringUtils.isNotNull(proId))
        {
            List<String> projectClazzList = clazzMapper.selectProjectClazzTree(proId);
            trees = getTrees(clazzList, true, projectClazzList);
        }
        else
        {
            trees = getTrees(clazzList, false, null);
        }
        return trees;
    }
    
    /**
     * 根据模块查询班级学生
     *
     * @param role 角色对象
     * @return 班级列表（数据权限）
     */
    @Override
    public List<Map<String, Object>> moduleStudentTreeData(ProjectCaseModule mod)
    {
    	Integer moduleId = mod.getModuleId();
        List<Map<String, Object>> trees;
        List<Clazz> clazzList = selectClazzStudentList(new Clazz());
        if (StringUtils.isNotNull(moduleId))
        {
            List<String> moduleStudent = clazzMapper.selectModuleStudentTree(moduleId);
            trees = getTrees(clazzList, true, moduleStudent);
        }
        else
        {
            trees = getTrees(clazzList, false, null);
        }
        return trees;
    }

    /**
     * 对象转班级树
     *
     * @param clazzList 班级列表
     * @param isCheck 是否需要选中
     * @param roleClazzList 角色已存在菜单列表
     * @return 返回班级树数据
     */
    public List<Map<String, Object>> getTrees(List<Clazz> clazzList, boolean isCheck, List<String> roleClazzList)
    {

        List<Map<String, Object>> trees = new ArrayList<>();
        for (Clazz clazz : clazzList)
        {
            if (UserConstants.DEPT_NORMAL.equals(clazz.getStatus()))
            {
                Map<String, Object> clazzMap = new HashMap<>();
                clazzMap.put("id", clazz.getClazzId());
                clazzMap.put("pId", clazz.getParentId());
                clazzMap.put("name", clazz.getClazzName());
                clazzMap.put("title", clazz.getClazzName());
                if (isCheck)
                {
                    clazzMap.put("checked", roleClazzList.contains(clazz.getClazzId().toString()));
                }
                else
                {
                    clazzMap.put("checked", false);
                }
                trees.add(clazzMap);
                try {
                	if(clazz.getStudents().get(0).getStudentNo()!=null) {
                		trees.addAll(getTreesTwo(clazz.getStudents(),true,roleClazzList));
                	}
				} catch (Exception e) {
					// TODO: handle exception
				}
            }
            
            
        }
        return trees;
    }
    
    /**
     * 对象转班级树
     *
     * @param clazzList 班级列表
     * @param isCheck 是否需要选中
     * @param roleClazzList 角色已存在菜单列表
     * @return 返回班级树数据
     */
    public List<Map<String, Object>> getTreesTwo(List<Student> studentList, boolean isCheck, List<String> checkStudentList)
    {

        List<Map<String, Object>> trees = new ArrayList<>();
        for (Student stu : studentList)
        {
            if (UserConstants.STUDENT_NORMAL.equals(stu.getDelFlag()))
            {
                Map<String, Object> studentMap = new HashMap<>();
                studentMap.put("id", stu.getStudentNo());
                studentMap.put("pId", stu.getClazzId());
                studentMap.put("name", stu.getStudentName());
                studentMap.put("title", stu.getStudentName());
                if (isCheck)
                {
                	studentMap.put("checked", checkStudentList.contains(stu.getStudentNo()));
                }
                else
                {
                	studentMap.put("checked", false);
                }
                trees.add(studentMap);
            }
            
            
        }
        return trees;
    }

    /**
     * 查询班级人数
     * 
     * @param parentId 班级ID
     * @return 结果
     */
    @Override
    public int selectClazzCount(Long parentId)
    {
        Clazz clazz = new Clazz();
        clazz.setParentId(parentId);
        return clazzMapper.selectClazzCount(clazz);
    }

    /**
     * 查询班级是否存在用户
     * 
     * @param clazzId 班级ID
     * @return 结果 true 存在 false 不存在
     */
    @Override
    public boolean checkClazzExistStudent(Long clazzId)
    {
        int result = clazzMapper.checkClazzExistStudent(clazzId);
        return result > 0;
    }

    /**
     * 删除班级管理信息
     * 
     * @param clazzId 班级ID
     * @return 结果
     */
    @Override
    public int deleteClazzById(Long clazzId)
    {
        return clazzMapper.deleteClazzById(clazzId);
    }

    /**
     * 新增保存班级信息
     * 
     * @param clazz 班级信息
     * @return 结果
     */
    @Override
    public int insertClazz(Clazz clazz)
    {
        Clazz info = clazzMapper.selectClazzById(clazz.getParentId());
        // 如果父节点不为"正常"状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus()))
        {
            throw new BusinessException("班级停用，不允许新增");
        }
        clazz.setCreateBy(ShiroUtils.getLoginName());
        clazz.setAncestors(info.getAncestors() + "," + clazz.getParentId());
        return clazzMapper.insertClazz(clazz);
    }

    /**
     * 修改保存班级信息
     * 
     * @param clazz 班级信息
     * @return 结果
     */
    @Override
    public int updateClazz(Clazz clazz)
    {
        Clazz info = clazzMapper.selectClazzById(clazz.getParentId());
        if (StringUtils.isNotNull(info))
        {
            String ancestors = info.getAncestors() + "," + info.getClazzId();
            clazz.setAncestors(ancestors);
            updateClazzChildren(clazz.getClazzId(), ancestors);
        }
        clazz.setUpdateBy(ShiroUtils.getLoginName());
        int result = clazzMapper.updateClazz(clazz);
        if (UserConstants.DEPT_NORMAL.equals(clazz.getStatus()))
        {
            // 如果该班级是启用状态，则启用该班级的所有上级班级
            updateParentClazzStatus(clazz);
        }
        return result;
    }

    /**
     * 修改该班级的父级班级状态
     * 
     * @param clazz 当前班级
     */
    private void updateParentClazzStatus(Clazz clazz)
    {
        String updateBy = clazz.getUpdateBy();
        clazz = clazzMapper.selectClazzById(clazz.getClazzId());
        clazz.setUpdateBy(updateBy);
        clazzMapper.updateClazzStatus(clazz);
    }

    /**
     * 修改子元素关系
     * 
     * @param clazzId 班级ID
     * @param ancestors 元素列表
     */
    public void updateClazzChildren(Long clazzId, String ancestors)
    {
        Clazz clazz = new Clazz();
        clazz.setParentId(clazzId);
        List<Clazz> childrens = clazzMapper.selectClazzList(clazz);
        for (Clazz children : childrens)
        {
            children.setAncestors(ancestors + "," + clazz.getParentId());
        }
        if (childrens.size() > 0)
        {
            clazzMapper.updateClazzChildren(childrens);
        }
    }

    /**
     * 根据班级ID查询信息
     * 
     * @param clazzId 班级ID
     * @return 班级信息
     */
    @Override
    public Clazz selectClazzById(Long clazzId)
    {
        return clazzMapper.selectClazzById(clazzId);
    }
    

    /**
     * 根据班级ID数组查询信息
     * 
     * @param clazzId 班级ID
     * @return 班级信息
     */
    @Override
    public List<Clazz> selectClazzByIds(Long[] clazzId)
    {
        return clazzMapper.selectClazzByIds(clazzId);
    }

    /**
     * 校验班级名称是否唯一
     * 
     * @param clazz 班级信息
     * @return 结果
     */
    @Override
    public String checkClazzNameUnique(Clazz clazz)
    {
        long clazzId = StringUtils.isNull(clazz.getClazzId()) ? -1L : clazz.getClazzId();
        Clazz info = clazzMapper.checkClazzNameUnique(clazz.getClazzName(), clazz.getParentId());
        if (StringUtils.isNotNull(info) && info.getClazzId() != clazzId)
        {
            return UserConstants.DEPT_NAME_NOT_UNIQUE;
        }
        return UserConstants.DEPT_NAME_UNIQUE;
    }
    
    @Override
    public List<Student> selectStudentByClazzId(Integer clazzId){
    	return clazzMapper.selectStudentByClazzId(clazzId);
    }
}
