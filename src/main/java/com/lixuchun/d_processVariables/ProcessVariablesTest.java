package com.lixuchun.d_processVariables;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ProcessVariablesTest {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * InputStream 部署
	 */
	@Test
	public void deployProcessDefinitition_inputStream() {
		
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("/diagrams/processVariables.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream("/diagrams/processVariables.png");
		
		Deployment deployment = processEngine.getRepositoryService() // 流程部署相关对象
				.createDeployment() // 创建一个部署对象
				.name("流程定义参数设定")
				.addInputStream("processVariables.bpmn", inputStreamBpmn) // 使用资源文件名称(与资源文件名称一致)以及输入流完成部署
				.addInputStream("processVariables.png", inputStreamPng) // 使用资源文件名称(与资源文件名称一致)以及输入流完成部署
				.deploy(); // 返回一个部署对象
			System.out.println("部署ID " + deployment.getId());
			System.out.println("部署名称 " + deployment.getName());
	}
	
	/**
	 * 启动流程实例 程序点击申请流程
	 */
	@Test
	public void startProcessInstance() {
		String processKey = "processVariables";
		ProcessInstance processInstance = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的 service 默认最新版本
			.startProcessInstanceByKey(processKey);// 启动流程通过流程定义key 对应helloworld.bpmn文件中id对应值
		System.out.println("流程实例id : " + processInstance.getId()); // 流程实例id 流程实例id : 101
		System.out.println("流程定义id" + processInstance.getProcessDefinitionId()); //流程定义id 流程定义idhellowworld:1:4
	}
	
	/**
	 * 提交流程的时候设置变量
	 */
	@Test
	public void setVariables() {
		TaskService taskService = processEngine.getTaskService();
		// 任务id
		String taskId = "1204";
		// 1 基本数据类型
//		taskService.setVariableLocal(taskId, "请假天数", 3); // 与任务id绑定
//		taskService.setVariable(taskId, "请假日期", new Date());
//		taskService.setVariable(taskId, "请假原因", "回家探亲");
		
		// 使用javabean设置参数
		/*
		 * 当一个javabean放置到流程变量中 要求javabean的结构不能变化
		 * 如果发生变化 再获取的时候会抛出异常
		 * 可以在类中添加固定版本号 （添加序列化id） 解决bean结构变化
		 */
		Person p = new Person();
		p.setId(10);
		p.setName("六66");
		p.setAge("18"); // 后添加javabean属性
		taskService.setVariable(taskId, "人员信息（固定版本）", p);
		
		System.out.println("设置成功了");
	}
	
	/**
	 * 获取流程变量
	 */
	@Test
	public void getVariables() {
		TaskService taskService = processEngine.getTaskService();
		// 任务id
		String taskId = "1204";
//		Integer day = (Integer) taskService.getVariable(taskId, "请假天数");
//		Date date = (Date) taskService.getVariable(taskId, "请假日期");
//		String resean = (String) taskService.getVariable(taskId, "请假原因");
//		
//		System.out.println("请假天数" + day);
//		System.out.println("请假日期" + date);
//		System.out.println("请假原因" + resean);
		
		//获取使用javabean类型
		Person p = (Person)taskService.getVariable(taskId, "人员信息（固定版本）");
		System.out.println(p);
	}
	
	/**
	 * 完成我的任务
	 */
	@Test
	public void complementMyPersonalTask() {
		String taskId = "1802";
		processEngine.getTaskService() // 与正在执行的任务管理相关的service
			.complete(taskId);
		System.out.println("完成任务 任务id :" + taskId);
	}
	
	/**
	 * 完成我的任务
	 */
	@Test
	public void completeMyPersonalTask() {
		String taskId = "";
		processEngine.getTaskService()
			.complete(taskId);
		System.out.println("完成任务 ： " + taskId);
	}
	
	/**
	 * 查询流程历史变量表
	 */
	@Test
	public void findHisatoryProcessVariables() {
		List<HistoricVariableInstance> list = processEngine.getHistoryService()
			.createHistoricVariableInstanceQuery()
			.variableName("请假日期")
			.list();
		if (list != null && list.size() > 0) {
			for (HistoricVariableInstance hv : list) {
				System.out.println(hv.getProcessInstanceId());
				System.out.println(hv.getVariableName());
			}
		}
	}
	
	/**
	 * 模拟设置和获取流程变量的场景
	 */
	public void setAndGetVariables() {
		// 与流程 实例 执行对象（正在执行）
		RuntimeService runtimeService = processEngine.getRuntimeService();
	
		// 与任务 正在执行的
		TaskService taskService = processEngine.getTaskService();
		
		// 设置流程变量
		
		// 表示使用执行对象的id 和流程变量的名称 设置流程变量的值 ， 一次一个值
		//runtimeService.setVariable(executionId, variableName, value);
		// 表示使用执行对象ID 和map集合的key就是流程变量的名称,value就是对应的值
		//runtimeService.setVariables(executionId, variables);
		
		// taskService一样 表示使用执行task的id 和流程变量的名称 设置流程变量的值 ， 一次一个值
		// taskService.setVariable(taskId, variableName, value);
		
		// 启动流程可以设置参数
		//runtimeService.startProcessInstanceById(processDefinitionId, variables);
		
		// 完成任务可以设定值
		// taskService.complete(taskId, variables);
		
		
		// 获取流程变量 其中任务 taskService 一样有三个方法 执行id变更为 taskId
		// runtimeService.getVariable(executionId, variableName);
		// runtimeService.getVariables(executionId) // 使用执行变量id 获取所有变量 防止到map中
		// runtimeService.getVariables(executionId, variableNames) // 使用执行对象id 获取流程变量值 通过设置流程变量名称存放集合中 获取指定流程变量名称的流程变量值
	}
	
	
}
