package com.lixuchun.c_processInstance;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class ProcessInstanceTest {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * zip文件部署
	 */
	@Test
	public void deployProcessDefinitition_Zip() {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		
		Deployment deployment = processEngine.getRepositoryService() // 流程部署相关对象
				.createDeployment() // 创建一个部署对象
				.name("流程定义")
				.addZipInputStream(zipInputStream)// 指定zip输入流
				.deploy(); // 返回一个部署对象
			System.out.println("部署ID " + deployment.getId());
			System.out.println("部署名称 " + deployment.getName());
	}
	
	/**
	 * 启动流程实例 程序点击申请流程
	 */
	@Test
	public void startProcessInstance() {
		String processKey = "hellowworld";
		ProcessInstance processInstance = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的 service 默认最新版本
			.startProcessInstanceByKey(processKey);// 启动流程通过流程定义key 对应helloworld.bpmn文件中id对应值
		System.out.println("流程实例id : " + processInstance.getId()); // 流程实例id 流程实例id : 101
		System.out.println("流程定义id" + processInstance.getProcessDefinitionId()); //流程定义id 流程定义idhellowworld:1:4
	}
	
	/**
	 * 查询当前人的个人任务
	 */
	@Test
	public void findMyPersionalTask () {
		String assignee = "王五";
		List<Task> list = processEngine.getTaskService() // 与正在执行的任务管理相关的service
			.createTaskQuery() // 创建任务查询
			.taskAssignee(assignee) // 个人查寻 指定办理人
			.list();
		if (list != null && list.size() > 0) {
			for (Task task : list) {
				System.out.println("任务ID" + task.getId());
				System.out.println("任务名称" + task.getName());
				System.out.println("任务创建时间" + task.getCreateTime());
				System.out.println("任务的办理人" + task.getAssignee());
				System.out.println("流程实例ID:" + task.getProcessInstanceId());
				System.out.println("执行对象的ID:" + task.getExecutionId());
				System.out.println("流程定义ID:" + task.getProcessDefinitionId());
				System.out.println("----------------------------------------------------");
			}
		}
	}
	
	/**
	 * 完成我的任务
	 */
	@Test
	public void complementMyPersonalTask() {
		String taskId = "302";
		processEngine.getTaskService() // 与正在执行的任务管理相关的service
			.complete(taskId);
		System.out.println("完成任务 任务id :" + taskId);
	}
	
	/**
	 * 判断流程是否结束 判断流程状态
	 */
	@Test
	public void isPerocessEnd() {
		String processInstanceId = "101";
		// 正在执行的流程实例表
		ProcessInstance pi = processEngine.getRuntimeService() // 正在执行的流程实例以及执行对象
			.createProcessInstanceQuery() // 流程实例查询
			.processInstanceId(processInstanceId) // 流程实例id查询
			.singleResult();
		
		if (pi == null) {
			System.out.println("流程结束");
		} else {
			System.out.println("流程没有结束");
		}
		
	}
	
	/**
	 * 查询历史任务
	 */
	@Test
	public void findHistoryTask() {
		List<HistoricTaskInstance> list = processEngine.getHistoryService() // 历史相关的service
			.createHistoricTaskInstanceQuery()
			.taskAssignee("张三")
			.list();
		 
		if (list != null && list.size() > 0) {
			for (HistoricTaskInstance hi :list) {
				System.out.println(hi.getId());
				System.out.println(hi.getName());
				System.out.println(hi.getProcessInstanceId());
				System.out.println(hi.getStartTime());
				System.out.println(hi.getEndTime());
				System.out.println(hi.getDurationInMillis());
				System.out.println("##################################################");
			}
		} 
			
	}
	
	/**
	 * 查询流程历史变量表
	 */
	@Test
	public void findHisatoryProcessVariables() {
		String processInstanceId = "1201";
		List<HistoricVariableInstance> list = processEngine.getHistoryService()
			.createHistoricVariableInstanceQuery()
			.processInstanceId(processInstanceId)
			.list();
		if (list != null && list.size() > 0) {
			for (HistoricVariableInstance hv : list) {
				System.out.println(hv.getProcessInstanceId());
				System.out.println(hv.getVariableName());
			}
		}
	}
}
