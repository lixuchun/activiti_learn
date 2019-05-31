package com.lixuchun.e_historyQuery;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.Test;

public class HistoryQueryTest {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * 查询历史流程实例
	 */
	@Test
	public void findHistoryProcessInstance() {
		String processInstanceId = "701";
		HistoricProcessInstance hpi = processEngine.getHistoryService()
				.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId)
				.singleResult();
		System.out.println(hpi.getProcessDefinitionId());
		System.out.println(hpi.getId());
		System.out.println(hpi.getStartTime());
		System.out.println(hpi.getEndTime());
	}
	
	/**
	 * 查询历史活动
	 */
	@Test
	public void findHistoryActiviti() {
		String processInstanceId = "701";
		List<HistoricActivityInstance> list = processEngine.getHistoryService()
			.createHistoricActivityInstanceQuery()
			.processInstanceId(processInstanceId)
			.orderByHistoricActivityInstanceEndTime().asc()
			.list();
		
		if (list != null && list.size() > 0) {
			for (HistoricActivityInstance hai : list) {
				System.out.println(hai.getActivityName());
				System.out.println(hai.getAssignee());
				System.out.println(hai.getTaskId());
				System.out.println(hai.getStartTime());
				System.out.println(hai.getEndTime());
				System.out.println("######################################");
			}
		}
	}
	
	/**
	 * 查询任务历史
	 */
	@Test
	public void findHistoryTask() {
		String processInstanceId = "701";
		List<HistoricTaskInstance> list = processEngine.getHistoryService()
				.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId)
				.orderByExecutionId().asc()
				.list();
		
		if (list != null && list.size() > 0) {
			for (HistoricTaskInstance hai : list) {
				System.out.println(hai.getName());
				System.out.println(hai.getAssignee());
				System.out.println(hai.getProcessDefinitionId());
				System.out.println(hai.getStartTime());
				System.out.println(hai.getEndTime());
				System.out.println("######################################");
			}
		}
	}
	
}
