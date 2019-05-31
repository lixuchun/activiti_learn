package com.lixuchun.k_group2;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class GroupTaskTest {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * inputstream文件部署
	 */
	@Test
	public void deployProcessDefinitition_InputStream() {
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("groupTask.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream("groupTask.png");
		
		Deployment deployment = processEngine.getRepositoryService() // 流程部署相关对象
				.createDeployment() // 创建一个部署对象
				.name("组任务03")
				.addInputStream("groupTask.bpmn", inputStreamBpmn)
				.addInputStream("groupTask.png", inputStreamPng)
				.deploy(); // 返回一个部署对象
			System.out.println("部署ID " + deployment.getId());
			System.out.println("部署名称 " + deployment.getName());
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
		String candidateUser = "大H";
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
		String taskId = "5105";
		
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

	/**
	 * 查询历史的任务办理人表
	 */
	@Test
	public void findHistoryPersonTask() {
		String processInstanceId = "5801";
		List<HistoricIdentityLink> list = processEngine.getHistoryService()
			.getHistoricIdentityLinksForProcessInstance(processInstanceId);
	
		if (list != null && list.size() > 0) {
			for (HistoricIdentityLink identityLink : list) {
				System.out.println("taskId -> : " + identityLink.getTaskId());
				System.out.println("type -> : " + identityLink.getType());
				System.out.println("processInstanceId -> : " + identityLink.getProcessInstanceId());
				System.out.println("userId -> : " + identityLink.getUserId());
				System.out.println("#################################");
			}
		}
	}
	
	/**
	 * 拾取任务 将组任务分配给个人任务
	 */
	@Test
	public void claim() {
		String taskId = "5804";

		String userId = "大F";
		
		// 将组任务分配给个人任务 需要任务id
		// 分配个人任务 （可以是组任务中成员 也可以是非组任务的成员）
		processEngine.getTaskService()
			.claim(taskId, userId);
	}
	
	/**
	 * 将个人任务回退到组任务
	 */
	@Test
	public void setAssigee() {
		// 任务ID
		String taskId = "5804";
		processEngine.getTaskService()
			.setAssignee(taskId, null);
		System.out.println("恢复组任务成功");
	}
	
	/**
	 * 向组任务中添加成员 删除成员
	 */
	@Test
	public void addUser() {
		String taskId = "5804";
		
		String userId = "大H";
		processEngine.getTaskService()
			.addCandidateUser(taskId, userId);
		System.out.println("添加组成员成功");
	}
	
	/**
	 * 向组任务中添加成员 删除成员
	 */
	@Test
	public void delUser() {
		String taskId = "5804";
		String userId = "大H";
		processEngine.getTaskService()
			.deleteCandidateUser(taskId, userId); // 删除候选者类型
		System.out.println("删除组成员成功");
	}
}
