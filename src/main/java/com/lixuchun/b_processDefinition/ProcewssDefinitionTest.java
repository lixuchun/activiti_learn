package com.lixuchun.b_processDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ProcewssDefinitionTest {
	
	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	
	/**
	 * �������̶��� classPath
	 */
	@Test
	public void deployProcessDefinitition_classPath() {
		Deployment deployment = processEngine.getRepositoryService() // ���̲�����ض���
			.createDeployment() // ����һ���������
			.name("helloworld���ų���")
			.addClasspathResource("diagrams/helloworld.bpmn") // classpath ���� һ�μ���һ���ļ�
			.addClasspathResource("diagrams/helloworld.png")
			.deploy(); // ����һ���������
		System.out.println("����ID " + deployment.getId());
		System.out.println("�������� " + deployment.getName());
	}
	
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
	 * ��ѯ���̶���
	 */
	@Test
	public void findProcessDefinitino() {
		List<ProcessDefinition> list = processEngine.getRepositoryService() // ���̶����ѯ
			.createProcessDefinitionQuery() // ���̶����ѯ
			// ָ����ѯ����
			//.deploymentId(deploymentId)
			//.processDefinitionKey(processDefinitionKey)
			//.processDefinitionNameLike(processDefinitionNameLike)
			//.list(); // �����б� ��װ���̶���
			//.singleResult() // ����Ψһ�����
			//.count() // ���������
			//.listPage(firstResult, maxResults) //��ҳ��ѯ
			//.orderByDeploymentId().asc() // ���̶���id��������
			.orderByProcessDefinitionVersion().asc()
			.list();
		
		if (list != null && list.size() > 0) {
			for (ProcessDefinition pd : list) {
				System.out.println("���̶���ID:" + pd.getId()); // ���̶���� key + �汾 + ���������
				System.out.println("���̶�������:" + pd.getName()); // ��Ӧhellowworld.bpmn �ļ��е�name
				System.out.println("���̶����key:" + pd.getKey()); // ��Ӧhelloworld.bpmn�е�id����
				System.out.println("���̶���汾:" + pd.getVersion()); // ��keyֵ��ͬ �汾������ Ĭ��1 
				System.out.println("��Դ��bpmn:" + pd.getResourceName());
				System.out.println("��Դ��PNG����:" + pd.getDiagramResourceName());
				System.out.println("�������ID:" + pd.getDeploymentId());
				System.out.println("####################################");
			}
		}
	}
	
	/**
	 * ɾ�����̶���
	 */
	@Test
	public void deleteProcessDefinition() {
		// ʹ�ò���ID����ɾ������
		String deploymentId = "601";
		/**
		 * ��������ɾ��
		 * 	ֻ��ɾ��û������������ ������� ����׳��쳣
		 */
//		processEngine.getRepositoryService()
//			.deleteDeployment(deploymentId);
		
		// ����ʹ�ü���ɾ�� ���������Ƿ����� ֱ��ȫ��ɾ��
		processEngine.getRepositoryService()
		.deleteDeployment(deploymentId, true);
	
		
		System.out.println("ɾ���ɹ�!");
	}
	
	/**
	 * �鿴����ͼ
	 * @throws IOException 
	 */
	@Test
	public void viewPic() throws IOException {
		/**
		 * �����ɵ�ͼƬ�ŵ��ļ�����
		 */
		String deploymentId = "501";
		
		// ��ȡͼƬ��Դ����
		List<String> list = processEngine.getRepositoryService() //
			.getDeploymentResourceNames(deploymentId);
		
		String resourceNmae = "";
		if (list != null && list.size() > 0) {
			for (String name : list) {
				if (name.indexOf(".png") >= 0) {
					resourceNmae = name;
				}
			}
		}
		
		// ��ȡͼƬ�������
		InputStream in = processEngine.getRepositoryService()//
			.getResourceAsStream(deploymentId, resourceNmae);
		
		// ��ͼƬ���ɵ�D����
		File file = new File("D:/" + resourceNmae);
		// ����������ͼƬд��D����
		FileUtils.copyInputStreamToFile(in, file);
	}
	
	/**
	 * ��ѯ���°汾���̶���
	 */
	@Test
	public void getNewProcessDefinition() {
		List<ProcessDefinition> list = processEngine.getRepositoryService()//
			.createProcessDefinitionQuery()//
			.orderByProcessDefinitionVersion().asc() // ���̶���汾��������
			.list();
		Map<String, ProcessDefinition> map = new LinkedHashMap<String, ProcessDefinition>();
		if (list != null && list.size() > 0) {
			for (ProcessDefinition pd : list) {
				map.put(pd.getKey(), pd);
			}
		}
		List<ProcessDefinition> pdList = new ArrayList<ProcessDefinition>(map.values());
		if (list != null && list.size() > 0) {
			for (ProcessDefinition pd : pdList) {
				System.out.println("���̶���ID:" + pd.getId()); // ���̶���� key + �汾 + ���������
				System.out.println("���̶�������:" + pd.getName()); // ��Ӧhellowworld.bpmn �ļ��е�name
				System.out.println("���̶����key:" + pd.getKey()); // ��Ӧhelloworld.bpmn�е�id����
				System.out.println("���̶���汾:" + pd.getVersion()); // ��keyֵ��ͬ �汾������ Ĭ��1 
				System.out.println("��Դ��bpmn:" + pd.getResourceName());
				System.out.println("��Դ��PNG����:" + pd.getDiagramResourceName());
				System.out.println("�������ID:" + pd.getDeploymentId());
				System.out.println("####################################");
			}
		}
	}
	
	/**
	 * �������� ɾ�����̶��� ��ɾ��key��ͬ�����в�ͬ�汾�����̶��壩
	 */
	@Test
	public void deleteProcessDefinitionByKey() {
		
		String processDefinitionKey = "hellowworld";
		
		// ��ʹ�����̶���� key��ѯ���̶��� ��ѯ�����а汾
		List<ProcessDefinition> list = processEngine.getRepositoryService()//
			.createProcessDefinitionQuery()//;
			.processDefinitionKey(processDefinitionKey) // ʹ�����̶����key��ѯ
			.list();
		// ���� ��ȡÿ�����̶��岿���id
		if (list != null && list.size() > 0) {
			for (ProcessDefinition pd : list) {
				// ��ȡ����id
				String deploymentId = pd.getDeploymentId();
				processEngine.getRepositoryService()
					.deleteDeployment(deploymentId, true);
			}
		}
			
	}
}
