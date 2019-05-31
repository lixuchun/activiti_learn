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
	 * �������̶���
	 */
	@Test
	public void deployProcessDefinitition_classPath() {
		Deployment deployment = processEngine.getRepositoryService() // ���̲�����ض���
			.createDeployment() // ����һ���������
			.name("helloworld���ų���")
			.addClasspathResource("diagrams/helloworld.bpmn") // classpath ���� һ�μ���һ���ļ�
			.addClasspathResource("diagrams/helloworld.png")
			.deploy(); // ����һ��������� ��ɲ���
		System.out.println("����ID " + deployment.getId());
		System.out.println("�������� " + deployment.getName());
	}
	
	/**
	 * ��������ʵ�� ��������������
	 */
	@Test
	public void startProcessInstance() {
		String processKey = "hellowworld";
		ProcessInstance processInstance = processEngine.getRuntimeService()// ������ִ�е�����ʵ����ִ�ж�����ص� service
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
		String taskId = "302";
		processEngine.getTaskService() // ������ִ�е����������ص�service
			.complete(taskId);
		System.out.println("������� ����id :" + taskId);
	}
}
