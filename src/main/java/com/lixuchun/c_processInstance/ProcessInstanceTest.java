package com.lixuchun.c_processInstance;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class ProcessInstanceTest {
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * zip�ļ�����
	 */
	@Test
	public void deployProcessDefinitition_Zip() {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		
		Deployment deployment = processEngine.getRepositoryService() // ���̲�����ض���
				.createDeployment() // ����һ���������
				.name("���̶���")
				.addZipInputStream(zipInputStream)// ָ��zip������
				.deploy(); // ����һ���������
			System.out.println("����ID " + deployment.getId());
			System.out.println("�������� " + deployment.getName());
	}
	
	/**
	 * ��������ʵ�� ��������������
	 */
	@Test
	public void startProcessInstance() {
		String processKey = "hellowworld";
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
		String taskId = "302";
		processEngine.getTaskService() // ������ִ�е����������ص�service
			.complete(taskId);
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
	
	/**
	 * ��ѯ��ʷ����
	 */
	@Test
	public void findHistoryTask() {
		List<HistoricTaskInstance> list = processEngine.getHistoryService() // ��ʷ��ص�service
			.createHistoricTaskInstanceQuery()
			.taskAssignee("����")
			.list();
		 
		if (list != null && list.size() > 0) {
			for (HistoricTaskInstance hi :list) {
				System.out.println(hi.getId());
				System.out.println(hi.getName());
				System.out.println(hi.getProcessInstanceId());
				System.out.println(hi.getStartTime());
				System.out.println(hi.getEndTime());
				System.out.println(hi.getDurationInMillis());
				System.out.println("##################################################");
			}
		} 
			
	}
	
	/**
	 * ��ѯ������ʷ������
	 */
	@Test
	public void findHisatoryProcessVariables() {
		String processInstanceId = "1201";
		List<HistoricVariableInstance> list = processEngine.getHistoryService()
			.createHistoricVariableInstanceQuery()
			.processInstanceId(processInstanceId)
			.list();
		if (list != null && list.size() > 0) {
			for (HistoricVariableInstance hv : list) {
				System.out.println(hv.getProcessInstanceId());
				System.out.println(hv.getVariableName());
			}
		}
	}
}
