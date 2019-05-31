package com.lixuchun.l_groupUser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class GroupUserTaskTest {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * inputstream文件部署
	 */
	@Test
	public void deployProcessDefinitition_InputStream() {
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("groupUserTask.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream("groupUserTask.png");
		
		Deployment deployment = processEngine.getRepositoryService() // 流程部署相关对象
				.createDeployment() // 创建一个部署对象
				.name("角色用户关联")
				.addInputStream("groupUserTask.bpmn", inputStreamBpmn)
				.addInputStream("groupUserTask.png", inputStreamPng)
				.deploy(); // 返回一个部署对象
			System.out.println("部署ID " + deployment.getId());
			System.out.println("部署名称 " + deployment.getName());
	
			/**
			 * 添加用户角色组
			 */
			IdentityService identityService = processEngine.getIdentityService();//
			//创建角色
			identityService.saveGroup(new GroupEntity("总经理"));
			identityService.saveGroup(new GroupEntity("部门经理"));
			//创建用户
			identityService.saveUser(new UserEntity("张三"));
			identityService.saveUser(new UserEntity("李四"));
			identityService.saveUser(new UserEntity("王五"));
			//建立用户和角色的关联关系
			identityService.createMembership("张三", "部门经理");
			identityService.createMembership("李四", "部门经理");
			identityService.createMembership("王五", "总经理");
			System.out.println("添加组织机构成功");
	}
	
	/**
	 * 启动流程实例 程序点击申请流程
	 */
	@Test
	public void startProcessInstance() {
		String processKey = "task";
		/**
		 * 启动流程变量指定任务的办理人
		 */
		ProcessInstance processInstance = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的 service 默认最新版本
			.startProcessInstanceByKey(processKey);// 启动流程通过流程定义key 对应helloworld.bpmn文件中id对应值
		System.out.println("流程实例id : " + processInstance.getId()); // 流程实例id 流程实例id : 101
		System.out.println("流程定义id" + processInstance.getProcessDefinitionId()); //流程定义id 流程定义idhellowworld:1:4
	}
	
	/**
	 * 查询当前人组任务
	 */
	@Test
	public void findGroupTask () {
		String candidateUser = "李四";
		List<Task> list = processEngine.getTaskService() // 与正在执行的任务管理相关的service
			.createTaskQuery() // 创建任务查询
			//.taskAssignee(candidateUser) // 个人查寻 指定办理人
			.taskCandidateUser(candidateUser) // 查询办理人表 act_ru_identitylink 其中对应userType="candidate" 候选者类型值
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
		String taskId = "7204";
		
		/**
		 *  完成任务的时候设置流程变量
		 *  下一个连线 对应 exclusiveGateWay.bpmn 文件中的 ${money<=1000 && >=500} 
		 */
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("money", 200);
		processEngine.getTaskService() // 与正在执行的任务管理相关的service
			.complete(taskId, variables);
		System.out.println("完成任务 任务id :" + taskId);
	}
	
	/**
	 * 查询正在执行的任务办理人表
	 */
	@Test
	public void findExcutingPersonTask() {
		String taskId = "5804";
		List<IdentityLink> list = processEngine.getTaskService()
			.getIdentityLinksForTask(taskId);
	
		if (list != null && list.size() > 0) {
			for (IdentityLink identityLink : list) {
				System.out.println("taskId -> : " + identityLink.getTaskId());
				System.out.println("type -> : " + identityLink.getType());
				System.out.println("processInstanceId -> : " + identityLink.getProcessInstanceId());
				System.out.println("userId -> : " + identityLink.getUserId());
				System.out.println("#################################");
			}
		}
	}
	
	/**拾取任务，将组任务分给个人任务，指定任务的办理人字段*/
	@Test
	public void claim(){
		//将组任务分配给个人任务
		//任务ID
		String taskId = "7504";
		//分配的个人任务（可以是组任务中的成员，也可以是非组任务的成员）
		String userId = "张三";
		processEngine.getTaskService()//
					.claim(taskId, userId);
	}
}
