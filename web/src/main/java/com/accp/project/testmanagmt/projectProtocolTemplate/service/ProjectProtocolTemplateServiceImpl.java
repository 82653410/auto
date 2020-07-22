package com.accp.project.testmanagmt.projectProtocolTemplate.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accp.common.constant.ProjectProtocolTemplateConstants;
import com.accp.common.exception.BusinessException;
import com.accp.common.support.Convert;
import com.accp.common.utils.StringUtils;
import com.accp.common.utils.security.PermissionUtils;
import com.accp.common.utils.security.ShiroUtils;
import com.accp.project.testmanagmt.projectCase.mapper.ProjectCaseStepsMapper;
import com.accp.project.testmanagmt.projectProtocolTemplate.domain.ProjectProtocolTemplate;
import com.accp.project.testmanagmt.projectProtocolTemplate.mapper.ProjectProtocolTemplateMapper;
import com.accp.project.testmanagmt.projectProtocolTemplate.mapper.ProjectTemplateParamsMapper;

/**
 * 协议模板管理 服务层实现
 * 
 * uckyframe
 *
 */
@Service
public class ProjectProtocolTemplateServiceImpl implements IProjectProtocolTemplateService 
{
	@Autowired
	private ProjectProtocolTemplateMapper projectProtocolTemplateMapper;
	
	@Autowired
	private ProjectTemplateParamsMapper projectTemplateParamsMapper;

	@Autowired
	private ProjectCaseStepsMapper projectCaseStepsMapper;
	
	/**
     * 查询协议模板管理信息
     * 
     * @param templateId 协议模板管理ID
     * @return 协议模板管理信息
     */
    @Override
	public ProjectProtocolTemplate selectProjectProtocolTemplateById(Integer templateId)
	{
	    return projectProtocolTemplateMapper.selectProjectProtocolTemplateById(templateId);
	}
	
	/**
     * 查询协议模板管理列表
     * 
     * @param projectProtocolTemplate 协议模板管理信息
     * @return 协议模板管理集合
     */
	@Override
	public List<ProjectProtocolTemplate> selectProjectProtocolTemplateList(ProjectProtocolTemplate projectProtocolTemplate)
	{
	    return projectProtocolTemplateMapper.selectProjectProtocolTemplateList(projectProtocolTemplate);
	}
	
    /**
     * 新增协议模板管理
     * 
     * @param projectProtocolTemplate 协议模板管理信息
     * @return 结果
     */
	@Override
	public int insertProjectProtocolTemplate(ProjectProtocolTemplate projectProtocolTemplate)
	{
		projectProtocolTemplate.setCreateBy(ShiroUtils.getLoginName());
		projectProtocolTemplate.setCreateTime(new Date());
		projectProtocolTemplate.setUpdateBy(ShiroUtils.getLoginName());
		projectProtocolTemplate.setUpdateTime(new Date());
		
	    return projectProtocolTemplateMapper.insertProjectProtocolTemplate(projectProtocolTemplate);
	}
	
	/**
     * 修改协议模板管理
     * 
     * @param projectProtocolTemplate 协议模板管理信息
     * @return 结果
     */
	@Override
	public int updateProjectProtocolTemplate(ProjectProtocolTemplate projectProtocolTemplate)
	{
		projectProtocolTemplate.setUpdateBy(ShiroUtils.getLoginName());
		projectProtocolTemplate.setUpdateTime(new Date());
	    return projectProtocolTemplateMapper.updateProjectProtocolTemplate(projectProtocolTemplate);
	}

	/**
     * 删除协议模板管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteProjectProtocolTemplateByIds(String ids) throws BusinessException
	{
		String[] templateIds=Convert.toStrArray(ids);
		int result=0;
		for(String templateIdstr:templateIds){
			Integer templateId=Integer.valueOf(templateIdstr);
			ProjectProtocolTemplate projectProtocolTemplate = projectProtocolTemplateMapper.selectProjectProtocolTemplateById(templateId);
			
			if(projectCaseStepsMapper.selectProjectCaseStepsCountByTemplateId(templateId)>0){
				throw new BusinessException(String.format("【%1$s】已绑定测试用例,不能删除", projectProtocolTemplate.getTemplateName()));
			}
			
			if(!PermissionUtils.isProjectPermsPassByProjectId(projectProtocolTemplate.getProjectId())){	
				  throw new BusinessException(String.format("模板【%1$s】没有项目删除权限", projectProtocolTemplate.getTemplateName()));
			}
			
			projectTemplateParamsMapper.deleteProjectTemplateParamsByTemplateId(templateId);
			projectProtocolTemplateMapper.deleteProjectProtocolTemplateById(templateId);
			result++;
		}

		return result;
	}
	
    /**
     * 检验模板名称在项目中的唯一性
     */
    @Override
    public String checkProjectProtocolTemplateNameUnique(ProjectProtocolTemplate projectProtocolTemplate)
    {
        long templateId = StringUtils.isNull(projectProtocolTemplate.getTemplateId()) ? -1L : projectProtocolTemplate.getTemplateId();
        ProjectProtocolTemplate info = projectProtocolTemplateMapper.checkProjectProtocolTemplateNameUnique(projectProtocolTemplate);
        if (StringUtils.isNotNull(info) && info.getTemplateId().longValue() != templateId)
        {
            return ProjectProtocolTemplateConstants.PROJECTPROTOCOLTEMPLATE_NAME_NOT_UNIQUE;
        }
        return ProjectProtocolTemplateConstants.PROJECTPROTOCOLTEMPLATE_NAME_UNIQUE;
    }
}
