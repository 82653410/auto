package com.accp.project.system.projectclazz.mapper;

import java.util.List;

import com.accp.project.system.projectclazz.domain.ProjectClazz;
import  com.accp.project.system.projectclazz.domain.ProjectClazzExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface ProjectClazzMapper {
    long countByExample(ProjectClazzExample example);

    int deleteByExample(ProjectClazzExample example);

    int insert(ProjectClazz record);

    int insertSelective(ProjectClazz record);

    List<ProjectClazz> selectByExample(ProjectClazzExample example);

    int updateByExampleSelective(@Param("record") ProjectClazz record, @Param("example") ProjectClazzExample example);

    int updateByExample(@Param("record") ProjectClazz record, @Param("example") ProjectClazzExample example);
    
    int deleteByProjectId(Integer projectId);
    
    int inserts(@Param("projectId") Integer projectId,@Param("list") List<Integer> list);
}