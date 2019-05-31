package com.lixuchun.i_receiveTask;

import java.io.InputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class receiveTask {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * inputstream文件部署
	 */
	@Test
	public void deployProcessDefinitition_InputStream() {
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("receiveTask.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream("receiveTask.png");
		
		Deployment deployment = processEngine.getRepositoryService() // 流程部署相关对象
				.createDeployment() // 创建一个部署对象
				.name("接收活动任务")
				.addInputStream("receiveTask.bpmn", inputStreamBpmn)
				.addInputStream("receiveTask.png", inputStreamPng)
				.deploy(); // 返回一个部署对象
			System.out.println("部署ID " + deployment.getId());
			System.out.println("部署名称 " + deployment.getName());
	}
	
	/**
	 * 启动流程实例 程序点击申请流程
	 */
	@Test
	public void startProcessInstance() {
		String processKey = "receiveTask";
		ProcessInstance processInstance = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的 service 默认最新版本
			.startProcessInstanceByKey(processKey);// 启动流程通过流程定义key 对应helloworld.bpmn文件中id对应值
		System.out.println("流程实例id : " + processInstance.getId()); // 流程实例id 流程实例id : 101
		System.out.println("流程定义id" + processInstance.getProcessDefinitionId()); //流程定义id 流程定义idhellowworld:1:4
	
		/**
		 * 查询执行ID
		 */
		Execution execution = processEngine.getRuntimeService()
			.createExecutionQuery() //创建执行对象
			.processInstanceId(processInstance.getId())
			.activityId("receivetask1") // 当前活动ID bpmn文件的任务id
			.singleResult();
			
		/**
		 * 使用流程变量设置当前销售额 用来传递业务参数
		 */
		processEngine.getRuntimeService()
			.setVariable(execution.getId(), "汇总当日销售额", 21000);
		
		// 如果流程正在处于等待状态 使得流程继续执行
		processEngine.getRuntimeService()
			.signal(execution.getId());
		
		/**
		 * 查询执行ID
		 */
		Execution execution2 = processEngine.getRuntimeService()
			.createExecutionQuery() //创建执行对象
			.processInstanceId(processInstance.getId())
			.activityId("receivetask2") // 当前活动ID bpmn文件的任务id
			.singleResult();
		
		/**
		 * 流程变量中获取汇总当日销售额
		 */
		Integer value = (Integer) processEngine.getRuntimeService()
			.getVariable(execution2.getId(), "汇总当日销售额");
		System.out.println("发送短信：金额是" + value);
		
		// 如果流程正在处于等待状态 使得流程继续执行
		processEngine.getRuntimeService()
			.signal(execution2.getId());
	}
	
}
