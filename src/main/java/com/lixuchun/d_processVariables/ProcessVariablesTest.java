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
	 * InputStream ����
	 */
	@Test
	public void deployProcessDefinitition_inputStream() {
		
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("/diagrams/processVariables.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream("/diagrams/processVariables.png");
		
		Deployment deployment = processEngine.getRepositoryService() // ���̲�����ض���
				.createDeployment() // ����һ���������
				.name("���̶�������趨")
				.addInputStream("processVariables.bpmn", inputStreamBpmn) // ʹ����Դ�ļ�����(����Դ�ļ�����һ��)�Լ���������ɲ���
				.addInputStream("processVariables.png", inputStreamPng) // ʹ����Դ�ļ�����(����Դ�ļ�����һ��)�Լ���������ɲ���
				.deploy(); // ����һ���������
			System.out.println("����ID " + deployment.getId());
			System.out.println("�������� " + deployment.getName());
	}
	
	/**
	 * ��������ʵ�� ��������������
	 */
	@Test
	public void startProcessInstance() {
		String processKey = "processVariables";
		ProcessInstance processInstance = processEngine.getRuntimeService()// ������ִ�е�����ʵ����ִ�ж�����ص� service Ĭ�����°汾
			.startProcessInstanceByKey(processKey);// ��������ͨ�����̶���key ��Ӧhelloworld.bpmn�ļ���id��Ӧֵ
		System.out.println("����ʵ��id : " + processInstance.getId()); // ����ʵ��id ����ʵ��id : 101
		System.out.println("���̶���id" + processInstance.getProcessDefinitionId()); //���̶���id ���̶���idhellowworld:1:4
	}
	
	/**
	 * �ύ���̵�ʱ�����ñ���
	 */
	@Test
	public void setVariables() {
		TaskService taskService = processEngine.getTaskService();
		// ����id
		String taskId = "1204";
		// 1 ������������
//		taskService.setVariableLocal(taskId, "�������", 3); // ������id��
//		taskService.setVariable(taskId, "�������", new Date());
//		taskService.setVariable(taskId, "���ԭ��", "�ؼ�̽��");
		
		// ʹ��javabean���ò���
		/*
		 * ��һ��javabean���õ����̱����� Ҫ��javabean�Ľṹ���ܱ仯
		 * ��������仯 �ٻ�ȡ��ʱ����׳��쳣
		 * ������������ӹ̶��汾�� ��������л�id�� ���bean�ṹ�仯
		 */
		Person p = new Person();
		p.setId(10);
		p.setName("��66");
		p.setAge("18"); // �����javabean����
		taskService.setVariable(taskId, "��Ա��Ϣ���̶��汾��", p);
		
		System.out.println("���óɹ���");
	}
	
	/**
	 * ��ȡ���̱���
	 */
	@Test
	public void getVariables() {
		TaskService taskService = processEngine.getTaskService();
		// ����id
		String taskId = "1204";
//		Integer day = (Integer) taskService.getVariable(taskId, "�������");
//		Date date = (Date) taskService.getVariable(taskId, "�������");
//		String resean = (String) taskService.getVariable(taskId, "���ԭ��");
//		
//		System.out.println("�������" + day);
//		System.out.println("�������" + date);
//		System.out.println("���ԭ��" + resean);
		
		//��ȡʹ��javabean����
		Person p = (Person)taskService.getVariable(taskId, "��Ա��Ϣ���̶��汾��");
		System.out.println(p);
	}
	
	/**
	 * ����ҵ�����
	 */
	@Test
	public void complementMyPersonalTask() {
		String taskId = "1802";
		processEngine.getTaskService() // ������ִ�е����������ص�service
			.complete(taskId);
		System.out.println("������� ����id :" + taskId);
	}
	
	/**
	 * ����ҵ�����
	 */
	@Test
	public void completeMyPersonalTask() {
		String taskId = "";
		processEngine.getTaskService()
			.complete(taskId);
		System.out.println("������� �� " + taskId);
	}
	
	/**
	 * ��ѯ������ʷ������
	 */
	@Test
	public void findHisatoryProcessVariables() {
		List<HistoricVariableInstance> list = processEngine.getHistoryService()
			.createHistoricVariableInstanceQuery()
			.variableName("�������")
			.list();
		if (list != null && list.size() > 0) {
			for (HistoricVariableInstance hv : list) {
				System.out.println(hv.getProcessInstanceId());
				System.out.println(hv.getVariableName());
			}
		}
	}
	
	/**
	 * ģ�����úͻ�ȡ���̱����ĳ���
	 */
	public void setAndGetVariables() {
		// ������ ʵ�� ִ�ж�������ִ�У�
		RuntimeService runtimeService = processEngine.getRuntimeService();
	
		// ������ ����ִ�е�
		TaskService taskService = processEngine.getTaskService();
		
		// �������̱���
		
		// ��ʾʹ��ִ�ж����id �����̱��������� �������̱�����ֵ �� һ��һ��ֵ
		//runtimeService.setVariable(executionId, variableName, value);
		// ��ʾʹ��ִ�ж���ID ��map���ϵ�key�������̱���������,value���Ƕ�Ӧ��ֵ
		//runtimeService.setVariables(executionId, variables);
		
		// taskServiceһ�� ��ʾʹ��ִ��task��id �����̱��������� �������̱�����ֵ �� һ��һ��ֵ
		// taskService.setVariable(taskId, variableName, value);
		
		// �������̿������ò���
		//runtimeService.startProcessInstanceById(processDefinitionId, variables);
		
		// �����������趨ֵ
		// taskService.complete(taskId, variables);
		
		
		// ��ȡ���̱��� �������� taskService һ������������ ִ��id���Ϊ taskId
		// runtimeService.getVariable(executionId, variableName);
		// runtimeService.getVariables(executionId) // ʹ��ִ�б���id ��ȡ���б��� ��ֹ��map��
		// runtimeService.getVariables(executionId, variableNames) // ʹ��ִ�ж���id ��ȡ���̱���ֵ ͨ���������̱������ƴ�ż����� ��ȡָ�����̱������Ƶ����̱���ֵ
	}
	
	
}
