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
	 * 部署流程定义 classPath
	 */
	@Test
	public void deployProcessDefinitition_classPath() {
		Deployment deployment = processEngine.getRepositoryService() // 流程部署相关对象
			.createDeployment() // 创建一个部署对象
			.name("helloworld入门程序")
			.addClasspathResource("diagrams/helloworld.bpmn") // classpath 加载 一次加载一个文件
			.addClasspathResource("diagrams/helloworld.png")
			.deploy(); // 返回一个部署对象
		System.out.println("部署ID " + deployment.getId());
		System.out.println("部署名称 " + deployment.getName());
	}
	
	/**
	 * zip文件部署
	 */
	@Test
	public void deployProcessDefinitition_Zip() {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		
		Deployment deployment = processEngine.getRepositoryService() // 流程部署相关对象
				.createDeployment() // 创建一个部署对象
				.name("流程定义")
				.addZipInputStream(zipInputStream)// 指定zip输入流
				.deploy(); // 返回一个部署对象
			System.out.println("部署ID " + deployment.getId());
			System.out.println("部署名称 " + deployment.getName());
	}
	
	/**
	 * 查询流程定义
	 */
	@Test
	public void findProcessDefinitino() {
		List<ProcessDefinition> list = processEngine.getRepositoryService() // 流程定义查询
			.createProcessDefinitionQuery() // 流程定义查询
			// 指定查询条件
			//.deploymentId(deploymentId)
			//.processDefinitionKey(processDefinitionKey)
			//.processDefinitionNameLike(processDefinitionNameLike)
			//.list(); // 集合列表 封装流程定义
			//.singleResult() // 返回唯一结果集
			//.count() // 结果集数量
			//.listPage(firstResult, maxResults) //分页查询
			//.orderByDeploymentId().asc() // 流程定义id升序排序
			.orderByProcessDefinitionVersion().asc()
			.list();
		
		if (list != null && list.size() > 0) {
			for (ProcessDefinition pd : list) {
				System.out.println("流程定义ID:" + pd.getId()); // 流程定义的 key + 版本 + 随机生成数
				System.out.println("流程定义名称:" + pd.getName()); // 对应hellowworld.bpmn 文件中的name
				System.out.println("流程定义的key:" + pd.getKey()); // 对应helloworld.bpmn中的id属性
				System.out.println("流程定义版本:" + pd.getVersion()); // 当key值相同 版本号升级 默认1 
				System.out.println("资源名bpmn:" + pd.getResourceName());
				System.out.println("资源名PNG名称:" + pd.getDiagramResourceName());
				System.out.println("部署对象ID:" + pd.getDeploymentId());
				System.out.println("####################################");
			}
		}
	}
	
	/**
	 * 删除流程定义
	 */
	@Test
	public void deleteProcessDefinition() {
		// 使用部署ID进行删除操作
		String deploymentId = "601";
		/**
		 * 不带级联删除
		 * 	只能删除没有启动的流程 如果启动 则会抛出异常
		 */
//		processEngine.getRepositoryService()
//			.deleteDeployment(deploymentId);
		
		// 可以使用级联删除 不管流程是否启动 直接全部删除
		processEngine.getRepositoryService()
		.deleteDeployment(deploymentId, true);
	
		
		System.out.println("删除成功!");
	}
	
	/**
	 * 查看流程图
	 * @throws IOException 
	 */
	@Test
	public void viewPic() throws IOException {
		/**
		 * 将生成的图片放到文件夹下
		 */
		String deploymentId = "501";
		
		// 获取图片资源名称
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
		
		// 获取图片的输出流
		InputStream in = processEngine.getRepositoryService()//
			.getResourceAsStream(deploymentId, resourceNmae);
		
		// 将图片生成到D盘下
		File file = new File("D:/" + resourceNmae);
		// 将输入流的图片写到D盘下
		FileUtils.copyInputStreamToFile(in, file);
	}
	
	/**
	 * 查询最新版本流程定义
	 */
	@Test
	public void getNewProcessDefinition() {
		List<ProcessDefinition> list = processEngine.getRepositoryService()//
			.createProcessDefinitionQuery()//
			.orderByProcessDefinitionVersion().asc() // 流程定义版本升序排列
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
				System.out.println("流程定义ID:" + pd.getId()); // 流程定义的 key + 版本 + 随机生成数
				System.out.println("流程定义名称:" + pd.getName()); // 对应hellowworld.bpmn 文件中的name
				System.out.println("流程定义的key:" + pd.getKey()); // 对应helloworld.bpmn中的id属性
				System.out.println("流程定义版本:" + pd.getVersion()); // 当key值相同 版本号升级 默认1 
				System.out.println("资源名bpmn:" + pd.getResourceName());
				System.out.println("资源名PNG名称:" + pd.getDiagramResourceName());
				System.out.println("部署对象ID:" + pd.getDeploymentId());
				System.out.println("####################################");
			}
		}
	}
	
	/**
	 * 附属功能 删除流程定义 （删除key相同的所有不同版本的流程定义）
	 */
	@Test
	public void deleteProcessDefinitionByKey() {
		
		String processDefinitionKey = "hellowworld";
		
		// 先使用流程定义的 key查询流程定义 查询出所有版本
		List<ProcessDefinition> list = processEngine.getRepositoryService()//
			.createProcessDefinitionQuery()//;
			.processDefinitionKey(processDefinitionKey) // 使用流程定义的key查询
			.list();
		// 遍历 获取每个流程定义部署的id
		if (list != null && list.size() > 0) {
			for (ProcessDefinition pd : list) {
				// 获取部署id
				String deploymentId = pd.getDeploymentId();
				processEngine.getRepositoryService()
					.deleteDeployment(deploymentId, true);
			}
		}
			
	}
}
