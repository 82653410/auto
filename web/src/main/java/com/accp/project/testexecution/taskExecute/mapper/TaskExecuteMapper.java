package com.accp.project.testexecution.taskExecute.mapper;

import java.util.Date;
import java.util.List;

import com.accp.project.testexecution.taskExecute.domain.TaskExecute;
import org.springframework.stereotype.Component;

/**
 * 测试任务执行 数据层
 * 
 * uckyframe
 *
 */
@Component
public interface TaskExecuteMapper 
{
	/**
     * 查询测试任务执行信息
     * 
     * @param taskId 测试任务执行ID
     * @return 测试任务执行信息
     */
	TaskExecute selectTaskExecuteById(Integer taskId);
	
	/**
	 * 查询最后一条测试任务执行信息
	 *
	 *
	 */
	TaskExecute selectTaskExecuteLastRecord();
	
	/**
	 * 查询当前项目下最后一条执行记录
	 * @param projectId 项目ID
	 *
	 *
	 */
	TaskExecute selectTaskExecuteLastRecordForProjectId(Integer projectId);
	
	/**
	 * 根据任务执行名称查询实体
	 * @param taskName 测试任务名称
	 *
	 *
	 */
	TaskExecute selectTaskExecuteByTaskName(String taskName);
	
	/**
     * 查询测试任务执行列表
     * 
     * @param taskExecute 测试任务执行信息
     * @return 测试任务执行集合
     */
	List<TaskExecute> selectTaskExecuteList(TaskExecute taskExecute);
	
	/**
     * 新增测试任务执行
     * 
     * @param taskExecute 测试任务执行信息
     * @return 结果
     */
	int insertTaskExecute(TaskExecute taskExecute);
	
	/**
     * 修改测试任务执行
     * 
     * @param taskExecute 测试任务执行信息
     * @return 结果
     */
	int updateTaskExecute(TaskExecute taskExecute);
	
	/**
     * 删除测试任务执行
     * 
     * @param taskId 测试任务执行ID
     * @return 结果
     */
	int deleteTaskExecuteById(Integer taskId);
	
	/**
     * 批量删除测试任务执行
     * 
     * @param taskIds 需要删除的数据ID
     * @return 结果
     */
	int deleteTaskExecuteByIds(String[] taskIds);
	
    /**
     * 查询项目下有没有执行任务
     * @param projectId 项目ID
     *
     *
     */
    int selectTaskExecuteCountByProjectId(Integer projectId);
    
    /**
     * 查询调度下有没有执行任务
     * @param schedulingId 调度ID
     *
     *
     */
    int selectTaskExecuteCountBySchedulingId(Integer schedulingId);
    
    /**
     * 查询最早的任务执行日期
     *
     *
     */
    Date selectTaskExecuteMinData();
    
    /**
     * 查询所有任务数
     *
     *
     */
    int selectTaskExecuteCount();
    
    /**
     * 查询指定天数内的任务执行统计数据
     * @param updateTime 指定时间
     *
     *
     */
    List<TaskExecute> selectTaskExecuteListForThirtyDays(String updateTime);
}