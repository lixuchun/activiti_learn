package com.lixuchun.a_helloworld;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class HelloWorld {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * 部署流程定义
	 */
	@Test
	public void deployProcessDefinitition_classPath() {
		Deployment deployment = processEngine.getRepositoryService() // 流程部署相关对象
			.createDeployment() // 创建一个部署对象
			.name("helloworld入门程序")
			.addClasspathResource("diagrams/helloworld.bpmn") // classpath 加载 一次加载一个文件
			.addClasspathResource("diagrams/helloworld.png")
			.deploy(); // 返回一个部署对象 完成部署
		System.out.println("部署ID " + deployment.getId());
		System.out.println("部署名称 " + deployment.getName());
	}
	
	/**
	 * 启动流程实例 程序点击申请流程
	 */
	@Test
	public void startProcessInstance() {
		String processKey = "hellowworld";
		ProcessInstance processInstance = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的 service
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
}
