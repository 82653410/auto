package com.accp.project.system.student.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accp.common.constant.UserConstants;
import com.accp.common.utils.StringUtils;
import com.accp.common.utils.security.ShiroUtils;
import com.accp.framework.aspectj.lang.annotation.DataScope;
import com.accp.project.system.clazz.domain.Clazz;
import com.accp.project.system.clazz.service.IClazzService;
import com.accp.project.system.project.domain.Project;
import com.accp.project.system.student.domain.Student;
import com.accp.project.system.student.mapper.StudentMapper;

/**
 * 学生管理 服务实现
 * 
 
 */
@Service
public class StudentServiceImpl implements IStudentService
{
    @Autowired
    private StudentMapper studentMapper;
    

    //@DataScope(tableAlias = "d")
    @Override
    public List<Student> selectStudentList(Student student){
    	return studentMapper.selectStudentList(student);
    	
    }
    
    /**
     * 新增学生
     */
    @Override
    public int insertStudent(Student stu) {
    	if(studentMapper.checkStudentNo(stu)<1) {
    		return studentMapper.insertStudent(stu);
    	}else {
    		return -1;
    	}
    }
    
    /**
     * 检查学号是否存在
     */
    @Override
    public String checkStudentNoUnique(Student student) {
    	if(studentMapper.checkStudentNo(student)>0) {
    		return UserConstants.STUDENT_NO_NOT_UNIQUE;
    	}else {
    		return UserConstants.STUDENT_NO_UNIQUE;
    	}
    }

    /**
     * 学号找学生
     */
    @Override
    public Student selectStudentByNo(String studentNo) {
    	return studentMapper.selectStudentByNo(studentNo);
    }
    /**
     * 删除学生
     * 
     * @param 学号
     * @return 结果
     */
    @Override
    public int deleteStudentByNo(String studentNo)
    {
        return studentMapper.deleteStudentByNo(studentNo);
    }
    
    /**
     * 修改保存学生信息
     * 
     * @param clazz 班级信息
     * @return 结果
     */
    @Override
    public int updateStudent(Student stu)
    {
        
        return studentMapper.updateStudent(stu);
    }
    
    
}
