package com.accp.project.system.role.service;

import java.util.List;

import com.accp.project.system.role.domain.RoleProject;

/**
 * 角色和项目关联 服务层
 * 
 * uckyframe
 * 
 */
public interface IRoleProjectService 
{
	/**
     * 查询角色和项目关联信息
     * 
     * @param roleId 角色和项目关联ID
     * @return 角色和项目关联信息
     */
	List<RoleProject> selectRoleProjectById(Integer roleId);
	
	/**
     * 查询角色和项目关联列表
     * 
     * @param roleProject 角色和项目关联信息
     * @return 角色和项目关联集合
     */
	List<RoleProject> selectRoleProjectList(RoleProject roleProject);
	
	/**
	 * 查询用户项目权限
	 * @param userId 用户ID
	 * @return 用户项目权限ID集合
	 *
	 * 
	 */
	List<Integer> selectProjectPermsByUserId(Long userId);
    
	/**
     * 新增角色和项目关联
     * 
     * @param roleProject 角色和项目关联信息
     * @return 结果
     */
	int insertRoleProject(RoleProject roleProject);
	
	/**
     * 修改角色和项目关联
     * 
     * @param roleProject 角色和项目关联信息
     * @return 结果
     */
	int updateRoleProject(RoleProject roleProject);
		
	/**
     * 删除角色和项目关联信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteRoleProjectByIds(String ids);
	
}
