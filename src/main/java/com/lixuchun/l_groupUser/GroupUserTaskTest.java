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
	 * inputstream�ļ�����
	 */
	@Test
	public void deployProcessDefinitition_InputStream() {
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("groupUserTask.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream("groupUserTask.png");
		
		Deployment deployment = processEngine.getRepositoryService() // ���̲�����ض���
				.createDeployment() // ����һ���������
				.name("��ɫ�û�����")
				.addInputStream("groupUserTask.bpmn", inputStreamBpmn)
				.addInputStream("groupUserTask.png", inputStreamPng)
				.deploy(); // ����һ���������
			System.out.println("����ID " + deployment.getId());
			System.out.println("�������� " + deployment.getName());
	
			/**
			 * ����û���ɫ��
			 */
			IdentityService identityService = processEngine.getIdentityService();//
			//������ɫ
			identityService.saveGroup(new GroupEntity("�ܾ���"));
			identityService.saveGroup(new GroupEntity("���ž���"));
			//�����û�
			identityService.saveUser(new UserEntity("����"));
			identityService.saveUser(new UserEntity("����"));
			identityService.saveUser(new UserEntity("����"));
			//�����û��ͽ�ɫ�Ĺ�����ϵ
			identityService.createMembership("����", "���ž���");
			identityService.createMembership("����", "���ž���");
			identityService.createMembership("����", "�ܾ���");
			System.out.println("�����֯�����ɹ�");
	}
	
	/**
	 * ��������ʵ�� ��������������
	 */
	@Test
	public void startProcessInstance() {
		String processKey = "task";
		/**
		 * �������̱���ָ������İ�����
		 */
		ProcessInstance processInstance = processEngine.getRuntimeService()// ������ִ�е�����ʵ����ִ�ж�����ص� service Ĭ�����°汾
			.startProcessInstanceByKey(processKey);// ��������ͨ�����̶���key ��Ӧhelloworld.bpmn�ļ���id��Ӧֵ
		System.out.println("����ʵ��id : " + processInstance.getId()); // ����ʵ��id ����ʵ��id : 101
		System.out.println("���̶���id" + processInstance.getProcessDefinitionId()); //���̶���id ���̶���idhellowworld:1:4
	}
	
	/**
	 * ��ѯ��ǰ��������
	 */
	@Test
	public void findGroupTask () {
		String candidateUser = "����";
		List<Task> list = processEngine.getTaskService() // ������ִ�е����������ص�service
			.createTaskQuery() // ���������ѯ
			//.taskAssignee(candidateUser) // ���˲�Ѱ ָ��������
			.taskCandidateUser(candidateUser) // ��ѯ�����˱� act_ru_identitylink ���ж�ӦuserType="candidate" ��ѡ������ֵ
			.list();
		if (list != null && list.size() > 0) {
			for (Task task : list) {
				System.out.println("����ID" + task.getId());
				System.out.println("��������" + task.getName());
				System.out.println("���񴴽�ʱ��" + task.getCreateTime());
				System.out.println("����İ�����" + task.getAssignee());
				System.out.println("����ʵ��ID:" + task.getProcessInstanceId());
				System.out.println("ִ�ж����ID:" + task.getExecutionId());
				System.out.println("���̶���ID:" + task.getProcessDefinitionId());
				System.out.println("----------------------------------------------------");
			}
		}
	}
	
	/**
	 * ����ҵ�����
	 */
	@Test
	public void complementMyPersonalTask() {
		String taskId = "7204";
		
		/**
		 *  ��������ʱ���������̱���
		 *  ��һ������ ��Ӧ exclusiveGateWay.bpmn �ļ��е� ${money<=1000 && >=500} 
		 */
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("money", 200);
		processEngine.getTaskService() // ������ִ�е����������ص�service
			.complete(taskId, variables);
		System.out.println("������� ����id :" + taskId);
	}
	
	/**
	 * ��ѯ����ִ�е���������˱�
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
	
	/**ʰȡ���񣬽�������ָ���������ָ������İ������ֶ�*/
	@Test
	public void claim(){
		//��������������������
		//����ID
		String taskId = "7504";
		//����ĸ������񣨿������������еĳ�Ա��Ҳ�����Ƿ�������ĳ�Ա��
		String userId = "����";
		processEngine.getTaskService()//
					.claim(taskId, userId);
	}
}
