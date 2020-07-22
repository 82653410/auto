package com.accp.project.testexecution.taskExecute.service;

import java.util.Date;
import java.util.List;

import com.accp.project.testexecution.taskExecute.domain.TaskExecute;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 测试任务执行 服务层
 * 
 * uckyframe
 * 
 */
@Component
@Primary
public interface ITaskExecuteService 
{
	/**
     * 查询测试任务执行信息
     * 
     * @param taskId 测试任务执行ID
     * @return 测试任务执行信息
     */
	TaskExecute selectTaskExecuteById(Integer taskId);
	
	/**
	 * 查询最后一条执行记录
	 *
	 * 
	 */
	TaskExecute selectTaskExecuteLastRecord();
	
	/**
	 * 根据任务名称查询任务实体
	 * @param taskName 任务执行名称
	 *
	 * 
	 */
	TaskExecute selectTaskExecuteByTaskName(String taskName);
    
	/**
	 * 查询当前项目下最后一条执行记录
	 * @param projectId 项目ID
	 *
	 * 
	 */
	TaskExecute selectTaskExecuteLastRecordForProjectId(Integer projectId);
    
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
     * 删除测试任务执行信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
	int deleteTaskExecuteByIds(String ids);
	
	/**
	 * 查询最早一条执行任务日期
	 *
	 * 
	 */
	Date selectTaskExecuteMinData();
	
	/**
	 * 查询任务总数
	 *
	 * 
	 */
	int selectTaskExecuteCount();
	
	/**
	 * 查询30天内任务统计执行数据
	 *
	 * 
	 */
	List<TaskExecute> selectTaskExecuteListForThirtyDays();
}
