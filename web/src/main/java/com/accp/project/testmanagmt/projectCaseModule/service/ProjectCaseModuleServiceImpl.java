package com.accp.project.testmanagmt.projectCaseModule.service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accp.common.exception.BusinessException;
import com.accp.common.utils.StringUtils;
import com.accp.common.utils.security.PermissionUtils;
import com.accp.common.utils.security.ShiroUtils;
import com.accp.project.system.clazz.domain.Clazz;
import com.accp.project.system.project.domain.Project;
import com.accp.project.system.project.mapper.ProjectMapper;
import com.accp.project.testmanagmt.projectCase.mapper.ProjectCaseMapper;
import com.accp.project.testmanagmt.projectCaseModule.domain.ProjectCaseModule;
import com.accp.project.testmanagmt.projectCaseModule.mapper.ProjectCaseModuleMapper;

/**
 * 测试用例模块管理 服务层实现
 * 
 * uckyframe
 *
 */
@Service
public class ProjectCaseModuleServiceImpl implements IProjectCaseModuleService 
{
	@Autowired
	private ProjectCaseModuleMapper projectCaseModuleMapper;
	
	@Autowired
	private ProjectCaseMapper projectCaseMapper;
	
	@Autowired
	private ProjectMapper projectMapper;

	/**
     * 查询测试用例模块管理信息
     * 
     * @param moduleId 测试用例模块管理ID
     * @return 测试用例模块管理信息
     */
    @Override
	public ProjectCaseModule selectProjectCaseModuleById(Integer moduleId)
	{
	    return projectCaseModuleMapper.selectProjectCaseModuleById(moduleId);
	}
    
    /**
     * 通过项目ID获取用例模块的项目根节点实体
     */
    public ProjectCaseModule selectProjectCaseModuleParentZeroByProjectId(Integer projectId){
    	return projectCaseModuleMapper.selectProjectCaseModuleParentZeroByProjectId(projectId);
    }
	
	/**
	 * 通过父级ID查询子列表
	 * @param parentId 父节点ID
	 *
	 *
	 */
    @Override
	public List<ProjectCaseModule> selectProjectCaseModuleByParentId(Integer parentId)
	{
	    return projectCaseModuleMapper.selectProjectCaseModuleByParentId(parentId);
	}
    
	/**
     * 查询测试用例模块管理列表
     * 
     * @param projectCaseModule 测试用例模块管理信息
     * @return 测试用例模块管理集合
     */
	@Override
	public List<ProjectCaseModule> selectProjectCaseModuleList(ProjectCaseModule projectCaseModule)
	{
		List<ProjectCaseModule> pcmList=projectCaseModuleMapper.selectProjectCaseModuleList(projectCaseModule);
		List<Project> projectList = projectMapper.selectProjectList(new Project());
		for(int j = 0; j < pcmList.size(); j++){
			pcmList.get(j).setProjectName("未知项目ID:"+pcmList.get(j).getProjectId());
			for(Project project:projectList){
				if(pcmList.get(j).getProjectId().equals(project.getProjectId())){
					pcmList.get(j).setProjectName(project.getProjectName());
					break;
				}				
			}
			List<String> lis = projectCaseModuleMapper.getStudentListByModId(pcmList.get(j).getModuleId());
			if(lis!=null && lis.size()>0)
			{
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < lis.size(); i++) {
					sb.append(lis.get(i)+",");
				}

				sb.deleteCharAt(sb.length()-1);
				pcmList.get(j).setStudentNo(sb.toString());
			}
			
		}
	    return pcmList;
	}
	
	/**
     * 查询测试用例模块管理列表
     * 新
     * @param projectCaseModule 测试用例模块管理信息
     * @return 测试用例模块管理集合
     */
	@Override
	public List<Object> selectProjectCaseModuleLists(ProjectCaseModule projectCaseModule)
	{
		List<Project> projectList = projectMapper.selectProjectListAndClazzs(new Project());

		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < projectList.size(); i++) {
			list.add(projectList.get(i));
			List<Clazz> li = projectList.get(i).getClazzs();
			for (int j = 0; j < li.size(); j++) {
				if(appearNumber(li.get(j).getAncestors(), ",")>=1) {
					Clazz  c= li.get(j);
					if(li.get(j).getParentId()==1) {
						c.setParentId(projectList.get(i).getProjectId().longValue());
					}
					list.add(c);
				}
			}
		}
	    return list;
	}
	
	/**
     * 获取指定字符串出现的次数
     * 
     * @param srcText 源字符串
     * @param findText 要查找的字符串
     * @return
     */
    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        Pattern p = Pattern.compile(findText);
        Matcher m = p.matcher(srcText);
        while (m.find()) {
            count++;
        }
        return count;
    }
	
    /**
     * 新增测试用例模块管理
     * 
     * @param projectCaseModule 测试用例模块管理信息
     * @return 结果
     */
	@Override
	public int insertProjectCaseModule(ProjectCaseModule projectCaseModule)
	{
		ProjectCaseModule parentProjectCaseModule=selectProjectCaseModuleById(projectCaseModule.getParentId());
		projectCaseModule.setProjectId(parentProjectCaseModule.getProjectId());
		projectCaseModule.setCreateBy(ShiroUtils.getLoginName());
		projectCaseModule.setCreateTime(new Date());
        projectCaseModule.setUpdateBy(ShiroUtils.getLoginName());
        projectCaseModule.setUpdateTime(new Date());
		projectCaseModule.setAncestors(parentProjectCaseModule.getAncestors() + "," + projectCaseModule.getParentId());
	    return projectCaseModuleMapper.insertProjectCaseModule(projectCaseModule);
	}
	
	/**
     * 修改测试用例模块管理
     * 
     * @param projectCaseModule 测试用例模块管理信息
     * @return 结果
     */
	@Override
	public int updateProjectCaseModule(ProjectCaseModule projectCaseModule)
	{
		ProjectCaseModule info = projectCaseModuleMapper.selectProjectCaseModuleById(projectCaseModule.getParentId());
        if (StringUtils.isNotNull(info))
        {
            String ancestors = info.getAncestors() + "," + info.getModuleId();
            projectCaseModule.setAncestors(ancestors);
            projectCaseModule.setProjectId(info.getProjectId());
            updateModuleChildren(projectCaseModule, ancestors);
        }
        projectCaseModule.setUpdateBy(ShiroUtils.getLoginName());
        projectCaseModule.setUpdateTime(new Date());
        return projectCaseModuleMapper.updateProjectCaseModule(projectCaseModule);
	}
	
	/**
     * 绑定测试用例模块管理
     * 
     * @param projectCaseModule 测试用例模块管理信息
     * @return 结果
     */
	@Override
	public int bindProjectCaseModuleStu(Integer moduleId,String StudentNo)
	{
		projectCaseModuleMapper.deleteByModuleId(moduleId);
		List<String> list = Arrays.asList(StudentNo.split(",")).stream().map(s -> s.trim())
				.collect(Collectors.toList());
        return projectCaseModuleMapper.bindProjectCaseModuleStu(moduleId,list);
	}
	
    /**
     * 修改子模块关系
     * @param parentProjectCaseModule 父节点模块对象
     * @param ancestors ancestors
     *
     *
     */
    private void updateModuleChildren(ProjectCaseModule parentProjectCaseModule, String ancestors)
    {
        List<ProjectCaseModule> childrens = projectCaseModuleMapper.selectProjectCaseModuleByParentId(parentProjectCaseModule.getModuleId());
        for (ProjectCaseModule children : childrens)
        {
            children.setAncestors(ancestors + "," + parentProjectCaseModule.getModuleId());
            children.setUpdateBy(ShiroUtils.getLoginName());
            children.setUpdateTime(new Date());
        }
        if (childrens.size() > 0)
        {
        	projectCaseModuleMapper.updateModuleChildren(childrens);
        }
    }

	/**
     * 删除测试用例模块管理对象
     * 
     * @param moduleId 需要删除的数据ID
     * @return 结果
     */
	@Override
	public int deleteProjectCaseModuleById(Integer moduleId) throws BusinessException
	{
		if(projectCaseMapper.selectProjectCaseCountByModuleId(moduleId)>0){
			throw new BusinessException(String.format("【%1$s】已绑定测试用例,不能删除", projectCaseModuleMapper.selectProjectCaseModuleById(moduleId).getModuleName()));
		}
		
		if(!PermissionUtils.isProjectPermsPassByProjectId(projectCaseModuleMapper.selectProjectCaseModuleById(moduleId).getProjectId())){	
			  throw new BusinessException("没有删除用例模块的项目权限");
		}
		projectCaseModuleMapper.deleteByModuleId(moduleId);
		return projectCaseModuleMapper.deleteProjectCaseModuleById(moduleId);
	}
	
    /**
     * 查询用例模块管理树
     */
    @Override
    public List<Map<String, Object>> selectProjectCaseModuleTree(ProjectCaseModule projectCaseModule)
    {
        List<Map<String, Object>> trees;
        List<ProjectCaseModule> pcmList = projectCaseModuleMapper.selectProjectCaseModuleList(projectCaseModule);
        trees = getTrees(pcmList);
        return trees;
    }
    
    /**
     * 对象转模块树
     * @param pcmList 模块集合
     *
     *
     */
    private List<Map<String, Object>> getTrees(List<ProjectCaseModule> pcmList)
    {

        List<Map<String, Object>> trees = new ArrayList<>();
        for (ProjectCaseModule pcm : pcmList)
        {
                Map<String, Object> pcmMap = new HashMap<>();
                pcmMap.put("id", pcm.getModuleId());
                pcmMap.put("pId", pcm.getParentId());
                pcmMap.put("name", pcm.getModuleName());
                pcmMap.put("title", pcm.getModuleName());
                pcmMap.put("checked", false);
                trees.add(pcmMap);
        }
        return trees;
    }
    
	/**
	 * 获取当前父节点下排序号
	 */
	@Override
	public int selectProjectCaseModuleMaxOrderNumByParentId(Integer parentModuleId)
	{
		return projectCaseModuleMapper.selectProjectCaseModuleMaxOrderNumByParentId(parentModuleId)+1;
	}

	/** 导入用例模块
	 * @param modulesList 用例模块列表
	 * @return 结果
	 * ummer
	 *
	 */
	@Override
	public String importProjectCaseModules(List<ProjectCaseModule> modulesList) {

		if (StringUtils.isNull(modulesList) || modulesList.size() == 0) {
			throw new BusinessException("导入用例模块不能为空！");
		}

		List<ProjectCaseModule> modulesListBeforeSort = new ArrayList<>(modulesList);

		//按照组模块列表长度升序排序
		modulesList.sort((o1, o2) -> {
			if(o1.getAncestors() != null&& !o1.getAncestors().equals("") &&o2.getAncestors()!=null&& !o2.getAncestors().equals("")) {
				return Integer.compare(o1.getAncestors().split(",").length, o2.getAncestors().split(",").length);
			}
			else if(o1.getAncestors() == null|| o1.getAncestors().equals("")){
				return -1;

			}
			else if(o2.getAncestors() == null|| o2.getAncestors().equals("")){
				return 1;

			}
			else{
				return 0;
			}
		});

		int insertcount = 0;
		int updatecount = 0;
		int failcount = 0;
		StringBuilder successMsg = new StringBuilder();
		StringBuilder failureMsg = new StringBuilder();


		HashMap<String,Integer> nodeMap = new HashMap<>();  //初始化节点map
		for (ProjectCaseModule module : modulesList) {
			try {
				// 验证是否存在这个项目
				String projectName = module.getProjectName();
				Project prj = new Project();
				prj.setProjectName(projectName);
				List<Project> projects = projectMapper.selectProjectList(prj);
				if (projects.size() == 1 && !module.getModuleName().equals("") && !module.getAncestors().equals(""))   //如果项目存在，且模块名称、祖模块列表不为空
				{
					int flag=1;
					Integer projectId =projects.get(0).getProjectId();
					ProjectCaseModule pcm = new ProjectCaseModule();
					pcm.setProjectId(projectId);
					pcm.setModuleName(module.getModuleName());
					//查询待插入的模块
					List<ProjectCaseModule> pcmlist = projectCaseModuleMapper.selectProjectCaseModuleListPrecise(pcm);
					//查询祖先模块路径
					String[] ancestors = module.getAncestors().split(",");
					StringBuilder sb = new StringBuilder(module.getAncestors());
					for(int i=0;i<ancestors.length;i++){
						if(nodeMap.get(ancestors[i].trim())==null) {  //如果map不存在该节点，则查询
							ProjectCaseModule pcmodule = new ProjectCaseModule();
							pcmodule.setModuleName(ancestors[i].trim());
							pcmodule.setProjectId(projectId);
							List<ProjectCaseModule> nodes = projectCaseModuleMapper.selectProjectCaseModuleListPrecise(pcmodule);
							if (nodes!=null&&nodes.size()>0) {   //如果节点存在，则从map取
								nodeMap.put(ancestors[i], nodes.get(0).getModuleId());
								if(i == 0) {
									sb.replace(module.getAncestors().indexOf(ancestors[i]), module.getAncestors().indexOf(ancestors[i]) + ancestors[i].length(), nodes.get(0).getModuleId().toString());
								}
								else {
									sb.replace(StringUtils.ordinalIndexOf(sb,",",i)+1, StringUtils.ordinalIndexOf(sb,",",i)+1+ ancestors[i].length(), nodes.get(0).getModuleId().toString());
								}

							}
							else{   //节点不存在则退出循环
								flag = 0;
								break;
							}
						}
						else{   //如果map存在该节点，则直接替换
							if(i == 0) {
								sb.replace(module.getAncestors().indexOf(ancestors[i]), module.getAncestors().indexOf(ancestors[i]) + ancestors[i].length(), nodeMap.get(ancestors[i]).toString());
							}
							else {
								sb.replace(StringUtils.ordinalIndexOf(sb,",",i)+1, StringUtils.ordinalIndexOf(sb,",",i)+1+ ancestors[i].length(), nodeMap.get(ancestors[i]).toString());
							}
						}

					}
					if(flag == 0){  //如果路径不存在，则抛出错误
						failcount++;
						failureMsg.append("<br/>" + "第").append(modulesListBeforeSort.indexOf(module) + 2).append("行,祖模块列表路径不正确！");
					}
					else{   //如果路径存在
						if (pcmlist.size() <= 0)   //如果模块不存在，则插入
						{
							
							module.setProjectId(projectId);
							module.setParentId(Integer.parseInt(sb.toString().split(",")[sb.toString().split(",").length-1]));
							this.insertProjectCaseModule(module);
							
							bindProjectCaseModuleStu(module.getModuleId(), module.getStudentNo());
							
							insertcount++;
						} else {       //如果参数存在，则更新
							module.setParentId(Integer.parseInt(sb.toString().split(",")[sb.toString().split(",").length-1]));
							
							for (int i = 0; i < pcmlist.size(); i++) {
								if(pcmlist.get(i).getModuleName().equals(module.getModuleName())) {
									module.setModuleId(pcmlist.get(i).getModuleId());

									bindProjectCaseModuleStu(module.getModuleId(), module.getStudentNo());
									this.updateProjectCaseModule(module);
									
								}
							}
							
							updatecount++;
						}
					}


				} else if (projects.size() <= 0)   //如果项目不存在
				{

					failcount++;
					failureMsg.append("<br/>" + "第").append(modulesListBeforeSort.indexOf(module) + 2).append("行,项目名称不正确！");
				} else if (module.getModuleName().equals(""))   //模块名称为空
				{
					failcount++;
					failureMsg.append("<br/>" + "第").append(modulesListBeforeSort.indexOf(module) + 2).append("行,模块名称不能为空！");
				} else if (module.getAncestors().equals(""))   //祖模块列表为空
				{
					failcount++;
					failureMsg.append("<br/>" + "第").append(modulesListBeforeSort.indexOf(module) + 2).append("行,祖模块列表不能为空！");
				}
			} catch (Exception e) {
				failcount++;
				String msg = "<br/>" + "第" +  (modulesListBeforeSort.indexOf(module) + 2) + "行导入失败：";
				failureMsg.append(msg).append(e.getMessage());

			}
		}
		if ((insertcount + updatecount) == modulesList.size()) {    //如果全部成功
			successMsg.append("<br/>").append(modulesList.size()).append("行全部导入成功，");
			if (insertcount > 0 && updatecount > 0) {
				successMsg.append("插入数据").append(insertcount).append("行，更新数据").append(updatecount).append("行！");
			} else if (insertcount > 0) {
				successMsg.append("插入数据").append(insertcount).append("行！");
			} else {
				successMsg.append("更新数据").append(updatecount).append("行！");
			}
		} else if (failcount == modulesList.size()) {  //如果全部失败
			failureMsg.insert(0, modulesList.size() + "行全部导入失败，" + failureMsg);
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
	
	/**
	 * 项目id和学生id找模块
	 * @param projectId
	 * @param studentNo
	 * @return
	 */
	@Override
	public List<ProjectCaseModule> findModByProjectIdAndStuNo(Integer projectId,String studentNo){
		return projectCaseModuleMapper.findModByProjectIdAndStuNo(projectId, studentNo);
	}
	
	/**
	 * 项目id找最大魔抗
	 */
	@Override
	public Integer findModByProjectId(Integer projectId) {
		return projectCaseModuleMapper.findModByProjectId(projectId);
	}
	
	@Override
	public List<ProjectCaseModule> selectModuleAndProject(String studentNo){
		return projectCaseModuleMapper.selectModuleAndProject(studentNo);
	}
}
