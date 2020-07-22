package com.accp.project.system.student.service;

import java.util.List;
import java.util.Map;

import com.accp.project.system.clazz.domain.Clazz;
import com.accp.project.system.student.domain.Student;

/**
 * 班级管理 服务层
 * 
 
 */
public interface IStudentService
{
    
    List<Student> selectStudentList(Student student);
    
    int insertStudent(Student student);
    
    String checkStudentNoUnique(Student student);
    
    int deleteStudentByNo(String studentNo);
    
    Student selectStudentByNo(String studentNo);
    
    int updateStudent(Student stu);
    
    
}
