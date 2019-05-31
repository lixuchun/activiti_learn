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
	 * inputstream�ļ�����
	 */
	@Test
	public void deployProcessDefinitition_InputStream() {
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("receiveTask.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream("receiveTask.png");
		
		Deployment deployment = processEngine.getRepositoryService() // ���̲�����ض���
				.createDeployment() // ����һ���������
				.name("���ջ����")
				.addInputStream("receiveTask.bpmn", inputStreamBpmn)
				.addInputStream("receiveTask.png", inputStreamPng)
				.deploy(); // ����һ���������
			System.out.println("����ID " + deployment.getId());
			System.out.println("�������� " + deployment.getName());
	}
	
	/**
	 * ��������ʵ�� ��������������
	 */
	@Test
	public void startProcessInstance() {
		String processKey = "receiveTask";
		ProcessInstance processInstance = processEngine.getRuntimeService()// ������ִ�е�����ʵ����ִ�ж�����ص� service Ĭ�����°汾
			.startProcessInstanceByKey(processKey);// ��������ͨ�����̶���key ��Ӧhelloworld.bpmn�ļ���id��Ӧֵ
		System.out.println("����ʵ��id : " + processInstance.getId()); // ����ʵ��id ����ʵ��id : 101
		System.out.println("���̶���id" + processInstance.getProcessDefinitionId()); //���̶���id ���̶���idhellowworld:1:4
	
		/**
		 * ��ѯִ��ID
		 */
		Execution execution = processEngine.getRuntimeService()
			.createExecutionQuery() //����ִ�ж���
			.processInstanceId(processInstance.getId())
			.activityId("receivetask1") // ��ǰ�ID bpmn�ļ�������id
			.singleResult();
			
		/**
		 * ʹ�����̱������õ�ǰ���۶� ��������ҵ�����
		 */
		processEngine.getRuntimeService()
			.setVariable(execution.getId(), "���ܵ������۶�", 21000);
		
		// ����������ڴ��ڵȴ�״̬ ʹ�����̼���ִ��
		processEngine.getRuntimeService()
			.signal(execution.getId());
		
		/**
		 * ��ѯִ��ID
		 */
		Execution execution2 = processEngine.getRuntimeService()
			.createExecutionQuery() //����ִ�ж���
			.processInstanceId(processInstance.getId())
			.activityId("receivetask2") // ��ǰ�ID bpmn�ļ�������id
			.singleResult();
		
		/**
		 * ���̱����л�ȡ���ܵ������۶�
		 */
		Integer value = (Integer) processEngine.getRuntimeService()
			.getVariable(execution2.getId(), "���ܵ������۶�");
		System.out.println("���Ͷ��ţ������" + value);
		
		// ����������ڴ��ڵȴ�״̬ ʹ�����̼���ִ��
		processEngine.getRuntimeService()
			.signal(execution2.getId());
	}
	
}
