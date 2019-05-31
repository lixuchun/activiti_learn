package activiti_01;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class TestActiviti {
	/**
	 * 使用代码创建工作流需要的23扎张表
	 * 所有操作都用流程引擎processEngin
	 */
	@Test
	public void creativeTable () {
		
		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
		processEngineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
		processEngineConfiguration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activiti_learn?characterEncoding=utf8&useSSL=true");
		processEngineConfiguration.setJdbcUsername("root");
		processEngineConfiguration.setJdbcPassword("101022");
		
		/**
		 * public static final String DB_SCHEMA_UPDATE_FALSE = "false"; // 不能自动创建表 需要表存在
		 * public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop"; // 先删除再创建
		 * public static final String DB_SCHEMA_UPDATE_TRUE = "true"; // 如果不存在 自动创建表
		 */
		processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		// 工作流的核心对象 流程引擎对象
		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
		System.out.println("processEngine" + processEngine);
	}
	
	/**
	 * 使用配置文件进行流程引擎创建测试
	 */
	@Test
	public void creativeTable2 () {
		ProcessEngineConfiguration processEngineConfiguration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		// 工作流的核心对象 流程引擎对象
		ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
		System.out.println("processEngine" + processEngine);
	}
	
}
