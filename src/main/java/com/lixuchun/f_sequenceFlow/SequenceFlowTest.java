package com.lixuchun.f_sequenceFlow;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class SequenceFlowTest {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * inputstream�ļ�����
	 */
	@Test
	public void deployProcessDefinitition_InputStream() {
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("sequenceFlow.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream("sequenceFlow.png");
		
		Deployment deployment = processEngine.getRepositoryService() // ���̲�����ض���
				.createDeployment() // ����һ���������
				.name("����")
				.addInputStream("sequenceFlow.bpmn", inputStreamBpmn)
				.addInputStream("sequenceFlow.png", inputStreamPng)
				.deploy(); // ����һ���������
			System.out.println("����ID " + deployment.getId());
			System.out.println("�������� " + deployment.getName());
	}
	
	/**
	 * ��������ʵ�� ��������������
	 */
	@Test
	public void startProcessInstance() {
		String processKey = "sequenceFlow";
		ProcessInstance processInstance = processEngine.getRuntimeService()// ������ִ�е�����ʵ����ִ�ж�����ص� service Ĭ�����°汾
			.startProcessInstanceByKey(processKey);// ��������ͨ�����̶���key ��Ӧhelloworld.bpmn�ļ���id��Ӧֵ
		System.out.println("����ʵ��id : " + processInstance.getId()); // ����ʵ��id ����ʵ��id : 101
		System.out.println("���̶���id" + processInstance.getProcessDefinitionId()); //���̶���id ���̶���idhellowworld:1:4
	}
	
	/**
	 * ��ѯ��ǰ�˵ĸ�������
	 */
	@Test
	public void findMyPersionalTask () {
		String assignee = "����";
		List<Task> list = processEngine.getTaskService() // ������ִ�е����������ص�service
			.createTaskQuery() // ���������ѯ
			.taskAssignee(assignee) // ���˲�Ѱ ָ��������
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
		String taskId = "2503";
		
		/**
		 *  ��������ʱ���������̱���
		 *  ��һ������ ��Ӧ sequenceFlow.bpmn �ļ��е� ${message=='����Ҫ'} 
		 */
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("message", "��Ҫ");
		processEngine.getTaskService() // ������ִ�е����������ص�service
			.complete(taskId, variables);
		System.out.println("������� ����id :" + taskId);
	}
	
	/**
	 * �ж������Ƿ���� �ж�����״̬
	 */
	@Test
	public void isPerocessEnd() {
		String processInstanceId = "101";
		// ����ִ�е�����ʵ����
		ProcessInstance pi = processEngine.getRuntimeService() // ����ִ�е�����ʵ���Լ�ִ�ж���
			.createProcessInstanceQuery() // ����ʵ����ѯ
			.processInstanceId(processInstanceId) // ����ʵ��id��ѯ
			.singleResult();
		
		if (pi == null) {
			System.out.println("���̽���");
		} else {
			System.out.println("����û�н���");
		}
		
	}
}
